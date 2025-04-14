package com.example.sklep.konwerter;

import com.example.sklep.Koszyk;
import com.example.sklep.PozycjawKoszyku;
import com.example.sklep.dto.ZamowienieDto;
import com.example.sklep.model.Przedmiot;
import com.example.sklep.model.zamowienie.ElementZamowienia;
import com.example.sklep.model.zamowienie.Zamowienie;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KonwerterZamowienia {

    public static Zamowienie konwertujZamowienie(ZamowienieDto dto, Long uzytkownikId) {
        return Zamowienie.builder()
                .imie(dto.getImie())
                .nazwisko(dto.getNazwisko())
                .adres(dto.getAdres())
                .kodPocztowy(dto.getKodPocztowy())
                .miasto(dto.getMiasto())
                .created(LocalDateTime.now())
                .uzytkownikId(uzytkownikId)
                .build();
    }

    public static List<ElementZamowienia> konwertujElementZamowieniaList(Koszyk koszyk, Zamowienie zamowienie) {
        List<ElementZamowienia> elementy = new ArrayList<>();
        for (PozycjawKoszyku pozycjawKoszyku : koszyk.getPozycjewKoszyku()) {
            ElementZamowienia element = new ElementZamowienia(
                    zamowienie.getZamowienieId(),
                    pozycjawKoszyku.getLicznik(),
                    pozycjawKoszyku.getCena(),
                    zamowienie.getCreated(), // Ustaw wartość created
                    zamowienie.getUzytkownikId(), // Ustaw wartość uzytkownikId
                    pozycjawKoszyku.getPrzedmiot() // Ustaw przedmiot
            );
            elementy.add(element);
        }
        return elementy;
    }

}

