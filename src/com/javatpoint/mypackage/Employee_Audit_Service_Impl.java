package com.javatpoint.mypackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;

public class Employee_Audit_Service_Impl implements Audit_Service {

	Audit_Service_Dao auditDao = new Employee_Audit_Service_Dao_Impl();

	@Override
	public List<LogRecord> startAudit(Session session) {
		// getting primary keys of records
		// int indexes[] = { 1, 2, 3 };

		List<Employee> emps = session.getNamedQuery("findPrimaryKeys").list();
		// System.out.println(">>>>>>>>>>>>>>>>>"+emps.get(0).getId()+"
		// "+emps.get(1).getId());

		List<LogRecord> allLogs = new ArrayList<>();

		for (Employee e : emps) {
			List<LogRecord> loadedData = auditDao.getLogRecordsById(session, e.getId());
			// setting the modified columns
			for (int i = 0; i < loadedData.size(); i++) {
				List<String> temp = new ArrayList<>();
				if (loadedData.get(i).getAction().equals("ADD") || loadedData.get(i).getAction().equals("DEL")) {
					temp.add("firstName");
					temp.add("lastName");
				} else {
					if ((i - 1) >= 0) {
						temp = auditDao.compareUpdates(loadedData.get(i), loadedData.get(i - 1));
					} else {
						// cannot audit cz no history
						System.out.println("no history!!!!!!!!!!");
					}
				}

				loadedData.get(i).setModifiedCols(temp);

			}
			allLogs.addAll(loadedData);
		}

		Collections.sort(allLogs);

		return allLogs;

	}

}
