package com.example.sklep.dane.zamowienie;

import com.example.sklep.model.zamowienie.Zamowienie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DaneZamowienia extends JpaRepository<Zamowienie, Long> {

}
