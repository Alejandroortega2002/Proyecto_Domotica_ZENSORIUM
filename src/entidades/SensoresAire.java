package entidades;
public class SensoresAire {
    private Boolean estado; // true: on, false: off

    public SensoresAire(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
