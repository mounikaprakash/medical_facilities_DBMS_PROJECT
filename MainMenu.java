import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {
    	MainMenu menu = new MainMenu();
        menu.runMenu();
    }

    public void runMenu() {
        	printHeader();
            printMainMenu();
            int choice = getMenuChoice();
            performAction(choice);
    }

    public void printHeader() {
        System.out.println("**********************************************************");
        System.out.println("***********************Welcome to*************************");
        System.out.println("*********Medical Facility Dashboard Application***********");
        System.out.println("**********************************************************");
    }

    public void printMainMenu() {
    	System.out.println("Please make a selection........");
        System.out.println("1) Sign In");
        System.out.println("2) Sign Up");
        System.out.println("3) Demo Queries");
        System.out.println("4) Exit");
    }	
    
    public int getMenuChoice() {
        Scanner keyboard = new Scanner(System.in);
        int choice = -1;
        do {
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(keyboard.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Numbers only please.");
            }
            if (choice < 0 || choice > 4) {
                System.out.println("Choice outside of range. Please chose again.");
            }
        } while (choice < 0 || choice > 4);
        return choice;
    }
    
    public void performAction(int choice) {
        switch (choice) {
            case 1: {
                try {
                	System.out.println("**************\n*Sign in Page*\n**************");
                	SignIn s = new SignIn();
                	s.runSignIn();
                } catch (Exception e) {
                    System.out.println("Sign in not successful."+ e);
                }
            }
            break;
            case 2: {
                try {
                	System.out.println("**************\n*Sign up Page*\n**************");
                	SignUp s = new SignUp();
                	s.runSignUp();
                } catch (Exception e) {
                    System.out.println("Sign up not successfull"+ e);
                }
            }
            break;
            case 3:
            	System.out.println("*******************\n*Demo Queries Page*\n*******************");
            	DemoQueries d = new DemoQueries();
            	d.runDemoQueries();
                break;
            case 4:
            	System.out.println("Thank You. \nVisit Again...");
            	System.exit(0);
                break;
            default:
                System.out.println("Error in switch block");
        }
    }

}
