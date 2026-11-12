package org.example.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.Controller.SceneManager;
import org.example.Dao.ProduktuaDAO;
import org.example.Dao.SalmentaDAO;
import org.example.Model.Produktua;

import java.util.List;
import java.util.Map;

/**
 * Aginte Koadroaren (Dashboard) interfaze grafikoa kudeatzen duen ikuspegi-klasea (View).
 * KPI txartel nagusiak (salmentak guztira, sistemaren egoera) eta salmenten zein prezioen
 * grafiko dinamikoak (LineChart eta BarChart) bistaratzen ditu.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class DashboardView extends BorderPane {

    private final SceneManager sceneManager;
    private final SalmentaDAO salmentaDAO;
    private final ProduktuaDAO produktuaDAO;
    private final VBox grafikoEdukiontzia;
    private final ComboBox<String> cbGrafikoMota;


    /**
     * DashboardView klasearen eraikitzailea.
     * KPI txartelak eraikitzen ditu eta grafiko interaktiboak hautatzeko ComboBox-a eta edukiontzia prestatzen ditu.
     *
     * @param sceneManager Bisten arteko nabigazioa eta DAOen atzipena kudeatzen dituen kontrolatzailea.
     */
    public DashboardView(SceneManager sceneManager, SalmentaDAO salmentaDAO, ProduktuaDAO produktuaDAO) {
        this.sceneManager = sceneManager;
        this.salmentaDAO = salmentaDAO;
        this.produktuaDAO = produktuaDAO;

        // 1. GOIKALDEA: KPI Txartelak
        HBox kpiBox = new HBox(25);
        kpiBox.setAlignment(Pos.CENTER);
        kpiBox.setPadding(new Insets(20));

        double totalVentas = salmentaDAO.lortuSalmentakGuztira();
        VBox cardTotal = sortuKPICard("Salmentak Guztira", String.format("%.2f €", totalVentas), "#27ae60");
        VBox cardEstado = sortuKPICard("Sistemaren Egoera", "Konektatuta", "#2980b9");

        kpiBox.getChildren().addAll(cardTotal, cardEstado);
        this.setTop(kpiBox);

        // 2. ERDIALDEA: Grafiko hautatzailea + Grafiko bakarra zabal
        cbGrafikoMota = new ComboBox<>();
        cbGrafikoMota.getItems().addAll("Salmenten Bilakaera", "Produktuen Prezioak");
        cbGrafikoMota.setValue("Salmenten Bilakaera");
        cbGrafikoMota.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
        cbGrafikoMota.setOnAction(e -> eguneratuGrafikoa());

        HBox selectorBox = new HBox(10, new Label("Aukeratu Grafikoa:"), cbGrafikoMota);
        selectorBox.setAlignment(Pos.CENTER_RIGHT);
        selectorBox.setPadding(new Insets(0, 30, 10, 0));

        grafikoEdukiontzia = new VBox(10);
        grafikoEdukiontzia.setPadding(new Insets(10, 30, 20, 30));

        VBox erdialdekoPanela = new VBox(selectorBox, grafikoEdukiontzia);
        this.setCenter(erdialdekoPanela);

        eguneratuGrafikoa();
    }

    /**
     * Hautatutako grafiko motaren arabera erdialdeko edukiontzia eguneratzen duen metodo pribatua.
     */
    private void eguneratuGrafikoa() {
        grafikoEdukiontzia.getChildren().clear();
        if ("Salmenten Bilakaera".equals(cbGrafikoMota.getValue())) {
            grafikoEdukiontzia.getChildren().add(sortuSalmentenGrafikoa());
        } else {
            grafikoEdukiontzia.getChildren().add(sortuProduktuenGrafikoa());
        }
    }

    /**
     * Hileroko salmenten bilakaera denboran zehar erakusten duen lerro-grafikoa (LineChart) sortzen du.
     *
     * @return {@link LineChart} Salmenten bilakaeraren lerro-grafikoa.
     */
    private LineChart<String, Number> sortuSalmentenGrafikoa() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Hilabetea");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Salmentak (€)");

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Salmenten Bilakaera Denboran Zehar");
        lineChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Map<String, Double> datosVentas = salmentaDAO.lortuHilerokoSalmentak();
        for (Map.Entry<String, Double> entry : datosVentas.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        lineChart.getData().add(series);
        return lineChart;
    }

    /**
     * Produktuen prezioen konparaketa erakusten duen marra-grafikoa (BarChart) sortzen du.
     *
     * @return {@link BarChart} Produktuen prezioen marra-grafikoa.
     */
    private BarChart<String, Number> sortuProduktuenGrafikoa() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Produktua");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Prezioa (€)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Produktuen Prezioen Konparaketa");
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Produktua> produktuak = produktuaDAO.lortuProduktuak();
        for (Produktua p : produktuak) {
            series.getData().add(new XYChart.Data<>(p.getIzena(), p.getPrezioa()));
        }

        barChart.getData().add(series);
        return barChart;
    }

    /**
     * Aginte Koadroko KPI txartel grafiko bakoitza eraikitzeko metodo laguntzailea.
     *
     * @param titulo   Txartelaren izenburua (adib. "Salmentak Guztira").
     * @param valor    Bistaratu beharreko balioa edo testua.
     * @param colorHex Ezkerreko ertzerako kolorearen kodea hexadezimalean.
     * @return {@link VBox} Estilizatutako KPI txartelaren edukiontzia.
     */
    private VBox sortuKPICard(String titulo, String valor, String colorHex) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15, 25, 15, 25));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #ffffff; " +
                "-fx-border-color: " + colorHex + "; " +
                "-fx-border-width: 0 0 0 5; " +
                "-fx-border-radius: 6; " +
                "-fx-background-radius: 6; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 2);");

        Label titleLbl = new Label(titulo);
        titleLbl.setFont(Font.font("System", FontWeight.NORMAL, 12));
        titleLbl.setStyle("-fx-text-fill: #7f8c8d;");

        Label valueLbl = new Label(valor);
        valueLbl.setFont(Font.font("System", FontWeight.BOLD, 20));
        valueLbl.setStyle("-fx-text-fill: #2c3e50;");

        card.getChildren().addAll(titleLbl, valueLbl);
        return card;
    }
}