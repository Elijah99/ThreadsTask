package com.epam.task.sixth.entity;


import java.util.concurrent.atomic.AtomicReference;

public class Lot {

    public static final double PERCENT_OF_STEM_FROM_PRICE = 0.1;

    private int id;
    private String name;
    private double startingPrice;
    private final double priceIncreaseStep;
    private AtomicReference<Double> currentPrice;
    private AtomicReference<Participant> lastBetted;

    public Lot(int id, String name, double startingPrice) {
        this.id = id;
        this.name = name;
        this.startingPrice = startingPrice;
        priceIncreaseStep = startingPrice * PERCENT_OF_STEM_FROM_PRICE;
        currentPrice = new AtomicReference<Double>(startingPrice);
        lastBetted = new AtomicReference<>(new Participant());
    }

    public static double getPercentOfStemFromPrice() {
        return PERCENT_OF_STEM_FROM_PRICE;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getPriceIncreaseStep() {
        return priceIncreaseStep;
    }

    public Double getCurrentPrice() {
        return currentPrice.get();
    }

    public Participant getLastBetted() {
        return lastBetted.get();
    }

    public void setLastBetted(Participant lastBetted) {
        this.lastBetted = new AtomicReference<>(lastBetted);
    }

    public void bet() {
        currentPrice = new AtomicReference<>(getCurrentPrice() + priceIncreaseStep);
    }

}
