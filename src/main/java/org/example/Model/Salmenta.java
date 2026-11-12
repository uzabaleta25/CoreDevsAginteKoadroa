package org.example.Model;

import java.time.LocalDate;

/**
 * Salmenta edo faktura baten datuak gordetzen dituen eredu-klasea (Model).
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class Salmenta {
    private int id;
    private String bezeroa;
    private LocalDate data;
    private double totala;
    private String egoera;

    /**
     * Eraikitzaile hutsa (JavaBean estandarra).
     */
    public Salmenta() {
    }

    /**
     * Salmentaren parametrodun eraikitzailea.
     *
     * @param id      Fakturaren edo salmentaren ID bakarra.
     * @param bezeroa Faktura/Eskaeraren kodea edo bezeroaren izena.
     * @param data    Fakturazio-data (LocalDate formatuan).
     * @param totala  Fakturaren zenbateko osoa (€).
     * @param egoera  Fakturaren uneko egoera Odoon (adib. 'posted').
     */
    public Salmenta(int id, String bezeroa, LocalDate data, double totala, String egoera) {
        this.id = id;
        this.bezeroa = bezeroa;
        this.data = data;
        this.totala = totala;
        this.egoera = egoera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeroa() {
        return bezeroa;
    }

    public void setBezeroa(String bezeroa) {
        this.bezeroa = bezeroa;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getTotala() {
        return totala;
    }

    public void setTotala(double totala) {
        this.totala = totala;
    }

    public String getEgoera() {
        return egoera;
    }

    public void setEgoera(String egoera) {
        this.egoera = egoera;
    }
}