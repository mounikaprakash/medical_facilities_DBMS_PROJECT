import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Patient {
	public String lastname;
	public String firstname;
	public int facilityid;
	public String patientid;
	public int choice;
	public String checkid;
	
	Patient(String id){
		this.patientid = id;
	}
	
    public void runPatientMenu() throws SQLException {
            this.choice = getPatientData();
            if(this.choice == 1) {  //Check-in
            		printPatientCheckinMenu();
            		getPatientCheckinData();//get Facility Id for a checkin process
            		if(validFacilityID() && validFacilityIDwrtPatient()) { //valid checkin
                    	System.out.println("Going to Check in Page...");
            			System.out.println("**************\n*Check in Page*\n**************");
            			Checkin c = new Checkin(this.facilityid, this.patientid);
            			try {
							c.runCheckIn();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            		}
            		else {//Already checkin
                    	System.out.println("Already Checked in that facility please perform check out or visit another facility");
                      	System.out.println("**************\n*Patient Main Page*\n**************");
                      	runPatientMenu();
            		}
            }
            else if(this.choice == 2) { //Check-out
            	if(validateCheckoutData()) {//Already check in and output report ready to ack
            		printAvailableCheckIDforCheckOut();
            		System.out.println("Enter one CheckID from above..");
                    Scanner keyboard = new Scanner(System.in);
                    this.checkid = keyboard.nextLine();
                    while(!validCheckID()) {
                		System.out.println("Invalid CheckID. Please select checkid from above list");
                        this.checkid = keyboard.nextLine();
                    }

                	System.out.println("Going to Ack Page...");
                	System.out.println("**************\n*Acknowledgement Page*\n**************");
                	PatientAcknowledge pa = new PatientAcknowledge(this.patientid,this.checkid);//Pass checkin id here
                	pa.runPatientAck();
            	}
            	else {//Already check in and output report not ready
            		System.out.println("Ack is not ready to sign. Check back again after sometime");
                  	System.out.println("**************\n*Patient Main Page*\n**************");
            		runPatientMenu();
            	}
            }
            else if(this.choice == 3) { //Go back
            	System.out.println("**************\n*Sign in Page*\n**************");
            	SignIn s = new SignIn();
            	s.runSignIn();
            }
            

    }

    private boolean validCheckID() {
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM Checkin_t C WHERE C.PatientID = '"+this.patientid+"' AND c.checkid = '"+this.checkid+"' AND C.Checkout_Time IS NULL AND TREATMENT_ENDTIME is not null");
		while (rs.next()) {
		    s = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
	    if(s > 0)
	    	return true;
	    else
	    	return false;
	}
    
    private boolean validateCheckoutData() {
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM Checkin_t C WHERE C.PatientID = '"+this.patientid+"' AND C.Checkout_Time IS NULL AND TREATMENT_ENDTIME is not null");
		while (rs.next()) {
		    s = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
	    if(s > 0)
	    	return true;
	    else
	    	return false;
	}
    
	public void printAvailableCheckIDforCheckOut() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT CHECKID FROM Checkin_t C WHERE C.PatientID = '"+this.patientid+"' AND C.Checkout_Time IS NULL AND TREATMENT_ENDTIME is not null");
		System.out.println("CHECKID");
		while (rs.next()) {
		    String n = rs.getString("CHECKID");
		    System.out.println(n);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    		
}	

	public boolean validFacilityIDwrtPatient() {//Checks if the patient is already checkedin at that facility--look here
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM Checkin_t C INNER JOIN Patient_T P ON C.patientid = P.patientid  WHERE C.FACILITYID = '"+this.facilityid+"' AND C.Checkout_Time IS NULL AND P.PATIENTID = '"+this.patientid+"'");
		while (rs.next()) {
		    s = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
	    if(s == 1)
	    	return false;
	    else
	    	return true;
	}

	public boolean validFacilityID() {//Checks if the facility id is invalid --look here
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM MedicalFacility_t WHERE FACILITYID = '"+this.facilityid+"'");
		while (rs.next()) {
		    s = rs.getInt("COUNT");
		}

        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
	    if(s == 1)
	    	return true;
	    else
	    	return false;
	}
	
	public void printPatientCheckinMenu() {
            try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT FACILITYID,FACILITYNAME FROM MedicalFacility_t");
    		System.out.println("FACILITYID   FACILITYNAME");
    		while (rs.next()) {
    		    int s = rs.getInt("FACILITYID");
    		    String n = rs.getString("FACILITYNAME");
    		    System.out.println(s + "         " + n);
    		}
            rs.close();
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
        		
    }	
    
    public int getPatientData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter 1)Check-in  2)Check-out acknowledgement  3)Go back\nChoice:");
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
    		System.out.print("Error in Patient choice....\n"+e);
    	}
        return choice;
    }
      
    public void getPatientCheckinData() {
    	try {
    		Scanner keyboard = new Scanner(System.in);
            do {
                System.out.print("Enter FacilityID: ");
                this.facilityid = Integer.parseInt(keyboard.nextLine()); 
                if(!validFacilityID())
                	System.out.println("FacilityID is Invalid.");
            }while(!validFacilityID());
    	}
    	catch(Exception e) {
    		System.out.print("Error in Patient Checkin choice....\n"+e);
    	}
    }

}

