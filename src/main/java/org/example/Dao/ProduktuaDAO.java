package org.example.Dao;

import org.example.DBKonexioa;
import org.example.Model.Produktua;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Odoo PostgreSQL datu-basetik produktuen datuak eta stock kopurua berreskuratzeko DAO klasea.
 *
 * @author CoreDevs Taldea
 * @version 1.1
 */
public class ProduktuaDAO {

    /**
     * Odooko 'product_template' eta 'stock_quant' tauletatik produktu aktiboen zerrenda eta stock-a kargatzen ditu.
     *
     * @return {@link List} Produktua objektuen zerrenda osoa.
     */
    public List<Produktua> lortuProduktuak() {
        List<Produktua> produktuak = new ArrayList<>();

        String sql = "SELECT \n" +
                "    pt.id, \n" +
                "    COALESCE(pt.name->>'es_ES', pt.name->>'en_US', pt.name->>'en_GB', pt.name::text) AS izena, \n" +
                "    pt.list_price, \n" +
                "    COALESCE((\n" +
                "        SELECT SUM(GREATEST(COALESCE(sq.quantity, 0), COALESCE(sq.inventory_quantity, 0)))\n" +
                "        FROM stock_quant sq \n" +
                "        JOIN product_product pp ON sq.product_id = pp.id \n" +
                "        WHERE pp.product_tmpl_id = pt.id\n" +
                "    ), 0) AS stock,\n" +
                "    att.db_datas AS argazkia_bytes\n" +
                "FROM product_template pt\n" +
                "LEFT JOIN ir_attachment att \n" +
                "       ON att.res_model = 'product.template' \n" +
                "      AND att.res_id = pt.id \n" +
                "      AND att.res_field = 'image_1920'\n" +
                "WHERE pt.active = true AND pt.sale_ok = true\n" +
                "ORDER BY pt.id DESC;";

        try (Connection conn = DBKonexioa.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String izena = rs.getString("izena");
                double prezioa = rs.getDouble("list_price");
                double stock = rs.getDouble("stock");
                byte[] argazkiaBytes = rs.getBytes("argazkia_bytes");

                Produktua p = new Produktua(id, izena, prezioa, stock, argazkiaBytes);
                produktuak.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Errorea produktuak kargatzean: " + e.getMessage());
        }

        return produktuak;
    }
}