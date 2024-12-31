//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Student extends Applicant {

    private String schoolAttending;
    private String major;
    private String honorsAwardsOrgs;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public Student(ResultSet rs, int rowIndex) {
        super(rs, rowIndex);
        try {
            rs.absolute(rowIndex);
            this.schoolAttending = (rs.getString("school"));
            this.major = (rs.getString("major"));
            this.honorsAwardsOrgs = (rs.getString("academia"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors and is built with email that has already been verified unique.
    public Student(String email) {
        super(accessType.STUDENT, email); // Calls super constructor with student type added
        this.enterSchool();
        this.enterMajor();
        this.enterHonorsAwards();
    }

    public Student() {
        super();
    }

    public void enterSchool() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter School Attending: ");
        setSchool(input.nextLine());
    }

    public void setSchool(String school) {
        school = school.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        this.schoolAttending = ((school.length() > 64) ? school.substring(0, 64) : school);
    }

    public void enterMajor() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Academic Major: ");
        setMajor(input.nextLine());
    }

    public void setMajor(String major) {
        major = major.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        this.major = ((major.length() > 64) ? major.substring(0, 64) : major);
    }

    public void enterHonorsAwards() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Honors, Awards, and Organizations Separated by Commas (255 char max): ");
        setHonorsAwardsOrgs(input.nextLine());
    }

    public void setHonorsAwardsOrgs(String honors) {
        honors = honors.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        this.honorsAwardsOrgs = ((honors.length() > 255) ? honors.substring(0, 255) : honors);
    }

    public String getSchool() {
        return schoolAttending;
    }

    public String getMajor() {
        return major;
    }

    public String getHonorsAwards() {
        return honorsAwardsOrgs;
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
            } while (!((menuPick >= -1 && menuPick <= 9) || menuPick == 99));
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
                    this.enterSchool();
                    break;
                case 8:
                    this.enterMajor();
                    break;
                case 9:
                    this.enterHonorsAwards();
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
        buildString[0] += ", school, major, academia";
        buildString[1] += ",'" + this.schoolAttending + "','" + this.major + "','" + this.honorsAwardsOrgs + "'";
        return buildString;
    }

    // Build String for SQL command to an existing object in Database. Overridden
    protected String buildUpdateString() {
        return (super.buildUpdateString() + ",school = '" + this.schoolAttending
                + "',major = '" + this.major + "',academia = '" + this.honorsAwardsOrgs + "'");
    }

    // .toString method to allow easy output of objects. Overridden
    public String toString() {
        return (super.toString() + "\nSchool Attending: " + this.getSchool()
                + "\nMajor: " + this.getMajor() + "\nHonors & Awards: " + this.getHonorsAwards());
    }

    // Build String for outputting listing of fields to allow choice of modification. Overridden
    public String listingString() {
        return (super.listingString() + "\n 7 School Attending: " + this.getSchool()
                + "\n 8 Major: " + this.getMajor() + "\n 9 Honors & Awards: " + this.getHonorsAwards());
    }


} // END Student class
