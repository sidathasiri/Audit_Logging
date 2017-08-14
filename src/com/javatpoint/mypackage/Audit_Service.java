package com.javatpoint.mypackage;

import java.util.List;

import org.hibernate.Session;

public interface Audit_Service {
	public List<LogRecord> startAudit(Session session);
}
