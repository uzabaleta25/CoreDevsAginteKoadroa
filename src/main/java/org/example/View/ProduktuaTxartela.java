package org.example.View;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.Model.Produktua;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

/**
 * Produktu baten informazioa, stock-a eta argazkia bistaratzen dituen txartel grafikoa (HBox).
 *
 * @author CoreDevs Taldea
 * @version 1.1
 */
public class ProduktuaTxartela extends HBox {

    private final Produktua produktua;

    public ProduktuaTxartela(Produktua p) {
        super(15);
        this.produktua = p;

        this.setPadding(new Insets(12));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPrefWidth(300);
        this.setMaxWidth(320);

        egokituEstiloak();

        // Irudiaren edukiontzia
        ImageView imageView = new ImageView();
        imageView.setFitWidth(65);
        imageView.setFitHeight(65);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(65, 65);
        clip.setArcWidth(10);
        clip.setArcHeight(10);
        imageView.setClip(clip);

        byte[] bytes = p.getArgazkiaBytes();
        if (bytes != null && bytes.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                Image img = new Image(bis);

                if (img.isError() || img.getWidth() == 0) {
                    ByteArrayInputStream bis2 = new ByteArrayInputStream(bytes);
                    BufferedImage bufferedImage = ImageIO.read(bis2);
                    if (bufferedImage != null) {
                        img = SwingFXUtils.toFXImage(bufferedImage, null);
                    }
                }

                if (img != null && !img.isError()) {
                    imageView.setImage(img);
                }
            } catch (Exception e) {
                System.err.println("Errorea irudia kargatzean (ID " + p.getId() + "): " + e.getMessage());
            }
        }

        // Testuen edukiontzia
        VBox testuBox = new VBox(4);

        Label izenaLbl = new Label(p.getIzena());
        izenaLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        izenaLbl.setStyle("-fx-text-fill: #2c3e50;");
        izenaLbl.setWrapText(true);

        // ID ordez Stock-a erakutsi
        int stockKopurua = (int) p.getStock();
        Label stockLbl = new Label("Stock: " + stockKopurua + " ale");
        if (stockKopurua > 5) {
            stockLbl.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;"); // Berdea
        } else if (stockKopurua > 0) {
            stockLbl.setStyle("-fx-text-fill: #e67e22; -fx-font-weight: bold;"); // Laranja (stock baxua)
        } else {
            stockLbl.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;"); // Gorria (agotuta)
        }

        Label prezioaLbl = new Label(String.format("%.2f €", p.getPrezioa()));
        prezioaLbl.setFont(Font.font("System", FontWeight.BOLD, 13));
        prezioaLbl.setStyle("-fx-text-fill: #0078d7;");

        testuBox.getChildren().addAll(izenaLbl, stockLbl, prezioaLbl);

        this.getChildren().addAll(imageView, testuBox);
    }

    private void egokituEstiloak() {
        String baseStyle = "-fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;";

        this.setStyle(baseStyle + " -fx-border-color: #bdc3c7; -fx-background-color: #ffffff;");

        this.setOnMouseEntered(e ->
                this.setStyle(baseStyle + " -fx-border-color: #0078d7; -fx-background-color: #f4f9ff;")
        );

        this.setOnMouseExited(e ->
                this.setStyle(baseStyle + " -fx-border-color: #bdc3c7; -fx-background-color: #ffffff;")
        );
    }

    public Produktua getProduktua() {
        return produktua;
    }
}