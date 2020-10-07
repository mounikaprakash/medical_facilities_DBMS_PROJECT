import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffTreatPatient {
	public int choice;
	public String PatientID;
	public String CheckID;
	public String severityValue;
	public String symptomSeverity;
	public String staffid;
	public String FacilityID;

	StaffTreatPatient(String id){
		this.staffid = id;
	}
	
	public void runStaffTreatPatient() throws SQLException {
			getFacility();
			printTreatedPatientList();
            this.choice = getStaffTreatPatientData();
            getCheckID();
            performStaffTreatPatientAction(this.choice);
    }

	public void printTreatedPatientList() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
        String Query = "SELECT CheckID, PatientID FROM Checkin_t WHERE FACILITYID = "+this.FacilityID+"  AND TREATMENT_ENDTIME is null AND TREATMENT_STARTTIME IS NOT NULL ";
		ResultSet rs = stmt.executeQuery(Query);
		System.out.println("CheckID   PatientID");
		while (rs.next()) {
			String s = rs.getString("CheckID");
		    String n = rs.getString("PatientID");
		    System.out.println(s + "         " + n);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
}	

	public void getFacility() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT SD.FacilityID FROM Staff_T S INNER JOIN ServiceDepartment_T SD ON SD.DepartmentID = S.Primary_DepartmentID WHERE S.EmployeeID = '"+this.staffid+"'");
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
	
	public void getCheckID() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT CheckID FROM Checkin_t WHERE FACILITYID  = '"+this.FacilityID+"' AND  PatientID = '"+this.PatientID+"'");
		while (rs.next()) {
			this.CheckID = rs.getString("CheckID");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
}	

	
    
    public int getStaffTreatPatientData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter PatientID:");
            this.PatientID = keyboard.nextLine(); 
            System.out.print("Enter \n 1)Check out\n 2)Go Back\nchoice:");
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
            }
            if (choice < 1 || choice > 2	) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 2);
    	}
    	catch(Exception e) {
    		System.out.print("Error in Staff treat patient choice data....\n"+e);
    	}
        return choice;
    }
       
    public void performStaffTreatPatientAction(int choice) throws SQLException {
        switch (choice) {
            case 1:
            	//System.out.println("**************\n*Staff Patient Check out*\n**************");
            	StaffPatientReport spr =new StaffPatientReport(this.staffid,this.PatientID,this.CheckID);
            	spr.runStaffPatientReport();
            	break;
            case 2://Go Back Staff Menu
            	System.out.println("**************\n*Staff Menu Page*\n**************");
              	StaffMenu sa = new StaffMenu(this.staffid);//Pass staff id here
                sa.runStaffMenu();
            	break;
            default:
                System.out.println("Error in staff treat patient switch block");
        }
    }

}


