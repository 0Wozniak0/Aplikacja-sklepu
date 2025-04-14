package com.example.sklep.model.zamowienie;

import com.example.sklep.model.Przedmiot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ElementZamowienia {

    @Id
    @GeneratedValue
    private Long elementZamowieniaId;

    private Long zamowienieId; // ID zamówienia
    private int licznik; // Ilość
    private BigDecimal cena; // Cena
    private LocalDateTime created; // Data utworzenia
    private Long uzytkownikId; // ID użytkownika

    @ManyToOne // Relacja do przedmiotu
    @JoinColumn(name = "przedmiot_id") // Kolumna w bazie danych
    private Przedmiot przedmiot;

    // Konstruktor z parametrami
    public ElementZamowienia(Long zamowienieId, int licznik, BigDecimal cena, LocalDateTime created, Long uzytkownikId, Przedmiot przedmiot) {
        this.zamowienieId = zamowienieId;
        this.licznik = licznik;
        this.cena = cena;
        this.created = created; // Ustaw wartość created
        this.uzytkownikId = uzytkownikId; // Ustaw wartość uzytkownikId
        this.przedmiot = przedmiot; // Ustaw przedmiot
    }
}
