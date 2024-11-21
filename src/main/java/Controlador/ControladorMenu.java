package Controlador;

import Vista.VistaMateriales;
import Vista.VistaMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorMenu implements ActionListener{
    
    private VistaMenu vista;
    
    public ControladorMenu(VistaMenu vista){
        this.vista = vista;
        
        add_action_listeners();
    }
    
    public static void main(String[] args) {
        VistaMenu vistaMenuObj = new VistaMenu();
        ControladorMenu controladorMenuObj = new ControladorMenu(vistaMenuObj);
        controladorMenuObj.iniciar_vista();
    }
    
    public void iniciar_vista(){
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);
    }
    
    public final void add_action_listeners(){
        this.vista.get_materiales_button().addActionListener(this);
        this.vista.get_ingreso_pedido_button().addActionListener(this);
        this.vista.get_productos_button().addActionListener(this);
        this.vista.get_proveedores_button().addActionListener(this);
    }
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == vista.get_materiales_button()){
            VistaMateriales vistaMaterialesObj = new VistaMateriales();
            ControladorMateriales controladorMaterialesObj = new ControladorMateriales(vistaMaterialesObj);
            controladorMaterialesObj.iniciar_vista();
            return;
        }
        if(e.getSource() == vista.get_ingreso_pedido_button()){
            System.out.println("Abrir Ingreso / Pedido");
            return;
        }
        if(e.getSource() == vista.get_productos_button()){
            System.out.println("Abrir Productos");
            return;
        }
        if(e.getSource() == vista.get_proveedores_button()){
            System.out.println("Abrir Proveedores");
        }
    }
}
