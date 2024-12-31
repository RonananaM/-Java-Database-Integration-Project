//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Professional extends Applicant {

    private int yrsExperience;
    private String educationLevel;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public Professional(ResultSet rs, int rowIndex) {
        super(rs, rowIndex);
        try {
            rs.absolute(rowIndex);
            this.yrsExperience = (rs.getInt("experience"));
            this.educationLevel = (rs.getString("education"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors and is built with email that has already been verified unique.
    public Professional(String email) {
        super(accessType.PROFESSIONAL, email);  // Calls super constructor with professional type added
        this.enterExperience();
        this.enterEduLevel();
    }

    public Professional() {
        super();
    }

    public void enterExperience() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Number of Years Experience: ");
        setExperience(Math.round(Float.valueOf("0" + input.nextLine().trim().replaceAll("[^.0123456789]", ""))));
    }

    public void setExperience(int experience) {
        if (experience >= 0 && experience < 100) {
            this.yrsExperience = experience;
        } else {
            System.out.println("*** Invalid Years Experience ***");
            enterExperience();
        }
    }

    public void enterEduLevel() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Education: ");
        setEduLevel(input.nextLine());
    }

    public void setEduLevel(String education) {
        education = education.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        this.educationLevel = ((education.length() > 255) ? education.substring(0, 255) : education);
    }

    public int getYrsExperience() {
        return yrsExperience;
    }

    public String getEducationLevel() {
        return educationLevel;
    }

    public void modifyProfile(Statement stmt) {

        int menuPick = 0;
        boolean done = false;
        Scanner input = new Scanner(System.in);

        while (!done) {
            super.modifyProfile(stmt);
            do {
                menuPick = input.nextInt();
                input.nextLine();
            } while (!((menuPick >= -1 && menuPick <= 8) || menuPick == 99));

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
                    this.enter1stLang();
                    break;
                case 5:
                    this.enterOtherLang();
                    break;
                case 6:
                    this.enterDesiredCategory();
                    break;
                case 7:
                    this.enterExperience();
                    break;
                case 8:
                    this.enterEduLevel();
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
    protected String[] buildInsertString() {
        String[] buildString = super.buildInsertString();
        buildString[0] += ", experience, education";
        buildString[1] += ",'" + this.yrsExperience + "','" + this.educationLevel + "'";
        return buildString;
    }
    // Build String for SQL command to update existing object in Database. Overridden
    protected String buildUpdateString() {
        return (super.buildUpdateString() + ",experience = '" + this.yrsExperience
                + "',education = '" + this.educationLevel + "'");
    }
    // .toString method to allow easy output of objects. Overridden
    public String toString() {
        return (super.toString() + "\nYears Experience in Field: " + this.getYrsExperience()
                + "\nEducation: " + this.getEducationLevel());
    }

    // Build String for outputting listing of fields to allow choice of modification. Overridden
    public String listingString() {
        return (super.listingString() + "\n 7 Years Experience in Field: " + this.getYrsExperience()
                + "\n 8 Education: " + this.getEducationLevel());
    }


} // END Professional class