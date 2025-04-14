package com.example.sklep;

import com.example.sklep.model.Przedmiot;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Getter
@Setter
public class Koszyk {
    private List<PozycjawKoszyku> pozycjewKoszyku =new ArrayList<>();
    private int licznik = 0;
    private BigDecimal sum = BigDecimal.ZERO;


    public void zwiekszPrzedmiot(Przedmiot przedmiot){

        getPozycjawKoszykuByPrzedmiot(przedmiot).ifPresentOrElse
                (PozycjawKoszyku::zwiekszLicznik,() -> pozycjewKoszyku.add(new PozycjawKoszyku(przedmiot))
                );
        recalculateCenaAndLicznik();

    }

    public void zmniejszPrzedmiot(Przedmiot przedmiot){
        Optional<PozycjawKoszyku> oPozycjawKoszyku = getPozycjawKoszykuByPrzedmiot(przedmiot);
        if(oPozycjawKoszyku.isPresent()){
            PozycjawKoszyku pozycjawKoszyku = oPozycjawKoszyku.get();
            pozycjawKoszyku.zmniejszLicznik();
            if(pozycjawKoszyku.ZeroPrzedmioty()){
                usunPrzedmioty(przedmiot);
            }
            else {
                recalculateCenaAndLicznik();
            }
        }
    }

    public void usunPrzedmioty (Przedmiot przedmiot){
        pozycjewKoszyku.removeIf(i -> i.idEquals(przedmiot));
        recalculateCenaAndLicznik();
    }

    private void recalculateCenaAndLicznik(){
        sum = pozycjewKoszyku.stream().map(PozycjawKoszyku::getCena)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        licznik = pozycjewKoszyku.stream().mapToInt(PozycjawKoszyku::getLicznik)
                .reduce(0, Integer::sum);
    }

    private Optional<PozycjawKoszyku> getPozycjawKoszykuByPrzedmiot(Przedmiot przedmiot){
        return pozycjewKoszyku.stream()
                .filter(i -> i.idEquals(przedmiot))
                .findFirst();
    }

    public void wyczysckoszyk(){
        pozycjewKoszyku.clear();
        licznik = 0;
        sum = BigDecimal.ZERO;
    }
}
