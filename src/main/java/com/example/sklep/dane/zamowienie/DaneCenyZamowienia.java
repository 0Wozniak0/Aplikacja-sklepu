package com.example.sklep.dane.zamowienie;

import com.example.sklep.model.zamowienie.CenaZamowienia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DaneCenyZamowienia extends JpaRepository<CenaZamowienia, Long> {
    CenaZamowienia findByZamowienieId(Long zamowienieId);
}
