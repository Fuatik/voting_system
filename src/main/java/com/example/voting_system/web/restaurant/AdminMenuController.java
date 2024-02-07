package com.example.voting_system.web.restaurant;

import com.example.voting_system.error.NotFoundException;
import com.example.voting_system.model.restaurant.Menu;
import com.example.voting_system.model.restaurant.Restaurant;
import com.example.voting_system.repository.MenuRepository;
import com.example.voting_system.to.MenuTo;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.voting_system.web.RestValidation.checkNew;
import static com.example.voting_system.web.restaurant.AdminRestaurantController.REST_URL;


/**
 * Controller for handling administrative operations related to restaurant menus.
 *
 * <p>The controller provides endpoints for retrieving, adding, updating, and removing dishes from a restaurant's menu.
 * All operations are performed under the "/api/restaurants/{restaurantId}/menus" base URL.
 */
@RestController
@RequestMapping(value = AdminMenuController.REST_URL_MENU, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminMenuController {
    static final String REST_URL_MENU = REST_URL + "/{restaurantId}/menus";

    private MenuRepository menuRepository;

    @GetMapping
    public List<MenuTo> getAllMenus(@PathVariable int restaurantId) {
        log.info("Admin getAll Menus for Restaurant with id={}", restaurantId);
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurantId);
        return menus.stream()
                .map(this::getMenuTo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{menuDate}")
    public MenuTo getMenuByDate(@PathVariable int restaurantId, @PathVariable LocalDate menuDate) {
        log.info("Admin get Menu for Restaurant with id={} on date {}", restaurantId, menuDate);
        Menu menu = menuRepository.findByRestaurantIdAndMenuDate(restaurantId, menuDate)
                .orElseThrow(() -> new NotFoundException("Menu not found for Restaurant with id=" + restaurantId + " on date " + menuDate));
        return getMenuTo(menu);
    }

    @DeleteMapping("/{menuDate}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", key = "#restaurantId")
    public void deleteMenu(@PathVariable int restaurantId, @PathVariable LocalDate menuDate) {
        log.info("Admin delete Menu for Restaurant with id={} on date {}", restaurantId, menuDate);
        menuRepository.findByRestaurantIdAndMenuDate(restaurantId, menuDate)
                .ifPresent(menuRepository::delete);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", key = "#restaurantId")
    public ResponseEntity<Menu> createMenu(@PathVariable int restaurantId, @Valid @RequestBody Menu menu) {
        log.info("Admin create Menu for Restaurant with id={}", restaurantId);
        checkNew(menu);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        menu.setRestaurant(restaurant);
        Menu created = menuRepository.save(menu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL_MENU + "/{menuId}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", key = "#restaurantId")
    public void updateMenu(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId, @RequestParam @Nullable LocalDate menuDate) {
        log.info("Admin update Menu for Restaurant with id={} on date {}", restaurantId, menuDate);

        Menu existingMenu = menuRepository.findByRestaurantIdAndMenuDate(restaurantId, menuDate)
                .orElseThrow(() -> new NotFoundException("Menu not found for Restaurant with id=" + restaurantId + " on date " + menuDate));

        existingMenu.setMenuDate(menuTo.getMenuDate());
        existingMenu.setMenuItems(menuTo.getMenuItems());

        menuRepository.save(existingMenu);
    }

    /**
     * Converts a Menu entity to a MenuTo transfer object.
     *
     * @param menu The Menu entity to be converted.
     * @return A MenuTo object containing relevant menu details.
     */
    private MenuTo getMenuTo(Menu menu) {
        return new MenuTo(menu.id(), menu.getMenuDate(), menu.getMenuItems());
    }
}
