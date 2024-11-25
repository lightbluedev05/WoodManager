package Controlador;

import Modelo.Productos;
import Vista.VistaProductos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorProductos implements ActionListener{

    private VistaProductos vistaProductos;
    private Productos productos;
    
    public ControladorProductos(VistaProductos vistaProductos, Productos productos){
        this.vistaProductos = vistaProductos;
        this.productos = productos;
        
        productos.actualizar_tabla(vistaProductos.get_tabla_productos());
        productos.mostrar_datos_seleccion(
            vistaProductos.get_tabla_productos(),
            vistaProductos.get_medida_input(),
            vistaProductos.get_color_cera_input(),
            vistaProductos.get_tipo_pintado_input(),
            vistaProductos.get_stock_input()
        );
        
        add_action_listeners();
    }
    
    public void iniciar_vista(){
        this.vistaProductos.setVisible(true);
        this.vistaProductos.setLocationRelativeTo(null);
    }
    
    public void add_action_listeners(){
        vistaProductos.get_anadir_button().addActionListener(this);
        vistaProductos.get_buscar_button().addActionListener(this);
        vistaProductos.get_editar_button().addActionListener(this);
        vistaProductos.get_eliminar_button().addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vistaProductos.get_anadir_button()){
            
            String medida = vistaProductos.get_medida_input().getText();
            String color_cera = vistaProductos.get_color_cera_input().getText();
            String tipo_pintado = vistaProductos.get_tipo_pintado_input().getText();
            int stock = Integer.parseInt(vistaProductos.get_stock_input().getText());
            
            boolean exito = productos.anadir_producto(medida, color_cera, tipo_pintado, stock);
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo agregar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Producto agregado con exito",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            productos.actualizar_tabla(vistaProductos.get_tabla_productos());
            return;
        }
        
        if(e.getSource() == vistaProductos.get_buscar_button()){
            
            int id = Integer.parseInt(vistaProductos.get_buscar_input().getText());
            boolean exito = productos.mostrar_busqueda(id, vistaProductos.get_tabla_productos());
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo encontrar el producto", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Busqueda Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if(e.getSource() == vistaProductos.get_editar_button()){
            String medida = vistaProductos.get_medida_input().getText();
            String color_cera = vistaProductos.get_color_cera_input().getText();
            String tipo_pintado = vistaProductos.get_tipo_pintado_input().getText();
            int stock = Integer.parseInt(vistaProductos.get_stock_input().getText());
            
            boolean exito = productos.editar_producto(medida, color_cera, tipo_pintado, stock);
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo editar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Edicion Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            
            productos.actualizar_tabla(vistaProductos.get_tabla_productos());
            return;
        }
        
        if(e.getSource() == vistaProductos.get_eliminar_button()){
            boolean exito = productos.eliminar_producto();
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo eliminar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Eliminacion Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            
            productos.actualizar_tabla(vistaProductos.get_tabla_productos());
        }
    }
    
}
