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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.voting_system.util.EntityMapper.getTo;
import static com.example.voting_system.web.restaurant.UserVoteController.REST_URL;

@RestController
@RequestMapping(value = REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class UserVoteController {
    static final String REST_URL = "/api/restaurants";

    private final VoteRepository voteRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @GetMapping
    @Transactional
    public List<RestaurantTo> getAll() {
        log.info("getAll restaurants");
        List<Restaurant> restaurants = restaurantRepository.findAllWithMenus(LocalDate.now()).orElseThrow(
                () -> new NotFoundException("Restaurants were not found")
        );
        List<RestaurantTo> restaurantTos = new ArrayList<>();
        restaurants.forEach(restaurant -> {
            RestaurantTo restaurantTo = getTo(restaurant);
            int countVotes = voteRepository.countByRestaurantIdAndVoteDate(restaurant.getId(), LocalDate.now());
            restaurantTo.setCountVotes(countVotes);
            restaurantTos.add(restaurantTo);
        });
        return restaurantTos;
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id) {
        log.info("get menu for restaurant {}", id);
        Restaurant restaurant = restaurantRepository.getRestaurantWithMenuAndVotesById(id, LocalDate.now()).orElseThrow(
                () -> new NotFoundException("Restaurant '" + id + "' was not found")
        );
        return getTo(restaurant);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void vote(@PathVariable int id, @AuthenticationPrincipal AuthUser authUser) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadLine = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),11, 0);
        User user = authUser.getUser();
        try {
            if (now.isBefore(deadLine)) {
                Optional<Vote> existingVote = voteRepository.findByUserIdAndVoteDate(
                        user.id(), now.toLocalDate());

                if (existingVote.isPresent()) {
                    // User already voted, consider it as a change of mind
                    Vote vote = existingVote.get();
                    Restaurant newRestaurant = restaurantRepository.getExisted(id);

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
                voteRepository.flush();
            } else {
                throw new VoteException("It's too late to change your vote for today.");
            }
        } catch (VoteException e) {
            throw e;
        } catch (Exception e) {
            throw new VoteException("Error processing vote: " + e);
        }
    }
}
