import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffAddAssessment {
	public int choice;
	public String severityType;
	public String severityValue;
	public String symptomSeverity;
	public String symptomName;
	public String symptomCode = "";
	public String staffid;
	public String bodyPartCode;
	public String comparison;
	public String assessmentid;

	StaffAddAssessment(String id){
		this.staffid = id;
	}
	
	public void runStaffAddAssessment() throws SQLException {
		this.assessmentid = getAssessmentID();
    	do
    	{
    		printStaffAddAssessment();
            this.symptomName = getStaffAddAssessmentData();
            if(!this.symptomName.equals("priority")) {
	            getSymptomCode();
	            if(!this.symptomCode.equals("")) {
		    		printBodyPartsList();
		            this.bodyPartCode = getBodyPartCode();
		            printSeverityScaleType();
		            this.severityType = getSeverityScaleType();
		            printSeverityScaleValue();
		            this.severityValue = getSeverityScaleValue();
		            Scanner keyboard = new Scanner(System.in);
		            System.out.println("Enter Comparison operator like >, <, =, !=, <=, >=");
		            this.comparison = keyboard.nextLine();
		            if(!insertAssessmentRule()) {
		            	System.out.println("Exception occured while inserting into Assessment table\n try again");
		            	runStaffAddAssessment();
		            }
	            }
            }
    	}while(!this.symptomName.equals("priority"));
    	
        this.choice = getStaffAddPriorityData();  //Enter High
        performStaffAddAssessmentAction(this.choice);
        //Go Back to staff menu
        System.out.println("**************\n*Staff Menu Page*\n**************");
        StaffMenu s = new StaffMenu(this.staffid);//Pass staff id here
        s.runStaffMenu();
    }
    public boolean insertAssessmentRule() throws SQLException {
    	boolean flag = false;
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("INSERT INTO ASSESSMENT_RULES_T(ASSESSMENT_ID, SYMPTOM_CODE, CODE, COMPARISION, SEVERITY_VALUE) VALUES("+this.assessmentid+",'"+this.symptomCode+"','"+this.bodyPartCode+"','"+this.comparison+"','"+this.severityValue+"')");
            stmt.close();
            conn.close();
            flag = true;
            }catch(SQLException e) {
          	  System.out.println("**Exception Occured in inserting assessment rule**\n try again"+e);
          }
         	return flag;
    }
	
	private void printSeverityScaleType() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT SCALE_TYPE FROM SYMPTOMS_T WHERE SYMPTOM_CODE = '"+this.symptomCode+"'");
		System.out.println("SCALE_TYPE");
		while (rs.next()) {
		    String n = rs.getString("SCALE_TYPE");
			System.out.println(n);
		}

        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	
	}

	private void printSeverityScaleValue() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT SCALE_VALUE FROM SEVERITY_SCALE_T WHERE SCALE_TYPE = '"+this.severityType+"'");
		System.out.println("SCALE_VALUE");
		while (rs.next()) {
		    String n = rs.getString("SCALE_VALUE");
			System.out.println(n);
		}

        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	
	}
	public void printStaffAddAssessment() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT SYMPTOM_NAME FROM SYMPTOMS_T");
		System.out.println("SYMPTOM_NAME");
		while (rs.next()) {
		    String n = rs.getString("SYMPTOM_NAME");
		}
		System.out.println("priority");
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	
    }
	
	public void getSymptomCode() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT SYMPTOM_CODE FROM SYMPTOMS_T WHERE SYMPTOM_NAME = '"+this.symptomName+"'");
		System.out.println("SYMPTOM_CODE");
		while (rs.next()) {
		    this.symptomCode = rs.getString("SYMPTOM_CODE");
		}
		System.out.println("priority");
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	
    }
	
	public void printBodyPartsList() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT BODYPARTNAME, CODE  FROM BODYPARTS_T");
		System.out.println("BODYPARTNAME     CODE");
		while (rs.next()) {
		    String n = rs.getString("BODYPARTNAME");
		    String m = rs.getString("CODE");
			System.out.println(n+"       "+m);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	
    }
	
	public String getAssessmentID() {
		String n = "";
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT RULE_PRIORITY_S.NEXTVAL AS value from dual");
		while (rs.next()) {
		    n = rs.getString("value");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    	return n;
    }
	
    public int getStaffAddPriorityData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter 1)High\n 2)Normal 3)Quanrantine \nchoice:");
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
    		System.out.print("Error in Staff adds priority choice data....\n"+e);
    	}
        return choice;
    }
    
    public String getStaffAddAssessmentData() {
		String SymptomName = null;
    	try {

    		Scanner keyboard = new Scanner(System.in);
            System.out.print("Enter symptom name or priority: ");
            SymptomName = keyboard.nextLine(); 
    	}
    	catch(Exception e) {
    		System.out.print("Error in StaffAddAssessmentData....\n"+e);
    	}
        return SymptomName;
    }
    
    
    
    public String getBodyPartCode() {
		String bodypartcode = null;
    	try {

    		Scanner keyboard = new Scanner(System.in);
            System.out.print("Enter body part code: ");
            bodypartcode = keyboard.nextLine(); 
    	}
    	catch(Exception e) {
    		System.out.print("Error in get BodyPart Code....\n"+e);
    	}
        return bodypartcode;
    }
    
    public String getSeverityScaleType() {
		String ScaleType = null;
    	try {

    		Scanner keyboard = new Scanner(System.in);
            System.out.print("Enter severity scale type from above: ");
            ScaleType = keyboard.nextLine(); 
    	}
    	catch(Exception e) {
    		System.out.print("Error in get severity scale type....\n"+e);
    	}
        return ScaleType;
    }
    
    public String getSeverityScaleValue() {
		String ScaleValue = null;
    	try {

    		Scanner keyboard = new Scanner(System.in);
            System.out.print("Enter severity scale value from above: ");
            ScaleValue = keyboard.nextLine(); 
    	}
    	catch(Exception e) {
    		System.out.print("Error in get severity scale value....\n"+e);
    	}
        return ScaleValue;
    }
       
   public boolean insertRulePriority(String value) {
	   	boolean flag = false;
	   	try {
	   		   Connection conn = JDBC.getConnection();
	           Statement stmt = conn.createStatement();
	   		   stmt.executeUpdate("INSERT INTO RULE_PRIORITY_T(ASSESSMENT_ID, PRIORITY) VALUES("+this.assessmentid+",'"+value+"')");
	           stmt.close();
	           conn.close();
	           flag = true;
	           }catch(SQLException e) {
	         	  System.out.println("**Exception Occured in inserting rule priority**\n try again"+e);
	         }
	    return flag;
   }
    
    public void performStaffAddAssessmentAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //Enter High          	
            	System.out.println("**************\n*Inserting Priority as High*\n**************");
            	insertRulePriority("High");
            	break;
            case 2://Enter Normal
            	System.out.println("**************\n*Inserting Priority as Normal*\n**************");
            	insertRulePriority("Normal");
            	break;
            case 3://Enter Quarantine
            	System.out.println("**************\n*Inserting Priority as Quarantine*\n**************");
            	insertRulePriority("Quarantine");
            	break;
            default:
                System.out.println("Error in staff add assessment switch block");
        }
    }

}


