package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Dish;
import com.example.voting_system.repository.DishRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.voting_system.web.RestValidation.assureIdConsistent;
import static com.example.voting_system.web.RestValidation.checkNew;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
public class AdminDishController {
    static final String REST_URL = "/api/admin/dishes";

    private DishRepository repository;

    @GetMapping
    public List<Dish> getAll() {
        log.info("getAll dishes for admin");
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name", "price"));
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get menu for dish {}", id);
        return repository.getExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish) {
        log.info("create {}", dish);
        checkNew(dish);
        Dish created = repository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        repository.save(dish);
    }
}
