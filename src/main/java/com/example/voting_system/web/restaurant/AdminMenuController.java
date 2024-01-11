package com.example.voting_system.web.restaurant;

import com.example.voting_system.model.restaurant.Dish;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.repository.DishRepository;
import com.example.voting_system.repository.RestaurantRepository;
import com.example.voting_system.to.DishTo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.example.voting_system.web.RestValidation.assureIdConsistent;
import static com.example.voting_system.web.RestValidation.checkNew;
import static com.example.voting_system.web.restaurant.AdminRestaurantController.REST_URL;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL_MENU, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminMenuController {
    static final String REST_URL_MENU = REST_URL + "/{restaurantId}/menu";

    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DishTo> getMenu(@PathVariable int restaurantId) {
        Restaurant restaurant = restaurantRepository.getExisted(restaurantId);
        return restaurant.getMenu().stream()
                .map(this::getTo)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Dish getDishInMenu(@PathVariable int id, @PathVariable String restaurantId) {
        log.info("get Dish with id={} in menu for Restaurant with id={}", id, restaurantId);
        return dishRepository.getExisted(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDishFromMenu(@PathVariable int id, @PathVariable String restaurantId) {
        log.info("remove Dish with id={} from menu for Restaurant with id={}", id, restaurantId);
        dishRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> addDishToMenu(@Valid @RequestBody Dish dish, @PathVariable String restaurantId) {
        log.info("add {} to menu for Restaurant with id={}", dish, restaurantId);
        checkNew(dish);

        int id = Integer.parseInt(restaurantId);

        Restaurant restaurant = restaurantRepository.getExisted(id);
        assureIdConsistent(restaurant, id);

        dish.setRestaurant(restaurant);

        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{restaurantId}" + "/menu" + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDishInMenu(@Valid @RequestBody Dish dish, @PathVariable int id, @PathVariable String restaurantId) {
        log.info("update {} with id={} in menu for Restaurant with id={}", dish, id, restaurantId);
        assureIdConsistent(dish, id);
        dishRepository.save(dish);
    }

    private DishTo getTo(Dish dish) {
        return new DishTo(dish.id(), dish.getName(), dish.getPrice());
    }
}
