package org.example.View;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.example.Controller.SceneManager;
import org.example.Dao.ProduktuaDAO;
import org.example.Dao.SalmentaDAO;
import org.example.Model.Produktua;
import org.example.Model.Salmenta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

/**
 * Txostenak ikusteko eta PDF zein CSV formatuetan esportatzeko interfaze grafikoa kudeatzen duen ikuspegi-klasea (View).
 * iText liburutegia erabiltzen du PDF txostenak sortzeko eta Odooren datuak taula interaktiboetan bistaratzen ditu.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class TxostenakView extends VBox {

    private final SceneManager sceneManager;
    private final SalmentaDAO salmentaDAO;
    private final ProduktuaDAO produktuaDAO;

    private ComboBox<String> cbTxostenMota;
    private TableView taula; // Mota orokorra (Salmenta zein Produktua onartzeko)

    /**
     * TxostenakView klasearen eraikitzailea.
     * Kontrol-panela, aukeraketa ComboBox-a, esportatzeko botoiak eta datuen taula nagusia eraikitzen ditu.
     *
     * @param sceneManager Bisten arteko nabigazioa eta DAOen atzipena kudeatzen dituen kontrolatzailea.
     */
    public TxostenakView(SceneManager sceneManager, SalmentaDAO salmentaDAO, ProduktuaDAO produktuaDAO) {
        this.sceneManager = sceneManager;
        this.salmentaDAO = salmentaDAO;
        this.produktuaDAO = produktuaDAO;

        setSpacing(20);
        setPadding(new Insets(30));
        setAlignment(Pos.TOP_CENTER);

        // 1. Izenburua eta Deskribapena
        Label titulua = new Label("Txostenak eta Esportazioa");
        titulua.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #1E293B;");

        // 2. Txosten Mota Aukeratzeko ComboBox-a
        Label lblAukeratu = new Label("Aukeratu Txosten Mota:");
        lblAukeratu.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        cbTxostenMota = new ComboBox<>();
        cbTxostenMota.getItems().addAll("Salmenten Txostena", "Produktuen Katalogoa");
        cbTxostenMota.setValue("Salmenten Txostena"); // Lehenetsitakoa
        cbTxostenMota.setStyle("-fx-font-size: 14px; -fx-padding: 4;");

        // Aukeraz aldatzean taula berritu
        cbTxostenMota.setOnAction(e -> eguneratuTaula());

        HBox aukeraBox = new HBox(12, lblAukeratu, cbTxostenMota);
        aukeraBox.setAlignment(Pos.CENTER_LEFT);

        // 3. Deskarga Botoiak
        Button btnExportPDF = new Button("Deskargatu PDF Txostena");
        btnExportPDF.setStyle("-fx-background-color: #E11D48; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 18; -fx-background-radius: 6; -fx-cursor: hand;");
        btnExportPDF.setOnAction(e -> esportatuPDF());

        Button btnExportCSV = new Button("Deskargatu CSV");
        btnExportCSV.setStyle("-fx-background-color: #0EA5E9; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 10 18; -fx-background-radius: 6; -fx-cursor: hand;");
        btnExportCSV.setOnAction(e -> esportatuCSV());

        HBox botoiKutxa = new HBox(12, btnExportCSV, btnExportPDF);
        botoiKutxa.setAlignment(Pos.CENTER_RIGHT);

        HBox kontrolPanela = new HBox(aukeraBox, botoiKutxa);
        kontrolPanela.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(aukeraBox, javafx.scene.layout.Priority.ALWAYS);

        // 4. Taula hasieratu
        taula = new TableView<>();
        taula.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        eguneratuTaula(); // Hasierako datuak kargatu

        getChildren().addAll(titulua, kontrolPanela, taula);
    }

    /**
     * Hautatutako txosten motaren arabera (Salmentak edo Produktuak) taularen zutabeak eta datuak eguneratzen dituen metodo pribatua.
     */
    @SuppressWarnings("unchecked")
    private void eguneratuTaula() {
        taula.getColumns().clear();

        if ("Salmenten Txostena".equals(cbTxostenMota.getValue())) {
            TableColumn<Salmenta, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Salmenta, String> colIzena = new TableColumn<>("Faktura / Eskaera");
            colIzena.setCellValueFactory(new PropertyValueFactory<>("bezeroa"));

            TableColumn<Salmenta, Object> colData = new TableColumn<>("Data");
            colData.setCellValueFactory(new PropertyValueFactory<>("data"));

            TableColumn<Salmenta, Double> colTotal = new TableColumn<>("Zenbatekoa (€)");
            colTotal.setCellValueFactory(new PropertyValueFactory<>("totala"));

            TableColumn<Salmenta, String> colEgoera = new TableColumn<>("Egoera");
            colEgoera.setCellValueFactory(new PropertyValueFactory<>("egoera"));

            taula.getColumns().addAll(colId, colIzena, colData, colTotal, colEgoera);
            taula.setItems(FXCollections.observableArrayList(salmentaDAO.lortuSalmentakGuztiak()));

        } else { // Produktuen Katalogoa
            TableColumn<Produktua, Integer> colId = new TableColumn<>("ID");
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Produktua, String> colIzena = new TableColumn<>("Produktutaren Izena");
            colIzena.setCellValueFactory(new PropertyValueFactory<>("izena"));

            TableColumn<Produktua, Double> colPrezioa = new TableColumn<>("Prezioa (€)");
            colPrezioa.setCellValueFactory(new PropertyValueFactory<>("prezioa"));

            taula.getColumns().addAll(colId, colIzena, colPrezioa);
            taula.setItems(FXCollections.observableArrayList(produktuaDAO.lortuProduktuak()));
        }
    }

    /**
     * Taulan bistaratutako datuak PDF formatuko fitxategi batera esportatzen dituen metodo pribatua.
     * iText liburutegia erabiliz izenburua, sorkuntza-data, taula estilizatua eta totalak biltzen ditu.
     */
    private void esportatuPDF() {
        boolean isSalmentak = "Salmenten Txostena".equals(cbTxostenMota.getValue());
        String fitxategiIzena = isSalmentak ? "Salmentak_Txostena.pdf" : "Produktuak_Txostena.pdf";

        FileChooser fileChooser = prepareFileChooser(fitxategiIzena, "PDF fitxategiak (*.pdf)", "*.pdf");
        File fitxategia = fileChooser.showSaveDialog(getScene().getWindow());

        if (fitxategia != null) {
            try {
                Document document = new Document(PageSize.A4, 36, 36, 36, 36);
                PdfWriter.getInstance(document, new FileOutputStream(fitxategia));
                document.open();

                // Goiburua
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
                String tituluTestua = isSalmentak ? "CoreDevs ERP - Salmenten Txostena" : "CoreDevs ERP - Produktuen Katalogoa";
                Paragraph title = new Paragraph(tituluTestua, titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);

                Font subFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
                Paragraph sub = new Paragraph("Sorkuntza data: " + LocalDate.now(), subFont);
                sub.setAlignment(Element.ALIGN_CENTER);
                sub.setSpacingAfter(20);
                document.add(sub);

                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
                BaseColor headerColor = new BaseColor(44, 62, 80);
                Font bodyFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

                if (isSalmentak) {
                    PdfPTable pdfTable = new PdfPTable(5);
                    pdfTable.setWidthPercentage(100);
                    pdfTable.setWidths(new float[]{1.5f, 4f, 3f, 3f, 2.5f});

                    String[] headers = {"ID", "Faktura / Eskaera", "Data", "Guztira (€)", "Egoera"};
                    for (String h : headers) {
                        PdfPCell c = new PdfPCell(new Phrase(h, headFont));
                        c.setBackgroundColor(headerColor);
                        c.setPadding(8);
                        pdfTable.addCell(c);
                    }

                    double totala = 0;
                    for (Object item : taula.getItems()) {
                        Salmenta s = (Salmenta) item;
                        pdfTable.addCell(new PdfPCell(new Phrase(String.valueOf(s.getId()), bodyFont)));
                        pdfTable.addCell(new PdfPCell(new Phrase(s.getBezeroa() != null ? s.getBezeroa() : "-", bodyFont)));
                        pdfTable.addCell(new PdfPCell(new Phrase(s.getData() != null ? s.getData().toString() : "-", bodyFont)));
                        pdfTable.addCell(new PdfPCell(new Phrase(String.format("%.2f €", s.getTotala()), bodyFont)));
                        pdfTable.addCell(new PdfPCell(new Phrase(s.getEgoera() != null ? s.getEgoera() : "-", bodyFont)));
                        totala += s.getTotala();
                    }
                    document.add(pdfTable);

                    Font totalFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new BaseColor(39, 174, 96));
                    Paragraph pTotal = new Paragraph(String.format("\nSalmentak Guztira: %.2f €", totala), totalFont);
                    pTotal.setAlignment(Element.ALIGN_RIGHT);
                    document.add(pTotal);

                } else { // Produktuak
                    PdfPTable pdfTable = new PdfPTable(3);
                    pdfTable.setWidthPercentage(100);
                    pdfTable.setWidths(new float[]{1.5f, 6f, 2.5f});

                    String[] headers = {"ID", "Produktua", "Prezioa (€)"};
                    for (String h : headers) {
                        PdfPCell c = new PdfPCell(new Phrase(h, headFont));
                        c.setBackgroundColor(headerColor);
                        c.setPadding(8);
                        pdfTable.addCell(c);
                    }

                    for (Object item : taula.getItems()) {
                        Produktua p = (Produktua) item;
                        pdfTable.addCell(new PdfPCell(new Phrase(String.valueOf(p.getId()), bodyFont)));
                        pdfTable.addCell(new PdfPCell(new Phrase(p.getIzena(), bodyFont)));
                        pdfTable.addCell(new PdfPCell(new Phrase(String.format("%.2f €", p.getPrezioa()), bodyFont)));
                    }
                    document.add(pdfTable);
                }

                document.close();
                erakutsiAlerta(Alert.AlertType.INFORMATION, "Arrakasta", "PDF txostena gorde da:\n" + fitxategia.getAbsolutePath());

            } catch (Exception ex) {
                erakutsiAlerta(Alert.AlertType.ERROR, "Errorea", ex.getMessage());
            }
        }
    }

    /**
     * Taulan bistaratutako datuak CSV formatuko fitxategi batera esportatzen dituen metodo pribatua (puntu eta komaz banatuta).
     */
    private void esportatuCSV() {
        boolean isSalmentak = "Salmenten Txostena".equals(cbTxostenMota.getValue());
        String fitxategiIzena = isSalmentak ? "Salmentak_Txostena.csv" : "Produktuak_Txostena.csv";

        FileChooser fileChooser = prepareFileChooser(fitxategiIzena, "CSV fitxategiak (*.csv)", "*.csv");
        File fitxategia = fileChooser.showSaveDialog(getScene().getWindow());

        if (fitxategia != null) {
            try (PrintWriter writer = new PrintWriter(fitxategia, StandardCharsets.UTF_8)) {
                if (isSalmentak) {
                    writer.println("ID;Faktura/Eskaera;Data;Guztira (€);Egoera");
                    for (Object item : taula.getItems()) {
                        Salmenta s = (Salmenta) item;
                        String dataStr = s.getData() != null ? s.getData().toString() : "";
                        writer.println(String.format("%d;\"%s\";\"%s\";%.2f;\"%s\"",
                                s.getId(), s.getBezeroa(), dataStr, s.getTotala(), s.getEgoera()));
                    }
                } else {
                    writer.println("ID;Produktua;Prezioa (€)");
                    for (Object item : taula.getItems()) {
                        Produktua p = (Produktua) item;
                        writer.println(String.format("%d;\"%s\";%.2f",
                                p.getId(), p.getIzena(), p.getPrezioa()));
                    }
                }
                erakutsiAlerta(Alert.AlertType.INFORMATION, "Arrakasta", "CSV txostena gorde da:\n" + fitxategia.getAbsolutePath());
            } catch (Exception ex) {
                erakutsiAlerta(Alert.AlertType.ERROR, "Errorea", ex.getMessage());
            }
        }
    }

    /**
     * Fitxategiak gordetzeko elkarrizketa-leihoa (FileChooser) prestatzen duen laguntza-metodo pribatua.
     *
     * @param fileName    Fitxategiaren hasierako izena.
     * @param description Fitxategi motaren deskribapena.
     * @param extension   Fitxategiaren luzapena (adib. "*.pdf").
     * @return {@link FileChooser} Konfiguratutako elkarrizketa-leihoa.
     */
    private FileChooser prepareFileChooser(String fileName, String description, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Gorde Txostena");
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, extension));

        String userHome = System.getProperty("user.home");
        File docsFolder = new File(userHome, "Documents");
        if (docsFolder.exists()) {
            fileChooser.setInitialDirectory(docsFolder);
        }
        return fileChooser;
    }

    /**
     * Erabiltzaileari abisu, errore edo informazio mezuak erakusteko elkarrizketa-leihoa (Alert) bistaratzen duen metodo laguntzailea.
     *
     * @param mota    Alertaren mota (INFORMATION, ERROR, etab.).
     * @param titulua Leihoaren izenburua.
     * @param edukia  Erakutsi nahi den mezua.
     */
    private void erakutsiAlerta(Alert.AlertType mota, String titulua, String edukia) {
        Alert alert = new Alert(mota);
        alert.setTitle(titulua);
        alert.setHeaderText(null);
        alert.setContentText(edukia);
        alert.showAndWait();
    }
}
