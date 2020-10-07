import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

public class SymptomMetaData {
	public String Symptom;
	public String bodypart;
	public String duration;
	public String reoccurring;
	public String severity;
	public String CheckID;
	public String cause;
	public String scaletype;
	public String symptomCode;
	public String scalevalue;
	public String description;
    public SymptomMetaData(String symptomName, String CheckID, String description) {
		this.Symptom = symptomName;
		this.CheckID = CheckID;
		this.description = description;
	}
	public void runSymptomMetaData() {
    	getSymptomCodeandScaletype(this.Symptom);
		System.out.println("Displaying Symptoms Metadata");
    	getSymptomsMetaData();
    	//insert data into symptommetadata table
    	try {
			insertSymptomsMetadata();
		} catch (SQLException e) {
			System.out.println("Exception occured. Try entering again");
        	runSymptomMetaData();
		}
    }
	
    public boolean insertSymptomsMetadata() throws SQLException {
    	boolean flag = false;
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
            String Query = "INSERT INTO CHECKIN_SYMPTOMS_METADATA_T(SYMPTOM_CODE,CHECKID,DURATION,ISFIRSTOCCURANCE,INCIDENT,SCALE_VALUE, DESCRIPTION, bodypartname) VALUES ('"+this.symptomCode+"','"+this.CheckID+"',"+this.duration+",'"+this.reoccurring+"','"+this.cause+"','"+this.severity+"','"+this.description+"','"+this.bodypart+"')";
    		stmt.executeUpdate(Query);
            stmt.close();
            conn.close();
            flag = true;
            }catch(SQLException e) {
          	  System.out.println("**Caught Exception**\n try again"+e);
          }
         	return flag;
    }
    
    public void getSymptomCodeandScaletype(String symptomname) {
	        try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT symptom_code,scale_type FROM Symptoms_T WHERE symptom_name = '"+symptomname+"'");
			while (rs.next()) {
				this.symptomCode = rs.getString("symptom_code");
				this.scaletype = rs.getString("scale_type");
				}
	        rs.close();
	        stmt.close();
	        conn.close();
	        } catch(Throwable oops) {
	            oops.printStackTrace();
	        }
    }
    
    public void printSeverityScaleRange() {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT scale_value, scale_type FROM severity_scale_t WHERE scale_type = '"+this.scaletype+"'");
		System.out.println("scale_type   scale_value");
		while (rs.next()) {
			String s1 = rs.getString("scale_type");
			String s2 = rs.getString("scale_value");
			System.out.println(s1+ "       " + s2);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
}
    
    public void getSymptomsMetaData() {
    	try {
        Scanner keyboard = new Scanner(System.in);
        	System.out.print("Enter following details:\n 1. Body part\n 2. Duration \n 3. Reoccuring?\n 4.Severity\n 5. Cause(Inclident)\n");
        	if(!validBodyPart()) {
                System.out.print("Enter Body Part: ");
                this.bodypart = keyboard.nextLine(); 
        	}
        	else {
                this.bodypart = getBodyPart(this.Symptom); 
        	}

            System.out.print("Enter Duration: ");
            this.duration = keyboard.nextLine(); 
            System.out.print("Enter Reoccurring?(Y/N): ");
            this.reoccurring = keyboard.nextLine(); 
            printSeverityScaleRange();
            System.out.print("Enter Severity scale_value: ");
            this.severity = keyboard.nextLine(); 
            System.out.print("Enter Cause(Incident): ");
            this.cause = keyboard.nextLine(); 
    	}
    	catch(Exception e) {
    		System.out.print("Error in get Symptoms Meta data....\n"+e);
    	}
    }
    
    public boolean validBodyPart() {
    	int s= 0;
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS COUNT FROM SYMPTOM_BODYPART_T SB INNER JOIN BODYPARTS_T B ON SB.CODE = B.CODE INNER JOIN SYMPTOMS_T S ON S.SYMPTOM_CODE = SB.SYMPTOM_CODE WHERE SYMPTOM_NAME = '"+this.Symptom+"'");
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
    
    public String getBodyPart(String symptomname1) {
    	String s= "";
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT DISTINCT B.BODYPARTNAME FROM SYMPTOM_BODYPART_T SB INNER JOIN BODYPARTS_T B ON SB.CODE = B.CODE INNER JOIN SYMPTOMS_T S ON S.SYMPTOM_CODE = SB.SYMPTOM_CODE WHERE SYMPTOM_NAME = '"+this.Symptom+"'");
		while (rs.next()) {
			s = rs.getString("BODYPARTNAME");
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(SQLException e) {
        	System.out.println("**Caught Exception in getBodyPart**\n try again"+e);
        	runSymptomMetaData();
        	
        }
        return s;
    }
}
