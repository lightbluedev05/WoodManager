package Modelo;

import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Materiales {
    
    private Connection cx;
    private int material_seleccionado = 0;
    
    public Materiales(){
        cx = new Conexion().Conectar();
    }
    
    public boolean anadir_material(String nombre_material, int id_proveedor, String unidad_medida, int stock) {
        
        if(nombre_material.isEmpty() || id_proveedor == 0 || unidad_medida.isEmpty()){
            return false;
        }
        if(stock<0){
            return false;
        }
        
        String sql = "INSERT INTO material_almacenado ("
                   + "nombre_material, id_proveedor, unidad_medida, stock"
                   + ") VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setString(1, nombre_material);
            ps.setInt(2, id_proveedor);
            ps.setString(3, unidad_medida);
            ps.setInt(4, stock);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean editar_material(String nombre_material, int id_proveedor, String unidad_medida, int stock) {

        StringBuilder sql = new StringBuilder("UPDATE material_almacenado SET ");
        boolean primero = true;

        if (!nombre_material.isEmpty()) {
            sql.append("nombre_material = ?");
            primero = false;
        }
        if (id_proveedor > 0) {
            if (!primero) sql.append(", ");
            sql.append("id_proveedor = ?");
            primero = false;
        }
        if (!unidad_medida.isEmpty()) {
            if (!primero) sql.append(", ");
            sql.append("unidad_medida = ?");
            primero = false;
        }
        if (stock >= 0) {
            if (!primero) sql.append(", ");
            sql.append("stock = ?");
        }

        sql.append(" WHERE id_material = ?");

        try {
            PreparedStatement ps = cx.prepareStatement(sql.toString());

            int index = 1;
            if (!nombre_material.isEmpty()) {
                ps.setString(index++, nombre_material);
            }
            if (id_proveedor > 0) {
                ps.setInt(index++, id_proveedor);
            }
            if (!unidad_medida.isEmpty()) {
                ps.setString(index++, unidad_medida);
            }
            if (stock >= 0) {
                ps.setInt(index++, stock);
            }
            ps.setInt(index, material_seleccionado);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean eliminar_material() {
        String sql = "DELETE FROM material_almacenado WHERE id_material = ?";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, material_seleccionado);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public HashMap<String, Object> buscar_material(int id_material) {
        HashMap<String, Object> material = new HashMap<>();
        String sql = "SELECT id_material, nombre_material, id_proveedor, unidad_medida, stock FROM material_almacenado WHERE id_material = ?";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, id_material);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                material.put("id_material", rs.getInt("id_material"));
                material.put("nombre_material", rs.getString("nombre_material"));
                material.put("id_proveedor", rs.getInt("id_proveedor"));
                material.put("unidad_medida", rs.getString("unidad_medida"));
                material.put("stock", rs.getInt("stock"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return material;
    }
    
    public boolean mostrar_busqueda(int id_material, JTable tabla){
        HashMap<String, Object> material = buscar_material(id_material);
        if(material == null || material.isEmpty()){
            return false;
        }
        
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{
            material.get("id_material"),
            material.get("nombre_material"),
            material.get("id_proveedor"),
            material.get("unidad_medida"),
            material.get("stock")
        });
        
        tabla.setModel(modelo);
        return true;
    }
    
    
    public ArrayList<HashMap<String, Object>> obtener_todos_materiales() {
        ArrayList<HashMap<String, Object>> materiales = new ArrayList<>();
        String sql = "SELECT id_material, nombre_material, id_proveedor, unidad_medida, stock FROM material_almacenado";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, Object> material = new HashMap<>();
                material.put("id_material", rs.getInt("id_material"));
                material.put("nombre_material", rs.getString("nombre_material"));
                material.put("id_proveedor", rs.getInt("id_proveedor"));
                material.put("unidad_medida", rs.getString("unidad_medida"));
                material.put("stock", rs.getInt("stock"));
                materiales.add(material);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return materiales;
    }
    
    public void actualizar_tabla(JTable tabla){
        ArrayList<HashMap<String, Object>> materiales = obtener_todos_materiales();
        
        Object[][] filas = new Object[materiales.size()][5];
        
        int i=0;
        for (HashMap<String, Object> ingreso : materiales) {
            filas[i][0] = ingreso.get("id_material");
            filas[i][1] = ingreso.get("nombre_material");
            filas[i][2] = ingreso.get("id_proveedor");
            filas[i][3] = ingreso.get("unidad_medida");
            filas[i][4] = ingreso.get("stock");
            i++;
        }
        
        DefaultTableModel modelo_tabla = new DefaultTableModel(
            filas,
            new Object[]{"ID", "Nombre", "ID Proveedor", "Unidad Medida", "Stock"}
        );
        
        tabla.setModel(modelo_tabla);
    }

    public void mostrar_datos_seleccion(JTable tabla, JTextField nombre_input, JTextField proveedor_input,
            JTextField unidad_medida_input, JTextField stock_input){
        
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int fila_seleccionada = tabla.getSelectedRow();
                if(fila_seleccionada != -1){
                    material_seleccionado = (int) tabla.getValueAt(fila_seleccionada, 0);
                    String nombre = (String) tabla.getValueAt(fila_seleccionada, 1);
                    String id_proveedor = Integer.toString((int) tabla.getValueAt(fila_seleccionada, 2));
                    String unidad_medida = (String) tabla.getValueAt(fila_seleccionada, 3);
                    String stock = Integer.toString((int) tabla.getValueAt(fila_seleccionada, 4));
                    
                    nombre_input.setText(nombre);
                    proveedor_input.setText(id_proveedor);
                    unidad_medida_input.setText(unidad_medida);
                    stock_input.setText(stock);
                }
            }
        });
    }

    
    
}
