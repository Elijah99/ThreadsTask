package com.epam.task.sixth.entity;


import java.util.concurrent.atomic.AtomicReference;

public class Participant {
    protected int id;
    protected AtomicReference<Double> money;

    public Participant() {
    }

    public Participant(int id, double money) {
        this.id = id;
        this.money = new AtomicReference<Double>(money);
    }

    public int getId() {
        return id;
    }


    public double getMoney() {
        return money.get();
    }

    public void setMoney(double money) {
        this.money = new AtomicReference<Double>(money);
    }
}
