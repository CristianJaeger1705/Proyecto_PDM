package sv.edu.ues.fia.proyecto_pdm.bodega;

public class Bodega {
    private int idBodega;
    private String nombreBodega;
    private String departamento;
    private String direccion;

    public Bodega() {
    }

    public int getIdBodega() {
        return idBodega;
    }

    public void setIdBodega(int idBodega) {
        this.idBodega = idBodega;
    }

    public String getNombreBodega() {
        return nombreBodega;
    }

    public void setNombreBodega(String nombreBodega) {
        this.nombreBodega = nombreBodega;
    }
    public String getDepartamento(){
            return departamento;
    }

    public void setDepartamento(String departamento){
        this.departamento = departamento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
