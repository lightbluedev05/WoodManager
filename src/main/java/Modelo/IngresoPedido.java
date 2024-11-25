package Modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class IngresoPedido {
    
    public ArrayList<HashMap<String, Object>> lista_ingresos = new ArrayList<>();
    private Connection cx = new Conexion().Conectar();
    
    public boolean ingreso_anadir_material(int id_material, String nombre, int cantidad, int stock){
        /*
        
        int aux_cant = 0;
        for(HashMap<String, Object> ingreso:lista_ingresos){
            
            if(ingreso.get("tipo").equals("producto")){
                continue;
            }
            
            if(!((int)ingreso.get("id") == id_material)){
                continue;
            }
            
            int cantidad_ingreso = (int) ingreso.get("cantidad");
            aux_cant += cantidad_ingreso;
        }
        
        if(aux_cant+cantidad > stock){
            return false;
        }
        */
        
        HashMap<String, Object> material = new HashMap<>();
        material.put("tipo", "material");
        material.put("id", id_material);
        material.put("nombre", nombre);
        material.put("cantidad", cantidad);
        try{
            lista_ingresos.add(material);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public boolean ingreso_anadir_producto(int id_producto, String nombre, int cantidad, int stock){
        /*
        
        int aux_cant = 0;
        for(HashMap<String, Object> ingreso:lista_ingresos){
            
            if(ingreso.get("tipo").equals("material")){
                continue;
            }
            
            if(!((int)ingreso.get("id") == id_producto)){
                continue;
            }
            
            int cantidad_ingreso = (int) ingreso.get("cantidad");
            aux_cant += cantidad_ingreso;
        }
        
        if(aux_cant+cantidad > stock){
            return false;
        }
        */
        
        
        HashMap<String, Object> material = new HashMap<>();
        material.put("tipo", "producto");
        material.put("id", id_producto);
        material.put("nombre", nombre);
        material.put("cantidad", cantidad);
        try{
            lista_ingresos.add(material);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public void actualizar_lista_ingreso(JTable tabla){
        
        Object[][] filas = new Object[lista_ingresos.size()][4];
        
        int i=0;
        for (HashMap<String, Object> ingreso : lista_ingresos) {
            filas[i][0] = ingreso.get("tipo");
            filas[i][1] = ingreso.get("id");
            filas[i][2] = ingreso.get("nombre");
            filas[i][3] = ingreso.get("cantidad");
            i++;
        }
        
        DefaultTableModel modelo_tabla = new DefaultTableModel(
            filas,
            new Object[]{"Tipo", "ID", "Nombre", "Cantidad"}
        );
        
        tabla.setModel(modelo_tabla);
    }
    
    public boolean guardar_ingresos() {
        boolean resultado = true;

        for (HashMap<String, Object> ingreso : lista_ingresos) {
            String tipo = (String) ingreso.get("tipo");
            int id = (int) ingreso.get("id");
            int cantidad = (int) ingreso.get("cantidad");

            if (tipo.equalsIgnoreCase("material")) {
                if (!guardar_ingreso_material(id, cantidad)) {
                    resultado = false;
                }
            } else if (tipo.equalsIgnoreCase("producto")) {
                if (!guardar_ingreso_producto(id, cantidad)) {
                    resultado = false;
                }
            }
        }

        return resultado;
    }

    public boolean guardar_ingreso_material(int id_material, int cantidad) {
        String actualizarMaterial = "UPDATE material_almacenado SET stock = stock + ? WHERE id_material = ?";
        String registrarAlmacen = "INSERT INTO registro_almacen "
                                 + "(entrada_salida, variacion, material_producto, id_material_producto, id_detalle_pedido, fecha, hora) "
                                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement psMaterial = cx.prepareStatement(actualizarMaterial);
            psMaterial.setInt(1, cantidad);
            psMaterial.setInt(2, id_material);
            psMaterial.executeUpdate();
            psMaterial.close();

            PreparedStatement psRegistro = cx.prepareStatement(registrarAlmacen);
            psRegistro.setString(1, "entrada");
            psRegistro.setInt(2, cantidad);
            psRegistro.setString(3, "material");
            psRegistro.setInt(4, id_material);
            psRegistro.setInt(5, 0);
            psRegistro.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            psRegistro.setTime(7, new java.sql.Time(System.currentTimeMillis()));
            psRegistro.executeUpdate();
            psRegistro.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean guardar_ingreso_producto(int id_producto, int cantidad) {
        String actualizarProducto = "UPDATE producto_almacenado SET stock = stock + ? WHERE id_producto = ?";
        String registrarAlmacen = "INSERT INTO registro_almacen "
                                 + "(entrada_salida, variacion, material_producto, id_material_producto, id_detalle_pedido, fecha, hora) "
                                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement psProducto = cx.prepareStatement(actualizarProducto);
            psProducto.setInt(1, cantidad);
            psProducto.setInt(2, id_producto);
            psProducto.executeUpdate();
            psProducto.close();

            PreparedStatement psRegistro = cx.prepareStatement(registrarAlmacen);
            psRegistro.setString(1, "entrada");
            psRegistro.setInt(2, cantidad);
            psRegistro.setString(3, "producto");
            psRegistro.setInt(4, id_producto);
            psRegistro.setInt(5, 0);
            psRegistro.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            psRegistro.setTime(7, new java.sql.Time(System.currentTimeMillis()));
            psRegistro.executeUpdate();
            psRegistro.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
}
