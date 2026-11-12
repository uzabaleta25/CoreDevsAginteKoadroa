package org.example.Controller;

import javafx.scene.Scene;
import org.example.Dao.ProduktuaDAO;
import org.example.Dao.SalmentaDAO;
import org.example.View.DashboardView;
import org.example.View.MainLayout;
import org.example.View.ProduktuaView;
import org.example.View.TxostenakView;

/**
 * Aplikazioaren kontrolatzaile nagusia eta bista-kudeatzailea (MVC arkitektura).
 * Leihoen arteko nabigazioa, interfazearen aldatzea eta DAO instantzien banaketa kudeatzen ditu.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class SceneManager {

    private final Scene scene;
    private final ProduktuaDAO produktuaDAO;
    private final SalmentaDAO salmentaDAO;
    private final MainLayout mainLayout;

    /**
     * SceneManager klaserako eraikitzailea.
     *
     * @param scene        Aplikazioaren eszena nagusia (Scene).
     * @param produktuaDAO Produktuen datuak atzitzeko DAOa.
     * @param salmentaDAO  Salmenten datuak atzitzeko DAOa.
     */
    public SceneManager(Scene scene, ProduktuaDAO produktuaDAO, SalmentaDAO salmentaDAO) {
        this.scene = scene;
        this.produktuaDAO = produktuaDAO;
        this.salmentaDAO = salmentaDAO;

        // MainLayout kargatu eta interfazearen erro nagusi gisa ezarri
        this.mainLayout = new MainLayout(this);
        this.scene.setRoot(mainLayout);

        // Hasieran Aginte Koadroa kargatu
        showDashboard();
    }

    /**
     * Aginte Koadroaren (Dashboard) bista kargatzen du erdialdeko panelean.
     */
    public void showDashboard() {
        DashboardView dashboardView = new DashboardView(this, this.salmentaDAO, this.produktuaDAO);
        mainLayout.setEdukia(dashboardView);
    }

    /**
     * Produktuen katalogoaren bista kargatzen du erdialdeko panelean.
     */
    public void showProduktuak() {
        ProduktuaView produktuaView = new ProduktuaView(this, this.produktuaDAO);
        mainLayout.setEdukia(produktuaView);
    }

    /**
     * Txostenen eta esportazioen bista kargatzen du erdialdeko panelean.
     */
    public void showTxostenak() {
        TxostenakView txostenakView = new TxostenakView(this, this.salmentaDAO, this.produktuaDAO);
        mainLayout.setEdukia(txostenakView);
    }
}