package hospital_System;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalDriver {
	
	private static final String url = "jdbc:postgresql://localhost:5432/hospital_System" ;
	private static final String user = "postgres" ;
	private static final String pass = "root" ;
	
	public static void main(String[] args) {
		
		try {
			Class.forName("org.postgresql.Driver") ;
			
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			Connection con = DriverManager.getConnection(url, user, pass) ;
			System.out.println("Connection estblished");
			Scanner sc = new Scanner(System.in) ;
			
			PatientDriver p1 = new PatientDriver(con,sc) ;
			DoctorDriver d1 = new DoctorDriver(con) ;
			
			while(true) {
				
				System.out.println("*** Hospital Management System ***");
				System.out.println();
				System.out.println("1. Add Patient");
				System.out.println("2. View Patient ");
				System.out.println("3. View Doctors ");
				System.out.println("4. Book Appointment");
				System.out.println("5. Exit");
				System.out.println("Enter your choice");
				int choice = sc.nextInt() ;
				
				switch(choice) {
				case 1:
					p1.addPatient();
					break;
				case 2:
					p1.view_patient();
					break;
				case 3:
					d1.view_Doctors();
					break;
				case 4: 
					bookAppintment(p1, d1, con, sc);
					break;
				case 5:
					System.out.println("Thank you for using Hospital System");
					return ;
				default :
					System.out.println("Invalid Choice! ");
					
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void bookAppintment(PatientDriver p1, DoctorDriver d1, Connection con, Scanner sc) {
	    System.out.println("Enter patient Id: ");
	    int pid = sc.nextInt();

	    System.out.println("Enter Doctor ID: ");
	    int did = sc.nextInt();

	    System.out.println("Enter date for an appointment in YYYY-MM-DD Format");
	    String dateInput = sc.next(); // "2020-01-01"

	    try {
	        java.sql.Date sqlDate = java.sql.Date.valueOf(dateInput); // ✅ convert String to SQL Date

	        if (p1.get_Patient(pid) && d1.get_Doctor(did)) {
	            if (checkDoctorAvailablity(did, sqlDate, con)) { // also update method to accept java.sql.Date

	                String query = "INSERT INTO appointments (p_id, d_id, appointment_date) VALUES (?, ?, ?)";
	                PreparedStatement ps = con.prepareStatement(query);
	                ps.setInt(1, pid);
	                ps.setInt(2, did);
	                ps.setDate(3, sqlDate); // ✅ correct type for DATE column

	                int rowsAffected = ps.executeUpdate();
	                if (rowsAffected > 0)
	                    System.out.println("Appointment Booked!");
	                else
	                    System.out.println("Failed to book appointment");

	            } else {
	                System.out.println("Doctor not available");
	            }
	        } else {
	            System.out.println("Either patient or doctor doesn't exist");
	        }
	    } catch (IllegalArgumentException e) {
	        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	
	public static boolean checkDoctorAvailablity(int did, Date date,Connection con) {
		
		String query = "SELECT COUNT(*) FROM appointments WHERE d_id = ? AND appointment_date = ?";
		try {
				PreparedStatement ps = con.prepareStatement(query) ;
				ps.setInt(1, did);
				ps.setDate(2, date);
				
				ResultSet rs = ps.executeQuery() ;
				if(rs.next()) {
					int count = rs.getInt(1) ;
							if(count == 0)
								return true;
							else
								return false;
				
				}
				
			}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return false;
	}
}
