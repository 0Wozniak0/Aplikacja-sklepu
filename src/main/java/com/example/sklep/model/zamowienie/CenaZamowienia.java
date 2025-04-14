package com.example.sklep.model.zamowienie;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CenaZamowienia {

    @Id
    @GeneratedValue
    private Long id;

    private Long zamowienieId; // ID zamówienia
    private BigDecimal lacznaCena; // Łączna cena

    public CenaZamowienia(Long zamowienieId, BigDecimal lacznaCena) {
        this.zamowienieId = zamowienieId;
        this.lacznaCena = lacznaCena;
    }
}
