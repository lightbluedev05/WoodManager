package Controlador;

import Vista.VistaMateriales;

public class ControladorMateriales {
    
    private VistaMateriales vistaMateriales;
    
    public ControladorMateriales(VistaMateriales vistaMateriales){
        this.vistaMateriales = vistaMateriales;
    }
    
    public void iniciar_vista(){
        this.vistaMateriales.setVisible(true);
        this.vistaMateriales.setLocationRelativeTo(null);
    }
}
