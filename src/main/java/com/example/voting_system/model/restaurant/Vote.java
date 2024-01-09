package com.example.voting_system.model.restaurant;

import com.example.voting_system.model.BaseEntity;
import com.example.voting_system.model.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Vote extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "restautant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    private LocalDateTime voteTime;
}
