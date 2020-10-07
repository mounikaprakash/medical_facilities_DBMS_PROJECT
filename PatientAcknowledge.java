import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PatientAcknowledge {
public int choice;
public String reason;
public String PatientID;
public String FacilityID;
public String patientid;
public String staffid;
public String dischargeStatus;
public String treatment;
public String checkid;
public String referrerid;
public String facilityid;
public String reason1;
public String reasonDescription1;
public String service1;
public String reason2;
public String reasonDescription2;
public String service2;
public String reason3;
public String reasonDescription3;
public String service3;
public String neCode;
public String neDescription;

	PatientAcknowledge(String id, String Checkid){
		this.PatientID = id;
		this.checkid = Checkid;
	}
    
    public void getData(){
        String reason;
        String service;
        String reasonDescription;
        try {
            Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "SELECT DISCHARGE_STATUS, TREATMENT_GIVEN from OUTCOME_REPORT_T where checkid = '"+this.checkid+"'";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.dischargeStatus = rs.getString("DISCHARGE_STATUS");
                this.treatment = rs.getString("TREATMENT_GIVEN");
            }
            
            sql = "SELECT FACILITYID, REFERRERID from REFERRAL_T where checkid = '"+this.checkid+"'";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.facilityid = rs.getString("FACILITYID"); 
                this.referrerid = rs.getString("REFERRERID");
            }
            
            sql = "SELECT REASONCODE, NAME_OF_SERVICE, DESCRIPION from REFERRAL_REASON_T where checkid = '"+this.checkid+"'";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                reason = rs.getString("REASONCODE");
                service = rs.getString("NAME_OF_SERVICE");
                reasonDescription = rs.getString("DESCRIPION");
                if(this.reason1 == null & this.service1 == null & this.reasonDescription1 == null) {
                    this.reason1 = reason;
                    this.service1 = service;
                    this.reasonDescription1 = reasonDescription;
                }
                else if(this.reason2 == null & this.service2== null & this.reasonDescription2 == null) {
                    this.reason2 = reason;
                    this.service2 = service;
                    this.reasonDescription2 = reasonDescription;
                }
                else if(this.reason3 == null & this.service3== null & this.reasonDescription3 == null) {
                    this.reason3 = reason;
                    this.service3 = service;
                    this.reasonDescription3 = reasonDescription;
                }
            }
            
            sql = "SELECT TEXTDESCRIPTION from NEGATIVE_EXPERIENCE_T where checkid = '"+this.checkid+"'";
            rs = stmt.executeQuery(sql);
            while(rs.next()) {
                this.neDescription = rs.getString("TEXTDESCRIPTION"); 
            }
            
            rs.close();
            stmt.close();
            conn.close();
         } catch(Throwable oops) {
            oops.printStackTrace();
         }
    }
    
    public void showReport() {
        getData();
        System.out.println("REPORT: ");
        System.out.println("");
        System.out.println("--------------------------------------------------");
        System.out.println(" Patient:       "+this.PatientID);
        System.out.println("--------------------------------------------------");
        System.out.print(" Discharge Status: ");
        System.out.println(this.dischargeStatus);
        System.out.println("--------------------------------------------------");
        if(this.dischargeStatus.contentEquals("REFERRED")) {
            System.out.print(" Referral Status: ");
            System.out.print("Referred to:  "+this.facilityid);
            System.out.print("\n            --------------------------");
            System.out.print("\n            Referred by:    "+this.referrerid);
            System.out.print("\n            --------------------------");
            System.out.print("\n            Referral Reasons:");
            if(this.reason1 != null) {
                System.out.print("\n            --------------------------");
                System.out.print("\n            Reason:         "+this.reason1);
                System.out.print("\n            Service:        "+this.service1);
                System.out.print("\n            Description:    "+this.reasonDescription1);
            }
            if(this.reason2 != null) {
                System.out.println("");
                System.out.print("\n            Reason:         "+this.reason2);
                System.out.print("\n            Service:        "+this.service2);
                System.out.print("\n            Description:    "+this.reasonDescription2);
            }
            if(this.reason3 != null) {
                System.out.println("");
                System.out.print("\n            Reason:         "+this.reason3);
                System.out.print("\n            Service:        "+this.service3);
                System.out.print("\n            Description:    "+this.reasonDescription3);
            }   
        }
        System.out.print("\n--------------------------------------------------");
        System.out.print("\n Treatment given:    ");
        System.out.println(this.treatment);
        System.out.println("--------------------------------------------------");
        System.out.print(" Negative Experience:  ");
        System.out.println(this.neDescription);
        System.out.println("--------------------------------------------------");
    }
	
	public void runPatientAck() throws SQLException {
		getFacilityID();
		showReport();
		//printPatientAck();
		this.choice = getPatientAckData();
         if(this.choice == 1) {//Yes
             //Update Ack Status into database table
        	 updateCheckOutTime();
             updateCheckoutstatus("YES");
             this.reason = "null";
             updateReason();
 	        System.out.print("Checkout Successful...Visit Again\n");
        	 
         }
         else if(this.choice == 2) {//No
        	 try {
        	        Scanner keyboard1 = new Scanner(System.in);
        	        System.out.print("Enter Reason:");
                    this.reason = "'"+keyboard1.nextLine()+"'";
                    
                    //Update Reason into database table
                    updateReason();
                    updateCheckoutstatus("NO");
                    
        	    	}
        	    	catch(Exception e) {
        	    		System.out.print("Error in Patient Ack Reason....\n"+e);
        	    	}
          }
         else if(this.choice == 3) {//Go Back
        	
          }
        System.out.println("**************\n*Patient Main Page*\n**************");
       	Patient p = new Patient(this.PatientID);//Pass patient id here
        p.runPatientMenu();
	}
	
	public void updateCheckOutTime() {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET CHECKOUT_TIME = systimestamp WHERE CheckID = '"+this.checkid+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
	}
	
    public void updateCheckoutstatus(String status) {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET CHECKOUT_REPORT_VALID = '"+status+"' WHERE CheckID = '"+this.checkid+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    }
    
    public void updateReason() {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET INVALID_REPORT_REASON = "+this.reason+" WHERE CheckID = '"+this.checkid+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    }
    
	public void printPatientAck() {
		//Display the report that is filled by the staff
        System.out.println("**************\n*Report Filled by staff*\n**************");
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT *  FROM Outcome_Report_T");
		System.out.println("CHECKID EMPLOYEEID   TREATMENT_GIVEN               DISCHARGE_STATUS");
		while (rs.next()) {
		    String Checkid = rs.getString("CHECKID");
		    String EMPLOYEEID = rs.getString("EMPLOYEEID");
		    String TREATMENT_GIVEN = rs.getString("TREATMENT_GIVEN");
		    String DISCHARGE_STATUS = rs.getString("DISCHARGE_STATUS");
		    System.out.println(Checkid + "         " + EMPLOYEEID + "         " + TREATMENT_GIVEN + "         " + DISCHARGE_STATUS);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    }	
	
	public void getFacilityID() {
		//Display the report that is filled by the staff
        System.out.println("**************\n*Check Out at Facility*\n**************");
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		//System.out.println("Facilities Available for check out");
		ResultSet rs = stmt.executeQuery("SELECT C.FacilityID FROM Checkin_t C WHERE C.PatientID = '"+this.PatientID+"' AND C.CHECKID = '"+this.checkid+"'");
		while (rs.next()) {
		    this.FacilityID = rs.getString("FacilityID");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
    }
	
    public int getPatientAckData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter 1)Yes  2)No  3)Go back\nChoice:");
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
    		System.out.print("Error in Patient Ack choice....\n"+e);
    	}
        return choice;
    }
}
