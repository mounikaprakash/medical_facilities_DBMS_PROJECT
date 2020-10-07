import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StaffPatientReport {
	public int choice;
	
	public String patientid;
	public String staffid;
	public String dischargeStatus;
	public String treatment;
	public String checkid;
	public String referrerid;
	public String facilityid;
	public String reason1;
	public String reasonDescription1;
	public String service1;
	public String reason2;
	public String reasonDescription2;
	public String service2;
	public String reason3;
	public String reasonDescription3;
	public String service3;
	public String neCode;
	public String neDescription;


	StaffPatientReport(String staffId, String patientId, String checkId){
		this.staffid = staffId;
		this.patientid = patientId;
		this.checkid = checkId;
	}
	
    public void runStaffPatientReport() throws SQLException {
        	printStaffPatientReportMenu();
            this.choice = getStaffPatientReportData();
            performStaffPatientReportAction(this.choice);
    }

	public void printStaffPatientReportMenu() {
		System.out.println("\n******** Staff Patient Report page ******");
		System.out.println("Please make a selection........");
        System.out.println("1) Discharge Status");
        System.out.println("2) Referral Status");
        System.out.println("3) Treatment");
        System.out.println("4) Negative Experience");
        System.out.println("5) Go back");
        System.out.println("6) Submit");
    }	
    
    public int getStaffPatientReportData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter your choice: ");
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
    	catch(Exception e) {
    		System.out.print("Error in Staff patient report choice data....\n"+e);
    	}
        return choice;
    }
    
    public void getDischargeStatus() throws SQLException {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
            }
            
            if (choice < 1 || choice > 4) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 4);
    	}
    	catch(Exception e) {
    		System.out.print("Error in Discharge Status choice data....\n"+e);
    	}
    	switch(choice) {
    	case 1://Successful treatment
    		this.dischargeStatus = "TREATED SUCCESSFULLY";
    		runStaffPatientReport();
    		break;
    	case 2://deceased
    		this.dischargeStatus = "DECEASED";
    		runStaffPatientReport();
    		break;
    	case 3://referred
    		this.dischargeStatus = "REFERRED";
    		runStaffPatientReport();
    		break;
    	case 4://do nothing
    		runStaffPatientReport();
    		break;
    	}
    }
    
    public void printDischargeMenuAndStoreChoice() throws SQLException {
    	System.out.println("Please make a selection........");
        System.out.println("1) Successful treatment");
        System.out.println("2) Deceased");
        System.out.println("3) Referred");
        System.out.println("4) Go back");
        System.out.println("Enter your choice:");
        //store discharge status
        getDischargeStatus();
    }
    
    public int checkDischargeStatus() {
    	if(this.dischargeStatus == null) {
    		System.out.print("Please select discharge status first\n");
    		return 0;
    	}
    	else if(this.dischargeStatus != "REFERRED") {
    		System.out.print("Discharge status is not 'REFERRED'\n");
    		return 0;
    	}
    	return 1;
    }
    
    public void showReportDetails() {
    	System.out.println("REPORT:	");
    	System.out.println("");
    	System.out.println("--------------------------------------------------");
    	System.out.println(" Patient:		"+this.patientid);
    	System.out.println("--------------------------------------------------");
    	System.out.print(" Discharge Status	");
    	System.out.println(this.dischargeStatus);
    	System.out.println("--------------------------------------------------");
    	if(this.dischargeStatus == "REFERRED") {
    		System.out.print(" Referral Status	");
    		System.out.print("Referred to:	"+this.facilityid);
    		System.out.print("\n			--------------------------");
    		System.out.print("\n			Referred by:	"+this.referrerid);
    		System.out.print("\n			--------------------------");
    		System.out.print("\n			Referral Reasons:");
    		if(this.reason1 != null) {
    			System.out.print("\n			--------------------------");
        		System.out.print("\n			Reason:	"+this.reason1);
        		System.out.print("\n			Service:	"+this.service1);
        		System.out.print("\n			Description:	"+this.reasonDescription1);
    		}
    		if(this.reason2 != null) {
    			System.out.println("");
    			System.out.print("\n			Reason:	"+this.reason2);
        		System.out.print("\n			Service:	"+this.service2);
        		System.out.print("\n			Description:	"+this.reasonDescription2);
    		}
    		if(this.reason3 != null) {
    			System.out.println("");
    			System.out.print("\n			Reason:	"+this.reason3);
        		System.out.print("\n			Service:	"+this.service3);
        		System.out.print("\n			Description:	"+this.reasonDescription3);
    		}	
    	}
    	System.out.print("\n--------------------------------------------------");
    	System.out.print("\n Treatment given	");
    	System.out.println(this.treatment);
    	System.out.println("--------------------------------------------------");
    	System.out.print(" Negative Experience	");
    	System.out.println(this.neDescription);
    	System.out.println("--------------------------------------------------");
    }
    
    public void printMenuOptionsConfirmation() {
    	System.out.println("\n******** This is the report ******** \nSelect an option:");
    	System.out.println("1) Confirm");
    	System.out.println("2) Go back");
    }
    
    public void addToDBReport() {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO OUTCOME_REPORT_T(CHECKID, EMPLOYEEID, TREATMENT_GIVEN, DISCHARGE_STATUS) VALUES('"+this.checkid+"','"+this.staffid+"','"+this.treatment+"','"+this.dischargeStatus+"')";
            int status = stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
         } catch(Throwable oops) {
            oops.printStackTrace();
         }
    }
    
    public void selectOptionsAndExecute() throws SQLException {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
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
    		//System.out.print("Error in choice data....\n"+e);
    	}
    	switch(choice) {
    	case 1://Confirmed. So add to db and redirect to staff menu
    		addToDB();
    		//Redirect to staff menu
        	System.out.println("**************\n*Staff Menu Page*\n**************");
    		StaffMenu sm = new StaffMenu(this.staffid);
    		sm.runStaffMenu();
    		break;
    	case 2://Go back to staff-patient report
    		System.out.println("Redirecting to staff-patient report page");
    		runStaffPatientReport();
    		break;
    	}
    }
    
    public void displayConfirmationPage() throws SQLException{
    	System.out.println("\n********** Staff-Patient Report confirmation **********");
    	showReportDetails();
    	printMenuOptionsConfirmation();
    	selectOptionsAndExecute();
    }
    
    public void performStaffPatientReportAction(int choice) throws SQLException {
    	int i;
        switch (choice) {
            case 1: //Discharge Status
            	System.out.println("\n*************** Discharge Status ***************");
            	//print discharge menu rather than navigating to discharge page and store the choice
            	printDischargeMenuAndStoreChoice();
            	break;
            case 2://Referral Status
            	System.out.println("\n*************** Referral Status ***************");
            	i = checkDischargeStatus();
            	if( i == 1 ) {//open referral status only if the discharge status is referred.
                	runReferralStatus();
            	}else {
            		runStaffPatientReport();
            	}
            	break;
            case 3://Treatment
            	System.out.println("\n*************** Treatment ***************");
            	System.out.print("Enter treatment description:");
            	//Update in database
            	try {
                    Scanner keyboard = new Scanner(System.in);
                    this.treatment = keyboard.nextLine();
                }
            	catch (Exception e) {
                    System.out.println("Error in EnterTreatment Description");
                }
            	runStaffPatientReport();
            case 4://Negative Experience
            	runNegativeExperience();
            	break;
            case 5://Go Back
            	System.out.println("\nGoing back...");
            	System.out.println("\n*************** Treated Patient Page ***************");
            	StaffTreatPatient tpp = new StaffTreatPatient(this.staffid);//Pass staff id here
                tpp.runStaffTreatPatient();
                break;
            case 6://Submit
            	if(this.dischargeStatus == null | this.treatment == null | this.neCode == null) {
            		System.out.println("************** Please make sure the required data is entered ***************");
            		runStaffPatientReport();
            	}	
            	else
            		displayConfirmationPage();
            		//
            	break;
            default:
                System.out.println("Error in staff patient report switch block");
        }
    }
    
  //#################### End of STAFF PATIENT REPORT PAGE #########################
    
    
    //#################### Begin of REFERRAL STATUS PAGE #########################
    
    public void runReferralStatus() throws SQLException {
        	printReferralStatusMenu();
            this.choice = getReferralStatusData();
            performReferralStatusAction(this.choice);           
    }

	public void printReferralStatusMenu() {

    }	
    
    public int getReferralStatusData() {
    	int choice = 0;
    	try {
        Scanner keyboard = new Scanner(System.in);
        do {
            System.out.print("Enter\n 1)Facility id\n 2)Referrer id \n 3)Add reason\n 4)Clear reasons\n 5)Go back\n choice:");
            try{
            	choice = Integer.parseInt(keyboard.nextLine()); 
            }    	catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter Numbers.");
            }
            
            if (choice < 1 || choice > 5) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 1 || choice > 5);
    	}
    	catch(Exception e) {
    		System.out.print("Error in Referral Status choice data....\n"+e);
    	}
        return choice;
    }
    
    public void addToDBReferral() {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO REFERRAL_T(CHECKID, REFERRERID, FACILITYID) VALUES("+"'"+this.checkid+"'"+","+"'"+this.referrerid+"'"+","+this.facilityid+")";
            int status = stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
         } catch(Throwable oops) {
            oops.printStackTrace();
         }
    }
    
    public boolean checkMaxReasons() {
    	if(this.reason1 != null & this.reason2 != null & this.reason3 != null) 
    		return false;
    	else
    		return true;
    }
    
    public void clearReasons() {
    	this.reason1 = null;
    	this.service1 = null;
    	this.reasonDescription1 = null;
    	this.reason2 = null;
    	this.service2 = null;
    	this.reasonDescription2 = null;
    	this.reason3 = null;
    	this.service3 = null;
    	this.reasonDescription3 = null;
    	System.out.println("\nAll the reasons are cleared\n");
    }
       
    public void performReferralStatusAction(int choice) throws SQLException {
        switch (choice) {
            case 1: //Facility ID      	
            	System.out.println("\n*************** Enter Facility ID ***************");
            	try {
                    Scanner keyboard = new Scanner(System.in);
                    this.facilityid = keyboard.nextLine();
                  //Update in referral status
                }    	
            	catch (NumberFormatException e) {
                            System.out.println("Invalid selection. Please enter Numbers.");
                }
            	runReferralStatus();
            	break;
            case 2://Referrer id
            	System.out.println("\n*************** Enter Referrer id ***************");
            	//Update in database
            	try {
                    Scanner keyboard = new Scanner(System.in);
                    this.referrerid = keyboard.nextLine(); 
                    //Update in referral status
                }    	
            	catch (NumberFormatException e) {
                            System.out.println("Invalid selection. Please enter Numbers.");
                }
            	runReferralStatus();
            	break;
            case 3://Reason
            	System.out.println("\n*************** Add reason ***************");
            	//Update in database
            	//redirect to staff patient report
            	boolean i = checkMaxReasons();
            	if(i)
            		runReferralReason();
            	else {
            		System.out.println("\nThree reasons entered already\n");
            		runReferralStatus();
            	}
            case 4://clear reasons
            	clearReasons();
            	runReferralStatus();
            	break;
            case 5://Go Back
            	System.out.println("Going back...");
				try {
					runStaffPatientReport();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	break;
            default:
                System.out.println("Error in Referral Status switch block");
        }
    }
    
  //#################### End of REFERRAL STATUS PAGE #########################
    
    
  //#################### Begin of REFERRAL REASON PAGE #########################
    
    public void runReferralReason() throws SQLException {
        this.choice = getReferralReasonData();
        performReferralReasonAction(this.choice);           
    }

	
	public int getReferralReasonData() {
		int choice = 0;
		try {
	    Scanner keyboard = new Scanner(System.in);
	    do {
	        System.out.print("Enter\n 1)Reason\n 2)Service\n 3)Description\n 4)Go back\n choice:");
	    try{
	    	choice = Integer.parseInt(keyboard.nextLine()); 
	    }    	catch (NumberFormatException e) {
	        System.out.println("Invalid selection. Please enter Numbers.");
	    }
	    
	    if (choice < 1 || choice > 4) {
	        System.out.println("Choice outside of range. Please chose again.");
	    }
	} while (choice < 1 || choice > 4);
	}
	catch(Exception e) {
		System.out.print("Error in Referral Reason choice data....\n"+e);
		}
	    return choice;
	}
	
	public void showServices(){
		try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
	        System.out.println("\n********** Services  **********");
		ResultSet rs = stmt.executeQuery("SELECT * FROM SERVICES_T");
		while (rs.next()) {
		    System.out.println("- "+rs.getString("NAME"));
		}
		System.out.println("- Other");
	        rs.close();
	        stmt.close();
	        conn.close();
	     } catch(Throwable oops) {
	        oops.printStackTrace();
	     }
	}
	
	public void showReasons(){
		System.out.println("1) Service unavailable at time of visit");
		System.out.println("2) Service not present at facility");
		System.out.println("3) Non-payment");
	}
	
	public void addToDBRefReason() {
		try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
	        String sql = "INSERT INTO REFERRAL_REASON_T(CHECKID, REASONCODE, NAME_OF_SERVICE, DESCRIPION) VALUES("+"'"+this.checkid+"'"+","+this.reason1+","+"'"+this.service1+"'" + ","+"'"+this.reasonDescription1+"'" + ")";
	        int status = stmt.executeUpdate(sql);
	        sql = "INSERT INTO REFERRAL_REASON_T(CHECKID, REASONCODE, NAME_OF_SERVICE, DESCRIPION) VALUES("+"'"+this.checkid+"'"+","+this.reason2+","+"'"+this.service2+"'" + ","+"'"+this.reasonDescription2+"'" + ")";
	        status = stmt.executeUpdate(sql);
	        sql = "INSERT INTO REFERRAL_REASON_T(CHECKID, REASONCODE, NAME_OF_SERVICE, DESCRIPION) VALUES("+"'"+this.checkid+"'"+","+this.reason3+","+"'"+this.service3+"'" + ","+"'"+this.reasonDescription2+"'" + ")";
	        status = stmt.executeUpdate(sql);
	        stmt.close();
	        conn.close();
	     } catch(Throwable oops) {
	        oops.printStackTrace();
	     }
	}
	
	public boolean checkReason(String reason) {
		if(this.reason1 != null) {
			if(Integer.parseInt(this.reason1) == Integer.parseInt(reason)) {
				System.out.println("\nReason entered already");
				return false;
			}
		}
		if(reason2 != null) {
			if(Integer.parseInt(this.reason2) == Integer.parseInt(reason)) {
				System.out.println("\nReason entered already");
				return false;
			}
		}
		return true;
	}
	
	public void updateReason(String reason) {
		if(this.reason1 == null) 
			this.reason1 = reason;
		else if(this.reason2 == null )
			this.reason2 = reason;
		else
			this.reason3 = reason;
	}
	
	public void updateService(String service) {
		if(this.service1 == null) 
			this.service1 = service;
		else if(this.service2 == null )
			this.service2 = service;
		else
			this.service3 = service;
	}
	
	public void updateReasonDesc(String reasonDescription) {
		if(this.reasonDescription1 == null) 
			this.reasonDescription1 = reasonDescription;
		else if(this.reasonDescription2 == null )
			this.reasonDescription2 = reasonDescription;
		else
			this.reasonDescription3 = reasonDescription;
	}
	
	public void performReferralReasonAction(int choice) throws SQLException {
		String reason = "";
		String service = "";
		String reasonDescription = "";
		boolean i;
	    switch (choice) {
	        case 1: //Reason     	
	    	System.out.println("\n*************** Reason ***************");
	    	showReasons();
	    	//Update reason in database
	    	System.out.println("Select a reason");
	    	try {
	            Scanner keyboard = new Scanner(System.in);
	            reason = keyboard.nextLine();
	            i = checkReason(reason);
	            if(i) {
	            	updateReason(reason);
	            }
	            else {
	            	runReferralReason();
	            }
	        }
	    	catch (Exception e) {
	                    System.out.println("Error in EnterTreatment Description");
	        }
	    	runReferralReason();	
	    	break;
	    case 2:
	    	//show all the services
	    	showServices();
	    	System.out.println("******** Please enter a service from above *********");
	    	try {
	            Scanner keyboard = new Scanner(System.in);
	            service = keyboard.nextLine();
	            updateService(service);
	        }
	    	catch (Exception e) {
	                    System.out.println("Error in EnterTreatment Description");
	        }
	    	runReferralReason();
	    	break;
	    case 3:
	    	//Enter a description for reason
	    	System.out.println("\n*************** Reason description ***************");
	    	System.out.print("Enter a description for reason");
	    	try {
	            Scanner keyboard = new Scanner(System.in);
	            reasonDescription = keyboard.nextLine();
	            updateReasonDesc(reasonDescription);
	        }    	
	    	catch (Exception e) {
	                    System.out.println("Error in EnterTreatment Description");
	        }
	    	runReferralReason();
	    	break;
	    case 4://Go Back to Referral Status page and commit to db
	    	//addToDB();
	    	System.out.println("Going back...");
	    	runReferralStatus();
	    	break;
	    default:
	        System.out.println("Error in Referral Reason switch block");
	    }
	}
  
    //#################### End of REFERRAL REASON PAGE #########################
	
	
	
	//#################### Begin of NEGATIVE EXPERIENCE PAGE #########################
		
	public void runNegativeExperience() throws SQLException {
		System.out.println("\n*************** Negative Experience ***************");
        this.choice = getNegativeExperienceData();
        performNegativeExperienceAction(this.choice);           
	}
	
	public void showNegativeExperience() {
		//Display menu with codes
		System.out.println("1) Misdiagnosis");
		System.out.println("2) Patient acquired an infection during hospital stay");
	}	
	
	public int getNegativeExperienceData() {
		int choice = 0;
		try {
	    Scanner keyboard = new Scanner(System.in);
	    do {
	        System.out.print("Please make a choice:\n1) Negative Experience code\n2) Go back\n choice:");
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
		System.out.print("Error in Negative Experience choice data....\n"+e);
		}
	    return choice;
	}
	   
	public void addToDBNE() {
		try {
			Connection conn = JDBC.getConnection();
	        Statement stmt = conn.createStatement();
	        //read the checkid from employeeid and patientid
	        String sql = "INSERT INTO NEGATIVE_EXPERIENCE_T(CODE, TEXTDESCRIPTION, CHECKID) VALUES('"+this.neCode+"','"+this.neDescription+"','"+this.checkid+"')";
	        int status = stmt.executeUpdate(sql);
	        stmt.close();
	        conn.close();
	     } catch(Throwable oops) {
	        oops.printStackTrace();
	     }
	}
	
	public void performNegativeExperienceAction(int choice) throws SQLException {
		String description;
		int code;
	    switch (choice) {
	        case 1: //Negative Experience code    	
	        System.out.println("\nPlease enter a choice:");
	    	//Update in database
	    	showNegativeExperience();
	    	System.out.println("Enter choice:");
	    	try {
	            Scanner keyboard = new Scanner(System.in);
	            //code = Integer.parseInt(keyboard.nextLine());
	            this.neCode = keyboard.nextLine();
	        }
	    	catch (NumberFormatException e) {
	                    System.out.println("Invalid selection. Please enter Numbers.");
	        }
	    	if(this.neCode == "1")
	    		this.neDescription = "Misdiagnosis";
	    	else
	    		this.neDescription = "Patient acquired an infection during hospital stay";
	    	runNegativeExperience();
	    	break;
	    case 2://Go Back
	    	System.out.println("Going back...");
	    	runStaffPatientReport();
	    	break;
	    default:
	        System.out.println("Error in Discharge Status switch block");
	    }
	}
	
	//#################### End of NEGATIVE EXPERIENCE PAGE #########################
	
	
	
	//########### COMMIT TO DB ############
	
	public void addToDB() {
		addToDBReferral();
		addToDBRefReason();
		addToDBNE();
		addToDBReport();
		updateTreatmentEndTime();
	}
	
	public void updateTreatmentEndTime() {
    	try {
    		Connection conn = JDBC.getConnection();
            Statement stmt = conn.createStatement();
    		stmt.executeUpdate("UPDATE Checkin_T SET TREATMENT_ENDTIME = systimestamp WHERE CheckID = '"+this.checkid+"'");
            stmt.close();
            conn.close();
            } catch(Throwable oops) {
                oops.printStackTrace();
            }
	}
}


