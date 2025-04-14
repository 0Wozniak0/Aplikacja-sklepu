package com.example.sklep.model;


import jakarta.persistence.*;


@Entity
@Table(name = "specyfikacja")
public class Specyfikacja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long przedmiotId;
    private String nazwa;
    @Column(name = "wartosc", length = 1000)
    private String wartosc;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPrzedmiotId() { return przedmiotId; }
    public void setPrzedmiotId(Long przedmiotId) { this.przedmiotId = przedmiotId; }
    public String getNazwa() { return nazwa; }
    public void setNazwa(String nazwa) { this.nazwa = nazwa; }
    public String getWartosc() { return wartosc; }
    public void setWartosc(String wartosc) { this.wartosc = wartosc; }
}
