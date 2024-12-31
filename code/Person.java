//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.util.Scanner;
import java.sql.*;

public abstract class Person {

    enum accessType {EMPLOYER, PROFESSIONAL, STUDENT}
    private accessType acessLevel;
    private long personID ;
    private String lastName;
    private String firstName;
    private String email;
    private String phone;

    // Constructor for creating an object directly from the database record. Uses overridden constructors
    public Person(ResultSet rs, int rowIndex) {
        try {
            rs.absolute(rowIndex);

            this.acessLevel = (accessType.valueOf(rs.getString("access_level"))) ;
            this.personID = rs.getLong("person_id" );
            this.lastName = (rs.getString("last_name") );
            this.firstName = (rs.getString("first_name"));
            this.email = ( rs.getString("email"));
            this.phone = ( rs.getString("phone"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Constructor to create an object from input - can then be inserted into database. Uses
    // overridden constructors and is built with email that has already been verified unique.
    public Person(accessType access, String email) {
        this.setAcessLevel(access);
        this.personID = 0;
        this.enterLname();
        this.enterFname();
        this.email = email;
        this.enterPhone();
    }

    public Person() {}

    public void enterLname(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Last Name: ");
        setLastName(input.nextLine());
    }
    public void setLastName(String lastName) {
        lastName = lastName.trim().replaceAll("[^, 0-9a-zA-Z]","");
        if ((lastName.matches("^[a-zA-Z][a-zA-Z -]*") && (lastName.length()<=64))){
            this.lastName = lastName;
        } else {
            System.out.println("*** Invalid Last Name Value ***");
            enterLname();
        }
    }

    public void enterFname(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter First Name: ");
        setFirstName(input.nextLine());
    }
    public void setFirstName(String firstName) {
        firstName = firstName.trim().replaceAll("[^, 0-9a-zA-Z]","");
        if ((firstName.matches("^[a-zA-Z][a-zA-Z -]*") && (firstName.length()<=64))){
            this.firstName = firstName;
        } else {
            System.out.println("*** Invalid First Name Value ***");
            enterFname();
        }
    }

    public static String checkEmail() {
        String email;
        Scanner input = new Scanner(System.in);
        System.out.println("Enter Email: ");
        email = input.nextLine().trim();
        while (!(email.matches("^[a-zA-Z0-9!#$%&'*+-/=?^_`{|}~;]+@[a-zA-Z0-9-_]+\\.[a-zA-Z0-9-._]+")
                && email.length() <= 64)) {
            System.out.println("*** Invalid Email Entry ***");
            System.out.println("Enter Email: ");
            email = input.nextLine().trim();
        }
        return email;
    }

    public void enterPhone(){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter 10 Digit Phone Number: ");
        setPhone(input.nextLine());
    }
    public void setPhone(String phone) {
        phone = phone.trim().replaceAll("[^0123456789]","");
        if ((phone.length()==10)) {
            this.phone = phone;
        } else {
            System.out.println("*** Invalid Phone Entry ***");
            enterPhone();
        }
    }

    public void setAcessLevel(accessType acessLevel) {
        this.acessLevel = acessLevel;
    }
    public void setPersonID(long personID) {
        System.out.println("** ERROR **\nPerson ID is set automatically by database");
    }
    public accessType getAcessLevel() {
        return acessLevel;
    }
    public long getPersonID() {
        return personID;
    }
    public String getLastName() {
        return ((lastName.length() > 2) ? lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase()
                    : lastName);
    }
    public String getFirstName() {
        return ((firstName.length() > 2) ? firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase()
                : firstName);
    }
    public String getEmail() {
        return email.toLowerCase();
    }
    public String getPhone() {
        return (phone.substring(0,3)+"-"+phone.substring(3,6)+"-"+phone.substring(6));
    }

    public void modifyProfile (Statement stmt){
        modifyProfileHeader();
        System.out.println(this.listingString());
        System.out.println("\nEnter Field Number to Modify,\nEnter 99 to Delete Record from Database," +
                "\nEnter 0 to Confirm Changes and Commit to Database," +
                "\nEnter -1 to Discard any Changes ");
    }
    public void modifyProfileHeader(){
        System.out.println("\n***************************");
        System.out.println("    Modify Person Menu");
        System.out.println("***************************");
    }

    // Output fields corresponding to class for selection purposes
    public String listingString(){
        return (" \n 1 Last Name: " + this.getLastName()
                + "\n 2 First Name: " + this.getFirstName() + "\n 3 Phone: " + this.getPhone() );
    }
    // .toString method to allow easy output of objects. Overridden
    public String toString(){
        return ("Profile Type: " + this.getAcessLevel() + "\nID Number: " + this.personID
                + "\nNAME: " + this.getLastName() + ", " + this.getFirstName()
                + "\nEmail: " + this.getEmail() + "\nPhone: " + this.getPhone() );
    }

    // Build String for SQL command to insert a new object into Database. Overridden
    protected String[] buildInsertString(){
        String[] buildString = new String[2];
        buildString[0] = "access_level, person_id, last_name, first_name, email, phone";
        buildString[1] = "'" +this.acessLevel+ "',' 0 ','" +this.lastName+ "','" +this.firstName
                + "','" +this.email+ "','" +this.phone+ "'";
        return buildString;
    }

    // Build String to use in SQL command to insert a new object into Database. Overridden
    protected String buildUpdateString() {
        return ("last_name = '" + this.lastName + "',first_name = '" + this.firstName
                + "',phone = '" + this.phone + "'");
    }

    // Build actual SQL Delete command to delete existing object from Database.
    public void userDeleteDB (Statement stmt){
        String queryString = "DELETE FROM `"+ DataBase.DBSchema+"`.`"+DataBase.PEOPLE_TABLE
                +"` WHERE person_id = '" + this.personID +"'";
        try {
            stmt.executeUpdate(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Build actual SQL Insert command to insert new object into Database.
    public void userInsertDB (Statement stmt){
        String[] buildString = this.buildInsertString();
        String queryString = "INSERT INTO `"+ DataBase.DBSchema+"`.`"+DataBase.PEOPLE_TABLE+"` ("+ buildString[0]
                +") VALUES (" + buildString[1] +")";
        try {
            stmt.executeUpdate(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Build actual SQL update command to update existing object from Database.
    public void userUpdateDB (Statement stmt){
        String buildString = this.buildUpdateString();
        String queryString = "UPDATE `"+ DataBase.DBSchema+"`.`"+DataBase.PEOPLE_TABLE+"` SET "+ buildString
                 + " WHERE person_id = '" + this.personID +"'";
        try {
            stmt.executeUpdate(queryString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} // END Person class