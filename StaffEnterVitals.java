import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffEnterVitals {
	public int choice;
	public float temperature;
	public int systolicBP;
	public int diastolicBP;
	public String patientID;
	public String staffid;
	public int facilityID; 
	public String checkID;

	StaffEnterVitals(String id, String pid, int fid, String cid ){
		this.staffid = id;
		this.patientID = pid;
		this.facilityID=fid;
		this.checkID=cid;
	}

	public void runStaffEnterVitals() throws SQLException {
    		//printStaffEnterVitalsMenu();
            this.choice = getStaffEnterVitalsData();
            performStaffEnterVitalsAction(this.choice);
    }

	public void printStaffEnterVitalsMenu() {
	        try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT PatientID, FIRSTNAME, LASTNAME FROM Patient_t");
			System.out.println("PatientID\tPatient_Name");
			while (rs.next()) {
			    int s = rs.getInt("PatientID");
			    String f = rs.getString("FIRSTNAME");
			    String l = rs.getString("LASTNAME");
			    System.out.println(s + "         " + f+"\t"+l);
			}
	        rs.close();
	        stmt.close();
	        conn.close();
	        } catch(Throwable oops) {
	            oops.printStackTrace();
	        }	
    }	
	
	public void displayPriority(String CheckID) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT ListStatus FROM CheckIN_t WHERE CheckID = '"+CheckID+"'");
		System.out.println("ListStatus");
		while (rs.next()) {
		    String f = rs.getString("ListStatus");
		    System.out.println("Status: "+f);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
}
	
	public boolean validCheckID(String checkid) {
		int count = 0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM RECORD_VITALS_T WHERE CHECKID = '"+checkid+"'");
		//System.out.println("PatientID\tPatient_Name");
		while (rs.next()) {
		    count = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
        if(count >0)
        	return false;
        else
        	return true;
}	
    
    public int getStaffEnterVitalsData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter Temperature: ");
            this.temperature = Float.parseFloat(keyboard.nextLine()); 
            //Validate temperature Data using SP
            System.out.print("Enter Systolic Blood Pressure: ");
            this.systolicBP = Integer.parseInt(keyboard.nextLine()); 
            System.out.print("Enter Diastolic Blood Pressure: ");
            this.diastolicBP = Integer.parseInt(keyboard.nextLine()); 
            System.out.print("Enter 1)Record 2)Go Back\nchoice:");
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
            }
            if (choice < 1 || choice > 2) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 2);
    	}
    	catch (NumberFormatException e) {
            System.out.println("Invalid selection. Please enter Numbers.");
        }
        return choice;
    }
    
    public void setCheckinEndTime(String CheckID) {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET checkin_endtime = systimestamp WHERE CheckID = '"+CheckID+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    }
    
    public void performStaffEnterVitalsAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //Record time, trigger assessment, display priority and go back
            	try {//Durga
            	System.out.println("**************\n*Recording Vitals*\n**************");
            	Connection conn = JDBC.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs=stmt.executeQuery("select checkid from checkin_t where patientid='"+this.patientID+"' AND facilityid="+this.facilityID);
				String s="";
				while(rs.next())
				{
					s = rs.getString("checkid");
				}
				if(validCheckID(s)) {
					stmt.executeUpdate("INSERT INTO RECORD_VITALS_T(EMPLOYEEID,CHECKID,TEMPERATURE,BPSYSTOLIC,BPDIASTOLIC) VALUES('"+this.staffid+"','"+s+"','"+this.temperature+"','"+this.systolicBP+"','"+this.diastolicBP+"')");
					displayPriority(s);
					setCheckinEndTime(s);
				}
				else {
	            	System.out.println("Vitals already enterd. Please select other patient");
				}
				rs.close();
		        stmt.close();
		        conn.close();
		        
            	//Record time, trigger assessment, display priority and go back
            	
            	System.out.println("**************\n*Staff Process Patient Page*\n**************");
            	StaffProcessPatient sp = new StaffProcessPatient(this.staffid,this.facilityID);
            	sp.runStaffProcessPatient();
            	} catch(Throwable oops) {
		            oops.printStackTrace();
		            runStaffEnterVitals();
            	}
            	break;
            	
            case 2://Go Back
            	System.out.println("**************\n*Staff Process Patient Page*\n**************");
            	StaffProcessPatient spp = new StaffProcessPatient(this.staffid,this.facilityID);
            	spp.runStaffProcessPatient();
            	break;
            default:
                System.out.println("Error in staff enter vitals switch block");
        }
    }

}


