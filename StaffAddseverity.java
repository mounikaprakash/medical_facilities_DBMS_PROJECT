import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffAddseverity {
	public int choice;
	public String severityType;
	public String severityValue;
	public String symptomSeverity;
	public String staffid;
	public String scaleName;
	public String scaleValue;
	public int count = 0;
	
	StaffAddseverity(String id){
		this.staffid = id;
	}
	
	public void runStaffAddseverity() {
    		printStaffAddseverity();
            do {
            this.choice = getStaffAddseverityData();
            try {
				performStaffAddseverityAction(this.choice);
			} catch (SQLException e) {
				e.printStackTrace();
			}
            }while(this.choice != 2 );
    }

	public void printStaffAddseverity() {
		
    }	
    
    public int getStaffAddseverityData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        if(this.count==0) {
	        do {
	        System.out.print("Enter Severity Scale Name:");
	        this.scaleName = keyboard.nextLine();
	        }while(!validScaleName());
        }
        this.count = 1;
        do {
            System.out.print("Enter 1)There’s another level for this scale\n 2)No more levels. Go Back\nchoice:");
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
    		System.out.print("Error in Staff adds severity choice data....\n"+e);
    	}
        return choice;
    }
    
	public boolean validScaleName() {
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SEVERITY_SCALE_T WHERE Scale_Type = '"+this.scaleName+"'");
		while (rs.next()) {
		    s = rs.getInt("COUNT");
		}

        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
        	  System.out.println("**Exception occured**\n try again");
        	  runStaffAddseverity();
        }
	    if(s > 0) {
	    	System.out.println("Scale Value is already present. Please try again");
	    	return false;
	    }
	    else
	    	return true;
	}
    
    public boolean insertSeveritydata() throws SQLException {
    	boolean flag = false;
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("INSERT INTO SEVERITY_SCALE_T(SCALE_VALUE,SCALE_TYPE) VALUES ('"+this.scaleValue+"','"+this.scaleName+"')");
            stmt.close();
            conn.close();
            flag = true;
            }catch(SQLException e) {
          	  System.out.println("**Exception occured**\n try again");
          	  runStaffAddseverity();
          }
         	return flag;
    }
       
    public void performStaffAddseverityAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //There is another level for this scale
                Scanner keyboard = new Scanner(System.in);
                System.out.print("Enter Severity Scale Value:");
                this.scaleValue = keyboard.nextLine();
            	System.out.println("**************\n*Recording Severity Data*\n**************");
            	insertSeveritydata();
            	
            	break;
            case 2://No more levels Go Back
            	System.out.println("**************\n*Staff Menu Page*\n**************");
              	StaffMenu sa = new StaffMenu(this.staffid);//Pass staff id here
                sa.runStaffMenu();
            	break;
            default:
                System.out.println("Error in staff add severity switch block");
        }
    }

}


