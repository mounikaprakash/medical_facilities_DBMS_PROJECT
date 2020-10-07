import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffProcessPatient {
	public int choice;
	public String PatientID;
	public String staffid;
	public int facilityID;
	public String checkID;
	public int count =0 ;

	StaffProcessPatient(String id,int fid){
		this.staffid = id;
		this.facilityID=fid;
	}
    public void runStaffProcessPatient()  throws SQLException {
    		//Validate if staff is a medical staff or not
    	boolean isMedicalStaff = true;
    	if(isMedicalStaff) {
        	printStaffProcessPatientMenu();
            this.choice = getStaffProcessPatientData();
            getCheckinID();
            performStaffProcessPatientAction(this.choice);
    	}
    	else {
        	System.out.println("**************\n*Invalid access(Only Medical Staff has access). Going back to Staff Main page*\n**************");
        	System.out.println("**************\n*Staff Main Page*\n**************");
          	StaffMenu s = new StaffMenu(this.staffid);//Pass staff id here
            s.runStaffMenu();
    	}
            

    }
    
    public void runStaffTreatsPatient()  throws SQLException {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET ListStatus = 'TREATED' WHERE CheckID = '"+this.checkID+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    }

	public void getCheckinID() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT CHECKID FROM CHECKIN_T C WHERE C.PatientID = '"+this.PatientID+"' AND C.CHECKOUT_TIME IS NULL AND C.FacilityID = "+this.facilityID+"");
		while (rs.next()) {
			this.checkID = rs.getString("CHECKID");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
	}	
	
	public void printStaffProcessPatientMenu() {
	        try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
	        String Query = "SELECT DISTINCT p.patientid, p.lastname AS Patient_Name FROM staff_t S INNER JOIN servicedepartment_t sd ON s.primary_departmentid = sd.DEPARTMENTID INNER JOIN medicalfacility_t mf ON mf.facilityid = sd.facilityid INNER JOIN checkin_t c on c.facilityid = mf.facilityid INNER JOIN patient_t p ON p.patientid = c.patientid WHERE s.employeeid = '"+this.staffid+"' AND c.checkin_starttime is not null AND (c.ListStatus IS NULL OR ListStatus NOT IN ('TREATED'))";
			ResultSet rs = stmt.executeQuery(Query);
			System.out.println("Patient_ID     Patient_Name");
			while (rs.next()) {
			    String s = rs.getString("patientid");
			    String n = rs.getString("Patient_Name");
			    System.out.println(s + "            " + n);
			    this.count =1;
			}
	        rs.close();
	        stmt.close();
	        conn.close();
	        } catch(Throwable oops) {
	            oops.printStackTrace();
	        }	
    }	
	
	public boolean validPatient() {
		int count =0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("\r\n" + 
				"SELECT COUNT(*) AS COUNT FROM staff_t S INNER JOIN servicedepartment_t sd ON s.primary_departmentid = sd.DEPARTMENTID INNER JOIN medicalfacility_t mf ON mf.facilityid = sd.facilityid INNER JOIN checkin_t c on c.facilityid = mf.facilityid INNER JOIN patient_t p ON p.patientid = c.patientid WHERE s.employeeid = '"+this.staffid+"' AND c.checkin_starttime is not null AND (c.ListStatus IS NULL OR ListStatus NOT IN ('TREATED')) AND c.patientid = '"+this.PatientID+"'");
		//System.out.println("Patient_ID     Patient_Name");
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
        	return true;
        else
        	return false;
	}	
    
	public boolean validCheckID() {
		int count1 =0;
        try {
        	String Query= "SELECT COUNT(*) AS COUNT FROM CheckIn_T WHERE CheckID = '"+this.checkID+"'  AND Checkin_EndTime IS NOT NULL";
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		while (rs.next()) {
			count1 = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
        if(count1 > 0)
        	return true;
        else
        	return false;
	}	
	
    public int getStaffProcessPatientData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
        	if(this.count == 0) {
        		 System.out.print("No patients available currently. Visit back again\n\n ** Redirecting to Staff Menu ** \n");
     	 		System.out.println("**************\n*Staff Menu Page*\n**************");
     	 		StaffMenu si = new StaffMenu(this.staffid);//Pass staff id here
     	 		si.runStaffMenu();
        	}
            System.out.print("Enter PatientID: ");
            this.PatientID = keyboard.nextLine();
            while(!validPatient())
        	{
                System.out.print("Invalid PatientID\n Please enter again: ");
                this.PatientID = keyboard.nextLine();
        	}
            System.out.print("Enter 1)Enter Vitals\n 2)Treat patient \n 3)Go Back\nchoice:");
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
            }
            
            if (choice < 1 || choice > 3) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 3);
    	}
    	catch(Exception e) {
    		System.out.print("Error in Staff process patient choice data....\n"+e);
    	}
        return choice;
    }
       
    public void performStaffProcessPatientAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //Enter Vitals
            	System.out.println("**************\n*Enter Vitals*\n**************");
            	StaffEnterVitals se = new StaffEnterVitals(this.staffid,this.PatientID,this.facilityID,this.checkID);
            	se.runStaffEnterVitals();
            	break;
            case 2://Treat patient
            	System.out.println("**************\n*Treat patient*\n**************");
            	if(validCheckID()) {
            		if(isTreatedPatient()) {
		            	setTreatmentStartTime(this.checkID);
		            	runStaffTreatsPatient();
		            	System.out.println("TREATED");
		            	System.out.println("**************\n*Staff Process Patient Page*\n**************");
                    	runStaffProcessPatient();
            		}
            		else {
                    	System.out.println("Inadequete privilege to treat patient\n Try another patient");
                    	System.out.println("**************\n*Staff Process Patient Page*\n**************");
                    	runStaffProcessPatient();
            		}
            	}
            	else {
                	System.out.println("Patient checkin is not completed yet. try other patient\n");
                	System.out.println("**************\n*Staff Process Patient Page*\n**************");
                	runStaffProcessPatient();
            	}
            	break;
            case 3://Go Back
            	System.out.println("**************\n*Staff Menu Page*\n**************");
              	StaffMenu s = new StaffMenu(this.staffid);//Pass staff id here
                s.runStaffMenu();
            default:
                System.out.println("Error in staff process patient switch block");
        }
    }
    
    public void setTreatmentStartTime(String CheckID) {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET TREATMENT_STARTTIME = systimestamp WHERE CheckID = '"+CheckID+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    }
    
	public boolean isTreatedPatient() {
		int count =0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM ((select b.BODYPARTNAME from BODYPARTS_T b, CHECKIN_SYMPTOMS_METADATA_T csm, CHECKIN_T c where c.checkid=csm.checkid AND csm.bodypartname=b.bodypartname AND c.checkid = '"+this.checkID+"') Intersect (select b.bodypartname from BODYPART_SERVICE_DEPT_T bsd inner join BODYPARTS_T b on b.code=bsd.code where bsd.DEPARTMENTID in ((select s.PRIMARY_DEPARTMENTID from STAFF_T s where employeeid='"+this.staffid+"') UNION (SELECT ssd.SECONDARY_DEPARTMENTID from STAFF_SECONDARY_DEPT_T ssd where employeeid ='"+this.staffid+"'))))");
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
        	return true;
        else
        	return false;
	}	
}


