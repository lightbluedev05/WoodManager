package Controlador;

import Modelo.Productos;
import Vista.VistaProductos;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorProductos implements ActionListener{

    private VistaProductos vistaProductos;
    private Productos productos;
    
    public ControladorProductos(VistaProductos vistaProductos, Productos productos){
        this.vistaProductos = vistaProductos;
        this.productos = productos;
        
        add_action_listeners();
    }
    
    public void iniciar_vista(){
        this.vistaProductos.setVisible(true);
        this.vistaProductos.setLocationRelativeTo(null);
    }
    
    public void add_action_listeners(){
        
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
