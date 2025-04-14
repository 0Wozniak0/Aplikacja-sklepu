package com.example.sklep.controller;

import com.example.sklep.OperacjeNaPrzedmiocie;
import com.example.sklep.dane.KategoriaDane;
import com.example.sklep.dane.PrzedmiotDane;
import com.example.sklep.model.Kategoria;
import com.example.sklep.model.Przedmiot;
import com.example.sklep.serwis.KoszykSerwis;
import com.example.sklep.serwis.PrzedmiotSerwis;
import com.example.sklep.serwis.SpecyfikacjaSerwis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class GlownaController {

    private final KoszykSerwis koszykSerwis;
    private final SpecyfikacjaSerwis specyfikacjaSerwis;
    private final PrzedmiotDane przedmiotDane;
    private final KategoriaDane kategoriaDane;
    private final PrzedmiotSerwis przedmiotSerwis;

    @Autowired
    public GlownaController(KoszykSerwis koszykSerwis, SpecyfikacjaSerwis specyfikacjaSerwis,
                            PrzedmiotDane przedmiotDane, KategoriaDane kategoriaDane, PrzedmiotSerwis przedmiotSerwis) {
        this.koszykSerwis = koszykSerwis;
        this.specyfikacjaSerwis = specyfikacjaSerwis;
        this.przedmiotDane = przedmiotDane;
        this.przedmiotSerwis = przedmiotSerwis;
        this.kategoriaDane = kategoriaDane;
    }

    @GetMapping("/")
    public String glowna(Model model) {
        model.addAttribute("przedmioty", koszykSerwis.getAllPrzedmioty());
        model.addAttribute("promocje", przedmiotDane.findTop6ByCenaPromocyjnaIsNotNull());
        model.addAttribute("nowosci", przedmiotDane.findTop6ByNowoscTrue());
        model.addAttribute("okazjaDnia",przedmiotDane.findByOkazjaDniaTrue());
        return "glowna";
    }

    @GetMapping("/promocje")
    public String wszystkiePromocje(Model model) {
        List<Przedmiot> promocje = przedmiotDane.findByCenaPromocyjnaIsNotNull(); // Zwróć wszystkie promocje
        model.addAttribute("przedmioty", promocje);
        return "promocje";
    }

    @GetMapping("/dodaj/{przedmiotId}")
    public String dodajDoKoszyka(@PathVariable("przedmiotId") Long przedmiotId,
                                 @RequestParam(value = "redirect", required = false) String redirect,
                                 @RequestParam(value = "kategoriaId", required = false) Long kategoriaId,
                                 Model model) {
        Przedmiot przedmiot = przedmiotDane.findById(przedmiotId)
                .orElseThrow(() -> new RuntimeException("Przedmiot nie znaleziony"));

        // Sprawdzenie dostępności przedmiotu
        if (!przedmiot.isDostepny()) {
            model.addAttribute("blad", "Ten przedmiot jest aktualnie niedostępny.");

            // Wyświetlenie odpowiedniego widoku w zależności od strony
            if ("przedmiot".equals(redirect)) {
                model.addAttribute("przedmiot", przedmiot);
                model.addAttribute("specyfikacje", specyfikacjaSerwis.findByPrzedmiotId(przedmiotId));
                return "przedmiot";
            } else if ("kategoria".equals(redirect) && kategoriaId != null) {
                return "redirect:/kategoria/" + kategoriaId;
            }

            model.addAttribute("przedmioty", koszykSerwis.getAllPrzedmioty());
            model.addAttribute("promocje", przedmiotDane.findTop6ByCenaPromocyjnaIsNotNull());
            model.addAttribute("nowosci", przedmiotDane.findTop6ByNowoscTrue());
            model.addAttribute("okazjaDnia",przedmiotDane.findByOkazjaDniaTrue());
            return "glowna";
        }

        // Jeśli produkt jest dostępny, dodaj do koszyka
        koszykSerwis.operacjeNaPrzedmiocie(przedmiotId, OperacjeNaPrzedmiocie.ZWIEKSZ);

        // Powrót na odpowiednią stronę po dodaniu
        if ("przedmiot".equals(redirect)) {
            model.addAttribute("przedmiot", przedmiot);
            model.addAttribute("specyfikacje", specyfikacjaSerwis.findByPrzedmiotId(przedmiotId));
            return "przedmiot";
        } else if ("kategoria".equals(redirect) && kategoriaId != null) {
            return "redirect:/kategoria/" + kategoriaId;
        }

        model.addAttribute("przedmioty", koszykSerwis.getAllPrzedmioty());
        model.addAttribute("promocje", przedmiotDane.findTop6ByCenaPromocyjnaIsNotNull());
        model.addAttribute("nowosci", przedmiotDane.findTop6ByNowoscTrue());
        model.addAttribute("okazjaDnia",przedmiotDane.findByOkazjaDniaTrue());
        return "glowna";
    }


    // Nowa metoda do obsługi wyszukiwania
    @GetMapping("/szukaj")
    public String szukaj(@RequestParam("query") String query, Model model) {
        model.addAttribute("przedmioty", koszykSerwis.searchPrzedmioty(query));
        model.addAttribute("promocje", przedmiotDane.findTop6ByCenaPromocyjnaIsNotNull());
        return "wyniki";
    }

    @GetMapping("/przedmiot/{id}")
    public String pokazPrzedmiot(@PathVariable("id") Long id, Model model) {
        Przedmiot przedmiot = przedmiotDane.findById(id)
                .orElseThrow(() -> new RuntimeException("Przedmiot nie znaleziony"));
        model.addAttribute("przedmiot", przedmiot);
        model.addAttribute("specyfikacje", specyfikacjaSerwis.findByPrzedmiotId(id));
        return "przedmiot";
    }

    @GetMapping("/kategoria/{kategoriaId}")
    public String pokazProduktyWKategorii(@PathVariable("kategoriaId") Long kategoriaId, Model model) {
        List<Przedmiot> przedmioty = przedmiotDane.findByKategoriaId(kategoriaId);
        Kategoria kategoria = kategoriaDane.findById(kategoriaId)
                .orElseThrow(() -> new RuntimeException("Kategoria nie znaleziona"));

        model.addAttribute("przedmioty", przedmioty);
        model.addAttribute("kategoria", kategoria);

        return "kategoria";
    }

    @GetMapping("/kategoria/{kategoriaId}/filtruj")
    public String filtrujPrzedmiotyWKategorii(
            @PathVariable("kategoriaId") Long kategoriaId,
            @RequestParam(value = "cenaOd", required = false) BigDecimal cenaOd,
            @RequestParam(value = "cenaDo", required = false) BigDecimal cenaDo,
            Model model) {

        List<Przedmiot> przedmioty = przedmiotDane.findByKategoriaIdAndCenaRange(kategoriaId, cenaOd, cenaDo);

        Kategoria kategoria = kategoriaDane.findById(kategoriaId)
                .orElseThrow(() -> new RuntimeException("Kategoria nie znaleziona"));

        model.addAttribute("przedmioty", przedmioty);
        model.addAttribute("kategoria", kategoria);

        return "kategoria";
    }

    @GetMapping("/nowosci")
    public String wszystkieNowosci(Model model) {
        model.addAttribute("przedmioty", przedmiotDane.findByNowoscTrue());
        return "nowosci";
    }

}

