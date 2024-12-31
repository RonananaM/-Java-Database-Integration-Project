//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public abstract class Applicant extends Person {

    private Posting.jobType desiredCategory;
    private String primaryLang;
    private String otherLang;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public Applicant(ResultSet rs, int rowIndex) {
        super(rs, rowIndex);
        try {
            rs.absolute(rowIndex);
            this.desiredCategory = (Posting.jobType.valueOf(rs.getString("desired_category")));
            this.primaryLang = (rs.getString("primary_language"));
            this.otherLang = (rs.getString("other_languages"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors and is built with email that has already been verified unique.
    public Applicant(accessType access, String email) {
        super(access, email);
        this.enterDesiredCategory();
        this.enter1stLang();
        this.enterOtherLang();
    }

    public Applicant() {
        super();
    }

    public void enterDesiredCategory() {
        Scanner input = new Scanner(System.in);
        int selection;
        do {
            System.out.println("Enter Desired Position Category by Number: ");
            for (int i = 0; i < Posting.jobType.values().length; i++) {
                System.out.printf("%3d: %s\n", i + 1, Posting.jobType.values()[i]);
            }
            selection = input.nextInt();
        } while (selection < 1 || selection > Posting.jobType.values().length);
        this.desiredCategory = Posting.jobType.values()[selection - 1];
    }

    public void setDesiredCategory(String desCategory) {
        boolean isValidCategory = false;
        desCategory = desCategory.trim().toUpperCase();
        for (Posting.jobType elem : Posting.jobType.values()) {
            if (elem.equals(desCategory)) {
                isValidCategory = true;
            }
        }
        if (isValidCategory) {
            this.desiredCategory = Posting.jobType.valueOf(desCategory);
        } else {
            System.out.println("*** Invalid Category Name Value ***");
            enterDesiredCategory();
        }
    }

    public void setDesiredCategory(Posting.jobType desCategory) {
        boolean isValidCategory = false;
        for (Posting.jobType elem : Posting.jobType.values()) {
            if (elem.equals(desCategory)) {
                isValidCategory = true;
            }
        }
        if (isValidCategory) {
            this.desiredCategory = (desCategory);
        } else {
            System.out.println("*** Invalid Category Name Value ***");
            enterDesiredCategory();
        }
    }

    public void enter1stLang() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Primary Language Spoken: ");
        set1stLang(input.nextLine());
    }

    public void set1stLang(String primeLang) {
        primeLang = primeLang.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        this.primaryLang = ((primeLang.length() > 64) ? primeLang.substring(0, 64) : primeLang);
    }

    public void enterOtherLang() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Any Other Languages Spoken (Separated by Commas): ");
        setOtherLang(input.nextLine());
    }

    public void setOtherLang(String otherLang) {
        otherLang = otherLang.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        this.otherLang = ((otherLang.length() > 255) ? otherLang.substring(0, 255) : otherLang);
    }

    public Posting.jobType getDesiredCategory() {
        return desiredCategory;
    }
    public String getPrimaryLang() {
        return primaryLang;
    }
    public String getOtherLang() {
        return otherLang;
    }

    // Build String for SQL command to insert a new object into Database. Overridden
    protected String[] buildInsertString() {
        String[] buildString = super.buildInsertString();
        buildString[0] += ", desired_category, primary_language, other_languages";
        buildString[1] += ",'" + this.desiredCategory + "','"
                + this.primaryLang + "','" + this.otherLang + "'";
        return buildString;
    }
    // Build String for SQL command to an existing object in Database. Overridden
    protected String buildUpdateString() {
        return (super.buildUpdateString()
                + ",desired_category = '" + this.desiredCategory + "',primary_language = '"
                + this.primaryLang + "',other_languages = '" + this.otherLang + "'");
    }
    // .toString method to allow easy output of objects. Overridden
    public String toString() {
        return (super.toString() + "\nPrimary Language: " + this.getPrimaryLang()
                + "\nOther Languages Spoken: " + this.getOtherLang()
                + "\nPosition Desired: " + this.getDesiredCategory());
    }
    // Build String for outputting listing of fields to allow choice of modification. Overridden
    public String listingString() {
        return (super.listingString() + "\n 4 Primary Language: " + this.getPrimaryLang()
                + "\n 5 Other Languages Spoken: " + this.getOtherLang()
                + "\n 6 Position Desired: " + this.getDesiredCategory());
    }


} // END Applicant class
