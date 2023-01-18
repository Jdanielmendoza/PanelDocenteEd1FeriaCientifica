/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import com.mysql.cj.xdevapi.PreparableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jdani
 */
public class Conexion {
    String BD = "railway"; 
    String Url = "jdbc:mysql://containers-us-west-190.railway.app:6104/"; 
    String user = "root";
    String password= "cKXiUDpADUdZFbakF0gi";
    String driver ="com.mysql.cj.jdbc.Driver"; 
    Connection cx; 
    
    
    public Conexion(){
        
    }
    public Connection conectar(){
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(Url+BD, user,password);
            System.out.println("Conexion Exitosa!");
        }catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error de Conexion!");
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx; 
    }
    
    public void desconectar(){
        try {
            cx.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        Conexion conn = new Conexion(); 
        Statement ps = null;   // para poder generar consultas sql
        ResultSet res=null;    // almacena los registros de la consulta
   
        try {
            ps = conn.conectar().createStatement(); 
            res = ps.executeQuery("SELECT * FROM prueba ");
            System.out.println(" registros");
            while(res.next()){
                System.out.println(res.getInt("id") + " "+ res.getString("nombre"));
            }
            conn.desconectar();
        } catch (Exception e) {
        }
        
    }
}
