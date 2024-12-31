//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

//////////////////////////////////////////////////////////////////////////////////////////
//                      ** This program demonstrates use of: **
//
// Database:
//      Utilizes MySQL database connection and dynamically builds SQL statements
//          across as much as three levels of inheritance, allowing modification
//          and deletion of records from database;
//      Performs verification of user email as simulation of login, immediately
//          identifying which subclass the user is, creating a new object of that
//          type, and placing all relevant fields from the Database into that object
//          to be used as the CurrentUser for the remainder of the session;
//      Uses the database to police unique email values as well as assign unique
//          user IDs (key);
//      Retrieves record sets - could easily be enhanced by use of various
//          specific WHERE queries built with user input variables to allow
//          search functionality from Database;
//
// Enumeration:
//      Uses Enumeration in both base classes - Person and Posting - to guarantee
//          data integrity within critical fields;
//      Uses Enumeration within the MySQL Database structure to allow enumerated values
//          to be processed and evaluated properly;
//
// Polymorphism:
//      Reads in a RecordSet from database, identifies subtype, builds new object
//          of that type and stores it into an ArrayList of the Base class type;
//      Iterates through ArrayList and uses overridden .toString to output all
//          elements in the proper format for their individual class.
//
// Database class:
//          Allows for cleanliness and modularity of database connection;
//          Database name, table names, port number, passwords, etc are stored in
//          variables which can be accessed and modified quickly; Variables are then used
//          to create SQL statement string and create a connection object which is passed;
//
// Inheritance:
//      PERSON: Abstract Class for all Person Profiles. Contains methods that are overriden
//              in subclasses and chained together to handle each set of fields for the
//              particular class it is in;
//          EMPLOYER:  Subclass of Person for Profiles of Employers. Allowed to View, Create,
//                  Update, and Delete all job Postings; Can Create, Update, and Delete
//                  own Profile;
//          APPLICANT: Abstract Class inheriting from PERSON; all Subclasses of applicant
//                  can View job Postings; Can Create, Update, and Delete own Profile; .
//              STUDENT: Subclass of Applicant; Intended to contain students seeking
//                  internships;
//              REGULAR: Subclass of Applicant; Intended to contain experienced job seekers;
//
//      POSTING: Abstract class for all Postings;
//          INTERNSHIP: Subclass of Posting to hold postings related to internships and
//              college students;
//          REGULAR: Subclass of Posting to hold postings related to experienced job
//              seekers;
//
// Data Validation and Formatting:
//      All user input, including email addresses, is checked for valid format and/or
//          stripped of special characters before being allowed to be assigned to fields
//          in an attempt to stop possible SQL injection;
//      Some fields, such as phone number, have output formatted by getter for
//          cleaner output and better user experience;
//

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class InherMain {

    public static void main(String[] args) throws SQLException {

        // Set up connection to database and object to store result set
        DataBase DB = new DataBase();
        Scanner input = new Scanner(System.in);
        ResultSet rs;

        Person currentUser;     // Declare Person object allow creation of any type of user
        int maxChoice = 3;      // Define the max # of choices in User Menu
        int menuPick = 0;       // Initialize menuPick
        boolean done = false;   // Flag to continue allowing user to work with database

        currentUser = Menu.welcomeMenu(DB.stmt);  // Create current user after validating email
        ArrayList<Posting> allPostings = Menu.postingsToArray(DB.stmt); // Place all postings into array

        while (!done) {
            Menu.mainMenu();
            if (currentUser.getAcessLevel() == Person.accessType.EMPLOYER) {
                Menu.employerMenu();  // If type is Employer, add the rest of choices
                maxChoice = 5;        // And change max number of choices to match
            }

            do {        // Get valid selection
                System.out.println("\n ** Enter Selection Number or -1 to Quit ** ");
                menuPick = input.nextInt();
                input.nextLine();
            } while (!(menuPick >= -1 && menuPick <= maxChoice));

            switch (menuPick) {
                case 0:
                case -1:                                    // Leave the program
                    done = true;
                    break;
                case 1:
                    currentUser.modifyProfile(DB.stmt);     // Call Modify Profile menu to
                    break;                                  // change or delete current user profile
                case 2:
                    Menu.viewAllPeople(DB.stmt);            // View all Persons in Database
                    break;
                case 3:
                    Menu.viewAllPostings(allPostings);      // View all Postings in Database
                    break;
                case 4:                                     // Employers can create new Postings
                    Menu.createPostingMenu(DB.stmt, currentUser.getPersonID());
                    allPostings = Menu.postingsToArray(DB.stmt);
                    break;
                case 5:
                    Menu.viewAllPostings(allPostings);      // Employers can Modify or Delete any posting
                    int numElement = allPostings.size();
                    System.out.println("\n**  Select Posting # to Modify or Delete  **");
                    do {
                        System.out.println("**  Enter Selection Number or -1 to Quit  ** ");
                        menuPick = input.nextInt();
                        input.nextLine();
                    } while (!(menuPick >= -1 && menuPick <= numElement));
                    if (menuPick > 0) {
                        allPostings.get(menuPick - 1).modifyPosting(DB.stmt);   // If pick is not -1 or 0, modify
                        allPostings = Menu.postingsToArray(DB.stmt);            // selected posting
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
