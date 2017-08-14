package com.javatpoint.mypackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.exception.NotAuditedException;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

public class StoreData {
	public static void main(String[] args) {

		// creating configuration object
		Configuration cfg = new Configuration();
		cfg.configure("hibernate.cfg.xml");

		// creating session factory object
		SessionFactory factory = cfg.buildSessionFactory();
		// creating session object
		Session session = factory.openSession();

		// creating transaction object
		Transaction t = session.beginTransaction();

		// Employee e1 = new Employee();
		// e1.setId(2);
		// e1.setFirstName("fname2");
		// e1.setLastName("lname2");
		//
		// session.persist(e1);// persisting the object

		// Student std = new Student();
		// std.setId(1);
		// std.setAge(20);
		// std.setName("student");
		//
		// session.persist(std);

		// Employee emp = (Employee) session.get(Employee.class, 1);
		// emp.setLastName("newnewfirst");
		// emp.setFirstName("newlast");
		// session.update(emp);
		// session.remove(emp);

		// Student std = (Student) session.get(Student.class, 1);
		// std.setName("new");
		// session.update(std);

		// for (LogRecord log : allLogs) {
		// System.out.println("firstName:" + log.getFirstName());
		// System.out.println("lastName:" + log.getLastName());
		// System.out.println("Timestamp:" + log.getTimestamp());
		// System.out.println("Action:" + log.getAction());
		// System.out.println("Modified by:" + log.getModifiedBy());
		// int numOfUpdatedCols = log.getModifiedCols().size();
		// System.out.println("#updated cols:" + numOfUpdatedCols);
		// if (numOfUpdatedCols == 1) {
		// System.out.println("updated col:" + log.getModifiedCols().get(0));
		// } else if (numOfUpdatedCols == 2) {
		// System.out.println("updated col1:" + log.getModifiedCols().get(0));
		// System.out.println("updated col2:" + log.getModifiedCols().get(1));
		// }
		// System.out.println("...............................");
		// }

		//
		// List<Employee> loadedData = getAuditsById(session, 1); for(Employee
		// emp: loadedData){
		// System.out.println("firstName:"+emp.getFirstName());
		// System.out.println("firstLast:"+emp.getLastName());
		// System.out.println("..................."); }

		Employee_Audit_Service_Impl auditService = new Employee_Audit_Service_Impl();
		List<LogRecord> result = auditService.startAudit(session);

		for (LogRecord log : result) {
			EmployeeLogRecord eLog = (EmployeeLogRecord) log;
			System.out.println("firstName:" + eLog.getFirstName());
			System.out.println("lastName:" + eLog.getLastName());
			System.out.println("Timestamp:" + log.getTimestamp());
			System.out.println("Action:" + eLog.getAction());
			System.out.println("Modified by:" + eLog.getModifiedBy());
			int numOfUpdatedCols = log.getModifiedCols().size();
			System.out.println("#updated cols:" + numOfUpdatedCols);
			if (numOfUpdatedCols == 1) {
				System.out.println("updated col:" + log.getModifiedCols().get(0));
			} else if (numOfUpdatedCols == 2) {
				System.out.println("updated col1:" + log.getModifiedCols().get(0));
				System.out.println("updated col2:" + log.getModifiedCols().get(1));
			}
			System.out.println("...............................");
		}
		//
		// Student_Audit_Service_Impl stdAuditService = new
		// Student_Audit_Service_Impl();
		// stdAuditService.startAudit(session);

		// startAudit(session);

		t.commit();// transaction is committed
		// session.close();

		System.out.println("successfully completed!");

	}

	public static void startAudit(Session session) {
		// getting primary keys of records
		int indexes[] = { 1, 2, 3 };

		List<EmployeeLogRecord> allLogs = new ArrayList<>();

		for (int index : indexes) {
			List<EmployeeLogRecord> loadedData = getLogRecordById(session, index);
			for (int i = 0; i < loadedData.size(); i++) {
				List<String> temp = new ArrayList<>();
				if (loadedData.get(i).getAction().equals("ADD") || loadedData.get(i).getAction().equals("DEL")) {
					temp.add("firstName");
					temp.add("lastName");
				} else {
					if ((i - 1) >= 0) {
						temp = compareUpdates(loadedData.get(i), loadedData.get(i - 1));
					} else {
						// cannot audit cz no history
						System.out.println("cannot detect history!!!!!!!!!!");
					}
				}

				loadedData.get(i).setModifiedCols(temp);

			}
			allLogs.addAll(loadedData);
		}

		// displaying results

		// Collections.sort(allLogs);

		for (EmployeeLogRecord log : allLogs) {
			System.out.println("firstName:" + log.getFirstName());
			System.out.println("lastName:" + log.getLastName());
			System.out.println("Timestamp:" + log.getTimestamp());
			System.out.println("Action:" + log.getAction());
			System.out.println("Modified by:" + log.getModifiedBy());
			int numOfUpdatedCols = log.getModifiedCols().size();
			System.out.println("#updated cols:" + numOfUpdatedCols);
			if (numOfUpdatedCols == 1) {
				System.out.println("updated col:" + log.getModifiedCols().get(0));
			} else if (numOfUpdatedCols == 2) {
				System.out.println("updated col1:" + log.getModifiedCols().get(0));
				System.out.println("updated col2:" + log.getModifiedCols().get(1));
			}
			System.out.println("...............................");
		}

	}

	public static List<EmployeeLogRecord> getLogRecordById(Session session, int id) {

		AuditReader reader = AuditReaderFactory.get(session);
		List<EmployeeLogRecord> audits = new ArrayList<>();

		List<Object[]> revTypes = getRevTypes(session, id);

		try {
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

		} catch (NotAuditedException ex) {
			System.out.println("no audit table 2 >>>>>>>>>>>>>>>>>>.");
		}

		return audits;
	}

	public static List<Object[]> getRevTypes(Session session, int id) {

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

	public static List<String> compareUpdates(EmployeeLogRecord obj1, EmployeeLogRecord obj2) {
		List<String> updatedColumns = new ArrayList<>();

		if (!obj1.getFirstName().equals(obj2.getFirstName())) {
			updatedColumns.add("firstName");
		}
		if (!obj1.getLastName().equals(obj2.getLastName())) {
			updatedColumns.add("lastName");
		}

		return updatedColumns;

	}

}