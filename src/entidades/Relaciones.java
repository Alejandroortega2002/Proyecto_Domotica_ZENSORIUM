package entidades;

public class Relaciones {

	private long tu_id;
	private long id_user_relacion;
	private String tipo_relacion;// AF-ADMIN A FAMILIAR FB-FAMILIAR A BASICO

	public Relaciones(long tu_id, long id_user_relacion, String tipo_relacion) {
		super();
		this.tu_id = tu_id;
		this.id_user_relacion = id_user_relacion;
		this.tipo_relacion = tipo_relacion;
	}

	public long getTu_id() {
		return tu_id;
	}

	public void setTu_id(long tu_id) {
		this.tu_id = tu_id;
	}

	public long getId_user_relacion() {
		return id_user_relacion;
	}

	public void setId_user_relacion(long id_user_relacion) {
		this.id_user_relacion = id_user_relacion;
	}

	public String getTipo_relacion() {
		return tipo_relacion;
	}

	public void setTipo_relacion(String tipo_relacion) {
		this.tipo_relacion = tipo_relacion;
	}

}
