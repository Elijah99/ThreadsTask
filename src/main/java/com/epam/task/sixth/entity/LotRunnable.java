package com.epam.task.sixth.entity;

import com.epam.task.sixth.logic.Auction;
import com.epam.task.sixth.logic.AuctionException;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class LotRunnable extends Lot implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(Auction.class);
    private final Lock getLastBettedLock = new ReentrantLock();
    private final Lock setLastBettedLock = new ReentrantLock();
    private final Lock getCurrentPriceLock = new ReentrantLock();
    private final Lock betLock = new ReentrantLock();


    public LotRunnable(Lot lot) {
        super(lot.getId(), lot.getName(), lot.getStartingPrice());
    }

    @Override
    public void run() {
        try {
            List<Participant> participants = Auction.getInstance().getParticipants();
            ExecutorService executorService = Executors.newFixedThreadPool(participants.size());

            List<Future<?>> futures =
                    participants.stream()
                            .map(participant -> {
                                ParticipantRunnable participantRunnable = new ParticipantRunnable(participant, this);
                                return executorService.submit(participantRunnable);
                            })
                            .collect(Collectors.toList());

            executorService.shutdown();
        } catch (AuctionException e) {
            LOGGER.error("Error in Lot thread", e);
        }
    }

    @Override
    public Double getCurrentPrice() {
        try {
            getCurrentPriceLock.lock();
            return super.getCurrentPrice();
        } finally {
            getCurrentPriceLock.unlock();
        }
    }

    @Override
    public Participant getLastBetted() {
        try {
            getLastBettedLock.lock();
            return super.getLastBetted();
        } finally {
            getLastBettedLock.unlock();
        }
    }

    @Override
    public void setLastBetted(Participant lastBetted) {
        try {
            setLastBettedLock.lock();
            super.setLastBetted(lastBetted);
        } finally {
            setLastBettedLock.unlock();
        }
    }

    @Override
    public void bet() {
        try {
            betLock.lock();
            super.bet();
        } finally {
            betLock.unlock();
        }
    }
}
