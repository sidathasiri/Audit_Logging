package com.javatpoint.mypackage;

import java.util.ArrayList;
import java.util.List;


public class EmployeeLogRecord extends LogRecord{
	private String firstName;
	private String lastName;  
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	@Override
	public String toString() {
		return "EmployeeLogRecord [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
