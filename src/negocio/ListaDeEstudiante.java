package negocio;
/**
 *
 * @author jdani
 */

/* METODOS 
    *-constructor()
    *-add(TDA_Alumno Alumno)                     // agrega un nuevo nodo estudiante a mi lista 
    *-size()         -return int                 // retorna la cantidad de alumnos de mi lista 
    *-get(int index) -return TdaAlumno           // retorna el nodo de un estudiante 
    *-isEmpty()      -return boolean             // retorna si la lista esta vacia
    *-remove(TDA_Alumno Alumno)                  // Elimina un estudiante de mi lista 

 */
public class ListaDeEstudiante {

    NodoEstudiante primero;
    private long tamaño;

    public ListaDeEstudiante() {
        this.primero = null;
        this.tamaño = 0;
    }

    public boolean isEmpty() {
        return (primero == null);
    }

    public long size() {
        return this.tamaño;
    }

    public void add(TDA_Alumno Alumno) {
        NodoEstudiante nuevoNodo = new NodoEstudiante(Alumno);
        if (primero == null) {
            primero = nuevoNodo;
            this.tamaño++;
        } else {
            NodoEstudiante nodoActual = primero.GetEnlace();
            if (nodoActual == null) {
                primero.setSiguiente(nuevoNodo);
            } else {
                while (nodoActual.GetEnlace() != null) {
                    nodoActual = nodoActual.GetEnlace();
                }
                nodoActual.setSiguiente(nuevoNodo);
            }
            this.tamaño++;
        }
    }

    public void remove(TDA_Alumno Alumno) {  // elimina un estudiante
        NodoEstudiante aux = primero;
        NodoEstudiante anterior = null;
        while (aux != null && aux.GetDato().getRegistro() != Alumno.getRegistro()) {
            anterior = aux;
            aux = aux.GetEnlace();
        }
        if (aux != null) {
            if (anterior == null) {
                primero = aux.siguiente;
                this.tamaño--;
            } else {
                anterior.siguiente = aux.siguiente;
                this.tamaño--;
            }
        }
    }

    public TDA_Alumno get(long posicion) { 
        NodoEstudiante reco = primero;
        TDA_Alumno estudiante = new TDA_Alumno();
        for (long i = 0; i <= posicion; i++) {
            estudiante = reco.GetDato();
            reco = reco.siguiente;
        }
        return estudiante;
    }

    // probando los metodos usados en el panel de administracion del docente (newPanelDocente)
    public static void main(String[] args) {
        //LinkedList<TDA_Alumno> a;   
        //a = new LinkedList<>();
        
        TDA_Alumno x = new TDA_Alumno(221045074, "daniel");
        TDA_Alumno y = new TDA_Alumno(221045079, "luana");
        
        ListaDeEstudiante a = new ListaDeEstudiante();
        System.out.println(a.isEmpty());
        a.add(x);
        System.out.println(a.get(0));
        a.add(y);
        System.out.println("size: "+ a.size() +a.get(1));
        
        a.remove(x);
        System.out.println("size: "+ a.size() +a.get(0));
        System.out.println(a.isEmpty());
    }

}
