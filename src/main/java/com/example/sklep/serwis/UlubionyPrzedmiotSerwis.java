package com.example.sklep.serwis;

import com.example.sklep.dane.UlubionyPrzedmiotDane;
import com.example.sklep.model.UlubionyPrzedmiot;
import com.example.sklep.model.Uzytkownik;
import com.example.sklep.model.Przedmiot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UlubionyPrzedmiotSerwis {

    private final UlubionyPrzedmiotDane ulubionyPrzedmiotDane;

    @Autowired
    public UlubionyPrzedmiotSerwis(UlubionyPrzedmiotDane ulubionyPrzedmiotDane) {
        this.ulubionyPrzedmiotDane = ulubionyPrzedmiotDane;
    }

    public void dodajUlubionyPrzedmiot(Uzytkownik uzytkownik, Przedmiot przedmiot) {
        if (!czyPrzedmiotIstnieje(uzytkownik, przedmiot.getId())) { // Sprawdzenie, czy przedmiot jest już ulubiony
            UlubionyPrzedmiot ulubionyPrzedmiot = new UlubionyPrzedmiot();
            ulubionyPrzedmiot.setUzytkownik(uzytkownik);
            ulubionyPrzedmiot.setPrzedmiot(przedmiot);
            ulubionyPrzedmiotDane.save(ulubionyPrzedmiot);
        }
    }

    @Transactional
    public void usunUlubionyPrzedmiot(Uzytkownik uzytkownik, Long przedmiotId) {
        ulubionyPrzedmiotDane.deleteByUzytkownik_IdAndPrzedmiot_Id(uzytkownik.getId(), przedmiotId);
    }

    public List<UlubionyPrzedmiot> pobierzUlubionePrzedmioty(Long uzytkownikId) {
        return ulubionyPrzedmiotDane.findByUzytkownik_Id(uzytkownikId);
    }

    public boolean czyPrzedmiotIstnieje(Uzytkownik uzytkownik, Long przedmiotId) {
        List<UlubionyPrzedmiot> ulubionePrzedmioty = pobierzUlubionePrzedmioty(uzytkownik.getId());
        return ulubionePrzedmioty.stream()
                .anyMatch(up -> up.getPrzedmiot().getId().equals(przedmiotId));
    }
}
