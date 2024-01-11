package com.example.voting_system.web.restaurant;

import com.example.voting_system.error.NotFoundException;
import com.example.voting_system.error.VoteException;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.model.restaurant.Vote;
import com.example.voting_system.model.user.User;
import com.example.voting_system.repository.RestaurantRepository;
import com.example.voting_system.repository.UserRepository;
import com.example.voting_system.repository.VoteRepository;
import com.example.voting_system.to.RestaurantTo;
import com.example.voting_system.web.AuthUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.voting_system.web.RestValidation.assureIdConsistent;
import static com.example.voting_system.web.restaurant.UserVoteController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserVoteController {
    static final String REST_URL = "/api/restaurants";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    private LocalTime endVotingTime;

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        log.info("User {} votes for a Restaurant with id={}", authUser, id);
        LocalDateTime now = LocalDateTime.now();

        User user = authUser.getUser();
        try {
            if (now.toLocalTime().isBefore(endVotingTime)) {
                Optional<Vote> existingVote = voteRepository.findByUserIdAndVoteDate(
                        user.id(), now.toLocalDate());

                if (existingVote.isPresent()) {
                    // User already voted, consider it as a change of mind
                    Vote vote = existingVote.get();
                    Restaurant newRestaurant = restaurantRepository.getExisted(id);

                    assureIdConsistent(newRestaurant, id);

                    vote.setRestaurant(newRestaurant);
                    vote.setVoteDate(LocalDate.now());
                    vote.setVoteTime(LocalTime.now());

                    voteRepository.save(vote);
                } else {
                    // New vote
                    Vote newVote = new Vote(
                            userRepository.getExisted(user.getId()),
                            restaurantRepository.getExisted(id),
                            now.toLocalDate(), now.toLocalTime());

                    voteRepository.save(newVote);
                }
            } else {
                throw new VoteException("It's too late to change your vote for today.");
            }
        } catch (VoteException e) {
            throw e;
        } catch (Exception e) {
            throw new VoteException("Error processing vote: " + e);
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Cacheable("restaurants")
    public List<RestaurantTo> getAll() {
        log.info("get all Restaurants to vote");
        return restaurantRepository.findAllWithMenusByDate(LocalDate.now())
                .orElseThrow(() -> new NotFoundException("Restaurants were not found"))
                .stream()
                .map(this::getToWithRating)
                .collect(Collectors.toList());
    }

    private RestaurantTo getToWithRating(Restaurant restaurant) {
        int id = restaurant.id();
        int rating = voteRepository.countByRestaurantIdAndVoteDate(id, LocalDate.now()).orElse(0);
        return new RestaurantTo(
                restaurant.id(),
                restaurant.getName(),
                restaurant.getMenu(),
                rating);
    }
}
