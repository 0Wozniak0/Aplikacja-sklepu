package com.example.sklep.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.ui.Model;
import com.example.sklep.model.Uzytkownik;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute
    public void dodajUzytkownikaDoModelu(Model model, HttpSession session) {
        Uzytkownik uzytkownik = (Uzytkownik) session.getAttribute("uzytkownik");
        if (uzytkownik != null) {
            model.addAttribute("uzytkownik", uzytkownik);
        }
    }
}