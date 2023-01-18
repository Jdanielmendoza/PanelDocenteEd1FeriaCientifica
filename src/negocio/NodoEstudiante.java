
package negocio;

/**
 *
 * @author jdani
 */
public class NodoEstudiante {
    public TDA_Alumno Alumno; 
    public NodoEstudiante siguiente; 
    
    public NodoEstudiante(){
      this.siguiente=null;
    } 
    
    public NodoEstudiante (TDA_Alumno Alumno){
       this.Alumno= Alumno;
       this.siguiente= null;
    }
    
    public void setDatos(TDA_Alumno Alumno){
        this.Alumno= Alumno;
    }
    
    public void setSiguiente(NodoEstudiante siguiente) {
        this.siguiente = siguiente;
    }
    
    public TDA_Alumno GetDato(){
        return this.Alumno;
    }
    
    public NodoEstudiante GetEnlace(){
       return this.siguiente;
    }
}
