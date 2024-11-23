package Controlador;

import Modelo.Proveedores;
import Vista.VistaProveedores;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorProveedores implements ActionListener{
    
    private Proveedores proveedores;
    private VistaProveedores vistaProveedores;
    
    public ControladorProveedores(VistaProveedores vistaProveedores, Proveedores proveedores){
        this.vistaProveedores = vistaProveedores;
        this.proveedores = proveedores;
        
        add_action_listeners();
    }
    
    public void inicar_vista(){
        this.vistaProveedores.setVisible(true);
        this.vistaProveedores.setLocationRelativeTo(null);
    }
    
    public void add_action_listeners(){
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
