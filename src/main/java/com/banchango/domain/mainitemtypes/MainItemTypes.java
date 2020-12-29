package com.banchango.domain.mainitemtypes;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "main_item_types")
public class MainItemTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MainItemType type;

    @Builder
    public MainItemTypes(MainItemType mainItemType) {
        this.type = mainItemType;
    }
}
