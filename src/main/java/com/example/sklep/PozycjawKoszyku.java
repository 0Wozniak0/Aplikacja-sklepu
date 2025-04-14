package com.example.sklep;

import com.example.sklep.model.Przedmiot;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PozycjawKoszyku {

    private Przedmiot przedmiot;

    private int licznik;

    private BigDecimal cena;

    public PozycjawKoszyku(Przedmiot przedmiot){
        this.przedmiot = przedmiot;
        this.licznik = 1;
        if (przedmiot.getCenaPromocyjna() != null) {
            this.cena = przedmiot.getCenaPromocyjna();
        } else {
            this.cena = przedmiot.getCena();
        }
    }
    public void zwiekszLicznik(){
        licznik++;
        recalculate();
    }
    public void zmniejszLicznik(){
        if (licznik > 0) {
            licznik--;
            recalculate();
        }
    }

    public boolean ZeroPrzedmioty(){

        return licznik == 0;
    }

    private void recalculate(){

        BigDecimal aktualnaCena = (przedmiot.getCenaPromocyjna() != null) ? przedmiot.getCenaPromocyjna() : przedmiot.getCena();
        cena = aktualnaCena.multiply(new BigDecimal(licznik));
    }


    public boolean idEquals(Przedmiot przedmiot) {

        return this.przedmiot.getId().equals(przedmiot.getId());
    }
}
