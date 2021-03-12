package com.epam.task.sixth.entity;

public class ParticipantRunnable extends Participant implements Runnable {

    Lot lot;
    private final double MAX_PERCENT_MONEY_TO_BET = 0.3;

    public ParticipantRunnable(Participant participant, Lot lot) {
        super(participant.getId(), participant.getMoney());
        this.lot = lot;
    }

    @Override
    public void run() {
        while (getMoney() * MAX_PERCENT_MONEY_TO_BET > (lot.getCurrentPrice() + lot.getPriceIncreaseStep())) {
            lot.bet();
            lot.setLastBetted(this);
        }
    }
}
