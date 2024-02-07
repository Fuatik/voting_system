package com.example.voting_system.model.restaurant;

import com.example.voting_system.model.NamedEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

@Entity
@Getter
@Setter
@Table(name = "menu_item")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "menu")
public class MenuItem extends NamedEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 0)
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Menu menu;

    public MenuItem(Integer id, String name, Long price) {
        super(id, name);
        this.price = price;
    }
}
