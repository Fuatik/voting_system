package com.example.voting_system.model.restaurant;

import com.example.voting_system.model.BaseEntity;
import com.example.voting_system.model.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vote", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "vote_date"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"user", "restaurant"})
public class Vote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "user must not be null")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull(message = "restaurant must not be null")
    @JsonIgnore
    private Restaurant restaurant;

    @Column(name = "vote_date",nullable = false)
    @NotNull(message = "date must not be null")
    private LocalDate voteDate;

    @Column(name = "vote_time",nullable = false)
    @NotNull(message = "time must not be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime voteTime;
}
