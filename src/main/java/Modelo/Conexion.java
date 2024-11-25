
package Modelo;
 

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;


public class Conexion {
    public String db="BDAlmacen";
    
    public String url="jdbc:mysql://localhost/" + db ;
    public String user="root";
    public String pass="";
    
    public static void main(String[] args) {
        Conexion cx = new Conexion();
        if(cx.Conectar() == null){
            System.out.println("Conexion nula");
        } else {
            System.out.println("conxion no nula");
        }
    }
    
    public Connection Conectar(){
       
        Connection link=null;
        
        try{
            Class.forName("org.gjt.mm.mysql.Driver");
            link = DriverManager.getConnection(url,user,pass);
            System.out.println("Se conecto con exito");
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,e);
            System.out.println("No se conecto");
        }
        
        return link;
    }
    
    
    
    
    
    
    
}
