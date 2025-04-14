package com.example.sklep.serwis;

import com.example.sklep.dane.SpecyfikacjaDane;
import com.example.sklep.model.Specyfikacja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecyfikacjaSerwis {

    private final SpecyfikacjaDane specyfikacjaDane; // Zależność do specyfikacji

    @Autowired
    public SpecyfikacjaSerwis(SpecyfikacjaDane specyfikacjaDane) {
        this.specyfikacjaDane = specyfikacjaDane;
    }

    public List<Specyfikacja> findByPrzedmiotId(Long przedmiotId) {
        return specyfikacjaDane.findByPrzedmiotId(przedmiotId);
    }

    public void dodajSpecyfikacje(Long przedmiotId, String nazwa, String wartosc) {
        Specyfikacja specyfikacja = new Specyfikacja();
        specyfikacja.setPrzedmiotId(przedmiotId);
        specyfikacja.setNazwa(nazwa);
        specyfikacja.setWartosc(wartosc);
        specyfikacjaDane.save(specyfikacja);
    }

    public Optional<Specyfikacja> findById(Long id) {
        return specyfikacjaDane.findById(id);
    }

    public void zapiszEdytowanaSpecyfikacje(Long id, Long przedmiotId, String nazwa, String wartosc) {
        Specyfikacja specyfikacja = specyfikacjaDane.findById(id)
                .orElseThrow(() -> new RuntimeException("Specyfikacja nie znaleziona"));

        specyfikacja.setNazwa(nazwa);
        specyfikacja.setWartosc(wartosc);
        specyfikacja.setPrzedmiotId(przedmiotId);
        specyfikacjaDane.save(specyfikacja);
    }

    public Long usunSpecyfikacje(Long id) {
        Specyfikacja specyfikacja = specyfikacjaDane.findById(id)
                .orElseThrow(() -> new RuntimeException("Specyfikacja nie znaleziona"));
        Long przedmiotId = specyfikacja.getPrzedmiotId();
        specyfikacjaDane.delete(specyfikacja);
        return przedmiotId;
    }
}
