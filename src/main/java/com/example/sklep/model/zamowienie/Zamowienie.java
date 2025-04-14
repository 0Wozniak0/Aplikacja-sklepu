package com.example.sklep.model.zamowienie;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "zamowienia")
public class Zamowienie {

    @Id
    @GeneratedValue
    private Long zamowienieId;
    private String imie;
    private String nazwisko;
    private String adres;
    private String kodPocztowy;
    private String miasto;
    private LocalDateTime created;
    private Long uzytkownikId;

    @OneToMany
    @JoinColumn(name = "zamowienieId")
    private List<ElementZamowienia> elementZamowien;
}
