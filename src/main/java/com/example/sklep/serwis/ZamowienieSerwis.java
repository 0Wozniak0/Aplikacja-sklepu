package com.example.sklep.serwis;

import com.example.sklep.Koszyk;
import com.example.sklep.dane.zamowienie.DaneElementuZamowienia;
import com.example.sklep.dane.zamowienie.DaneZamowienia;
import com.example.sklep.dane.UzytkownikDane; // Dodaj repozytorium użytkownika
import com.example.sklep.dto.ZamowienieDto;
import com.example.sklep.konwerter.KonwerterZamowienia;
import com.example.sklep.model.Uzytkownik;
import com.example.sklep.model.zamowienie.CenaZamowienia;
import com.example.sklep.model.zamowienie.ElementZamowienia;
import com.example.sklep.model.zamowienie.Zamowienie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.sklep.dane.zamowienie.DaneCenyZamowienia;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ZamowienieSerwis {

    private final Koszyk koszyk;
    private final DaneZamowienia daneZamowienia;
    private final DaneElementuZamowienia daneElementuZamowienia;
    private final DaneCenyZamowienia daneCenyZamowienia;
    private final UzytkownikDane uzytkownikDane;

    @Autowired
    public ZamowienieSerwis(Koszyk koszyk, DaneZamowienia daneZamowienia,
                            DaneElementuZamowienia daneElementuZamowienia,
                            DaneCenyZamowienia daneCenyZamowienia,
                            UzytkownikDane uzytkownikDane) {
        this.koszyk = koszyk;
        this.daneZamowienia = daneZamowienia;
        this.daneElementuZamowienia = daneElementuZamowienia;
        this.daneCenyZamowienia = daneCenyZamowienia;
        this.uzytkownikDane = uzytkownikDane;
    }

    public void zapiszZamowienie(ZamowienieDto zamowienieDto, Long uzytkownikId) {
        Zamowienie zamowienie = KonwerterZamowienia.konwertujZamowienie(zamowienieDto, uzytkownikId);
        System.out.println("Zamówienie do zapisania: " + zamowienie);

        // Zapisz zamówienie
        daneZamowienia.save(zamowienie);

        // Zapisz elementy zamówienia
        List<ElementZamowienia> elementyZamowienia = KonwerterZamowienia.konwertujElementZamowieniaList(koszyk, zamowienie);
        daneElementuZamowienia.saveAll(elementyZamowienia);

        BigDecimal lacznaCena = koszyk.getSum();

        CenaZamowienia cenaZamowienia = new CenaZamowienia(zamowienie.getZamowienieId(), lacznaCena);
        daneCenyZamowienia.save(cenaZamowienia);

        if (uzytkownikId != null) {
            przyznajPunkty(uzytkownikId, lacznaCena);
        }

        koszyk.wyczysckoszyk();
    }

    private void przyznajPunkty(Long uzytkownikId, BigDecimal lacznaCena) {
        int punkty = lacznaCena.intValue() / 10; // 1 punkt za każde 5 zł
        uzytkownikDane.findById(uzytkownikId).ifPresent(uzytkownik -> {
            uzytkownik.setPunkty(uzytkownik.getPunkty() + punkty);
            uzytkownikDane.save(uzytkownik);
        });
    }

    public List<ElementZamowienia> pobierzHistorieZamowien(Long uzytkownikId) {
        return daneElementuZamowienia.findByUzytkownikId(uzytkownikId);
    }

    public BigDecimal pobierzLacznaCene(Long zamowienieId) {
        CenaZamowienia cenaZamowienia = daneCenyZamowienia.findByZamowienieId(zamowienieId);
        return cenaZamowienia != null ? cenaZamowienia.getLacznaCena() : BigDecimal.ZERO;
    }
    public int obliczPunktyZaZakupy(Long uzytkownikId) {
        BigDecimal lacznaCena = koszyk.getSum();
        return lacznaCena.intValue() / 10; // 1 punkt za każde 5 zł
    }

    private final BigDecimal wartoscPunktu = new BigDecimal("0.10"); // Wartość 1 punktu to 0,20 PLN

    public BigDecimal obliczZnizkeZaPunkty(int punkty) {
        return wartoscPunktu.multiply(new BigDecimal(punkty)); // Wartość zniżki z punktów
    }

    public void zmniejszPunkty(Uzytkownik uzytkownik, int punktyDoWykorzystania) {
        uzytkownik.setPunkty(uzytkownik.getPunkty() - punktyDoWykorzystania);
        uzytkownikDane.save(uzytkownik); // Aktualizacja użytkownika w bazie danych
    }

}
