package com.example.voting_system.model.restaurant;

import com.example.voting_system.model.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "menu", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"restaurant_id", "menu_date"})
})
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"restaurant", "menuItems"})
public class Menu extends BaseEntity {

    @Column(name = "menu_date", nullable = false)
    @NotNull(message = "date must not be null")
    private LocalDate menuDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @BatchSize(size = 10)
    private Set<MenuItem> menuItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Restaurant restaurant;

    public Menu(Integer id, LocalDate menuDate, Set<MenuItem> menuItems, Restaurant restaurant) {
        super(id);
        this.menuDate = menuDate;
        this.menuItems = menuItems;
        this.restaurant = restaurant;
    }
}
