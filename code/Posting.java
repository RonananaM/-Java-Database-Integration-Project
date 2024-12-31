//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public abstract class Posting {

    enum jobType {CLERICAL, COMPUTER, ENGINEERING, FINANCIAL, HEALTH, RETAIL, SALES, SERVICE, OTHER}

    enum postingType {REGULAR, INTERNSHIP}


    private postingType postingType;
    private long postingID;
    private long creatorID;
    private String jobTitle;
    private String company;
    private String contactEmail;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public Posting(ResultSet rs, int rowIndex) {
        try {
            rs.absolute(rowIndex);

            this.postingType = (postingType.valueOf(rs.getString("posting_type")));
            this.postingID = rs.getLong("posting_id");
            this.creatorID = rs.getLong("creator_id");
            this.jobTitle = (rs.getString("job_title"));
            this.company = (rs.getString("company_name"));
            this.contactEmail = (rs.getString("contact_email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors.
    public Posting(postingType type, long creator) {
        this.postingID = 0;
        this.postingType = type;
        this.creatorID = creator;
        this.enterCompany();
        this.enterJobTitle();
        this.setContactEmail();
    }

    public Posting() {
    }

    public void setPostingType(postingType type) {
        this.postingType = type;
    }

    public void enterJobTitle() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Name of Position: ");
        setJobTitle(input.nextLine());
    }

    public void setJobTitle(String jobName) {
        jobName = jobName.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        if ((jobName.matches("^[a-zA-Z][a-zA-Z '-]*") && (jobName.length() <= 64))) {
            this.jobTitle = jobName;
        } else {
            System.out.println("*** Invalid Job Title ***");
            enterJobTitle();
        }
    }

    public void setPostingID(long postingID) {
        System.out.println("** ERROR **\nPerson ID is set automatically by database");
    }

    public void enterCompany() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Company Name: ");
        setCompany(input.nextLine());
    }

    public void setCompany(String companyName) {
        companyName = companyName.trim().replaceAll("[^, 0-9a-zA-Z]", "");
        if ((companyName.matches("^[a-zA-Z][a-zA-Z -]*") && (companyName.length() <= 64))) {
            this.company = companyName;
        } else {
            System.out.println("*** Invalid Company Name Value ***");
            enterCompany();
        }
    }

    public static String enterEmail() {
        String email;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Contact Email: ");
        email = input.nextLine().trim();
        while (!(email.matches("^[a-zA-Z0-9!#$%&*+-/=?^_{|}~;]+@[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-._]+")
                && email.length() <= 64)) {
            System.out.println("*** Invalid Contact Email Entry ***");
            System.out.println("Enter Contact Email: ");
            email = input.nextLine().trim();
        }
        return email;
    }

    public void setContactEmail() {
        this.contactEmail = enterEmail();
    }

    public void setContactEmail(String email) {
        if (email.matches("^[a-zA-Z0-9!#$%&*+-/=?^_{|}~;]+@[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-._]+")
                && email.length() <= 64) {
            this.contactEmail = email;
        } else {
            System.out.println("*** Invalid Contact Email Entry ***");
            enterEmail();
        }
    }


    public postingType getPostingType() {
        return postingType;
    }
    public long getPostingID() {
        return postingID;
    }
    public long getCreatorID() {
        return creatorID;
    }
    public String getJobTitle() {
        return ((jobTitle.length() > 2) ? jobTitle.substring(0, 1).toUpperCase() + jobTitle.substring(1).toLowerCase()
                : jobTitle);
    }
    public String getCompany() {
        return ((company.length() > 2) ? company.substring(0, 1).toUpperCase() + company.substring(1).toLowerCase()
                : company);
    }
    public String getContactEmail() {
        return contactEmail.toLowerCase();
    }

    public abstract void modifyPosting(Statement stmt);

    public void modifyPostingHeader() {
        System.out.println("\n***************************");
        System.out.println("    Modify Posting Menu");
        System.out.println("***************************");
        System.out.println(this.listingString());
        System.out.println("\nEnter Field Number to Modify,\nEnter 99 to Delete Record from Database," +
                "\nEnter 0 to Confirm Changes and Commit to Database," +
                "\nEnter -1 to Discard any Changes ");
    }

    // Build String for outputting listing of fields to allow choice of modification. Overridden
    public String listingString() {
        return (" 1 Job Title: " + this.getJobTitle() + "\n 2 Contact Email: "
                + this.getContactEmail());
    }
    // Build String for SQL command to insert a new object into Database. Overridden
    protected String[] buildInsertString() {
        String[] buildString = new String[2];
        buildString[0] = "posting_type, posting_id, creator_id, job_title, company_name, contact_email";
        buildString[1] = "'" + this.postingType + "',' 0 ','" + this.creatorID + "','" + this.jobTitle
                + "','" + this.company + "','" + this.contactEmail + "'";
        return buildString;
    }
    // Build String for SQL command to update existing object in Database. Overridden
    protected String buildUpdateString() {
        return ("posting_type = '" + this.postingType + "',job_title = '" + this.jobTitle
                + "', contact_email = '" + this.contactEmail + "'");
    }
    // Build actual SQL Delete command to delete existing object from Database.
    public void postingDeleteDB(Statement stmt) {
        String queryString = "DELETE FROM `" + DataBase.DBSchema + "`.`" + DataBase.POSTINGS_TABLE
                + "` WHERE posting_id = '" + this.postingID + "'";
        try {
            stmt.executeUpdate(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Build actual SQL Insert command to insert new object into Database.
    public void postingInsertDB(Statement stmt) {
        String[] buildString = this.buildInsertString();
        String queryString = "INSERT INTO `" + DataBase.DBSchema + "`.`" + DataBase.POSTINGS_TABLE + "` (" + buildString[0]
                + ") VALUES (" + buildString[1] + ")";
        try {
            stmt.executeUpdate(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Build actual SQL Update command to update existing object from Database.
    public void postingUpdateDB(Statement stmt) {
        String buildString = this.buildUpdateString();
        String queryString = "UPDATE `" + DataBase.DBSchema + "`.`" + DataBase.POSTINGS_TABLE + "` SET " + buildString
                + " WHERE posting_id = '" + this.postingID + "'";
        try {
            stmt.executeUpdate(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // .toString method to allow easy output of objects. Overridden
    public String toString() {
        return ("Posting Type: " + this.getPostingType() + "\nPosting Number: " + this.postingID
                + "\nCreator ID Num: " + creatorID
                + "\nJob Title: " + this.getJobTitle() + "\nCompany Name: " + this.getCompany()
                + "\nEmail: " + this.getContactEmail());
    }

} // END Posting class
