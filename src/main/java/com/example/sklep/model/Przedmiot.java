package com.example.sklep.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Przedmiot {

    @Id
    @GeneratedValue
    private Long id;
    private String nazwa;
    private BigDecimal cena;
    private BigDecimal cenaPromocyjna;
    private String imgUrl;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean dostepny = true;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean nowosc = false;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean okazjaDnia = false;


    @ManyToOne
    @JoinColumn(name = "kategoria_id")
    private Kategoria kategoria;


    public Przedmiot(String nazwa, BigDecimal cena, String imgUrl) {
        this.nazwa = nazwa;
        this.cena = cena;
        this.imgUrl = imgUrl;
    }
}


