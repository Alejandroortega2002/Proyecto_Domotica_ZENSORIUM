package entidades;

import java.io.Serializable;

public class Usuario implements Serializable {

	private long id_user;
	private String username;
	private String email;
	private String password;
	private String repetirPass;
	private String tipodecuenta;

	public Usuario(long id_user, String username, String email, String password, String repetirPass,
			String tipodecuenta) {
		super();
		this.id_user = id_user;
		this.username = username;
		this.email = email;
		this.password = password;
		this.repetirPass = repetirPass;
		this.tipodecuenta = tipodecuenta;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepetirPass() {
		return repetirPass;
	}

	public void setRepetirPass(String repetirPass) {
		this.repetirPass = repetirPass;
	}

	public String getTipodecuenta() {
		return tipodecuenta;
	}

	public void setTipodecuenta(String tipodecuenta) {
		this.tipodecuenta = tipodecuenta;
	}

}
