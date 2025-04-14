package com.example.sklep.serwis;

import com.example.sklep.Koszyk;
import com.example.sklep.OperacjeNaPrzedmiocie;
import com.example.sklep.dane.PrzedmiotDane;
import com.example.sklep.model.Przedmiot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KoszykSerwis {

    private final PrzedmiotDane przedmiotDane;
    private final Koszyk koszyk;

    @Autowired
    public KoszykSerwis(PrzedmiotDane przedmiotDane, Koszyk koszyk) {
        this.przedmiotDane = przedmiotDane;
        this.koszyk = koszyk;
    }

    public List<Przedmiot> getAllPrzedmioty() {
        return przedmiotDane.findAll();
    }

    public List<Przedmiot> searchPrzedmioty(String query) {
        return przedmiotDane.findAll().stream()
                .filter(przedmiot -> przedmiot.getNazwa().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
    }

    public void operacjeNaPrzedmiocie(Long przedmiotId, OperacjeNaPrzedmiocie operacjeNaPrzedmiocie) {
        Optional<Przedmiot> oPrzedmiot = przedmiotDane.findById(przedmiotId);
        if (oPrzedmiot.isPresent()) {
            Przedmiot przedmiot = oPrzedmiot.get();
            switch (operacjeNaPrzedmiocie) {
                case ZWIEKSZ -> koszyk.zwiekszPrzedmiot(przedmiot);
                case ZMNIEJSZ -> koszyk.zmniejszPrzedmiot(przedmiot);
                case USUN -> koszyk.usunPrzedmioty(przedmiot);
                default -> throw new IllegalArgumentException();
            }
        }
    }
    public void pomniejszSume(BigDecimal znizka) {
        koszyk.setSum(koszyk.getSum().subtract(znizka));
    }

}
