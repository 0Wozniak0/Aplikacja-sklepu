package com.example.sklep.dane;

import com.example.sklep.model.UlubionyPrzedmiot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UlubionyPrzedmiotDane extends JpaRepository<UlubionyPrzedmiot, Long> {
    List<UlubionyPrzedmiot> findByUzytkownik_Id(Long uzytkownikId);
    void deleteByUzytkownik_IdAndPrzedmiot_Id(Long uzytkownikId, Long przedmiotId);
}
