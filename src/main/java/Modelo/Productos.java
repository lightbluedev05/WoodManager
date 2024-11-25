
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


public class Productos {

    private Connection cx;
    private int producto_seleccionado = 0;

    public Productos() {
        cx = new Conexion().Conectar();
    }

    public boolean anadir_producto(String medida, String color_cera,
                                   String tipo_pintado, int stock) {
        
        if(medida.isEmpty() || color_cera.isEmpty() || tipo_pintado.isEmpty()){
            return false;
        }
        
        String sql = "INSERT INTO producto_almacenado (medida, color_cera, "
                   + "tipo_pintado, stock) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setString(1, medida);
            ps.setString(2, color_cera);
            ps.setString(3, tipo_pintado);
            ps.setInt(4, stock);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editar_producto(String medida, String color_cera, String tipo_pintado, int stock) {
        
        StringBuilder sql = new StringBuilder("UPDATE producto_almacenado SET ");
        boolean primero = true;

        if (!medida.isEmpty()) {
            sql.append("medida = ?");
            primero = false;
        }
        if (!color_cera.isEmpty()) {
            if (!primero) sql.append(", ");
            sql.append("color_cera = ?");
            primero = false;
        }
        if (!tipo_pintado.isEmpty()) {
            if (!primero) sql.append(", ");
            sql.append("tipo_pintado = ?");
            primero = false;
        }
        if (stock >= 0) {
            if (!primero) sql.append(", ");
            sql.append("stock = ?");
        }

        sql.append(" WHERE id_producto = ?");

        try {
            PreparedStatement ps = cx.prepareStatement(sql.toString());

            int index = 1;
            if (!medida.isEmpty()) {
                ps.setString(index++, medida);
            }
            if (!color_cera.isEmpty()) {
                ps.setString(index++, color_cera);
            }
            if (!tipo_pintado.isEmpty()) {
                ps.setString(index++, tipo_pintado);
            }
            if (stock >= 0) {
                ps.setInt(index++, stock);
            }
            ps.setInt(index, producto_seleccionado);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean eliminar_producto() {
        String sql = "DELETE FROM producto_almacenado WHERE id_producto = ?";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, producto_seleccionado);

            int filas_afectadas = ps.executeUpdate();
            ps.close();

            return filas_afectadas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public HashMap<String, Object> buscar_producto(int id_producto) {
        HashMap<String, Object> producto = new HashMap<>();
        String sql = "SELECT id_producto, medida, color_cera, tipo_pintado, "
                   + "stock FROM producto_almacenado WHERE id_producto = ?";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ps.setInt(1, id_producto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                producto.put("id_producto", rs.getInt("id_producto"));
                producto.put("medida", rs.getString("medida"));
                producto.put("color_cera", rs.getString("color_cera"));
                producto.put("tipo_pintado", rs.getString("tipo_pintado"));
                producto.put("stock", rs.getInt("stock"));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return producto;
    }
    
    public boolean mostrar_busqueda(int id_producto, JTable tabla){
        HashMap<String, Object> producto = buscar_producto(id_producto);
        if(producto == null || producto.isEmpty()){
            return false;
        }
        
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        
        modelo.addRow(new Object[]{
            producto.get("id_producto"),
            producto.get("medida"),
            producto.get("color_cera"),
            producto.get("tipo_pintado"),
            producto.get("stock")
        });
        
        tabla.setModel(modelo);
        return true;
    }
    

    public ArrayList<HashMap<String, Object>> obtener_todos_productos() {
        ArrayList<HashMap<String, Object>> productos = new ArrayList<>();
        String sql = "SELECT id_producto, medida, color_cera, tipo_pintado, "
                   + "stock FROM producto_almacenado";

        try {
            PreparedStatement ps = cx.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                HashMap<String, Object> producto = new HashMap<>();
                producto.put("id_producto", rs.getInt("id_producto"));
                producto.put("medida", rs.getString("medida"));
                producto.put("color_cera", rs.getString("color_cera"));
                producto.put("tipo_pintado", rs.getString("tipo_pintado"));
                producto.put("stock", rs.getInt("stock"));
                productos.add(producto);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }
    
    public void actualizar_tabla(JTable tabla){
        ArrayList<HashMap<String, Object>> productos = obtener_todos_productos();
        
        Object[][] filas = new Object[productos.size()][5];
        
        int i=0;
        for (HashMap<String, Object> ingreso : productos) {
            filas[i][0] = ingreso.get("id_producto");
            filas[i][1] = ingreso.get("medida");
            filas[i][2] = ingreso.get("color_cera");
            filas[i][3] = ingreso.get("tipo_pintado");
            filas[i][4] = ingreso.get("stock");
            i++;
        }
        
        DefaultTableModel modelo_tabla = new DefaultTableModel(
            filas,
            new Object[]{"ID", "Medida", "Color Cera", "Tipo Pintado", "Stock"}
        );
        
        tabla.setModel(modelo_tabla);
    }
    
    public void mostrar_datos_seleccion(JTable tabla, JTextField medida_input, JTextField color_cera_input,
            JTextField tipo_pintado_input, JTextField stock_input){
        
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int fila_seleccionada = tabla.getSelectedRow();
                if(fila_seleccionada != -1){
                    producto_seleccionado = (int) tabla.getValueAt(fila_seleccionada, 0);
                    String medida = (String) tabla.getValueAt(fila_seleccionada, 1);
                    String color_cera = (String) tabla.getValueAt(fila_seleccionada, 2);
                    String tipo_pintado = (String) tabla.getValueAt(fila_seleccionada, 3);
                    String stock = Integer.toString((int) tabla.getValueAt(fila_seleccionada, 4));
                    
                    medida_input.setText(medida);
                    color_cera_input.setText(color_cera);
                    tipo_pintado_input.setText(tipo_pintado);
                    stock_input.setText(stock);
                }
            }
        });
    }
    
}

