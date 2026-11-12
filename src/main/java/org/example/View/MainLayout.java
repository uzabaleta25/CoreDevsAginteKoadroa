package org.example.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.Controller.SceneManager;

/**
 * Aplikazioaren egitura nagusia (Layout). Alboko menua (Sidebar) finkatuta mantentzen du
 * eta erdialdeko edukia dinamikoki aldatzea ahalbidetzen du.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class MainLayout extends BorderPane {

    private final SceneManager sceneManager;

    /**
     * MainLayout klaserako eraikitzailea.
     * Ezkerreko alboko menua finko ezartzen du eraikuntzan.
     *
     * @param sceneManager Bisten arteko nabigazioa kudeatzen duen kontrolatzailea.
     */
    public MainLayout(SceneManager sceneManager) {
        this.sceneManager = sceneManager;

        // Ezkerreko alboko menua finko ezarri
        this.setLeft(sortuSidebar());
    }

    /**
     * Erdialdeko ikuspegia aldatzeko metodoa (Dashboard, Produktuak edo Txostenak).
     *
     * @param edukia Erdialdean bistaratuko den Node osagaia.
     */
    public void setEdukia(Node edukia) {
        this.setCenter(edukia);
    }

    /**
     * Ezkerreko Sidebar panela eraikitzeko metodo pribatua.
     * Aplikazioaren izenburua eta nabigazio-botoiak sortzen ditu.
     *
     * @return VBox alboko menuaren edukiontzi grafikoa.
     */
    private VBox sortuSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(25, 15, 25, 15));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #2c3e50;"); // Urdin ilun korporatiboa

        // Aplikazioaren izenburu nagusia
        Label appTitle = new Label("CoreDevs ERP");
        appTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        appTitle.setStyle("-fx-text-fill: #ffffff;");
        appTitle.setAlignment(Pos.CENTER);

        // 1. Aginte Koadroa Botoia
        Button dashboardBtn = sortuMenuBotoia("Aginte Koadroa");
        dashboardBtn.setOnAction(e -> sceneManager.showDashboard());

        // 2. Produktuak Botoia
        Button produktuakBtn = sortuMenuBotoia("Produktuak");
        produktuakBtn.setOnAction(e -> sceneManager.showProduktuak());

        // 3. Txostenak Botoia (Enpresako arduradunarentzat)
        Button txostenakBtn = sortuMenuBotoia("Txostenak");
        txostenakBtn.setOnAction(e -> sceneManager.showTxostenak());

        // Elementu guztiak alboko menura gehitu
        sidebar.getChildren().addAll(appTitle, dashboardBtn, produktuakBtn, txostenakBtn);
        return sidebar;
    }

    /**
     * Menu botoiei estilo uniformea eta hover efektua emateko metodo laguntzailea.
     *
     * @param testua Botoian bistaratuko den testua.
     * @return Button eraikitako eta konfiguratutako botoia.
     */
    private Button sortuMenuBotoia(String testua) {
        Button btn = new Button(testua);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; " +
                "-fx-text-fill: #ecf0f1; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 10 15; " +
                "-fx-cursor: hand;");

        // Sagua gainetik igarotzean kolorea iluntzeko efektua
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-padding: 10 15; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-padding: 10 15; -fx-cursor: hand;"));

        return btn;
    }
}