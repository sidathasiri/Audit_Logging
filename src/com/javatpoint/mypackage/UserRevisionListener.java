package com.javatpoint.mypackage;

import org.hibernate.envers.RevisionListener;

public class UserRevisionListener implements RevisionListener {
	
	public final static String USERNAME = "I edited";

	@Override
	public void newRevision(Object revisionEntity) {
		
		UserRevEntity exampleRevEntity = (UserRevEntity) revisionEntity;
        exampleRevEntity.setUsername(USERNAME);
	}
	
}
