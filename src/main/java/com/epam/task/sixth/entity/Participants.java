package com.epam.task.sixth.entity;

import java.util.List;

public class Participants {
    List<Participant> participants;

    public Participants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Participant> getParticipants() {
        return participants;
    }
}
