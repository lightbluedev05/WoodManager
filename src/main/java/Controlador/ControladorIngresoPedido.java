package Controlador;

import Modelo.IngresoPedido;
import Vista.VistaIngresoPedido;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorIngresoPedido implements ActionListener{

    private VistaIngresoPedido vistaIngresoPedido = new VistaIngresoPedido();
    private IngresoPedido ingresoPedido = new IngresoPedido();
    
    public ControladorIngresoPedido(VistaIngresoPedido vistaIngresoPedido, IngresoPedido ingresoPedido){
        this.vistaIngresoPedido = vistaIngresoPedido;
        this.ingresoPedido = ingresoPedido;
        
        add_action_listeners();
    }
    
    public void iniciar_vista(){
        this.vistaIngresoPedido.setVisible(true);
        this.vistaIngresoPedido.setLocationRelativeTo(null);
    }
    
    public void add_action_listeners(){
        
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
