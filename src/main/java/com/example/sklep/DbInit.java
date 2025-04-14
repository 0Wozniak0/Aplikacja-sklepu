package com.example.sklep;

import com.example.sklep.dane.PrzedmiotDane;
import com.example.sklep.model.Przedmiot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;


@Configuration
public class DbInit implements CommandLineRunner {

   private final PrzedmiotDane przedmiotDane;

   @Autowired
   public DbInit(PrzedmiotDane przedmiotDane) {
        this.przedmiotDane = przedmiotDane;
    }

    @Override
    public void run(String... args) throws Exception {
       /*przedmiotDane.saveAll(List.of(
              /* new Przedmiot("Procesor AMD Ryzen 5 5500", new BigDecimal("550.00"),"https://cdn.x-kom.pl/i/setup/images/prod/big/product-medium,,2022/4/pr_2022_4_4_13_9_46_828_00.jpg"),
               new Przedmiot("Lexar 32GB (2x16GB) 3200MHz CL16 Thor", new BigDecimal("309.00"),"https://cdn.x-kom.pl/i/setup/images/prod/big/product-medium,,2022/7/pr_2022_7_26_8_53_33_828_00.jpg"),
               new Przedmiot("Gigabyte GeForce RTX 3060 GAMING OC LHR 12GB GDDR6", new BigDecimal("1429.00"),"https://cdn.x-kom.pl/i/setup/images/prod/big/product-medium,,2021/6/pr_2021_6_15_13_24_23_26_05.jpg")

       ));
     */
    }
}
