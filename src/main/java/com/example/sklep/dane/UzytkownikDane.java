package com.example.sklep.dane;

import com.example.sklep.model.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UzytkownikDane extends JpaRepository<Uzytkownik, Long> {
    Optional<Uzytkownik> findByLogin(String login);
}
