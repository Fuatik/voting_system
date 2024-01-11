package com.example.voting_system.web.restaurant;

import com.example.voting_system.error.NotFoundException;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.repository.RestaurantRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.voting_system.web.RestValidation.assureIdConsistent;
import static com.example.voting_system.web.RestValidation.checkNew;

/**
 * Controller for handling administrative operations related to restaurants.
 *
 * <p>The controller provides endpoints for retrieving, creating, updating, and deleting restaurant entities.
 * All operations are performed under the "/api/admin/restaurants" base URL.
 */
@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminRestaurantController {
    static final String REST_URL = "/api/admin/restaurants";

    private RestaurantRepository repository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Restaurant> getAll() {
        log.info("Admin getAll Restaurants");
        return repository.findAllRestaurantsWithMenuAndVotes()
                .orElseThrow(() -> new NotFoundException("Restaurants were not found"));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Restaurant get(@PathVariable int id) {
        log.info("Admin get menu for Restaurant with id={}", id);
        return repository.getRestaurantWithMenuAndVotesById(id).orElseThrow(
                () -> new NotFoundException("menu for Restaurant not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("Admin delete Restaurant with id={}", id);
        repository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("Admin create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = repository.save(new Restaurant(restaurant.getName()));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("Admin update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }
}
