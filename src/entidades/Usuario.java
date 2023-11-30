package entidades;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	 private String email;
	 private String password;
	 
	public Usuario(String username, String password) {
		super();
		this.email = username;
		this.password = password;
	}

	public String getUsername() {
		return email;
	}

	public void setUsername(String username) {
		this.email = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
