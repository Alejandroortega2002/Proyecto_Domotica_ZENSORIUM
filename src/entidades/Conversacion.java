package entidades;

import java.util.Date;

import modelo.ListaEnlazada;
import modelo.RegistroManager;

public class Conversacion {
	private long idConversacion;
	private long idEmisor;
	private long idReceptor;
	private String texto;
	private Date fechaEnvio;

	public Conversacion(long idConversacion, long idEmisor, long idReceptor, String texto, Date fechaEnvio) {
		this.idConversacion = idConversacion;
		this.idEmisor = idEmisor;
		this.idReceptor = idReceptor;
		this.texto = texto;
		this.fechaEnvio = fechaEnvio;
	}

	public long getIdConversacion() {
		return idConversacion;
	}

	public void setIdConversacion(long idConversacion) {
		this.idConversacion = idConversacion;
	}

	public long getIdEmisor() {
		return idEmisor;
	}

	public void setIdEmisor(long idEmisor) {
		this.idEmisor = idEmisor;
	}

	public long getIdReceptor() {
		return idReceptor;
	}

	public void setIdReceptor(long idReceptor) {
		this.idReceptor = idReceptor;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}



}
