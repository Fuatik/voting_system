package com.example.voting_system.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;


@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo{
    Integer restaurantId;
    LocalDate voteDate;
    LocalTime voteTime;

    public VoteTo(Integer id, Integer restaurantId, LocalDate voteDate, LocalTime voteTime) {
        super(id);
        this.restaurantId = restaurantId;
        this.voteDate = voteDate;
        this.voteTime = voteTime;
    }
}
