package entidades;

public class Sensores {

	private float dato_actual;
	private String nombre;
	private String tipo;
	private long id_user_relacionado;

	public Sensores(float dato_actual, String nombre, String tipo, long id_user_relacionado) {
		super();
		this.dato_actual = dato_actual;
		this.nombre = nombre;
		this.tipo = tipo;
		this.id_user_relacionado = id_user_relacionado;
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

	public long getId_user_relacionado() {
		return id_user_relacionado;
	}

	public void setId_user_relacionado(long id_user_relacionado) {
		this.id_user_relacionado = id_user_relacionado;
	}

}
