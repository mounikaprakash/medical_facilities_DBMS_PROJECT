import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class SignIn {
	public String lastname;
	public int facilityid;
	public String dob;
	public String city;
	public int choice;
	public String type;
	public String patientid;
	public String employeeid;
	
    public void runSignIn() throws SQLException {
    		System.out.println("Please enter following details to sign in");
        	//printSignInMenu();
            this.choice = getSignInData();
            if(this.choice == 1) {
                if(this.type.contentEquals("yes")) {
	            	if(validatePatientSignInData())
	            		performSignInAction("patient");
	            	else {
	            		System.out.println("Sign in Incorrect..\nPlease Enter Again");
	            		runSignIn();
	            	}
                }
                else {
	            	if(validateStaffSignInData())
	            		performSignInAction("staff"); 
	            	else {
	            		System.out.println("Sign in Incorrect..\nPlease Enter Again");
	            		runSignIn();
	            	}
                }
            }
            if(this.choice == 2) {
            	MainMenu menu = new MainMenu();
                menu.runMenu();
            }
            

    }

    public boolean validatePatientSignInData() {
    	String s = "";
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
            String Query = "SELECT PATIENTID FROM Patient_T WHERE LASTNAME = '"+ this.lastname+"' AND CITYNAME = '"+ this.city+"' AND TO_DATE(DOB) = '"+ this.dob+"'";
    		ResultSet rs = stmt.executeQuery(Query);
    		while (rs.next()) {
    		    s = rs.getString("PATIENTID");
    		}
            rs.close();
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    	if(!s.isEmpty()) {
    		this.patientid = s;
    		return true;
    	}
    	else {
    		return false;
    	}
		
	}

    public boolean validateStaffSignInData() throws SQLException {
    	String s = "";
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	try {
    		conn = JDBC.getConnection();
            stmt = conn.createStatement();
    		rs = stmt.executeQuery("SELECT employeeid FROM STAFF_T WHERE STAFFNAME = '"+ this.lastname+"' AND CITYNAME = '"+ this.city+"' AND TO_DATE(DOB) = '"+ this.dob+"'");
    		while (rs.next()) {
    		    s = rs.getString("employeeid");
    		    this.employeeid = s;
    		}
            } catch(Throwable oops) {
                oops.printStackTrace();
            } finally {
                rs.close();
                stmt.close();
                conn.close();
            }
    	if(!s.isEmpty()) {
    		this.employeeid = s;
    		return true;
    	}
    	else {
    		return false;
    	}
		
	}
    
	public void printSignInMenu() throws SQLException {
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
        try {
		conn = JDBC.getConnection();
        stmt = conn.createStatement();
		rs = stmt.executeQuery("SELECT FACILITYID,FACILITYNAME FROM MedicalFacility_t");
		System.out.println("FACILITYID   FACILITYNAME");
		while (rs.next()) {
		    int s = rs.getInt("FACILITYID");
		    String n = rs.getString("FACILITYNAME");
		    System.out.println(s + "         " + n);
		}
        } catch(Throwable oops) {
            oops.printStackTrace();
        } finally {
            rs.close();
            stmt.close();
            conn.close();
        }
    	
    }	
    
    public int getSignInData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Enter Last name: ");
        this.lastname = keyboard.nextLine();
        System.out.print("Enter Date of Birth: ");
        this.dob = keyboard.nextLine(); 
        System.out.print("Enter City: ");
        this.city = keyboard.nextLine();
        System.out.print("Enter yes for Patient: ");
        this.type = keyboard.nextLine(); 
        do {

            System.out.print("Enter 1)Sign-In 2)Go Back\nchoice:");
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }catch (NumberFormatException e) {
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
       
    public boolean validFacilityID(int facilityid2) {
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM MedicalFacility_t WHERE FACILITYID = '"+facilityid2+"'");
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
    
    public boolean isMedicalStaff(String staffid) {
    	int s=0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM Staff_t S LEFT JOIN Staff_Secondary_Dept_T SSD ON S.EmployeeID = SSD.EmployeeID WHERE (S.DESIGNATION = 'MEDICAL' OR SSD.Secondary_DepartmentID IN (SELECT DepartmentID FROM ServiceDepartment_T WHERE DeptType = 'MEDICAL'))  AND S.EmployeeID = '"+staffid+"'");
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

	public void performSignInAction(String value) throws SQLException {
      System.out.println("Sign in Successful...");
      if(value == "patient") {
      	System.out.println("**************\n*Patient Menu Page*\n**************");
      	Patient p = new Patient(this.patientid);//Pass patient id here
        p.runPatientMenu();
      }
      else if(value == "staff") {
    	  if(isMedicalStaff(this.employeeid)) {
	        	System.out.println("**************\n*Staff Menu Page*\n**************");
	          	StaffMenu s = new StaffMenu(this.employeeid);//Pass staff id here
	            s.runStaffMenu();
    	  }
    	  else {
	        	System.out.println("\n************Invalid Access to Staff Page. Only Medical Staff has access***********\n");
	        	
	        	System.out.println("\n\nRedirecting to Main Menu\n\n");
	        	MainMenu menu = new MainMenu();
	            menu.runMenu();
    	  }
          }
    }

}
