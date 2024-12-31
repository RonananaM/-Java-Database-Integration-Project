//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PostingIntrn extends Posting {

    private String requiredMajor;
    private String requiredClass;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public PostingIntrn(ResultSet rs, int rowIndex) {
        super(rs, rowIndex);
        try {
            rs.absolute(rowIndex);
            this.requiredMajor = (rs.getString("major") );
            this.requiredClass = (rs.getString("classification"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors.
    public PostingIntrn(long creator) {
        super(Posting.postingType.INTERNSHIP, creator);
        enterReqMajor();
        enterReqClass();
    }

    public PostingIntrn() {}

    public void enterReqMajor(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Required Major: ");
        setRequiredMajor(input.nextLine());
    }
    public void setRequiredMajor(String major) {
        major = major.trim().replaceAll("[^, 0-9a-zA-Z]","");
        if ((major.matches("^[a-zA-Z][a-zA-Z -]*") && (major.length()<=64))){
            this.requiredMajor = major;
        } else {
            System.out.println("*** Invalid Major ***");
            enterReqMajor();
        }
    }

    public void enterReqClass(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Required Grade Classification: ");
        setRequiredClass(input.nextLine());
    }
    public void setRequiredClass(String classification) {
        classification = classification.trim().replaceAll("[^, 0-9a-zA-Z]","");
        if ((classification.matches("^[a-zA-Z][a-zA-Z -]*") && (classification.length()<=64))){
            this.requiredClass = classification;
        } else {
            System.out.println("*** Invalid College Classification ***");
            enterReqClass();
        }
    }

    public String getRequiredMajor() {
        return requiredMajor;
    }
    public String getRequiredClass() {
        return requiredClass;
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
                    this.enterReqMajor();
                    break;
                case 4:
                    this.enterReqClass();
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
        buildString[0] += ", major, classification";
        buildString[1] += ",'" +this.requiredMajor+ "' ,'" +this.requiredClass+  "'";
        return buildString;
    }
    // Build String for SQL command to update existing object into Database. Overridden
    protected String buildUpdateString() {
        return (super.buildUpdateString() + ", major = '" + this.requiredMajor
                + "', classification = '" + this.requiredClass + "'");
    }

    // Build String for outputting listing of fields to allow choice of modification. Overridden
        public String listingString(){
        return (super.listingString() + "\n 3 Required Major: " + this.getRequiredMajor()
                + "\n 4 Required Grade Classification: " + this.getRequiredClass() );
    }
    // .toString method to allow easy output of objects. Overridden
    public String toString(){
        return (super.toString() + "\nRequired Major: " + this.getRequiredMajor()
                + "\nRequired Grade Classification: " + this.getRequiredClass() );
    }
} // END PostingIntrn class
