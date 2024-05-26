package entidades;

public class Sensores {

	private long id_sensor;
	private String descripcion;
	private String tipo;

	public Sensores(long id_sensor, String descripcion, String tipo) {
		super();
		this.id_sensor = id_sensor;
		this.descripcion = descripcion;
		this.tipo = tipo;
	}

	public long getId_sensor() {
		return id_sensor;
	}

	public void setId_sensor(long id_sensor) {
		this.id_sensor = id_sensor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String nombre) {
		this.descripcion = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}