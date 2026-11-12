package org.example.Model;

/**
 * Produktu baten informazioa errepresentatzen duen eredu-klasea (Model).
 * Stock informazioa eta irudiaren byteak barne biltzen ditu.
 *
 * @author CoreDevs Taldea
 * @version 1.1
 */
public class Produktua {
    private int id;
    private String izena;
    private double prezioa;
    private double stock;
    private byte[] argazkiaBytes;

    /**
     * Eraikitzaile hutsa (JavaBean estandarra).
     */
    public Produktua() {
    }

    /**
     * Produktuaren parametrodun eraikitzailea.
     *
     * @param id            Produktuaren ID bakarra.
     * @param izena         Produktuaren izena.
     * @param prezioa       Produktuaren salmenta-prezioa.
     * @param stock         Biltegian dagoen kopurua (stocka).
     * @param argazkiaBytes Irudiaren edukia byte array gisa.
     */
    public Produktua(int id, String izena, double prezioa, double stock, byte[] argazkiaBytes) {
        this.id = id;
        this.izena = izena;
        this.prezioa = prezioa;
        this.stock = stock;
        this.argazkiaBytes = argazkiaBytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public double getPrezioa() {
        return prezioa;
    }

    public void setPrezioa(double prezioa) {
        this.prezioa = prezioa;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public byte[] getArgazkiaBytes() {
        return argazkiaBytes;
    }

    public void setArgazkiaBytes(byte[] argazkiaBytes) {
        this.argazkiaBytes = argazkiaBytes;
    }
}