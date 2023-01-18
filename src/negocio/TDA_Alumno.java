/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.Serializable;
import java.util.Vector;
import javax.swing.JOptionPane;

public class TDA_Alumno implements Comparable<TDA_Alumno>, Serializable{
     String nombrecompleto;
     private int registro;
     private long carnet;
     private String nombreMaterias;
     private int notas;
     private String gestion; 
     private int nota1; 
     private int nota2; 
     private int notaExFin; 
     private int notaPractico; 
     private int notaRepechaje; 
     private int notaPromedio; 
     

    public TDA_Alumno(int registro, long carnet){
        this.registro = registro;
        this.carnet = carnet;
    }
    
    public TDA_Alumno(String nombreMateria){
        this.nombreMaterias=nombreMateria;
    }

    public void setNombrecompleto(String nombrecompleto) {
        this.nombrecompleto = nombrecompleto;
    }

    public void setRegistro(int registro) {
        this.registro = registro;
    }

    public void setNombreMaterias(String nombreMaterias) {
        this.nombreMaterias = nombreMaterias;
    }

    public void setGestion(String gestion) {
        this.gestion = gestion;
    }

    public void setNota1(int nota1) {
        this.nota1 = nota1;
    }

    public void setNota2(int nota2) {
        this.nota2 = nota2;
    }

    public void setNotaExFin(int notaExFin) {
        this.notaExFin = notaExFin;
    }

    public void setNotaPractico(int notaPractico) {
        this.notaPractico = notaPractico;
    }

    public void setNotaRepechaje(int notaRepechaje) {
        this.notaRepechaje = notaRepechaje;
    }

    public void setNotaPromedio(int notaPromedio) {
        this.notaPromedio = notaPromedio;
    }
    
    public void setNuevosDatos(String nombrecompleto,String nombreMateria, String Gestion, int nota1, int nota2, int notaExFinal, int notaPractico, int notaRepechaje, int notaPromedio){
        this.nombrecompleto = nombrecompleto;
        this.nombreMaterias= nombreMateria;
        this.gestion = Gestion; 
        this.nota1 = nota1; 
        this.nota2= nota2; 
        this.notaExFin=notaExFinal; 
        this.notaPractico=notaPractico; 
        this.notaRepechaje=notaRepechaje; 
        this.notaPromedio= notaPromedio;
    }
    
    
    public TDA_Alumno(String nombrecompleto, int registro, long carnet, String nombreMateria){
        this.nombrecompleto = nombrecompleto;
        this.registro = registro;
        this.carnet = carnet;
        this.nombreMaterias= nombreMateria;
    }
    
    public TDA_Alumno(int registro, String nombreMateria){ 
        this.registro = registro;
        this.nombreMaterias= nombreMateria;
    }
    public TDA_Alumno() {
       
    }
    public TDA_Alumno(int registro,String nombrecompleto,String nombreMateria, String Gestion, int nota1, int nota2, int notaExFinal, int notaPractico, int notaRepechaje, int notaPromedio){
        this.registro = registro;
        this.nombrecompleto = nombrecompleto;
        this.nombreMaterias= nombreMateria;
        this.gestion = Gestion; 
        this.nota1 = nota1; 
        this.nota2= nota2; 
        this.notaExFin=notaExFinal; 
        this.notaPractico=notaPractico; 
        this.notaRepechaje=notaRepechaje; 
        this.notaPromedio= notaPromedio; 
    }
    
    
    public TDA_Alumno(Vector v){
        this.registro =  (int)v.elementAt(0);
        this.nombrecompleto = (String) v.elementAt(1);
        this.nombreMaterias= (String) v.elementAt(2);
        this.gestion =(String) v.elementAt(3); 
        this.nota1 =(int) v.elementAt(4);
        this.nota2= (int) v.elementAt(5);
        this.notaExFin=(int) v.elementAt(6); 
        this.notaPractico=(int) v.elementAt(7); 
        this.notaRepechaje=(int) v.elementAt(8); 
        this.notaPromedio=(int) v.elementAt(9); 
    }
    
    public String getNombrecompleto() {
        return this.nombrecompleto;
    }

    public long getRegistro() {
        return this.registro;
    }

    public long getCarnet() {
        return this.carnet;
    }
    
    public String getMateria() {
        return this.nombreMaterias;
    }
    public String getGestion(){
        return this.gestion; 
    }
    public float getNota1(){
        return this.nota1; 
    }
    public float getNota2(){
        return this.nota2; 
    }
    public float getNotaExFinal(){
        return this.notaExFin; 
    }
    public float getNotaPractico(){
        return this.notaPractico; 
    }
    public float getNotaRepechaje(){
        return this.notaRepechaje; 
    }
    public float getNotaPromedio(){
        return this.notaPromedio; 
    }
   
    
    
    
    
    public void verificando(Object obj){
        TDA_Alumno o = (TDA_Alumno) obj;
        if (getRegistro() == o.getRegistro() && getCarnet() == o.getCarnet()){
            JOptionPane.showMessageDialog(null, "Existe registro y carnet similares, por tanto no se puede añadirlo");
        } else if (getCarnet() == o.getCarnet()){
            JOptionPane.showMessageDialog(null, "Existe carnet similares, por tanto no se puede añadirlo");
        } else {
            JOptionPane.showMessageDialog(null, "Existe registro similares, por tanto no se puede añadirlo");
        }
    }

    public boolean conocer(long regist, long carne) {
        if (getRegistro() == regist && getCarnet() == carne){
            return false;
        } else {
            return true;
        }
    }
    
    public void nota_P1(byte nota1, byte nota2, byte nota3){
        notas = nota1;
        notas = notas<<7;
        notas = notas|nota2;
        notas = notas<<7;
        notas = notas|nota3;
        notas = notas<<7;
    }
    
    public void nota_P2(byte nota1, byte nota2, byte nota3, byte nota4){
        nota_P1(nota1, nota2, nota3);
        notas = notas | nota4;
        notas = notas<<7;
    }
  
    @Override
    public String toString() {
        return  " " + getRegistro() + " " + getNombrecompleto() + "" + getMateria() + " " + getGestion() + " " + getNota1() + " " + getNota2() + " " + getNotaExFinal() + " " + getNotaPractico() + " " + getNotaRepechaje() + " " + getNotaPromedio();                           
    }
    
    public String[] toArray() {
        String[] v = new String[10]; 
        v[0] = String.valueOf(this.registro);
        v[1] = String.valueOf(this.nombrecompleto);
        v[2] = String.valueOf(this.nombreMaterias);
        v[3] = String.valueOf(this.gestion);
        v[4] = String.valueOf(this.nota1);
        v[5] = String.valueOf(this.nota2);
        v[6] = String.valueOf(this.notaExFin);
        v[7] = String.valueOf(this.notaPractico);
        v[8] = String.valueOf(this.notaRepechaje);
        v[9] = String.valueOf(this.notaPromedio);
        return v; 
    }
    
    public int compareTo(TDA_Alumno o) {
        if (getRegistro() == o.getRegistro() && !o.getMateria().equals(getMateria()) && o.getNombrecompleto().equals(getNombrecompleto()) )   {
           return 1;  
        } else  if (getRegistro() > o.getRegistro() && getCarnet() != o.getCarnet()){
            return 1;
        } else if (getRegistro() < o.getRegistro() && getCarnet() != o.getCarnet()){
            return -1;
        } else   {
            return 0;
        }   
    }
}
