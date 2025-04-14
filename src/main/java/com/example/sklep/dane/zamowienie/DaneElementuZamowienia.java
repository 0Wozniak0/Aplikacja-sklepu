package com.example.sklep.dane.zamowienie;

import com.example.sklep.model.zamowienie.ElementZamowienia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DaneElementuZamowienia extends JpaRepository<ElementZamowienia, Long> {
    @Query("SELECT e FROM ElementZamowienia e JOIN FETCH e.przedmiot WHERE e.uzytkownikId = ?1")
    List<ElementZamowienia> findByUzytkownikId(Long uzytkownikId);
}
