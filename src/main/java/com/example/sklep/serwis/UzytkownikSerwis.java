package com.example.sklep.serwis;

import com.example.sklep.dane.UzytkownikDane;
import com.example.sklep.model.Uzytkownik;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UzytkownikSerwis {
    @Autowired
    private UzytkownikDane uzytkownikDane;

    public void zarejestruj(Uzytkownik uzytkownik) {
        uzytkownik.setHaslo(haszujHaslo(uzytkownik.getHaslo()));
        uzytkownik.setRola("użytkownik");
        uzytkownikDane.save(uzytkownik);
    }

    public Uzytkownik zaloguj(String login, String haslo) {
        Optional<Uzytkownik> uzytkownik = uzytkownikDane.findByLogin(login);
        if (uzytkownik.isPresent() && sprawdzHaslo(haslo, uzytkownik.get().getHaslo())) {
            return uzytkownik.get();
        }
        return null;
    }

    public boolean czyLoginIstnieje(String login) {
        Optional<Uzytkownik> uzytkownik = uzytkownikDane.findByLogin(login);
        return uzytkownik.isPresent();
    }

    public void zaktualizujLoginIHaslo(Uzytkownik uzytkownik, String nowyLogin, String noweHaslo) {
        uzytkownik.setLogin(nowyLogin);
        uzytkownik.setHaslo(haszujHaslo(noweHaslo));
        uzytkownikDane.save(uzytkownik);
    }

    private String haszujHaslo(String haslo) {
        return haslo;
    }

    private boolean sprawdzHaslo(String haslo, String hasloZBazy) {
        return haslo.equals(hasloZBazy);
    }
}
