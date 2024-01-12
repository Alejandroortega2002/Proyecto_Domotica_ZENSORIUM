package entidades;

public class Dsipositivos {

	private long id_dispo;
	private long id_sensor;
	private long id_usu_relacionado;
	private boolean estado;
	private String Tipo;
	private String Nombre;
	
	public Dsipositivos(long id_dispo, long id_sensor, long id_usu_relacionado, boolean estado, String tipo,
			String nombre) {
		super();
		this.id_dispo = id_dispo;
		this.id_sensor = id_sensor;
		this.id_usu_relacionado = id_usu_relacionado;
		this.estado = estado;
		Tipo = tipo;
		Nombre = nombre;
	}

	public long getId_dispo() {
		return id_dispo;
	}

	public void setId_dispo(long id_dispo) {
		this.id_dispo = id_dispo;
	}

	public long getId_sensor() {
		return id_sensor;
	}

	public void setId_sensor(long id_sensor) {
		this.id_sensor = id_sensor;
	}

	public long getId_usu_relacionado() {
		return id_usu_relacionado;
	}

	public void setId_usu_relacionado(long id_usu_relacionado) {
		this.id_usu_relacionado = id_usu_relacionado;
	}

	public boolean isEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	public String getNombre() {
		return Nombre;
	}

	public void setNombre(String nombre) {
		Nombre = nombre;
	}

}
