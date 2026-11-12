package org.example.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.Controller.SceneManager;
import org.example.Dao.ProduktuaDAO;
import org.example.Model.Produktua;

import java.util.List;

/**
 * Produktuen katalogo osoa eta bilatzaile dinamikoa bistaratzen dituen ikuspegi-klasea (View).
 * Stock informazioarekin batera produktuen txartelak sarean (FlowPane) antolatzen ditu eta
 * testu bidezko iragazketa denbora errealean kudeatzen du.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class ProduktuaView extends BorderPane {

    private FlowPane edukiontzia;
    private List<Produktua> jatorrizkoZerrenda;
    private SceneManager sceneManager;
    private ProduktuaDAO produktuaDAO;

    /**
     * ProduktuaView klasearen eraikitzailea.
     * Izenburua, bilaketa-testu eremua eta produktuen sare-egitura (ScrollPane eta FlowPane) eraikitzen ditu.
     *
     * @param sceneManager Datuak atzitzeko eta DAOak kudeatzeko kontrolatzailea.
     */
    public ProduktuaView(SceneManager sceneManager, ProduktuaDAO produktuaDAO) {

        this.sceneManager = sceneManager;
        this.produktuaDAO = produktuaDAO;

        // 1. GOIKALDEA (Izenburua eta Bilatzailea)
        VBox goikoBox = new VBox(10);
        goikoBox.setAlignment(Pos.CENTER);
        goikoBox.setPadding(new Insets(15, 0, 10, 0));

        Label izenburuaLbl = new Label("Dendako Produktuak");
        izenburuaLbl.setFont(Font.font("System", FontWeight.BOLD, 22));
        izenburuaLbl.setStyle("-fx-text-fill: #2c3e50;");

        // Bilatzaileko testu-eremua
        TextField bilatzailea = new TextField();
        bilatzailea.setPromptText("Bilatu produktua izenaren arabera...");
        bilatzailea.setMaxWidth(350);
        bilatzailea.setStyle("-fx-padding: 8px; -fx-background-radius: 5px; -fx-border-radius: 5px; -fx-border-color: #bdc3c7;");

        goikoBox.getChildren().addAll(izenburuaLbl, bilatzailea);
        this.setTop(goikoBox);

        // 2. EDUKIONTZIA (FlowPane)
        edukiontzia = new FlowPane();
        edukiontzia.setHgap(20);
        edukiontzia.setVgap(20);
        edukiontzia.setPadding(new Insets(20));
        edukiontzia.setAlignment(Pos.TOP_CENTER);

        // Datu-basetik jatorrizko zerrenda kargatu
        jatorrizkoZerrenda = produktuaDAO.lortuProduktuak();

        // Hasieran produktu guztiak erakutsi
        eguneratuProduktuenSarea(jatorrizkoZerrenda);

        // Bilatzaileko entzulea (testua aldatzen den bakoitzean iragazi)
        bilatzailea.textProperty().addListener((observable, oldValue, newValue) -> {
            iragaziProduktuak(newValue);
        });

        // 3. ERDIALDEA (ScrollPane)
        ScrollPane scrollPane = new ScrollPane(edukiontzia);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        this.setCenter(scrollPane);

    }

    /**
     * Sartutako testuaren arabera produktuen zerrenda iragazten duen metodo pribatua.
     * Produktuaren izenean bilaketa-testua dutenak soilik mantentzen ditu.
     *
     * @param testua Bilaketa-eremuan idatzitako testua.
     */
    private void iragaziProduktuak(String testua) {
        if (testua == null || testua.trim().isEmpty()) {
            eguneratuProduktuenSarea(jatorrizkoZerrenda);
        } else {
            String bilaketa = testua.toLowerCase().trim();
            List<Produktua> filtratuak = jatorrizkoZerrenda.stream()
                    .filter(p -> p.getIzena().toLowerCase().contains(bilaketa))
                    .toList();
            eguneratuProduktuenSarea(filtratuak);
        }
    }

    /**
     * FlowPane edukiontziko txartelak birsortzen dituen metodo pribatua.
     * Emandako zerrendako produktu bakoitzeko {@link ProduktuaTxartela} bat txertatzen du.
     *
     * @param zerrenda Bistaratu nahi den produktuen zerrenda.
     */
    private void eguneratuProduktuenSarea(List<Produktua> zerrenda) {
        edukiontzia.getChildren().clear();

        if (zerrenda.isEmpty()) {
            Label hutsaLbl = new Label("Ez da produkturik aurkitu");
            hutsaLbl.setFont(Font.font(16));
            hutsaLbl.setStyle("-fx-text-fill: #7f8c8d;");
            edukiontzia.getChildren().add(hutsaLbl);
            return;
        }

        for (int i = 0; i < zerrenda.size(); i++) {
            Produktua p = zerrenda.get(i);

            // ProduktuaTxartela erabiliz kodea askoz modularragoa da:
            ProduktuaTxartela txartela = new ProduktuaTxartela(p);
            edukiontzia.getChildren().add(txartela);
        }
    }
}