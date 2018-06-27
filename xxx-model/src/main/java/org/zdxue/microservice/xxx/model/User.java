package org.zdxue.microservice.xxx.model;

import java.io.Serializable;

/**
 * @author xuezd
 */
public class User implements Serializable {
    private static final long serialVersionUID = -3549900714539247891L;
    
    private int id;
	private String name;
	private String email;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
