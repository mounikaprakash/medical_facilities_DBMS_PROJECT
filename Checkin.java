import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class Checkin {
	public int facilityid;
	public String patientid;
	public String SymptomName;
	public String CheckID;
	public String symptomdescription;
	public Checkin(int facilityid, String patientid) {
		this.facilityid = facilityid;
		this.patientid = patientid;
	}
	
    public void runCheckIn() throws SQLException {
    		//Create checkinid for the patient.
    		createCheckinID(this.facilityid,this.patientid);
    		this.CheckID = getCheckinID(this.facilityid,this.patientid);
    		System.out.println(this.CheckID);
    		System.out.println("Displaying Symptoms");
        	printSymptomsMenu();
        	do {
            this.SymptomName = getSymptomData();
            if((validSymptomsName(this.SymptomName) && (checkSymptomsName())) || this.SymptomName.contentEquals("Other") || this.SymptomName.contentEquals("Done")) {
	            System.out.println(this.SymptomName);
	            if(this.SymptomName.contentEquals("Other")) {
	            	//Add Symptom Description
	                System.out.println("Enter description for your symptom");
	                Scanner keyboard = new Scanner(System.in);
	                this.symptomdescription = keyboard.nextLine();
	            	SymptomMetaData sm = new SymptomMetaData(this.SymptomName, this.CheckID, this.symptomdescription);
	                sm.runSymptomMetaData();
	                
	            }
	            else if(this.SymptomName.contentEquals("Done")) {
	            	//Record systimestamp Checkin Start Time and validate
	            	setCheckinStartTime();
	            }
	            else {
	            	SymptomMetaData sm = new SymptomMetaData(this.SymptomName, this.CheckID, "");
	                sm.runSymptomMetaData();
            }
            }
            else {
            	System.out.println("Invalid Symptom Name, or symptom already entered Enter a valid Symptom Name:");
            }
        	}while(!this.SymptomName.contentEquals("Done"));
        	//validate, record time, logout, return to home page
        	
        	MainMenu menu = new MainMenu();
            menu.runMenu();
        	

    }
    
    public void setCheckinStartTime() {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET checkin_starttime = systimestamp WHERE CheckID = '"+this.CheckID+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    }
    
    public boolean createCheckinID(int facilityid,String patientid) throws SQLException {
    	boolean flag = false;
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("INSERT INTO Checkin_T(CHECKID,FACILITYID,PATIENTID,LISTSTATUS,CHECKIN_STARTTIME,CHECKIN_ENDTIME,TREATMENT_STARTTIME) VALUES(TO_CHAR(CHECKIN_S.NEXTVAL),'"+this.facilityid+"','"+this.patientid+"',null,null,null,null)");
            stmt.close();
            conn.close();
            flag = true;
            }catch(SQLException e) {
          	  System.out.println("**Exception Occured**\n try again");
          	  runCheckIn();
          }
         	return flag;
    }
    
    public String getCheckinID(int facilityid,String patientid) {
    	String checkID = "";
	        try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT CheckID FROM Checkin_T WHERE Facilityid = "+ facilityid+" AND Checkout_time IS NULL AND PATIENTID = '"+ patientid+"'");
			while (rs.next()) {
				checkID = rs.getString("CheckID");
			}
	        rs.close();
	        stmt.close();
	        conn.close();
	        } catch(Throwable oops) {
	            oops.printStackTrace();
	        }
	    	return checkID;
    }
    
	public void printSymptomsMenu() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT SYMPTOM_NAME FROM SYMPTOMS_T");
		System.out.println("SYMPTOM_NAME");
		while (rs.next()) {
		    String n = rs.getString("SYMPTOM_NAME");
		    if(!n.contentEquals("Other")) {
		    	System.out.println(n);
		    }
		}
		System.out.println("Other");
		System.out.println("Done");
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	
    }
	
	public boolean validSymptomsName(String symptomname1) {
		int count = 0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SYMPTOMS_T WHERE SYMPTOM_NAME = '"+symptomname1+"'");
		while (rs.next()) {
		    count = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
	    if(count == 1)
	    	return true;
	    else
	    	return false;
    	
    }
	
	public boolean checkSymptomsName() { //Symptom already entered for this checkid
		int count = 0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM checkin_symptoms_metadata_t sm INNER JOIN symptoms_t s ON sm.symptom_code = s.symptom_code WHERE sm.checkid = '"+this.CheckID+"' AND s.symptom_name = '"+this.SymptomName+"'");
		while (rs.next()) {
		    count = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
	    if(count > 0)
	    	return false;
	    else
	    	return true;
    	
    }
	
	
    public String getSymptomData() {
		String SymptomName1 = null;
    	try {

    		Scanner keyboard = new Scanner(System.in);
            System.out.print("Enter Symptom name: ");
            SymptomName1 = keyboard.nextLine(); 
    	}
    	catch(Exception e) {
    		System.out.print("Error in get Symptom data....\n"+e);
    	}
        return SymptomName1;
    }

}
