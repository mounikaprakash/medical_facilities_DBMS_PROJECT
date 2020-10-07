import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffMenu {
	public int choice;
	public String staffid;
	public int FacilityID;
	
	StaffMenu(String id){
		this.staffid = id;
	}
	
    public void runStaffMenu() throws SQLException {
        	printStaffMenu();
            this.choice = getStaffData();
            performStaffAction(this.choice);
            

    }

	public void printStaffMenu() {
        System.out.print("Please enter following details: ");
    }	
    
    public int getStaffData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter \n 1)Checked-in patient list\n 2)Treated patient list \n 3)Add symptoms\n 4) Add severity\n 5) Add assessment\n 6)Go Back\nchoice:");
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
            }
            
            if (choice < 1 || choice > 6) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 6);
    	}
    	catch (NumberFormatException e) {
            System.out.println("Invalid selection. Please enter Numbers.");
        }
        return choice;
    }
    
	public void getFacility() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT SD.FacilityID FROM Staff_T S INNER JOIN ServiceDepartment_T SD ON SD.DepartmentID = S.Primary_DepartmentID WHERE S.EmployeeID = '"+this.staffid+"'");
		while (rs.next()) {
			this.FacilityID = rs.getInt("FacilityID");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }	
	}	
    
    public void performStaffAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //Check in patient list
            	getFacility();
            	System.out.println("**************\n*Staff Process Patient Page*\n**************");
            	StaffProcessPatient spp = new StaffProcessPatient(this.staffid, this.FacilityID);
            	spp.runStaffProcessPatient();
            	break;
            case 2://Treated patient list
            	System.out.println("Treated patient list");
            	System.out.println("**************\n*Staff Check-out Patient Page*\n**************");
            	StaffTreatPatient sas = new StaffTreatPatient(this.staffid);
            	sas.runStaffTreatPatient();
            	break;
            case 3://Add Symptoms
            	System.out.println("Add Symptoms");
            	System.out.println("**************\n*Staff Add Symptoms Page*\n**************");
            	StaffAddSymptoms sasa = new StaffAddSymptoms(this.staffid);
				try {
					sasa.runStaffAddSymptoms();
				} catch (SQLException e) {
					e.printStackTrace();
				}
                break;
            case 4://Add severity scale
            	System.out.println("Add severity scale");
            	System.out.println("**************\n*Staff Add severity Page*\n**************");
            	StaffAddseverity sev = new StaffAddseverity(this.staffid);
            	sev.runStaffAddseverity();
                break;
            case 5://Add assessment scale
            	System.out.println("Add assessment scale");
            	System.out.println("**************\n*Staff Add Assesment Page*\n**************");
            	StaffAddAssessment sa = new StaffAddAssessment(this.staffid);
            	sa.runStaffAddAssessment();
                break;
            case 6://Go Back
            	System.out.println("**************\n*Sign in Page*\n**************");
            	SignIn s = new SignIn();
            	s.runSignIn();
            default:
                System.out.println("Error in staff switch block");
        }
    }

}

