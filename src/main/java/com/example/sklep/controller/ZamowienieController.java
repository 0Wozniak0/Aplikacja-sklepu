package com.example.sklep.controller;

import com.example.sklep.dto.ZamowienieDto;
import com.example.sklep.model.Uzytkownik;
import com.example.sklep.serwis.ZamowienieSerwis;
import com.example.sklep.serwis.KoszykSerwis;
import com.example.sklep.OperacjeNaPrzedmiocie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/zamowienie")
public class ZamowienieController {

    private final KoszykSerwis koszykSerwis;
    private final ZamowienieSerwis zamowienieSerwis;

    @Autowired
    public ZamowienieController(KoszykSerwis koszykSerwis, ZamowienieSerwis zamowienieSerwis) {
        this.koszykSerwis = koszykSerwis;
        this.zamowienieSerwis = zamowienieSerwis;
    }

    @GetMapping("/koszyk")
    public String showCart(Model model, @SessionAttribute(value = "uzytkownik", required = false) Uzytkownik uzytkownik) {
        if (uzytkownik != null) {
            // Oblicz punkty za zakupy
            int punktyZaZakupy = zamowienieSerwis.obliczPunktyZaZakupy(uzytkownik.getId());

            // Oblicz punkty, które użytkownik już posiada
            int punkty = uzytkownik.getPunkty();

            // Oblicz zniżkę na podstawie posiadanych punktów
            BigDecimal znizka = zamowienieSerwis.obliczZnizkeZaPunkty(punkty);

            // Dodanie punktów i zniżki do modelu
            model.addAttribute("punktyZaZakupy", punktyZaZakupy);  // Punkty za bieżące zamówienie
            model.addAttribute("punkty", punkty);                  // Punkty użytkownika
            model.addAttribute("znizka", znizka);                  // Zniżka na podstawie punktów
        }
        return "koszykWidok";
    }


    @PostMapping("/wykorzystajPunkty")
    public String wykorzystajPunkty(@RequestParam int punktyDoWykorzystania, @SessionAttribute("uzytkownik") Uzytkownik uzytkownik) {
        BigDecimal znizka = zamowienieSerwis.obliczZnizkeZaPunkty(punktyDoWykorzystania);
        koszykSerwis.pomniejszSume(znizka); // Zmniejsz sumę koszyka o zniżkę
        zamowienieSerwis.zmniejszPunkty(uzytkownik, punktyDoWykorzystania);
        return "redirect:/zamowienie/koszyk";
    }

    @GetMapping("/zwieksz/{przedmiotId}")
    public String zwiekszPrzedmiot(@PathVariable("przedmiotId") Long przedmiotId) {
        koszykSerwis.operacjeNaPrzedmiocie(przedmiotId, OperacjeNaPrzedmiocie.ZWIEKSZ);
        return "redirect:/zamowienie/koszyk";
    }

    @GetMapping("/zmniejsz/{przedmiotId}")
    public String zmniejszPrzedmiot(@PathVariable("przedmiotId") Long przedmiotId) {
        koszykSerwis.operacjeNaPrzedmiocie(przedmiotId, OperacjeNaPrzedmiocie.ZMNIEJSZ);
        return "redirect:/zamowienie/koszyk";
    }

    @GetMapping("/usun/{przedmiotId}")
    public String usunPrzedmiotyZKoszyka(@PathVariable("przedmiotId") Long przedmiotId) {
        koszykSerwis.operacjeNaPrzedmiocie(przedmiotId, OperacjeNaPrzedmiocie.USUN);
        return "redirect:/zamowienie/koszyk";
    }

    @GetMapping("/daneDoWysylki")
    public String daneDoWysylki(Model model) {
        model.addAttribute("zamowienieDto", new ZamowienieDto());
        return "daneDoWysylki";
    }

    @PostMapping("/przejdzDoPodsumowania")
    public String przejdzDoPodsumowania(@ModelAttribute ZamowienieDto zamowienieDto, Model model) {
        model.addAttribute("zamowienieDto", zamowienieDto);
        return "podsumowanie"; // Przekierowanie na stronę podsumowania
    }

    @GetMapping("/podsumowanie")
    public String pokazPodsumowanie(Model model) {
        model.addAttribute("zamowienieDto", new ZamowienieDto());
        return "podsumowanie";
    }

    @PostMapping("/zapiszZamowienie")
    public String zapiszZamowienie(@ModelAttribute ZamowienieDto zamowienieDto,
                                   @SessionAttribute(value = "uzytkownik", required = false)
                                   Uzytkownik uzytkownik, Model model) {
        Long uzytkownikId = (uzytkownik != null) ? uzytkownik.getId() : null;
        try {
            zamowienieSerwis.zapiszZamowienie(zamowienieDto, uzytkownikId);
            return "redirect:/zamowienie/dziekujemy"; // Przekierowanie na stronę podziękowania
        } catch (Exception e) {
            model.addAttribute("error", "Wystąpił problem z przetwarzaniem zamówienia.");
            e.printStackTrace();
            return "podsumowanie";
        }
    }

    @GetMapping("/dziekujemy")
    public String dziekujemy() {
        return "dziekujemy";
    }
}
