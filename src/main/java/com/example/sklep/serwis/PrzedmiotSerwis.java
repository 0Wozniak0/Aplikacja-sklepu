package com.example.sklep.serwis;

import com.example.sklep.model.Przedmiot;
import com.example.sklep.dane.PrzedmiotDane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrzedmiotSerwis {

    private final PrzedmiotDane przedmiotDane;

    @Autowired
    public PrzedmiotSerwis(PrzedmiotDane przedmiotDane) {
        this.przedmiotDane = przedmiotDane;
    }

    public Przedmiot findById(Long id) {
        Optional<Przedmiot> przedmiot = przedmiotDane.findById(id);
        return przedmiot.orElse(null);
    }

}
