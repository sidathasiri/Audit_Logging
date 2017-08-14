package com.javatpoint.mypackage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

public interface Audit_Service_Dao {

	public List<LogRecord> getLogRecordsById(Session session, int id);
	public List<String> compareUpdates(LogRecord obj1, LogRecord obj2);
	public List<Object[]> getRevTypes(Session session, int id);
}
