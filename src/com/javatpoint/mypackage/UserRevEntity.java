package com.javatpoint.mypackage;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;


@RevisionEntity(UserRevisionListener.class)
public class UserRevEntity extends DefaultRevisionEntity {
	
	private String username;
		
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
		
}
