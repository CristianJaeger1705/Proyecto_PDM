package sv.edu.ues.fia.proyecto_pdm.seccion;

public class Seccion {
    private int idSeccion;
    private int idBodega;
    private int nivel;

    private int capacidadMax;

    public Seccion() {
    }

    public Seccion(int idSeccion, int idBodega, int nivel, int capacidadMax) {
        this.idSeccion = idSeccion;
        this.idBodega = idBodega;
        this.nivel = nivel;
        this.capacidadMax = capacidadMax;
    }

    public int getIdSeccion() {
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdBodega(){
        return idBodega;
    }

    public void setIdBodega(int idBodega){
        this.idBodega = idBodega;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getCapacidadMax() {
        return capacidadMax;
    }

    public void setCapacidadMax(int capacidadMax) {
        this.capacidadMax = capacidadMax;
    }
}
