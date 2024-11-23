package Controlador;

import Modelo.Materiales;
import Modelo.Productos;
import Vista.VistaMateriales;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorMateriales implements ActionListener{
    
    private VistaMateriales vistaMateriales;
    private Materiales materiales;
    
    public ControladorMateriales(VistaMateriales vistaMateriales, Materiales materiales){
        this.vistaMateriales = vistaMateriales;
        this.materiales = materiales;
        add_action_listeners();
    }
    
    public void iniciar_vista(){
        this.vistaMateriales.setVisible(true);
        this.vistaMateriales.setLocationRelativeTo(null);
    }
    
    public final void add_action_listeners(){
        this.vistaMateriales.get_anadir_button().addActionListener(this);
        this.vistaMateriales.get_editar_button().addActionListener(this);
        this.vistaMateriales.get_eliminar_button().addActionListener(this);
    }
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Boton presionado");
    }
}
