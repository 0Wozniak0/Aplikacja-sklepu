package com.example.sklep.controller;

import com.example.sklep.model.Przedmiot;
import com.example.sklep.model.UlubionyPrzedmiot;
import com.example.sklep.model.Uzytkownik;
import com.example.sklep.model.zamowienie.ElementZamowienia;
import com.example.sklep.serwis.PrzedmiotSerwis;
import com.example.sklep.serwis.UlubionyPrzedmiotSerwis;
import com.example.sklep.serwis.UzytkownikSerwis;
import com.example.sklep.serwis.ZamowienieSerwis;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class KontoController {

    private final ZamowienieSerwis zamowienieSerwis;
    private final UzytkownikSerwis uzytkownikSerwis;
    private final UlubionyPrzedmiotSerwis ulubionyPrzedmiotSerwis;
    private final PrzedmiotSerwis przedmiotSerwis;

    @Autowired
    public KontoController(ZamowienieSerwis zamowienieSerwis, UzytkownikSerwis uzytkownikSerwis,
                           UlubionyPrzedmiotSerwis ulubionyPrzedmiotSerwis, PrzedmiotSerwis przedmiotSerwis) {
        this.zamowienieSerwis = zamowienieSerwis;
        this.uzytkownikSerwis = uzytkownikSerwis;
        this.ulubionyPrzedmiotSerwis = ulubionyPrzedmiotSerwis;
        this.przedmiotSerwis = przedmiotSerwis;
    }

    @GetMapping("/konto")
    public String pokazKonto() {
        return "konto"; // nazwa widoku HTML bez rozszerzenia
    }

    @GetMapping("/konto/historiaZamowien")
    public String pokazHistorieZamowien(Model model, HttpSession session) {
        Uzytkownik uzytkownik = (Uzytkownik) session.getAttribute("uzytkownik");

        if (uzytkownik != null) {
            Long uzytkownikId = uzytkownik.getId();
            List<ElementZamowienia> historiaZamowien = zamowienieSerwis.pobierzHistorieZamowien(uzytkownikId);

            // Zbieranie łącznych cen dla zamówień
            Map<Long, BigDecimal> laczneCeny = new HashMap<>();
            for (ElementZamowienia element : historiaZamowien) {
                Long zamowienieId = element.getZamowienieId();
                laczneCeny.put(zamowienieId, zamowienieSerwis.pobierzLacznaCene(zamowienieId));
            }

            model.addAttribute("historiaZamowien", historiaZamowien);
            model.addAttribute("laczneCeny", laczneCeny);
        } else {
            return "redirect:/login";
        }

        return "historiaZamowien";
    }

    @GetMapping("/konto/ustawieniaKonta")
    public String ustawieniaKonta() {
        return "ustawieniaKonta";
    }

    @PostMapping("/konto/ustawieniaKonta")
    public String zaktualizujKonto(@RequestParam("login") String login,
                                   @RequestParam("haslo") String haslo,
                                   @RequestParam("hasloPotwierdzenie") String hasloPotwierdzenie,
                                   HttpSession session, Model model, RedirectAttributes redirectAttributes) {

        Uzytkownik uzytkownik = (Uzytkownik) session.getAttribute("uzytkownik");

        if (!haslo.equals(hasloPotwierdzenie)) {
            model.addAttribute("blad", "Hasła nie są zgodne.");
            return "ustawieniaKonta";
        }

        if (uzytkownikSerwis.czyLoginIstnieje(login) && !login.equals(uzytkownik.getLogin())) {
            model.addAttribute("blad", "Login już istnieje.");
            return "ustawieniaKonta";
        }

        uzytkownikSerwis.zaktualizujLoginIHaslo(uzytkownik, login, haslo);
        session.setAttribute("uzytkownik", uzytkownik); // Zaktualizowanie danych w sesji
        session.invalidate();

        redirectAttributes.addFlashAttribute("sukces", "Dane zostały pomyślnie zaktualizowane! Możesz się teraz zalogować");
        return "redirect:/logowanie";
    }

    @GetMapping("/konto/ulubionePrzedmioty")
    public String pokazUlubionePrzedmioty(Model model, @SessionAttribute("uzytkownik") Uzytkownik uzytkownik) {
        List<UlubionyPrzedmiot> ulubionePrzedmioty = ulubionyPrzedmiotSerwis.pobierzUlubionePrzedmioty(uzytkownik.getId());
        model.addAttribute("ulubionePrzedmioty", ulubionePrzedmioty);
        return "ulubionePrzedmioty"; // Strona z ulubionymi przedmiotami
    }

    @PostMapping("/konto/dodajUlubionyPrzedmiot")
    public String dodajUlubionyPrzedmiot(@RequestParam Long przedmiotId,
                                         @SessionAttribute(value = "uzytkownik", required = false) Uzytkownik uzytkownik,
                                         Model model, RedirectAttributes redirectAttributes) {

        // Jeśli użytkownik nie jest zalogowany
        if (uzytkownik == null) {
            redirectAttributes.addFlashAttribute("blad", "Musisz być zalogowany, aby dodać przedmiot do ulubionych.");
            return "redirect:/przedmiot/" + przedmiotId;
        }

        Przedmiot przedmiot = przedmiotSerwis.findById(przedmiotId);

        // Sprawdzenie, czy przedmiot jest już na liście ulubionych
        if (ulubionyPrzedmiotSerwis.czyPrzedmiotIstnieje(uzytkownik, przedmiotId)) {
            redirectAttributes.addFlashAttribute("blad", "Ten przedmiot jest już na liście ulubionych.");
            return "redirect:/przedmiot/" + przedmiotId;
        }

        ulubionyPrzedmiotSerwis.dodajUlubionyPrzedmiot(uzytkownik, przedmiot);
        redirectAttributes.addFlashAttribute("sukces", "Przedmiot został dodany do ulubionych.");
        return "redirect:/przedmiot/" + przedmiotId;
    }



    @PostMapping("/konto/usunUlubionyPrzedmiot")
    public String usunUlubionyPrzedmiot(@RequestParam Long przedmiotId, @SessionAttribute("uzytkownik") Uzytkownik uzytkownik) {
        ulubionyPrzedmiotSerwis.usunUlubionyPrzedmiot(uzytkownik, przedmiotId);
        return "redirect:/konto/ulubionePrzedmioty"; // Przekierowanie po usunięciu przedmiotu z ulubionych
    }
}
