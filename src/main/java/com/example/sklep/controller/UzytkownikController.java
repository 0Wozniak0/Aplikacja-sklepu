package com.example.sklep.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.sklep.model.Uzytkownik;
import com.example.sklep.serwis.UzytkownikSerwis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UzytkownikController {

    private final UzytkownikSerwis uzytkownikSerwis;

    @Autowired
    public UzytkownikController(UzytkownikSerwis uzytkownikSerwis) {
        this.uzytkownikSerwis = uzytkownikSerwis;
    }

    @GetMapping("/rejestracja")
    public String pokazRejestracje(Model model) {
        model.addAttribute("nowyUzytkownik", new Uzytkownik());
        return "rejestracja";
    }

    @PostMapping("/rejestracja")
    public String rejestracja(@ModelAttribute Uzytkownik uzytkownik, RedirectAttributes redirectAttributes) {
        if (uzytkownikSerwis.czyLoginIstnieje(uzytkownik.getLogin())) {
            redirectAttributes.addFlashAttribute("error", "Login już istnieje. Wybierz inny.");
            return "redirect:/rejestracja";
        }

        uzytkownikSerwis.zarejestruj(uzytkownik);
        redirectAttributes.addFlashAttribute("sukces", "Rejestracja zakończona sukcesem! Możesz się teraz zalogować.");
        return "redirect:/logowanie";
    }

    @GetMapping("/logowanie")
    public String pokazLogowanie(Model model) {
        return "logowanie";
    }

    @PostMapping("/logowanie")
    public String logowanie(@RequestParam String login, @RequestParam String haslo, RedirectAttributes redirectAttributes, HttpSession session) {
        Uzytkownik uzytkownik = uzytkownikSerwis.zaloguj(login, haslo);
        if (uzytkownik != null) {
            session.setAttribute("uzytkownik", uzytkownik);
            return "redirect:/"; // Przekierowanie dla zalogowanego użytkownika
        }
        redirectAttributes.addFlashAttribute("error", "Błędny login lub hasło");
        return "redirect:/logowanie";
    }

    @GetMapping("/wyloguj")
    public String wyloguj(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
