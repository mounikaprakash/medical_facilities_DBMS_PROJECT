import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffAddSymptoms {
	public int choice;
	public String symptomName;
	public String symptomBodyPart;
	public String symptomSeverity;
	public String staffid;

	StaffAddSymptoms(String id){
		this.staffid = id;
	}
	
	
	public void runStaffAddSymptoms() throws SQLException {
    		printStaffAddSymptoms();
            this.choice = getStaffAddSymptomsData();
            performStaffAddSymptomsAction(this.choice);
    }

	public void printStaffAddSymptoms() {

    }	
    
    public int getStaffAddSymptomsData() throws SQLException {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
        	do
        	{
            System.out.print("Enter symptomName: ");
            this.symptomName = keyboard.nextLine();            	
            //Validate temperature Data using SP
            System.out.print("Enter Symptom Body Part (leave blank if not exclusively associated to one body part)");
            this.symptomBodyPart = keyboard.nextLine(); 
            getSeverityScale();
            System.out.print("Enter Symptom Severity (select severity to choose from above, leave blank for no severity associated)");
            this.symptomSeverity = keyboard.nextLine(); 
            }while(!validateSymptomName());// Check if symptom is already entered
            //Add Severity Function
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
    
    private boolean validateSymptomName() throws SQLException {
    	int count = 0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM symptoms_t WHERE Symptom_Name = '"+this.symptomName+"' AND  scale_type = '"+this.symptomSeverity+"'");
		while (rs.next()) {
			count = rs.getInt("COUNT");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
            runStaffAddSymptoms();
        }
        if(count > 0)
        	return false;
        else
        	return true;
	}


	public boolean addNewSymptoms() throws SQLException {
    	boolean flag = false;
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("INSERT INTO SYMPTOMS_T(SYMPTOM_CODE,SYMPTOM_NAME,SCALE_TYPE) VALUES (CONCAT('SYM', CAST(SYMPTOMS_S.NEXTVAL AS VARCHAR2(6))),'"+this.symptomName+"','"+this.symptomSeverity+"')");
            stmt.close();
            conn.close();
            flag = true;
	        }catch(SQLIntegrityConstraintViolationException e) {
	        	  System.out.println("**Integrity Constraint violated**\n try again");
	        	  runStaffAddSymptoms();
	        }
	       	return flag;
  }
    
    
    public void getSeverityScale() {
    	String scaletype = "";
	        try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DISTINCT scale_type FROM severity_scale_t");
			System.out.println("**ScaleType**");
			while (rs.next()) {
				scaletype = rs.getString("scale_type");
				System.out.println(" "+scaletype);
			}
	        rs.close();
	        stmt.close();
	        conn.close();
	        } catch(Throwable oops) {
	            oops.printStackTrace();
	        }
    }
    
    public void performStaffAddSymptomsAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //Record time, trigger assessment, display priority and go back
            	
            	System.out.println("**************\n*Recording Symptom Data*\n**************");
            	addNewSymptoms();
            	
            	System.out.println("**************\n*Staff Menu Page*\n**************");
              	StaffMenu s = new StaffMenu(this.staffid);//Pass staff id here
                s.runStaffMenu();
            	break;
            case 2://Go Back
            	System.out.println("**************\n*Staff Menu Page*\n**************");
              	StaffMenu sw = new StaffMenu(this.staffid);//Pass staff id here
                sw.runStaffMenu();
            	break;
            default:
                System.out.println("Error in staff add symptoms switch block");
        }
    }

}


