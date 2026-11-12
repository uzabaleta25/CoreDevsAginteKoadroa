package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.Controller.SceneManager;
import org.example.Dao.ProduktuaDAO;
import org.example.Dao.SalmentaDAO;

/**
 * JavaFX aplikazioaren sarrera-puntu nagusia (Main Class).
 * Interfaze grafikoa abiarazten du, DAOak sortzen ditu eta SceneManager kudeatzailea abiarazten du.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class Main extends Application {

    /**
     * JavaFX aplikazioaren abio-metodo nagusia.
     * DAOak eta eszena nagusia eraikitzen ditu, eta leihoaren izenburua ezartzen du.
     *
     * @param primaryStage Aplikazioaren leiho nagusia (Stage).
     */
    @Override
    public void start(Stage primaryStage) {
        // 1. DAO eta Hasierako Scenea sortu
        ProduktuaDAO produktuaDAO = new ProduktuaDAO();
        SalmentaDAO salmentaDAO = new SalmentaDAO();
        Scene scene = new Scene(new Pane(), 800, 600);

        // 2. SceneManager abiarazi eta hasierako pantaila kargatu
        SceneManager sceneManager = new SceneManager(scene, produktuaDAO, salmentaDAO);

        // 3. Leihoa bistaratu
        primaryStage.setTitle("Erronka CoreDevs - Denda");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Aplikazioa abiarazteko metodo estandarra.
     *
     * @param args Komando-lineako argumentuak.
     */
    public static void main(String[] args) {
        launch(args);
    }
}