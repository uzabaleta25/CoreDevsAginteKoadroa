package org.example.Dao;

import org.example.DBKonexioa;
import org.example.Model.Salmenta;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Odooren 'account_move' taulatik fakturen eta salmenten datuak berreskuratzeko DAO klasea.
 *
 * @author CoreDevs Taldea
 * @version 1.0
 */
public class SalmentaDAO {

    /**
     * LineChart grafikora bideratzeko hileroko salmenten batura lortzen du.
     *
     * @return {@link Map} Gako gisa hilabetea ('YYYY-MM') eta balio gisa salmenten batura (€).
     */
    public Map<String, Double> lortuHilerokoSalmentak() {
        Map<String, Double> salmentak = new LinkedHashMap<>();
        String sql = "SELECT TO_CHAR(invoice_date, 'YYYY-MM') AS mes, SUM(amount_total) AS total " +
                "FROM account_move " +
                "WHERE state = 'posted' AND move_type = 'out_invoice' " +
                "GROUP BY mes " +
                "ORDER BY mes ASC";

        try (Connection conn = DBKonexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                salmentak.put(rs.getString("mes"), rs.getDouble("total"));
            }
        } catch (SQLException e) {
            System.err.println("Errorea hileroko salmentak kargatzean: " + e.getMessage());
        }
        return salmentak;
    }

    /**
     * KPI txartelerako salmenta/faktura guztien batura osoa lortzen du.
     *
     * @return double Salmenten zenbateko osoa (€).
     */
    public double lortuSalmentakGuztira() {
        String sql = "SELECT SUM(amount_total) FROM account_move WHERE state = 'posted' AND move_type = 'out_invoice'";
        try (Connection conn = DBKonexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Errorea KPI salmenta guztiak lortzean: " + e.getMessage());
        }
        return 0.0;
    }

    /**
     * Faktura guztien zerrenda kargatzen du Salmenta objektu gisa, txostenak eta taulak osatzeko.
     *
     * @return {@link List} Salmenta objektuen zerrenda osoa.
     */
    public List<Salmenta> lortuSalmentakGuztiak() {
        List<Salmenta> zerrenda = new ArrayList<>();
        String sql = "SELECT id, name, invoice_date, amount_total, state " +
                "FROM account_move " +
                "WHERE state = 'posted' AND move_type = 'out_invoice' " +
                "ORDER BY invoice_date DESC";

        try (Connection conn = DBKonexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Date sqlDate = rs.getDate("invoice_date");
                Salmenta s = new Salmenta(
                        rs.getInt("id"),
                        rs.getString("name"),
                        sqlDate != null ? sqlDate.toLocalDate() : null,
                        rs.getDouble("amount_total"),
                        rs.getString("state")
                );
                zerrenda.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Errorea salmenten zerrenda lortzean: " + e.getMessage());
        }
        return zerrenda;
    }
}
