//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Employer extends Person {

    private String company;
    private String title;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public Employer(ResultSet rs, int rowIndex) {
        super(rs, rowIndex);
        try {
            rs.absolute(rowIndex);
            this.company = rs.getString("company");
            this.title = rs.getString("title");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors and is built with email that has already been verified unique.
    public Employer(String email) {
        super(accessType.EMPLOYER, email);  // Calls super constructor with employer type added
        this.enterCompany();
        this.enterTitle();
    }
    public Employer () { super(); }

    // User input of values, verify valid, and set class fields
    public void enterCompany(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Company Name: ");
        setCompanyName(input.nextLine().trim().replaceAll("[^ a-zA-Z0-9-]",""));
    }
    public void setCompanyName(String company) {
        while (!(company.length()<=64)){
            Scanner input = new Scanner(System.in);
            System.out.println("*** Invalid Company Name Value ***\nEnter Company Name: ");
            company = input.nextLine().trim().replaceAll("[^ a-zA-Z0-9-]","");
        }
        this.company = company;
    }
    public void enterTitle(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Title: ");
        setTitle(input.nextLine());
    }
    public void setTitle(String title) {
        title = title.trim().replaceAll("[^ a-zA-Z0-9-]","");
        this.title = ((title.length() > 64) ? title.substring(0,64) :  title );
    }

    // Get values
    public String getCompany() { return (this.company); }
    public String getTitle() { return (this.title); }

    // Method to allow modification of an Employee Profile
    public void modifyProfile(Statement stmt) {
        int menuPick = 0;
        boolean done = false;
        Scanner input = new Scanner(System.in);

        while (!done) {
            super.modifyProfile(stmt);      // Call method in parent class to show menu and values
            do {
                menuPick = input.nextInt();
                input.nextLine();
            } while (!((menuPick >= -1 && menuPick <= 5) || menuPick == 99));

            switch (menuPick) {
                case -1:
                    System.out.println("\nNO MODIFICATIONS MADE");
                    done = true;
                    break;

                case 0:
                    this.userUpdateDB(stmt);
                    System.out.println("\n** Changes have been committed to database **");
                    done = true;
                    break;
                case 1:
                    this.enterLname();
                    break;
                case 2:
                    this.enterFname();
                    break;
                case 3:
                    this.enterPhone();
                    break;
                case 4:
                    this.enterCompany();
                    break;
                case 5:
                    this.enterTitle();
                    break;
                case 99:
                    System.out.println("DELETE RECORD? Enter Y to Confirm");
                    if (Character.toLowerCase(input.nextLine().charAt(0)) == 'y') {
                        this.userDeleteDB(stmt);
                        System.out.println("\n** Record has been Deleted from Database **");
                        done = true;
                    } else {
                        System.out.println("\n*** RECORD LEFT IN PLACE ***");
                    }
                    break;
                default:
                    break;
            }
        }
    }
    // Build String for SQL command to insert a new object into Database. Overridden
    protected String[] buildInsertString(){
        String[] buildString = super.buildInsertString() ;
        buildString[0] += ", company, title";
        buildString[1] += ",'" + this.company + "','" + this.title + "'";
        return buildString;
    }
    protected String buildUpdateString() {
        return (super.buildUpdateString() + ",company = '"+ this.company
                + "',title = '" + this.title +"'");
    }

    public String toString (){
        return (super.toString() + "\nCompany Name: " + this.getCompany()+ "\nPosition/Title:  " + this.getTitle());
    }

    // Build String for outputting listing of fields to allow choice of modification. Overridden
    public String listingString (){
        return (super.listingString() + "\n 4 Company Name: " + this.getCompany()
                + "\n 5 Position/Title:  " + this.getTitle());
    }
} // END Employer class
