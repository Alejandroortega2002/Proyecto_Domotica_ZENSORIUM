package entidades;

import java.util.Date;

public class Reporte {
	
	private long id_reporte;
	private long id_user_emisor;
	private long id_user_receptor;
	private String titulo;
	private String queja;
	private Date fecha;
	
	public Reporte(long id_reporte, long id_user_emisor, long id_user_receptor, String titulo, String queja, Date fecha) {
		super();
		this.id_reporte = id_reporte;
		this.id_user_emisor = id_user_emisor;
		this.id_user_receptor = id_user_receptor;
		this.titulo = titulo;
		this.queja = queja;
		this.fecha = fecha;
		
	}
	
	public long getId_reporte() {
		return id_reporte;
	}

	public void setId_reporte(long id_reporte) {
		this.id_reporte = id_reporte;
	}

	public long getId_user_emisor() {
		return id_user_emisor;
	}
	public void setId_user_emisor(long id_user_emisor) {
		this.id_user_emisor = id_user_emisor;
	}
	public long getId_user_receptor() {
		return id_user_receptor;
	}
	public void setId_user_receptor(long id_user_receptor) {
		this.id_user_receptor = id_user_receptor;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getQueja() {
		return queja;
	}
	public void setQueja(String queja) {
		this.queja = queja;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
}
