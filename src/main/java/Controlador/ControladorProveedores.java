package Controlador;

import Modelo.Proveedores;
import Vista.VistaProveedores;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorProveedores implements ActionListener{
    
    private Proveedores proveedores;
    private VistaProveedores vistaProveedores;
    
    public ControladorProveedores(VistaProveedores vistaProveedores, Proveedores proveedores){
        this.vistaProveedores = vistaProveedores;
        this.proveedores = proveedores;
        
        proveedores.actualizar_tabla(vistaProveedores.get_tabla_proveedores());
        proveedores.mostrar_datos_seleccion(
                vistaProveedores.get_tabla_proveedores(),
                vistaProveedores.get_nombre_input(),
                vistaProveedores.get_telefono_input(),
                vistaProveedores.get_descripcion_input()
        );
        
        add_action_listeners();
    }
    
    public void inicar_vista(){
        this.vistaProveedores.setVisible(true);
        this.vistaProveedores.setLocationRelativeTo(null);
    }
    
    public void add_action_listeners(){
        vistaProveedores.get_anadir_button().addActionListener(this);
        vistaProveedores.get_buscar_button().addActionListener(this);
        vistaProveedores.get_editar_button().addActionListener(this);
        vistaProveedores.get_eliminar_button().addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vistaProveedores.get_anadir_button()){
            String nombre = vistaProveedores.get_nombre_input().getText();
            String telefono = vistaProveedores.get_telefono_input().getText();
            String descripcion = vistaProveedores.get_descripcion_input().getText();
            
            boolean exito = proveedores.anadir_proveedor(nombre, telefono, descripcion);
            if(!exito){
                JOptionPane.showMessageDialog(null, "Ocurrió un error inesperado.", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Proveedor agregado con exito",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            proveedores.actualizar_tabla(vistaProveedores.get_tabla_proveedores());
            return;
        }
        
        if(e.getSource() == vistaProveedores.get_buscar_button()){
            int id = Integer.parseInt(vistaProveedores.get_buscar_input().getText());
            boolean exito = proveedores.mostrar_busqueda(id, vistaProveedores.get_tabla_proveedores());
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo encontrar al proveedor", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Busqueda Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if(e.getSource() == vistaProveedores.get_editar_button()){
            String nombre = vistaProveedores.get_nombre_input().getText();
            String telefono = vistaProveedores.get_telefono_input().getText();
            String descripcion = vistaProveedores.get_descripcion_input().getText();
            
            boolean exito = proveedores.editar_proveedor(nombre, telefono, descripcion);
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo editar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Edicion Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            
            proveedores.actualizar_tabla(vistaProveedores.get_tabla_proveedores());
            return;
        }
        
        if(e.getSource() == vistaProveedores.get_eliminar_button()){
            boolean exito = proveedores.eliminar_proveedor();
            
            if(!exito){
                JOptionPane.showMessageDialog(null, "No se pudo eliminar", 
                    "Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(null, "Eliminacion Exitosa",
                    "Exito", JOptionPane.INFORMATION_MESSAGE);
            
            proveedores.actualizar_tabla(vistaProveedores.get_tabla_proveedores());
        }
    }
    
}
