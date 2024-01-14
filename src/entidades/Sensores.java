package entidades;

public class Sensores {

	private long id_sensor;
	private float dato_actual;
	private String nombre;
	private String tipo;
	private long orden_registro;


	public Sensores(long id_sensor, float dato_actual, String nombre, String tipo, long orden_registro) {
		super();
		this.id_sensor = id_sensor;
		this.dato_actual = dato_actual;
		this.nombre = nombre;
		this.tipo = tipo;
		this.orden_registro = orden_registro;
	}

	public float getDato_actual() {
		return dato_actual;
	}

	public void setDato_actual(float dato_actual) {
		this.dato_actual = dato_actual;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getId_sensor() {
		return id_sensor;
	}

	public void setId_sensor(long id_sensor) {
		this.id_sensor = id_sensor;
	}

	public long getOrden_registro() {
		return orden_registro;
	}

	public void setOrden_registro(long orden_registro) {
		this.orden_registro = orden_registro;
	}
	
	

}
