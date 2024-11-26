import Modelo.IngresoPedido;
import Modelo.Materiales;
import Modelo.Proveedores;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MaterialesTest {
    
    @Test
    public void ingresar_material_negativo(){
        Materiales materialesObj = new Materiales();
        int stock_negativo = -12;
        boolean exito = materialesObj.anadir_material("nombre_material", 2, "unidad_medida", stock_negativo);
        assertEquals(false, exito);
    }
    
    @Test
    public void ingresar_telefono_proveedor_letras(){
        Proveedores proveedoresObj = new Proveedores();
        String telefono_letras = "aaaaaaaaa";
        boolean exito = proveedoresObj.anadir_proveedor("nombre_proveedor", telefono_letras, "descripcion");
        assertEquals(false, exito);
    }
    
    @Test
    public void ingresar_proveedor_no_existente(){
        Materiales materialesObj = new Materiales();
        //Proveedor no existente
        int proveedor = 50;
        boolean exito = materialesObj.anadir_material("nombre_material", proveedor, "unidad_medida", 5);
        assertEquals(false, exito);
    }
    
    @Test
    public void editar_stock_material(){
        IngresoPedido ip = new IngresoPedido();
        int cantidad_negativa = -10;
        boolean exito = ip.ingreso_anadir_material(3, "nombre", cantidad_negativa);
        assertEquals(false, exito);
    }
    
    @Test
    public void editar_stock_producto(){
        IngresoPedido ip = new IngresoPedido();
        int cantidad_negativa = -10;
        boolean exito = ip.ingreso_anadir_producto(3, "nombre", cantidad_negativa);
        assertEquals(false, exito);
    }
    
}
