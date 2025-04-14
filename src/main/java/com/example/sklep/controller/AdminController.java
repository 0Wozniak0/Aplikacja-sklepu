package com.example.sklep.controller;

import com.example.sklep.dane.KategoriaDane;
import com.example.sklep.dane.PrzedmiotDane;
import com.example.sklep.model.Przedmiot;
import com.example.sklep.model.Specyfikacja;
import com.example.sklep.serwis.SpecyfikacjaSerwis; // Nowy serwis
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final PrzedmiotDane przedmiotDane;
    private final SpecyfikacjaSerwis specyfikacjaSerwis;
    private final KategoriaDane kategoriaDane;

    @Autowired
    public AdminController(PrzedmiotDane przedmiotDane, SpecyfikacjaSerwis specyfikacjaSerwis,
                           KategoriaDane kategoriaDane) {
        this.przedmiotDane = przedmiotDane;
        this.specyfikacjaSerwis = specyfikacjaSerwis;
        this.kategoriaDane = kategoriaDane;
    }

    @GetMapping
    public String stronaAdmina(Model model) {
        model.addAttribute("przedmioty", przedmiotDane.findAll());
        model.addAttribute("kategorie", kategoriaDane.findAll());
        return "admin/dodajPrzedmiot";
    }

    @PostMapping
    public String dodajPrzedmiot(Przedmiot przedmiot, @RequestParam("kategoriaId") Long kategoriaId)
                                  {
        przedmiot.setKategoria(kategoriaDane.findById(kategoriaId).orElseThrow());
        przedmiotDane.save(przedmiot);
        return "redirect:/admin";
    }

    @GetMapping("/zmienDostepnosc/{id}")
    public String zmienDostepnoscPrzedmiotu(@PathVariable("id") Long id) {
        Przedmiot przedmiot = przedmiotDane.findById(id).orElseThrow(() -> new RuntimeException("Przedmiot nie znaleziony"));
        przedmiot.setDostepny(!przedmiot.isDostepny()); // Przełączenie stanu dostępności
        przedmiotDane.save(przedmiot);
        return "redirect:/admin";
    }

    @GetMapping("/zmienNowosc/{id}")
    public String zmienNowoscPrzedmiotu(@PathVariable("id") Long id) {
        Przedmiot przedmiot = przedmiotDane.findById(id)
                .orElseThrow(() -> new RuntimeException("Przedmiot nie znaleziony"));
        przedmiot.setNowosc(!przedmiot.isNowosc()); // Przełączenie stanu nowości
        przedmiotDane.save(przedmiot);
        return "redirect:/admin";
    }


    @GetMapping("/search")
    public String wyszukajPrzedmiot(@RequestParam("query") String query, Model model) {
        List<Przedmiot> znalezionePrzedmioty = przedmiotDane.findByNazwaContainingIgnoreCase(query);
        model.addAttribute("przedmioty", znalezionePrzedmioty);
        return "admin/dodajPrzedmiot";
    }

    @GetMapping("/specyfikacje/{id}")
    public String zarzadzajSpecyfikacjami(@PathVariable("id") Long id, Model model) {
        Przedmiot przedmiot = przedmiotDane.findById(id)
                .orElseThrow(() -> new RuntimeException("Przedmiot nie znaleziony"));
        List<Specyfikacja> specyfikacje = specyfikacjaSerwis.findByPrzedmiotId(id); // Przeniesiono do serwisu

        model.addAttribute("przedmiot", przedmiot);
        model.addAttribute("specyfikacje", specyfikacje);
        return "admin/specyfikacje";
    }

    @PostMapping("/specyfikacje")
    public String dodajSpecyfikacje(@RequestParam("przedmiotId") Long przedmiotId,
                                    @RequestParam("nazwa") String nazwa,
                                    @RequestParam("wartosc") String wartosc,
                                    Model model) {
        if (!przedmiotDane.existsById(przedmiotId)) {
            model.addAttribute("blad", "Nie można dodać specyfikacji - przedmiot o podanym ID nie istnieje.");
            model.addAttribute("przedmioty", przedmiotDane.findAll());
            return "admin/dodajPrzedmiot";
        }

        specyfikacjaSerwis.dodajSpecyfikacje(przedmiotId, nazwa, wartosc); // Przeniesiono do serwisu
        return "redirect:/admin/specyfikacje/" + przedmiotId;
    }

    @GetMapping("/specyfikacja/edytuj/{id}")
    public String edytujSpecyfikacje(@PathVariable("id") Long id, Model model) {
        Specyfikacja specyfikacja = specyfikacjaSerwis.findById(id)
                .orElseThrow(() -> new RuntimeException("Specyfikacja nie znaleziona"));
        model.addAttribute("specyfikacja", specyfikacja);
        return "admin/edytujSpecyfikacje";
    }

    @PostMapping("/specyfikacja/edytuj")
    public String zapiszEdytowanaSpecyfikacje(@RequestParam("id") Long id,
                                              @RequestParam("przedmiotId") Long przedmiotId,
                                              @RequestParam("nazwa") String nazwa,
                                              @RequestParam("wartosc") String wartosc) {
        specyfikacjaSerwis.zapiszEdytowanaSpecyfikacje(id, przedmiotId, nazwa, wartosc); // Przeniesiono do serwisu
        return "redirect:/admin/specyfikacje/" + przedmiotId;
    }

    @GetMapping("/specyfikacja/usun/{id}")
    public String usunSpecyfikacje(@PathVariable("id") Long id) {
        Long przedmiotId = specyfikacjaSerwis.usunSpecyfikacje(id); // Przeniesiono do serwisu
        return "redirect:/admin/specyfikacje/" + przedmiotId;
    }

    @PostMapping("/zmienCene")
    public String zmienCene(@RequestParam("id") Long id, @RequestParam("cena") BigDecimal cena) {
        Przedmiot przedmiot = przedmiotDane.findById(id).orElseThrow();
        przedmiot.setCena(cena); // Upewnij się, że masz setter dla ceny
        przedmiotDane.save(przedmiot);
        return "redirect:/admin"; // Przekierowanie do strony admina po zmianie ceny
    }


    // Metoda do wyświetlania formularza dodawania promocji dla konkretnego przedmiotu
    @GetMapping("/promocje/{id}")
    public String dodajPromocje(@PathVariable("id") Long id, Model model) {
        Przedmiot przedmiot = przedmiotDane.findById(id).orElseThrow();
        model.addAttribute("przedmiot", przedmiot);
        return "admin/dodajPromocje"; // Nowy widok do dodawania promocji
    }


    @PostMapping("/promocje/dodaj")
    public String dodajPromocje(@RequestParam("id") Long id,
                                @RequestParam("cenaPromocyjna") BigDecimal cenaPromocyjna) {
        Przedmiot przedmiot = przedmiotDane.findById(id).orElseThrow();
        przedmiot.setCenaPromocyjna(cenaPromocyjna); // Upewnij się, że masz setter dla ceny promocyjnej
        przedmiotDane.save(przedmiot);
        return "redirect:/admin"; // Przekierowanie do strony admina po dodaniu promocji
    }

    // Endpoint do usuwania promocji
    @PostMapping("/promocje/usun")
    public String usunPromocje(@RequestParam("id") Long id) {
        Przedmiot przedmiot = przedmiotDane.findById(id).orElseThrow();
        przedmiot.setCenaPromocyjna(null); // Ustawienie ceny promocyjnej na null
        przedmiotDane.save(przedmiot);
        return "redirect:/admin"; // Przekierowanie do strony admina po usunięciu promocji
    }

    @GetMapping("/zmienOkazjaDnia/{id}")
    public String zmienOkazjaDnia(@PathVariable("id") Long id) {
        Przedmiot przedmiot = przedmiotDane.findById(id).orElseThrow(() -> new RuntimeException("Przedmiot nie znaleziony"));
        przedmiot.setOkazjaDnia(!przedmiot.isOkazjaDnia()); // Przełączenie statusu
        przedmiotDane.save(przedmiot);
        return "redirect:/admin/promocje/" + id; // Powrót do strony zarządzania promocją
    }


}
