package hospital_System;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class DoctorDriver {
	
	private Connection con ;
	
	
	public DoctorDriver(Connection con ) {
		this.con = con ;	
	}
	
	
	
	
	public void view_Doctors() {
		try {
			String query = "SELECT * FROM doctors";
			PreparedStatement ps = con.prepareStatement(query) ;
			ResultSet rs = ps.executeQuery() ;
			
			System.out.println("Doctors:");
			System.out.println("+--------------+--------------+------------------+") ;
			System.out.println("|  Doctor_id   |      Name    |  Specialization  |");
			System.out.println("+--------------+--------------+------------------+");
			
			while(rs.next()) {
				int id = rs.getInt("d_id") ;
				String name = rs.getString("name") ;
				String specialization = rs.getString("specialization") ;
				
				System.out.printf("| %-12d | %-12s | %-15s |%n", id, name, specialization);
				System.out.println("+--------------+--------------+------------------+");
			}
			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean get_Doctor(int id) {
		
		String query = "SELECT * FROM doctors where d_id = ?" ;
		
		try {
			
			PreparedStatement ps = con.prepareStatement(query) ;
			ps.setInt(1,id);
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
