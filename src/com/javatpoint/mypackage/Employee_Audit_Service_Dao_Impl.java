package com.javatpoint.mypackage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

public class Employee_Audit_Service_Dao_Impl implements Audit_Service_Dao{

	@Override
	public List<LogRecord> getLogRecordsById(Session session, int id) {
		AuditReader reader = AuditReaderFactory.get(session);
		List<LogRecord> audits = new ArrayList<>();

		List<Object[]> revTypes = getRevTypes(session, id);

		try{
			List<Number> revisions = reader.getRevisions(Employee.class, id);
			
			for (Number revNum : revisions) {
				Employee emp = reader.find(Employee.class, id, revNum);
				UserRevEntity rev = (UserRevEntity) session.getNamedQuery("findRevisionsById")
						.setParameter("id", revNum.intValue()).list().get(0);
				EmployeeLogRecord record = new EmployeeLogRecord();
				if (emp != null)
					record.setFirstName(emp.getFirstName());
				if (emp != null)
					record.setLastName(emp.getLastName());
				record.setModifiedBy(rev.getUsername());
				record.setTimestamp(String.valueOf(rev.getTimestamp()));
				record.setAction(String.valueOf(revTypes.get(0)));
				revTypes.remove(0);
				audits.add(record);
				// System.out.println(record.toString());

			}
			
		} catch(NotAuditedException ex){
			System.out.println("no audit table 2 >>>>>>>>>>>>>>>>>>.");
		}
		

		return audits;
	}

	@Override
	public List<String> compareUpdates(LogRecord obj1, LogRecord obj2) {
		List<String> updatedColumns = new ArrayList<>();
		
		EmployeeLogRecord empObj1 = (EmployeeLogRecord) obj1;
		EmployeeLogRecord empObj2 = (EmployeeLogRecord) obj2;

		if (!empObj1.getFirstName().equals(empObj2.getFirstName())) {
			updatedColumns.add("firstName");
		}
		if (!empObj1.getLastName().equals(empObj2.getLastName())) {
			updatedColumns.add("lastName");
		}

		return updatedColumns;
	}

	@Override
	public List<Object[]> getRevTypes(Session session, int id) {
		List<Object[]> result = new ArrayList<>();

		try {
			AuditReader auditReader = AuditReaderFactory.get(session);
			AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(Employee.class, false, true)
					.addProjection(AuditEntity.revisionType()).add(AuditEntity.property("id").eq(id));
			result = query.getResultList();
		} catch (NotAuditedException ex) {
			System.out.println("no audit table>>>>>>>>>>>>>");
		}

		return result;
	}

}
