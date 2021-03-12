package com.epam.task.sixth.logic;

import com.epam.task.sixth.entity.*;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Auction {

    private static final Logger LOGGER = Logger.getLogger(Auction.class);

    private static final AtomicReference<Auction> INSTANCE = new AtomicReference<>();
    private static final Lock INSTANCE_LOCK = new ReentrantLock();

    private static final String LOTS_FILENAME = "./src/main/resources/lots.json";
    private static final String PARTICIPANTS_FILENAME = "./src/main/resources/participants.json";

    private final List<Participant> participants;

    private Auction() throws AuctionException {
        try {
            participants = readParticipants();
        } catch (FileNotFoundException e) {
            throw new AuctionException("No such file of participants", e);
        }
    }

    public static Auction getInstance() throws AuctionException {
        if (INSTANCE.get() == null) {
            try {
                INSTANCE_LOCK.lock();
                if (INSTANCE.get() == null) {
                    Auction base = new Auction();
                    INSTANCE.getAndSet(base);
                }

            } finally {
                INSTANCE_LOCK.unlock();
            }
        }

        return INSTANCE.get();
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void startBargains() {
        try {
            List<Lot> lots = readLots();
            List<LotRunnable> lotRunnables = new ArrayList<>();
            ExecutorService executorService = Executors.newFixedThreadPool(lots.size());

            List<Future<?>> futures =
                    lots.stream()
                            .map(lot -> {
                                        LotRunnable lotRunnable = new LotRunnable(lot);
                                        lotRunnables.add(lotRunnable);
                                        return executorService.submit(lotRunnable);
                                    }
                            )
                            .collect(Collectors.toList());

            executorService.shutdown();

        } catch (FileNotFoundException e) {
            LOGGER.error("No such json file", e);
        }
    }

    private List<Lot> readLots() throws FileNotFoundException {
        Gson gson = new Gson();

        Lots lots = gson.fromJson(new FileReader(LOTS_FILENAME), Lots.class);

        return lots.getLots();
    }

    private List<Participant> readParticipants() throws FileNotFoundException {
        Gson gson = new Gson();
        Participants participants = gson.fromJson(new FileReader(PARTICIPANTS_FILENAME), Participants.class);
        return participants.getParticipants();
    }
}
