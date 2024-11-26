package Controlador;

import Modelo.IngresoPedido;
import Vista.IngresoInventario;
import Vista.MaterialesProductosPedido;
import Vista.PedidoInventario;
import Vista.VistaIngresoPedido;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorIngresoPedido implements ActionListener{

    private VistaIngresoPedido vistaIngresoPedido;
    private IngresoPedido ingresoPedido;
    
    private IngresoInventario iv;
    private PedidoInventario pv;
    
    private IngresoInventario mpp;
    private MaterialesProductosPedido mp;
    
    private int ingreso_seleccionado;
    private ArrayList<HashMap<String, Object>> salidas = new ArrayList<>();
    
    public ControladorIngresoPedido(VistaIngresoPedido vistaIngresoPedido, IngresoPedido ingresoPedido){
        this.vistaIngresoPedido = vistaIngresoPedido;
        this.ingresoPedido = ingresoPedido;
        
        this.iv = vistaIngresoPedido.ingresoInventario;
        this.pv = vistaIngresoPedido.pedidoInventario;
        
        this.mp = vistaIngresoPedido.pedidoInventario.elegirMaterialesProductosObj;
        this.mpp = mp.ingreso_inventario;
        
        
        ingresoPedido.mostrar_datos_seleccion(iv.get_tabla_ingresos());
        ingresoPedido.seleccionar_detalle(pv.get_tabla_pedido());
        
        add_action_listeners();
    }
    
    public void iniciar_vista(){
        this.vistaIngresoPedido.setVisible(true);
        this.vistaIngresoPedido.setLocationRelativeTo(null);
    }
    
    public void add_action_listeners(){
        //--------------------- INGRESOS -------------------------------
        
        //GENERAL
        iv.get_guardar_button().addActionListener(this);
        iv.get_eliminar_button().addActionListener(this);
        iv.get_salir_button().addActionListener(this);
        
        //MATERIAL
        iv.get_material_anadir_button().addActionListener(this);
        iv.get_material_buscar_button().addActionListener(this);
        iv.sm.get_seleccionar_button().addActionListener(this);
        iv.sm.get_cancelar_button().addActionListener(this);
        
        //PRODUCTO
        iv.get_producto_anadir_button().addActionListener(this);
        iv.get_producto_buscar_button().addActionListener(this);
        iv.sp.get_seleccionar_button().addActionListener(this);
        iv.sp.get_cancelar_button().addActionListener(this);
        
        //------------------- PEDIDOS -------------------------------
        
        //GENERAL
        pv.get_anadir_button().addActionListener(this);
        pv.get_guardar_button().addActionListener(this);
        pv.get_eliminar_button().addActionListener(this);
        pv.get_salir_button().addActionListener(this);
        
        //GENERAL
        mpp.get_guardar_button().addActionListener(this);
        mpp.get_eliminar_button().addActionListener(this);
        mpp.get_salir_button().addActionListener(this);
        
        //MATERIAL
        mpp.get_material_anadir_button().addActionListener(this);
        mpp.get_material_buscar_button().addActionListener(this);
        mpp.sm.get_seleccionar_button().addActionListener(this);
        mpp.sm.get_cancelar_button().addActionListener(this);
        
        //PRODUCTO
        mpp.get_producto_anadir_button().addActionListener(this);
        mpp.get_producto_buscar_button().addActionListener(this);
        mpp.sp.get_seleccionar_button().addActionListener(this);
        mpp.sp.get_cancelar_button().addActionListener(this);
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //-------------------------- GENERAL ------------------------------------
        
        if(e.getSource() == iv.get_guardar_button()){
            boolean exito = ingresoPedido.guardar_ingresos();
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo guardar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Ingresos registrados con exito",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (e.getSource() == iv.get_eliminar_button()) {
            ingresoPedido.eliminar_ingreso();
            return;
        }

        if (e.getSource() == iv.get_salir_button()) {
            vistaIngresoPedido.dispose();
            return;
        }

        //-------------------------------- INGRESO MATERIAL ---------------------------
        
        if (e.getSource() == iv.get_material_anadir_button()) {
            int id_material = Integer.parseInt(iv.get_material_codigo_input().getText());
            String nombre = iv.get_material_nombre_input().getText();
            int cantidad = Integer.parseInt(iv.get_material_cantidad_input().getText());
            
            ingresoPedido.ingreso_anadir_material(id_material, nombre, cantidad);
            ingresoPedido.actualizar_lista_ingreso(iv.get_tabla_ingresos());
            return;
        }

        if (e.getSource() == iv.get_material_buscar_button()) {
            iv.sm.setVisible(true);
            iv.sm.setLocationRelativeTo(null);
            return;
        }
        
        if(e.getSource() == iv.sm.get_seleccionar_button()){
            HashMap<String, Object> material = iv.sm.get_material_seleccionad();
            iv.get_material_nombre_input().setText((String) material.get("nombre_material"));
            iv.get_material_stock_input().setText(Integer.toString((int) material.get("stock")));
            iv.get_material_unidad_input().setText((String) material.get("unidad_medida"));
            iv.get_material_codigo_input().setText(Integer.toString((int) material.get("id_material")));
            
            iv.sm.setVisible(false);
            return;
        }
        
        if(e.getSource() == iv.sm.get_cancelar_button()){
            iv.sm.setVisible(false);
            return;
        }
        
        //------------------------------------ INGRESO PRODUCTO --------------------------
        
        if (e.getSource() == iv.get_producto_anadir_button()) {
            int id_producto = Integer.parseInt(iv.get_producto_codigo_input().getText());
            String nombre = iv.get_producto_medida_input().getText();
            int cantidad = Integer.parseInt(iv.get_producto_cantidad_input().getText());
            
            ingresoPedido.ingreso_anadir_producto(id_producto, nombre, cantidad);
            ingresoPedido.actualizar_lista_ingreso(iv.get_tabla_ingresos());
            return;
        }

        if (e.getSource() == iv.get_producto_buscar_button()) {
            iv.sp.setVisible(true);
            iv.sp.setLocationRelativeTo(null);
            return;
        }
        
        if(e.getSource() == iv.sp.get_seleccionar_button()){
            HashMap<String, Object> producto = iv.sp.get_producto_seleccionado();
            iv.get_producto_medida_input().setText((String) producto.get("medida"));
            iv.get_producto_stock_input().setText(Integer.toString((int) producto.get("stock")));
            iv.get_producto_tipo_pintado_input().setText((String) producto.get("tipo_pintado"));
            iv.get_producto_codigo_input().setText(Integer.toString((int) producto.get("id_producto")));
            
            iv.sp.setVisible(false);
            return;
        }
        
        if(e.getSource() == iv.sp.get_cancelar_button()){
            iv.sp.setVisible(false);
            return;
        }
        
        
        
        
        //------------------------- GENERAL ---------------------------
        if(e.getSource() == pv.get_anadir_button()){
            salidas = new ArrayList<>();
            mpp.get_tabla_ingresos().getSelectionModel().addListSelectionListener(a -> {
                if(!a.getValueIsAdjusting()){
                    int fila_seleccionada = mpp.get_tabla_ingresos().getSelectedRow();
                    if(fila_seleccionada != -1){
                        ingreso_seleccionado = fila_seleccionada;
                    }
                }
            });
            mp.setVisible(true);
            mp.setLocationRelativeTo(null);
            return;
        }
        
        if(e.getSource() == pv.get_guardar_button()){
            String descripcion = pv.get_descripcion_input().getText();
            ingresoPedido.guardar_pedido(descripcion);
            JOptionPane.showMessageDialog(null, "Guardado exitoso", "Exito", JOptionPane.INFORMATION_MESSAGE);
            vistaIngresoPedido.dispose();
            return;
        }
        
        if(e.getSource() == pv.get_eliminar_button()){
            ingresoPedido.eliminar_detalle();
            ingresoPedido.actualizar_tabla_detalle_pedido(pv.get_tabla_pedido());
            return;
        }
        
        if(e.getSource() == pv.get_salir_button()){
            vistaIngresoPedido.dispose();
            return;
        }
        
        if (e.getSource() == mpp.get_guardar_button()) {
            String tipo_pintado = pv.get_tipo_pintado_input().getText();
            String color_cera = pv.get_color_cera_input().getText();
            String medida = pv.get_medida_input().getText();
            int cantidad = Integer.parseInt(pv.get_cantidad_input().getText());
            
            ingresoPedido.anadir_detalle_pedido(tipo_pintado, color_cera, medida, cantidad, salidas);
            mp.setVisible(false);
            ingresoPedido.actualizar_tabla_detalle_pedido(pv.get_tabla_pedido());
            return;
        }

        if (e.getSource() == mpp.get_eliminar_button()) {
            if (ingreso_seleccionado >= 0 && ingreso_seleccionado < mpp.get_tabla_ingresos().getRowCount()) { 
                DefaultTableModel modelo = (DefaultTableModel) mpp.get_tabla_ingresos().getModel();
                modelo.removeRow(ingreso_seleccionado); 
            } else {
                JOptionPane.showMessageDialog(null, "No se pueded eliminar", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        if (e.getSource() == mpp.get_salir_button()) {
            mp.setVisible(false);
            return;
        }

        if (e.getSource() == mpp.get_material_anadir_button()) {
            int id_material = Integer.parseInt(mpp.get_material_codigo_input().getText());
            String nombre = mpp.get_material_nombre_input().getText();
            int cantidad = Integer.parseInt(mpp.get_material_cantidad_input().getText());
            
            HashMap<String, Object> material = new HashMap<>();
            material.put("tipo", "material");
            material.put("id", id_material);
            material.put("nombre", nombre);
            material.put("cantidad", cantidad);
            
            salidas.add(material);
            ingresoPedido.actualizar_tabla_salidas_pedido(mpp.get_tabla_ingresos(), salidas);
            return;
        }

        if (e.getSource() == mpp.get_material_buscar_button()) {
            mpp.sm.setVisible(true);
            return;
        }

        if (e.getSource() == mpp.sm.get_seleccionar_button()) {
            HashMap<String, Object> material = mpp.sm.get_material_seleccionad();
            mpp.get_material_nombre_input().setText((String) material.get("nombre_material"));
            mpp.get_material_stock_input().setText(Integer.toString((int) material.get("stock")));
            mpp.get_material_unidad_input().setText((String) material.get("unidad_medida"));
            mpp.get_material_codigo_input().setText(Integer.toString((int) material.get("id_material")));
            
            mpp.sm.setVisible(false);
            return;
        }

        if (e.getSource() == mpp.sm.get_cancelar_button()) {
            mpp.sm.setVisible(false);
            return;
        }

        if (e.getSource() == mpp.get_producto_anadir_button()) {
            int id_producto = Integer.parseInt(mpp.get_producto_codigo_input().getText());
            String nombre = mpp.get_producto_medida_input().getText();
            int cantidad = Integer.parseInt(mpp.get_producto_cantidad_input().getText());
            
            HashMap<String, Object> producto = new HashMap<>();
            producto.put("tipo", "producto");
            producto.put("id", id_producto);
            producto.put("nombre", nombre);
            producto.put("cantidad", cantidad);
            
            salidas.add(producto);
            ingresoPedido.actualizar_tabla_salidas_pedido(mpp.get_tabla_ingresos(), salidas);
            return;
        }

        if (e.getSource() == mpp.get_producto_buscar_button()) {
            mpp.sp.setVisible(true);
            return;
        }

        if (e.getSource() == mpp.sp.get_seleccionar_button()) {
            HashMap<String, Object> producto = mpp.sp.get_producto_seleccionado();
            mpp.get_producto_medida_input().setText((String) producto.get("medida"));
            mpp.get_producto_stock_input().setText(Integer.toString((int) producto.get("stock")));
            mpp.get_producto_tipo_pintado_input().setText((String) producto.get("tipo_pintado"));
            mpp.get_producto_codigo_input().setText(Integer.toString((int) producto.get("id_producto")));
            
            mpp.sp.setVisible(false);
            return;
        }

        if (e.getSource() == mpp.sp.get_cancelar_button()) {
            mpp.sp.setVisible(false);
            return;
        }
    }
    
}
