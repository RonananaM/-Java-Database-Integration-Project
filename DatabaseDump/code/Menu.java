//
// Name: G. Holden
// Date: 3/23/2018
// Job Tracking - Object Oriented Inheritance and Database Connection
//

import java.util.ArrayList;
import java.sql.*;
import java.util.Scanner;

public class Menu {

    // Welcome menu gets email address input and checks if it is in database
    // If yes, it makes Current User object with their info, if no,
    // it allows creation of new profile and then makes that the Current User.
    public static Person welcomeMenu(Statement stmt) {
        Person currentUser = new Professional();
        ResultSet rs = null;
        System.out.println("*****************************************");
        System.out.println("Welcome to the Job Tracking System");
        System.out.println("Please Enter Your Email Address to Log In");
        System.out.println("*****************************************");
        String myEmail = Person.checkEmail();

        try {
            // Check email against database to determine if it exists or not
            rs = stmt.executeQuery("select * from "
                    + DataBase.PEOPLE_TABLE + " where email = '" + myEmail + "'");

            if (rs.last() == false) {
                System.out.println("You do not appear to have an account with us,");
                System.out.println(" ** Would you like to create an account? ** ");
                System.out.println("(Enter Y to create account, anything else to exit) ");
                Scanner input = new Scanner(System.in);

                if (Character.toLowerCase(input.nextLine().charAt(0)) == 'y') {

                    currentUser = createUserMenu(myEmail);
                    currentUser.userInsertDB(stmt);

                } else {
                    System.out.println("\nThank you, Goodbye!");
                    System.exit(1);
                }
            } else {
                currentUser = createUser(rs, 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("\n------------------------------------------------");
        System.out.println("**     Welcome to the Job Tracking System     **");
        System.out.println("**        Your Profile is Shown Below         **");
        System.out.println("------------------------------------------------");
        System.out.println(currentUser); // Use implied .toString
        return currentUser;
    }

    public static void mainMenu() {
        System.out.println("\n***************************");
        System.out.println("        User Menu        ");
        System.out.println("***************************");

        System.out.println(" 1: Modify/Delete Profile");
        System.out.println(" 2: View Applicants");
        System.out.println(" 3: View Postings");
    }

    public static void employerMenu() {
        System.out.println(" 4: Add Posting");
        System.out.println(" 5: Modify/Delete Posting");
    }

    // Create an object based on the type listed in the Database
    public static Person createUser(ResultSet rs, int row) throws SQLException {
        switch (Person.accessType.valueOf(rs.getString("access_level"))) {
            case STUDENT:
                return (new Student(rs, row));
            case EMPLOYER:
                return (new Employer(rs, row));
            case PROFESSIONAL:
                return (new Professional(rs, row));
            default:
                break;
        }
        return new Professional();
    }

    // Create an object based on the selected type and a email not in the database
    public static Person createUser(Person.accessType type, String email) {
        Person tempUser;
        switch (type) {
            case STUDENT:
                tempUser = new Student(email);
                break;
            case EMPLOYER:
                tempUser = new Employer(email);
                break;
            case PROFESSIONAL:
                tempUser = new Professional(email);
                break;
            default:
                tempUser = new Professional(email);
        }
        return tempUser;
    }

    public static Person createUserMenu(String email) {
        Scanner input = new Scanner(System.in);

        int selection;
        do {
            System.out.println("Enter Desired User Type by Number: ");
            for (int i = 0; i < Person.accessType.values().length; i++) {
                System.out.printf("%3d: %s\n", i + 1, Person.accessType.values()[i]);
            }
            selection = input.nextInt();
        } while (selection < 1 || selection > Person.accessType.values().length);
        return createUser(Person.accessType.values()[selection - 1], email);
    }

    public static void createPostingMenu(Statement stmt, long creatorID) {
        Scanner input = new Scanner(System.in);
        Posting tempPost = new PostingReg();

        int selection;
        do {
            System.out.println("Enter Desired Posting Type by Number: ");
            for (int i = 0; i < Posting.postingType.values().length; i++) {
                System.out.printf("%3d: %s\n", i + 1, Posting.postingType.values()[i]);
            }
            selection = input.nextInt();
        } while (selection < 1 || selection > Posting.postingType.values().length);
        switch (Posting.postingType.values()[selection - 1]) {
            case INTERNSHIP:
                tempPost = new PostingIntrn(creatorID);
                break;
            case REGULAR:
                tempPost = new PostingReg(creatorID);
                break;
            default:
                break;
        }
        tempPost.postingInsertDB(stmt);
        System.out.println("\n------------------------------------------------");
        System.out.println("**   Thank You For Adding A New Job Posting   **");
        System.out.println("**     Posting Information is Shown Below     **");
        System.out.println("------------------------------------------------");
        System.out.println(tempPost);

    }

    // For each record in Person Table of Database, determine type, create object of that
    // type, and add to ArrayList
    public static ArrayList<Person> viewAllPeople(Statement stmt) {
        ResultSet rs;
        ArrayList<Person> allResults = new ArrayList<>();
        try {
            rs = stmt.executeQuery("select * from " + DataBase.PEOPLE_TABLE
                    + " order by access_level desc");
            int row = 0;
            while (rs.next()) {
                row++;
                switch (Person.accessType.valueOf(rs.getString("access_level"))) {
                    case STUDENT:
                        allResults.add(new Student(rs, row));
                        break;
                    case EMPLOYER:
                        allResults.add(new Employer(rs, row));
                        break;
                    case PROFESSIONAL:
                        allResults.add(new Professional(rs, row));
                        break;
                }
            }

            // Output all fields of each element in ArrayList
            for (Person elem : allResults) {
                System.out.println("-------------------------------------");
                System.out.println(elem + "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("\n ** All Profiles Listed Above **\n\n");
        return allResults;
    }

    // For each record in Posting Table of Database, determine type, create object of that
    // type, and add to ArrayList
    public static ArrayList<Posting> postingsToArray(Statement stmt) {
        ResultSet rs;
        ArrayList<Posting> allResults = new ArrayList<>();
        try {
            rs = stmt.executeQuery("select * from " + DataBase.POSTINGS_TABLE
                    + " order by posting_type desc");
            int row = 0;
            while (rs.next()) {
                row++;
                switch (Posting.postingType.valueOf(rs.getString("posting_type"))) {
                    case INTERNSHIP:
                        allResults.add(new PostingIntrn(rs, row));
                        break;
                    case REGULAR:
                        allResults.add(new PostingReg(rs, row));
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return allResults;
    }

    // Iterate through ArrayList of postings and output a selection number with each one
    public static void viewAllPostings(ArrayList<Posting> allResults) {
        int i = 0;
        for (Posting elem : allResults) {
            i++;
            System.out.println("\n-------------------------------------");
            System.out.printf("          Posting #%d\n", i);
            System.out.println("-------------------------------------");
            System.out.println(elem + "");
        }
        System.out.println("\n ** All Postings Listed Above **\n\n");
    }
} // END Menu class
