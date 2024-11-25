package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Proveedores {

    private Connection cx;
    private int proveedor_seleccionado = 0;

    public Proveedores(){
        cx = new Conexion().Conectar();
    }

    public boolean anadir_proveedor(String nombre, String telefono, String descripcion) {
        
        if(nombre.isEmpty() || telefono.isEmpty() || descripcion.isEmpty()){
            return false;
        }
        
        String sql = "INSERT INTO proveedor (nombre, telefono, descripcion) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, descripcion);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editar_proveedor(String nombre, String telefono, String descripcion) {
        
        StringBuilder sql = new StringBuilder("UPDATE proveedor SET ");
        boolean primero = true;

        if (!nombre.isEmpty()) {
            sql.append("nombre = ?");
            primero = false;
        }
        if (!telefono.isEmpty()) {
            if (!primero) sql.append(", ");
            sql.append("telefono = ?");
            primero = false;
        }
        if (!descripcion.isEmpty()) {
            if (!primero) sql.append(", ");
            sql.append("descripcion = ?");
        }

        sql.append(" WHERE id_proveedor = ?");

        try {
            PreparedStatement ps = cx.prepareStatement(sql.toString());

            int index = 1;
            if (!nombre.isEmpty()) {
                ps.setString(index++, nombre);
            }
            if (!telefono.isEmpty()) {
                ps.setString(index++, telefono);
            }
            if (!descripcion.isEmpty()) {
                ps.setString(index++, descripcion);
            }
            ps.setInt(index, proveedor_seleccionado);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar_proveedor() {
        String sql = "DELETE FROM proveedor WHERE id_proveedor = ?";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, proveedor_seleccionado);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, Object> buscar_proveedor(int id_proveedor) {
        HashMap<String, Object> proveedor = new HashMap<>();
        String sql = "SELECT id_proveedor, nombre, telefono, descripcion FROM proveedor WHERE id_proveedor = ?";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, id_proveedor);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                proveedor.put("id_proveedor", rs.getInt("id_proveedor"));
                proveedor.put("nombre", rs.getString("nombre"));
                proveedor.put("telefono", rs.getString("telefono"));
                proveedor.put("descripcion", rs.getString("descripcion"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proveedor;
    }
    
    public boolean mostrar_busqueda(int id_proveedor, JTable tabla){
        HashMap<String, Object> proveedor = buscar_proveedor(id_proveedor);
        if(proveedor == null || proveedor.isEmpty()){
            return false;
        }
        
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{
            proveedor.get("id_proveedor"),
            proveedor.get("nombre"),
            proveedor.get("telefono"),
            proveedor.get("descripcion")
        });
        
        tabla.setModel(modelo);
        return true;
    }

    public ArrayList<HashMap<String, Object>> obtener_todos_proveedores() {
        ArrayList<HashMap<String, Object>> proveedores = new ArrayList<>();
        String sql = "SELECT id_proveedor, nombre, telefono, descripcion FROM proveedor";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, Object> proveedor = new HashMap<>();
                proveedor.put("id_proveedor", rs.getInt("id_proveedor"));
                proveedor.put("nombre", rs.getString("nombre"));
                proveedor.put("telefono", rs.getString("telefono"));
                proveedor.put("descripcion", rs.getString("descripcion"));
                proveedores.add(proveedor);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return proveedores;
    }
    
    public void actualizar_tabla(JTable tabla){
        ArrayList<HashMap<String, Object>> proveedores = obtener_todos_proveedores();
        
        Object[][] filas = new Object[proveedores.size()][4];
        
        int i=0;
        for (HashMap<String, Object> ingreso : proveedores) {
            filas[i][0] = ingreso.get("id_proveedor");
            filas[i][1] = ingreso.get("nombre");
            filas[i][2] = ingreso.get("telefono");
            filas[i][3] = ingreso.get("descripcion");
            i++;
        }
        
        DefaultTableModel modelo_tabla = new DefaultTableModel(
            filas,
            new Object[]{"ID", "Nombre", "Telefono", "Descripcion"}
        );
        
        tabla.setModel(modelo_tabla);
    }
    
    public void mostrar_datos_seleccion(JTable tabla, JTextField nombre_input, JTextField telefono_input, JTextField descripcion_input){
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int fila_seleccionada = tabla.getSelectedRow();
                if(fila_seleccionada != -1){
                    proveedor_seleccionado = (int) tabla.getValueAt(fila_seleccionada, 0);
                    String nombre = (String) tabla.getValueAt(fila_seleccionada, 1);
                    String telefono = (String) tabla.getValueAt(fila_seleccionada, 2);
                    String descripcion = (String) tabla.getValueAt(fila_seleccionada, 3);
                    
                    nombre_input.setText(nombre);
                    telefono_input.setText(telefono);
                    descripcion_input.setText(descripcion);
                }
            }
        });
    }
}
