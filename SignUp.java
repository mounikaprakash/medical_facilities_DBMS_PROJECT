import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class SignUp {
	public String lastname;
	public String firstname;
	public int facilityid;
	public String dob;
	public int doornumber;
	public String streetname;
	public String city;
	public String phonenumber;
	public int choice;
	
    public void runSignUp() {
    		System.out.println("Please enter following details to sign up");
        	printSignUpMenu();
            this.choice = getSignUpData();
            if(this.choice == 1) {
            	if(validateSignUpData()) {
            		try {
	            		if(performSignUpAction()) {
	                	System.out.println("Sign up Successful... Going to Sign In Page");
	                	System.out.println("**************\n*Sign in Page*\n**************");
	                	SignIn s = new SignIn();
	                	s.runSignIn();
	            		}
            		}    	catch(Exception e) {
                		System.out.print("Error in SignUp data....\n"+e);
                	}
            	}
            	else {
            		System.out.println("Sign Up Incorrect..\nPlease Enter Again");
            		runSignUp();
            	}
            }
            if(this.choice == 2) {
            	MainMenu menu = new MainMenu();
                menu.runMenu();
            }
            

    }

    public boolean validateSignUpData() {
    	int s = 1;
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM Patient_T WHERE LASTNAME = '"+ this.lastname+"' AND  DOB = '"+this.dob+"' AND CITYNAME ='"+this.city+"'");
    		while (rs.next()) {
    		    s = rs.getInt("COUNT");
    		}
            rs.close();
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
    	if(s == 0)
    		return true;
    	else
    		return false;

	}

	public void printSignUpMenu() {

    }	
    
    public int getSignUpData() {
    	int choice =0 ;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter First name: ");
            this.firstname = keyboard.nextLine(); 
            System.out.print("Enter Last name: ");
            this.lastname = keyboard.nextLine(); 
            System.out.print("Enter Date of Birth: ");
            this.dob = keyboard.nextLine(); 
            System.out.print("Enter DoorNumber: ");
            this.doornumber = Integer.parseInt(keyboard.nextLine()); 
            System.out.print("Enter Streetname: ");
            this.streetname = keyboard.nextLine(); 
            System.out.print("Enter City: ");
            this.city = keyboard.nextLine(); 
            System.out.print("Enter Phone number: ");
            this.phonenumber = keyboard.nextLine(); 
            System.out.print("Enter 1)Sign-Up 2)Go Back\nchoice:");
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
    	catch(Exception e) {
    		System.out.print("Error in SignUp data....\n"+e);
    	}
        return choice;
    }
       
    public boolean performSignUpAction() throws SQLException {
    	boolean flag = false;
       	try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		stmt.executeUpdate("INSERT INTO Patient_T (FIRSTNAME,LASTNAME,PATIENTID,PHONE,DOB,DOORNO,STREETNAME,CITYNAME) VALUES('"+ this.firstname+"','"+ this.lastname+"',TO_CHAR(PATIENT_S.NEXTVAL),'"+this.phonenumber+"','"+this.dob+"','"+ this.doornumber+"','"+this.streetname+"','"+this.city+"')");
        stmt.close();
        conn.close();
        flag = true;
        }catch(SQLException e) {
        	  System.out.println("**Exception Occured**\n try again" + e);
        	  runSignUp();
        }
       	return flag;
    }

}
