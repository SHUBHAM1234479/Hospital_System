package hospital_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PatientDriver {
	
	private Connection con ;
	private Scanner sc ;
	
	public PatientDriver(Connection con ,Scanner sc) {
		this.con = con ;
		this.sc = sc ;
		
	}
	
	
	public void addPatient() {
		
		System.out.print("Enter patient Name: ");
		String name = sc.next() ;
		System.out.print("Enter age of Patient: ");
		int age = sc.nextInt() ;
		System.out.print("Enter Patient gender: ");
		String gender = sc.next() ;
		
		
		try {
			
			String query = "INSERT INTO public.patients (p_name,age,gender) VALUES (?,?,?)" ;
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, name);
			ps.setInt(2, age);
			ps.setString(3, gender);
			
			int affectedrows = ps.executeUpdate();
			if(affectedrows > 0) 
				System.out.println("Patient added successfully");
			else 
				System.out.println("Failed to add patient!");
			
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void view_patient() {
		try {
			String query = "SELECT * FROM patients";
			PreparedStatement ps = con.prepareStatement(query) ;
			ResultSet rs = ps.executeQuery() ;
			
			System.out.println("Patients:");
			System.out.println("+--------------+--------------+----------+------------+") ;
			System.out.println("|  Patient_id  |      Name    |    Age   |    Gender  |");
			System.out.println("+--------------+--------------+----------+------------+");
			
			while(rs.next()) {
				int id = rs.getInt("p_id") ;
				String name = rs.getString("p_name") ;
				int age = rs.getInt("age") ;
				String gender = rs.getString("gender") ;
				
				 System.out.printf("| %-12d | %-12s | %-8d | %-10s |%n", id, name, age, gender);
				System.out.println("+--------------+--------------+----------+------------+");
			}
			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean get_Patient(int id) {
		
		String query = "SELECT * FROM patients where p_id = ?" ;
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query) ;
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery() ;
			if(rs.next())
				return true;
			else
				return false;
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}
