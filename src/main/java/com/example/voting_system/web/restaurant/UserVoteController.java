package com.example.voting_system.web.restaurant;

import com.example.voting_system.error.IllegalRequestDataException;
import com.example.voting_system.error.NotFoundException;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.model.restaurant.Vote;
import com.example.voting_system.model.user.User;
import com.example.voting_system.repository.RestaurantRepository;
import com.example.voting_system.repository.VoteRepository;
import com.example.voting_system.to.RestaurantTo;
import com.example.voting_system.to.VoteTo;
import com.example.voting_system.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.voting_system.web.restaurant.UserVoteController.REST_URL;

/**
 * Controller for handling user votes on restaurants.
 *
 * <p>The controller provides endpoints for users to vote on their preferred restaurants.
 * It also retrieves a list of all restaurants with their current ratings.
 * The voting is restricted to a specific time frame determined by the configured endVotingTime.
 * * All operations are performed under the "/api/restaurants" base URL.
 */
@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserVoteController {
    static final String REST_URL = "/api/restaurants";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;

    @Setter
    private LocalTime endVotingTime;

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("User {} votes for a Restaurant with id={}", authUser, id);
        LocalDateTime now = LocalDateTime.now();

        User user = authUser.getUser();
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Restaurant not found with id=" + id));

        Vote existingVote = voteRepository.findByUserIdAndVoteDate(user.getId(), now.toLocalDate())
                .orElse(null);

        if (existingVote != null) {
            if (now.toLocalTime().isBefore(endVotingTime)) {
                // User already voted, consider it as a change of mind
                existingVote.setRestaurant(restaurant);
                existingVote.setVoteDate(now.toLocalDate());
                existingVote.setVoteTime(now.toLocalTime());

                voteRepository.save(existingVote);
            } else {
                throw new IllegalRequestDataException("It's too late to change your vote for today.");
            }
        } else {
            // New vote
            Vote newVote = new Vote(user, restaurant, now.toLocalDate(), now.toLocalTime());
            voteRepository.save(newVote);
        }
    }

    @GetMapping
    @Cacheable("restaurants")
    public List<RestaurantTo> getAll() {
        log.info("get all Restaurants to vote");
        return restaurantRepository.findAllWithMenusByDate(LocalDate.now())
                .stream()
                .map(this::getTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/votes/today")
    public VoteTo getTodayVoteForUser(@AuthenticationPrincipal AuthUser authUser) {
        log.info("User {} requests his vote for today", authUser);
        User user = authUser.getUser();
        LocalDateTime now = LocalDateTime.now();
        Vote todayVote = voteRepository.findByUserIdAndVoteDate(user.getId(), now.toLocalDate())
                .orElseThrow(() -> new NotFoundException("Vote not found for user " + user.getId() + " for today"));
        return new VoteTo(todayVote.getId(), todayVote.getRestaurant().getId(), todayVote.getVoteDate(), todayVote.getVoteTime());
    }

    @GetMapping("/votes")
    public List<VoteTo> getVotesByUserId(@AuthenticationPrincipal AuthUser authUser) {
        log.info("User {} requests his vote history", authUser);
        User user = authUser.getUser();
        List<Vote> userVotes = voteRepository.findAllByUserId(user.getId());
        return userVotes.stream()
                .map(vote -> new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getVoteDate(), vote.getVoteTime()))
                .collect(Collectors.toList());
    }

    private RestaurantTo getTo(Restaurant restaurant) {
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getMenus());
    }
}
