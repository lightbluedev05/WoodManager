package Controlador;

import Modelo.IngresoPedido;
import Modelo.Materiales;
import Modelo.Productos;
import Modelo.Proveedores;
import Vista.VistaIngresoPedido;
import Vista.VistaMateriales;
import Vista.VistaMenu;
import Vista.VistaProductos;
import Vista.VistaProveedores;
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
            Materiales materialesObj = new Materiales();
            ControladorMateriales controladorMaterialesObj = new ControladorMateriales(vistaMaterialesObj, materialesObj);
            controladorMaterialesObj.iniciar_vista();
            return;
        }
        if(e.getSource() == vista.get_ingreso_pedido_button()){
            VistaIngresoPedido vistaIngresoPedidoObj = new VistaIngresoPedido();
            IngresoPedido ingresoPedidoObj = new IngresoPedido();
            ControladorIngresoPedido controladorIngresoPedidoObj = new ControladorIngresoPedido(vistaIngresoPedidoObj, ingresoPedidoObj);
            controladorIngresoPedidoObj.iniciar_vista();
            return;
        }
        if(e.getSource() == vista.get_productos_button()){
            VistaProductos vistaProductosObj = new VistaProductos();
            Productos productosObj = new Productos();
            ControladorProductos controladorProductosObj = new ControladorProductos(vistaProductosObj, productosObj);
            controladorProductosObj.iniciar_vista();
            return;
        }
        if(e.getSource() == vista.get_proveedores_button()){
            VistaProveedores vistaProveedoresObj = new VistaProveedores();
            Proveedores proveedoresObj = new Proveedores();
            ControladorProveedores controladorProveedoresobj = new ControladorProveedores(vistaProveedoresObj, proveedoresObj);
            controladorProveedoresobj.inicar_vista();
        }
    }
}
