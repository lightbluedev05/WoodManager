package Modelo;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import javax.naming.ldap.HasControls;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class IngresoPedido {
    
    public ArrayList<HashMap<String, Object>> lista_ingresos = new ArrayList<>();
    public ArrayList<HashMap<String, Object>> detalles_pedido = new ArrayList<>();
    
    private Connection cx = new Conexion().Conectar();
    
    private int ingreso_seleccionado = 0;
    private int detalle_seleccionado = 0;
    
    public boolean ingreso_anadir_material(int id_material, String nombre, int cantidad){
        
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
    
    public boolean ingreso_anadir_producto(int id_producto, String nombre, int cantidad){
        
        HashMap<String, Object> producto = new HashMap<>();
        producto.put("tipo", "producto");
        producto.put("id", id_producto);
        producto.put("nombre", nombre);
        producto.put("cantidad", cantidad);
        try{
            lista_ingresos.add(producto);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public void anadir_detalle_pedido(String tipo_pintado, String color_cera, String medida,
            int cantidad, ArrayList<HashMap<String, Object>> salidas){
        
        HashMap<String, Object> detalle = new HashMap<>();
        detalle.put("tipo_pintado",tipo_pintado);
        detalle.put("color_cera", color_cera);
        detalle.put("medida", medida);
        detalle.put("cantidad", cantidad);
        detalle.put("salidas", salidas);
        
        detalles_pedido.add(detalle);
    }
    
    public boolean salida_anadir_material(ArrayList<HashMap<String, Object>> salidas, int id_material, String nombre, int cantidad, int stock){
        int aux_cantidad = 0;
        for(HashMap<String, Object> salida:salidas){
            if(salida.get("tipo").equals("material") && ((int) salida.get("id_material")) == id_material){
                aux_cantidad += (int) salida.get("cantidad");
                if(aux_cantidad + cantidad > stock){
                    return false;
                }
            }
        }
        for(HashMap<String, Object> detalle:detalles_pedido){
            ArrayList<HashMap<String, Object>> salidas_detalle;
            salidas_detalle = (ArrayList<HashMap<String, Object>>) detalle.get("salidas");
            for(HashMap<String, Object> salida:salidas_detalle){
                if(salida.get("tipo").equals("material") && ((int) salida.get("id_material")) == id_material){
                    aux_cantidad += (int) salida.get("cantidad");
                    if(aux_cantidad + cantidad > stock){
                        return false;
                    }
                }
            }
        }
        
        HashMap<String, Object> material = new HashMap<>();
        material.put("tipo", "material");
        material.put("id", id_material);
        material.put("nombre", nombre);
        material.put("cantidad", cantidad);
        try{
            salidas.add(material);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public boolean salida_anadir_producto(ArrayList<HashMap<String, Object>> salidas, int id_producto, String nombre, int cantidad, int stock){
        
        int aux_cantidad = 0;
        for(HashMap<String, Object> salida:salidas){
            if(salida.get("tipo").equals("producto") && ((int) salida.get("id_producto")) == id_producto){
                aux_cantidad += (int) salida.get("cantidad");
                if(aux_cantidad + cantidad > stock){
                    return false;
                }
            }
        }
        for(HashMap<String, Object> detalle:detalles_pedido){
            ArrayList<HashMap<String, Object>> salidas_detalle;
            salidas_detalle = (ArrayList<HashMap<String, Object>>) detalle.get("salidas");
            for(HashMap<String, Object> salida:salidas_detalle){
                if(salida.get("tipo").equals("producto") && ((int) salida.get("id_producto")) == id_producto){
                    aux_cantidad += (int) salida.get("cantidad");
                    if(aux_cantidad + cantidad > stock){
                        return false;
                    }
                }
            }
        }
        
        HashMap<String, Object> producto = new HashMap<>();
        producto.put("tipo", "producto");
        producto.put("id", id_producto);
        producto.put("nombre", nombre);
        producto.put("cantidad", cantidad);
        try{
            salidas.add(producto);
            return true;
        } catch (Exception e){
            return false;
        }
    }
    
    public boolean salida_anadir_detalle_pedido(String tipo_pintado, String color_cera, String medida,
            int cantidad, ArrayList<HashMap<String, Object>> salidas){
        
        HashMap<String, Object> detalle_pedido = new HashMap<>();
        detalle_pedido.put("tipo_pintado", tipo_pintado);
        detalle_pedido.put("color_cera", color_cera);
        detalle_pedido.put("medida",medida);
        detalle_pedido.put("cantidad", cantidad);
        detalle_pedido.put("salidas", salidas);
        
        try{
            detalles_pedido.add(detalle_pedido);
            return true;
        } catch (Exception e){
            return false;
        }
        
    }
    
    public boolean guardar_pedido(String descripcion) {
        String queryPedido = "INSERT INTO pedido (descripcion) VALUES (?)";
        
        System.out.println(detalles_pedido);

        try {
            PreparedStatement psPedido = cx.prepareStatement(queryPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setString(1, descripcion);
            psPedido.executeUpdate();

            // Obtener el ID generado para el pedido
            ResultSet rs = psPedido.getGeneratedKeys();
            if (rs.next()) {
                int idPedido = rs.getInt(1);
                rs.close();
                psPedido.close();

                for(HashMap<String, Object> detalle:detalles_pedido){
                    System.out.println("detalle");
                    guardar_detalle_pedido(detalle, idPedido);
                }
            }

            rs.close();
            psPedido.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean guardar_detalle_pedido(HashMap<String, Object> detalle_pedido, int id_pedido){
        String queryDetallePedido = "INSERT INTO detalle_pedido "
                + "(tipo_pintado, medida, color_cera, id_pedido, cantidad) "
                + "VALUES (?, ?, ?, ?, ?)";
        
        String tipoPintado = (String) detalle_pedido.get("tipo_pintado");
        System.out.println(tipoPintado);
        String medida = (String) detalle_pedido.get("medida");
        String colorCera = (String) detalle_pedido.get("color_cera");
        int cantidad = (int) detalle_pedido.get("cantidad");
        
        try {
            PreparedStatement psDetalle = cx.prepareStatement(queryDetallePedido, Statement.RETURN_GENERATED_KEYS);
            psDetalle.setString(1, tipoPintado);
            psDetalle.setString(2, medida);
            psDetalle.setString(3, colorCera);
            psDetalle.setInt(4, id_pedido);
            psDetalle.setInt(5, cantidad);
            psDetalle.executeUpdate();

            // Obtener el ID generado para el detalle del pedido
            ResultSet rs = psDetalle.getGeneratedKeys();
            if (rs.next()) {
                int idDetallePedido = rs.getInt(1);
                rs.close();
                psDetalle.close();
                
                guardar_salidas((ArrayList<HashMap<String, Object>>) detalle_pedido.get("salidas"), idDetallePedido);

                // Retornar verdadero indicando que el detalle se guardó correctamente
                return true;
            }

            rs.close();
            psDetalle.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println("Se guardo un detalle");
        return false;
    }
    
    public boolean guardar_salidas(ArrayList<HashMap<String, Object>> lista_salidas, int id_detalle_pedido) {
        boolean resultado = true;

        if (lista_salidas.isEmpty() || lista_salidas == null) {
            return false;
        }

        for (HashMap<String, Object> salida : lista_salidas) {
            String tipo = (String) salida.get("tipo");
            int id = (int) salida.get("id");
            int cantidad = (int) salida.get("cantidad");

            if (tipo.equalsIgnoreCase("material")) {
                if (!guardar_salida_material(id, cantidad, id_detalle_pedido)) {
                    resultado = false;
                }
            } else if (tipo.equalsIgnoreCase("producto")) {
                if (!guardar_salida_producto(id, cantidad, id_detalle_pedido)) {
                    resultado = false;
                }
            }
        }

        return resultado;
    }

    public boolean guardar_salida_material(int id_material, int cantidad, int id_detalle_pedido) {
        String actualizarMaterial = "UPDATE material_almacenado SET stock = stock - ? WHERE id_material = ? AND stock >= ?";
        String registrarAlmacen = "INSERT INTO registro_almacen "
                                 + "(entrada_salida, variacion, material_producto, id_material_producto, id_detalle_pedido, fecha, hora) "
                                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement psMaterial = cx.prepareStatement(actualizarMaterial);
            psMaterial.setInt(1, cantidad);
            psMaterial.setInt(2, id_material);
            psMaterial.setInt(3, cantidad); // Validar que hay suficiente stock
            int filasActualizadas = psMaterial.executeUpdate();
            psMaterial.close();

            if (filasActualizadas == 0) {
                return false; // No hay suficiente stock para realizar la salida
            }

            PreparedStatement psRegistro = cx.prepareStatement(registrarAlmacen);
            psRegistro.setString(1, "salida");
            psRegistro.setInt(2, cantidad); // Se guarda la salida como negativa
            psRegistro.setString(3, "material");
            psRegistro.setInt(4, id_material);
            psRegistro.setInt(5, id_detalle_pedido);
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

    public boolean guardar_salida_producto(int id_producto, int cantidad, int id_detalle_pedido) {
        String actualizarProducto = "UPDATE producto_almacenado SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";
        String registrarAlmacen = "INSERT INTO registro_almacen "
                                 + "(entrada_salida, variacion, material_producto, id_material_producto, id_detalle_pedido, fecha, hora) "
                                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement psProducto = cx.prepareStatement(actualizarProducto);
            psProducto.setInt(1, cantidad);
            psProducto.setInt(2, id_producto);
            psProducto.setInt(3, cantidad); // Validar que hay suficiente stock
            int filasActualizadas = psProducto.executeUpdate();
            psProducto.close();

            if (filasActualizadas == 0) {
                return false; // No hay suficiente stock para realizar la salida
            }

            PreparedStatement psRegistro = cx.prepareStatement(registrarAlmacen);
            psRegistro.setString(1, "salida");
            psRegistro.setInt(2, cantidad); // Se guarda la salida como negativa
            psRegistro.setString(3, "producto");
            psRegistro.setInt(4, id_producto);
            psRegistro.setInt(5, id_detalle_pedido);
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
    
    public void eliminar_detalle(){
        detalles_pedido.remove(detalle_seleccionado);
    }
    
    
    
    
    
    public void eliminar_ingreso(){
        lista_ingresos.remove(ingreso_seleccionado);
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
    
    public void actualizar_tabla_detalle_pedido(JTable tabla){
        
        Object[][] filas = new Object[detalles_pedido.size()][4];
        
        int i=0;
        for (HashMap<String, Object> ingreso : detalles_pedido) {
            filas[i][0] = ingreso.get("tipo_pintado");
            filas[i][1] = ingreso.get("color_cera");
            filas[i][2] = ingreso.get("medida");
            filas[i][3] = ingreso.get("cantidad");
            i++;
        }
        
        DefaultTableModel modelo_tabla = new DefaultTableModel(
            filas,
            new Object[]{"Pintado", "Color Cera", "Medida", "Cantidad"}
        );
        
        tabla.setModel(modelo_tabla);
    }
    
    public void actualizar_tabla_salidas_pedido(JTable tabla, ArrayList<HashMap<String, Object>> salidas){
        
        Object[][] filas = new Object[salidas.size()][4];
        
        int i=0;
        for (HashMap<String, Object> ingreso : salidas) {
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
        
        if(lista_ingresos.isEmpty() || lista_ingresos == null){
            return false;
        }
        
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
    
    
    public void mostrar_datos_seleccion(JTable tabla){
        
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int fila_seleccionada = tabla.getSelectedRow();
                if(fila_seleccionada != -1){
                    ingreso_seleccionado = fila_seleccionada;
                }
            }
        });
    }
    
    public void seleccionar_detalle(JTable tabla){
        
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int fila_seleccionada = tabla.getSelectedRow();
                if(fila_seleccionada != -1){
                    detalle_seleccionado = fila_seleccionada;
                }
            }
        });
    }
    
}
