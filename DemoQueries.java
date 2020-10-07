import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DemoQueries {

	public int choice;
	
    public void runDemoQueries() {
        	printDemoQueriesMenu();
            do {
            	this.choice = getDemoQueriesData();
                System.out.println("\n\n");
                performDemoQueriesAction(this.choice);
                System.out.println("\n\n");
            }while(this.choice != 7);

    }

	public void printDemoQueriesMenu() {
        //System.out.print("Please enter following details: ");
    }	
    
    public int getDemoQueriesData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.println("-----List of Demo Queries-----");
            System.out.println("1. Find all patients that were discharged but had negative experiences at any facility, list their names, facility, check-in date, discharge date and negative experiences.");
            System.out.println("2. Find facilities that did not have a negative experience for a specific period (to be given).");
            System.out.println("3. For each facility, find the facility that is sends the most referrals to.");
            System.out.println("4. Find facilities that had no negative experience for patients with cardiac symptoms.");
            System.out.println("5. Find the facility with the most number of negative experiences (overall i.e. of either kind)");
            System.out.println("6. Find each facility, list the patient encounters with the top five longest check-in phases (i.e. time from begin check-in to when treatment phase begins (list the name of patient, date, facility, duration and list of symptoms).");	
            System.out.println("7. Go Back");	
            System.out.print("Enter your choice: ");

            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
                //getDemoQueriesData();
            }
            
            if (choice < 1 || choice > 7) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 7);
    	}
    	catch (NumberFormatException e) {
            System.out.println("Invalid selection. Please enter Numbers.");
        }
        return choice;
    }
    
	public void runQuery1(String Query) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		System.out.println("FirstName     facilityname     checkin_starttime     checkout_time     TextDescription");
		while (rs.next()) {
		    String a = rs.getString("FirstName");
		    String b = rs.getString("facilityname");
		    String c = rs.getString("checkin_starttime");
		    String d = rs.getString("checkout_time");
		    String e = rs.getString("TextDescription");
		    System.out.println(a + "     " + b+ "     " + c+ "     " + d+ "     " + e);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    		
}
	
	public void runQuery2(String Query) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
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
	
	public void runQuery3(String Query) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		System.out.println("FacilityID");
		while (rs.next()) {
		    int s = rs.getInt("FACILITYID");
		    System.out.println(s);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    		
}
	
	public void runQuery4(String Query) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		System.out.println("FacilityName");
		while (rs.next()) {
		    String n = rs.getString("FacilityName");
		    System.out.println(n);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    		
}
	
	public void runQuery5(String Query) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		System.out.println("FacilityName");
		while (rs.next()) {
		    String n = rs.getString("FACILITYNAME");
		    System.out.println(n);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    		
}   
	
	public void runQuery6(String Query) {
        try {
		Connection conn = JDBC.getConnection();
        Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(Query);
		System.out.println("Firstname              Lastname              Checkin_starttime              Facilityname              Duration              Symptom_name");
		while (rs.next()) {
		    String a = rs.getString("Firstname");
		    String b = rs.getString("Lastname");
		    String c = rs.getString("Checkin_starttime");
		    String d = rs.getString("Facilityname");
		    float 	e  = rs.getFloat("Duration");
		    String f = rs.getString("Symptom_name");
		    System.out.println(a + "                   " + b+ "                " + c+ "              " + d+ "              " + e+ "                " + f);
		}
        rs.close();
        stmt.close();
        conn.close();
        } catch(Throwable oops) {
            oops.printStackTrace();
        }
    		
}
	
    public void performDemoQueriesAction(int choice) {
    	String Query = "";
        switch (choice) {
            case 1://Query 1
            	System.out.println("Query 1");
            	Query = "SELECT FirstName,facilityname, checkin_starttime, checkout_time, TextDescription FROM NEGATIVE_EXPERIENCE_T N  INNER JOIN OUTCOME_REPORT_T OT ON N.CHECKID = OT.CHECKID INNER JOIN Checkin_T C  ON OT.CHECKID = C.CHECKID INNER JOIN Patient_t P ON P.Patientid = C.Patientid INNER JOIN MedicalFacility_T F ON F.FacilityID = C.FacilityID";
            	runQuery1(Query);
            	break;
            case 2://Query 2
            	System.out.println("Query 2");
                Scanner keyboard = new Scanner(System.in);
            	System.out.println("Enter var1");
            	String var1 = keyboard.nextLine(); 
            	System.out.println("Enter var2");
            	String var2 = keyboard.nextLine(); 
            	Query = "select m.facilityname from checkin_t c, medicalfacility_t m, negative_experience_t n, outcome_report_t o where m.facilityId=c.facilityid AND c.checkid=o.checkid AND c.checkid not in (select checkid from negative_experience_t) and c.treatment_endtime between '"+var1+"' and '"+var2+"'";
            	runQuery2(Query);
            	break;
            case 3://Query 3
            	System.out.println("Query 3");
            	Query = "select r.facilityid from REFERRAL_T r where r.facilityid in (select FACILITYID from (select count(r.FACILITYID) as count1,c.FACILITYID from REFERRAL_T r, CHECKIN_T c where c.CHECKID=r.CHECKID  group by c.FACILITYID) where count1 = (SELECT max(count1) from (select count(r.FACILITYID) as count1,c.FACILITYID from REFERRAL_T r, CHECKIN_T c where c.CHECKID=r.CHECKID  group by c.FACILITYID)))";
            	runQuery3(Query);
            	break;
            case 4://Query 4
            	System.out.println("Query 4");
            	Query = "select m.facilityname from checkin_t c, medicalfacility_t m, negative_experience_t n, outcome_report_t o,CHECKIN_SYMPTOMS_METADATA_T csm, SYMPTOMS_T s where m.facilityId=c.facilityid AND c.checkid=o.checkid AND c.checkid=csm.checkid AND s.SYMPTOM_CODE=csm.SYMPTOM_CODE AND s.SYMPTOM_NAME='cardiac' AND c.checkid not in (select checkid from negative_experience_t)";
            	runQuery4(Query);
            	break;
            case 5://Query 5
            	System.out.println("Query 5");
            	Query = "select m.facilityname from medicalfacility_t m, checkin_t c, negative_experience_t ne where m.facilityId=c.facilityid and c.checkid=ne.checkid and c.facilityid in (select max(cfid) from (select count(cin.facilityid) cfid from negative_experience_t ne, checkin_t cin where ne.checkid=cin.checkid group by cin.facilityid))";
            	runQuery5(Query);
            	break;
            case 6://Query 6
            	System.out.println("Query 6");
            	Query = "select firstname,lastname, checkin_starttime,facilityname, duration,symptom_name from (select firstname,lastname, checkin_starttime,facilityname, (treatment_starttime - treatment_endtime) duration,symptom_name from medicalfacility_t m, checkin_t c, patient_t p, symptoms_t s,checkin_symptoms_metadata_t cs where m.facilityId = c.facilityId and p.patientid = c.patientid and c.checkid = cs.checkid and cs.symptom_code = s.symptom_code order by duration desc) where rownum <= 5";
            	runQuery6(Query);
            	break;
            case 7://Go Back
            	System.out.println("Going back....");
            	MainMenu menu = new MainMenu();
                menu.runMenu();
            	break;
            default:
                System.out.println("Error in demo queries switch block");
        }
    }

}

