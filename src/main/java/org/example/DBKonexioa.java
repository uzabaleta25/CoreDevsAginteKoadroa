package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Odooren PostgreSQL datu-basearekin konexioa ezartzeaz eta kudeatzeaz arduratzen den klase laguntzailea.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class DBKonexioa {

    private static final String URL = "jdbc:postgresql://192.168.80.116:5432/CoreDevs_ERP";
    private static final String USER = "openpg";
    private static final String PASSWORD = "openpgpwd";

    /**
     * PostgreSQL datu-basearekiko konexio aktibo bat irekitzen eta itzultzen du.
     *
     * @return Connection datu-basearen konexio objektua.
     * @throws SQLException Konexioak huts egiten badu edo kredentzialak okerrak badira.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
