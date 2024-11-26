package Controlador;

import Modelo.Materiales;
import Modelo.Productos;
import Vista.SeleccionProveedor;
import Vista.VistaMateriales;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorMateriales implements ActionListener{
    
    private VistaMateriales vistaMateriales;
    private Materiales materiales;
    private SeleccionProveedor sc;
    
    public ControladorMateriales(VistaMateriales vistaMateriales, Materiales materiales){
        this.vistaMateriales = vistaMateriales;
        this.materiales = materiales;
        sc = new SeleccionProveedor();
        
        materiales.actualizar_tabla(vistaMateriales.get_tabla_materiales());
        materiales.mostrar_datos_seleccion(
                vistaMateriales.get_tabla_materiales(),
                vistaMateriales.get_nombre_input(),
                vistaMateriales.get_proveedor_input(),
                vistaMateriales.get_unidad_medida_input(),
                vistaMateriales.get_stock_input()
        );
        
        add_action_listeners();
    }
    
    public void iniciar_vista(){
        this.vistaMateriales.setVisible(true);
        this.vistaMateriales.setLocationRelativeTo(null);
    }
    
    public final void add_action_listeners(){
        vistaMateriales.get_anadir_button().addActionListener(this);
        vistaMateriales.get_buscar_button().addActionListener(this);
        vistaMateriales.get_editar_button().addActionListener(this);
        vistaMateriales.get_eliminar_button().addActionListener(this);
        vistaMateriales.get_buscar_proveedor_button().addActionListener(this);
        sc.get_seleccionar_button().addActionListener(this);
        sc.get_cancelar_button().addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vistaMateriales.get_anadir_button()){
            try{   
                String nombre = vistaMateriales.get_nombre_input().getText();
                int id_proveedor = Integer.parseInt(vistaMateriales.get_proveedor_input().getText());
                String unidad_medida = vistaMateriales.get_unidad_medida_input().getText();
                int stock = Integer.parseInt(vistaMateriales.get_stock_input().getText());

                boolean exito = materiales.anadir_material(nombre, id_proveedor, unidad_medida, stock);

                if(!exito){
                    JOptionPane.showMessageDialog(null, "No se pudo agregar", 
                        "Error",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(null, "Material agregado con exito",
                        "Exito", JOptionPane.INFORMATION_MESSAGE);
                materiales.actualizar_tabla(vistaMateriales.get_tabla_materiales());
                return;
            } catch (Exception ex){
                JOptionPane.showMessageDialog(null, "No se puede agregar", 
                "Error",JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if(e.getSource() == vistaMateriales.get_buscar_button()){
            
            int id = Integer.parseInt(vistaMateriales.get_buscar_input().getText());
            boolean exito = materiales.mostrar_busqueda(id, vistaMateriales.get_tabla_materiales());
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo encontrar el material", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Busqueda Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if(e.getSource() == vistaMateriales.get_editar_button()){
            
            String nombre = vistaMateriales.get_nombre_input().getText();
            int id_proveedor = Integer.parseInt(vistaMateriales.get_proveedor_input().getText());
            String unidad_medida = vistaMateriales.get_unidad_medida_input().getText();
            int stock = Integer.parseInt(vistaMateriales.get_stock_input().getText());
            
            boolean exito = materiales.editar_material(nombre, id_proveedor, unidad_medida, stock);
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo editar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Edicion Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            
            materiales.actualizar_tabla(vistaMateriales.get_tabla_materiales());
            return;
        }
        
        if(e.getSource() == vistaMateriales.get_eliminar_button()){
            boolean exito = materiales.eliminar_material();
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo eliminar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Eliminacion Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            
            materiales.actualizar_tabla(vistaMateriales.get_tabla_materiales());
            return;
        }
        
        if(e.getSource() == vistaMateriales.get_buscar_proveedor_button()){
            sc.setVisible(true);
            sc.setLocationRelativeTo(null);
            return;
        }
        
        if(e.getSource() == sc.get_cancelar_button()){
            vistaMateriales.get_proveedor_input().setText("");
            sc.setVisible(false);
            return;
        }
        
        if(e.getSource() == sc.get_seleccionar_button()){
            vistaMateriales.get_proveedor_input().setText(""+sc.get_proveedor_seleccionado());
            sc.setVisible(false);
        }
    }
}
