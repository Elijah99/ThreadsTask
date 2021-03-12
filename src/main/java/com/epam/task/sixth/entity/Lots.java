package com.epam.task.sixth.entity;

import java.util.List;

public class Lots {
    private List<Lot> lots;

    public Lots(List<Lot> lots) {
        this.lots = lots;
    }

    public List<Lot> getLots() {
        return lots;
    }
}
