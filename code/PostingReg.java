//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostingReg extends Posting {

    private int requiredExp;
    private int salary;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public PostingReg(ResultSet rs, int rowIndex) {
        super(rs, rowIndex);
        try {
            rs.absolute(rowIndex);
            this.requiredExp = (rs.getInt("req_experience") );
            this.salary = (rs.getInt("salary"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors.
    public PostingReg(long creator) {
        super(Posting.postingType.REGULAR, creator);
        enterReqExp();
        enterSalary();
    }

    public PostingReg() {}

    public void enterReqExp(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Number of Years Experience Required: ");
        setRequiredExp(Math.round(Float.valueOf("0"+input.nextLine().trim().replaceAll("[^.0123456789]",""))));
    }
    public void setRequiredExp(int ReqExp) {
        if (ReqExp >= 0 && ReqExp < 100) {
            this.requiredExp = ReqExp;
        } else {
            System.out.println("*** Invalid Years Experience ***");
            enterReqExp();
        }
    }

    public void enterSalary(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Starting Salary for Position: ");
        setSalary(Math.round(Float.valueOf("0"+input.nextLine().trim().replaceAll("[^.0123456789]",""))));
    }
    public void setSalary(int pay) {
        if (pay >= 0 && pay < 10000000) {
            this.salary = pay;
        } else {
            System.out.println("*** Invalid Salary ***");
            enterSalary();
        }
    }

    public int getRequiredExp() {
        return requiredExp;
    }

    public int getSalary() {
        return salary;
    }

    // Determine the correct action to take based on user choice
    public void modifyPosting(Statement stmt) {
        int menuPick = 0;
        boolean done = false;
        Scanner input = new Scanner(System.in);

        while (!done) {
            this.modifyPostingHeader();
            do {
                menuPick = input.nextInt();
                input.nextLine();
            } while (!((menuPick >= -1 && menuPick <= 4) || menuPick == 99));

            switch (menuPick) {
                case -1:
                    System.out.println("\nNO MODIFICATIONS MADE");
                    done = true;
                    break;

                case 0:
                    this.postingUpdateDB(stmt);
                    System.out.println("\n** Changes have been committed to database **");
                    done = true;
                    break;
                case 1:
                    this.enterJobTitle();
                    break;
                case 2:
                    this.setContactEmail();
                    break;
                case 3:
                    this.enterReqExp();
                    break;
                case 4:
                    this.enterSalary();
                    break;
                case 99:
                    System.out.println("DELETE RECORD? Enter Y to Confirm");
                    if (Character.toLowerCase(input.nextLine().charAt(0)) == 'y') {
                        this.postingDeleteDB(stmt);
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
        String[] buildString = super.buildInsertString();
        buildString[0] += ", req_experience, salary";
        buildString[1] += ",'" +this.requiredExp+ "' ,'" +this.salary+  "'";
        return buildString;
    }
    // Build String for SQL command to update existing object in Database. Overridden
    protected String buildUpdateString() {
        return (super.buildUpdateString() + ", req_experience = '" + this.requiredExp
                + "', salary = '" + this.salary + "'");
    }

    // Build String for outputting listing of fields to allow choice of modification. Overridden
    public String listingString(){
        return (super.listingString() + "\n 3 Years Experience Required: " + this.getRequiredExp()
                + "\n 4 Starting Salary: " + this.getSalary() );
    }
    // .toString method to allow easy output of objects. Overridden
    public String toString(){
        return (super.toString() + "\nYears Experience Required: " + this.getRequiredExp()
                + "\nStarting Salary: " + this.getSalary() );
    }

}  // END PostingReg Class

