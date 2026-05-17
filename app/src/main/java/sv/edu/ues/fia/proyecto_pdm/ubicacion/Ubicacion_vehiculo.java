package sv.edu.ues.fia.proyecto_pdm.ubicacion;

import java.time.LocalDate;

public class Ubicacion_vehiculo {
    private int idUbicacion;
    private int idSeccion;
    private int idVehiculo;
    private LocalDate fechaAsignacion;
    private Boolean activa;

    public Ubicacion_vehiculo() {
    }

    public Ubicacion_vehiculo(int idUbicacion, int idSeccion, int idVehiculo, LocalDate fechaAsignacion, Boolean activa) {
        this.idUbicacion = idUbicacion;
        this.idSeccion = idSeccion;
        this.idVehiculo = idVehiculo;
        this.fechaAsignacion = fechaAsignacion;
        this.activa = activa;
    }

    public int getIdUbicacion(){
        return idUbicacion;
    }

    public void setIdUbicacion(int idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public int getIdSeccion(){
        return idSeccion;
    }

    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}
