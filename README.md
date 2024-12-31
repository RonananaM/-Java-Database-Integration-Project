Java Database Integration Project
=================================

* A “Job Tracking” program  written in Java - Using Object Oriented Inheritance 
     and Database Connection.  

* The Code directory contains the 10 classes and Main, which comprise the final program.  

* The Database Dump directory contains the MySQL Table definitions and insertions to 
     recreate the database.  In addition, the database tables can be found as CSV files 
     in the CSVData subdirectory.  



 This program demonstrates the use of: 
======================================

 Database:
----------
      Utilizes MySQL database connection and dynamically builds SQL statements
          across as much as three levels of inheritance, allowing modification
          and deletion of records from database;  
          
      Performs verification of user email as simulation of login, immediately
          identifying which subclass the user is, creating a new object of that
          type, and placing all relevant fields from the Database into that object
          to be used as the CurrentUser for the remainder of the session;  
          
      Uses the database to police unique email values as well as assign unique
          user IDs (key);  
          
      Retrieves record sets - could easily be enhanced by use of various
          specific WHERE queries built with user input variables to allow
          search functionality from Database;  
          

 Enumeration:
-------------
      Uses Enumeration in both base classes - Person and Posting - to guarantee
          data integrity within critical fields;  
          
      Uses Enumeration within the MySQL Database structure to allow enumerated values
          to be processed and evaluated properly;  
          

 Polymorphism:
--------------
      Reads in a RecordSet from database, identifies subtype, builds new object
          of that type and stores it into an ArrayList of the Base class type;  
          
      Iterates through ArrayList and uses overridden .toString to output all
          elements in the proper format for their individual class.  
          

 Database class:
----------------
          Allows for cleanliness and modularity of database connection;  
          
          Database name, table names, port number, passwords, etc are stored in
          variables which can be accessed and modified quickly; Variables are then used
          to create SQL statement string and create a connection object which is passed;  





 Inheritance:
-------------
      PERSON: Abstract Class for all Person Profiles. Contains methods that are overriden
              in subclasses and chained together to handle each set of fields for the
              particular class it is in;  

          EMPLOYER:  Subclass of Person for Profiles of Employers. Allowed to View, Create,
                  Update, and Delete all job Postings; Can Create, Update, and Delete
                  own Profile;  

          APPLICANT: Abstract Class inheriting from PERSON; all Subclasses of applicant
                  can View job Postings; Can Create, Update, and Delete own Profile;   

              STUDENT: Subclass of Applicant; Intended to contain students seeking
                  internships;  

              REGULAR: Subclass of Applicant; Intended to contain experienced job seekers;  


      POSTING: Abstract class for all Postings;  

          INTERNSHIP: Subclass of Posting to hold postings related to internships and
              college students;  

          REGULAR: Subclass of Posting to hold postings related to experienced job
              seekers;  



 Data Validation and Formatting:
--------------------------------
      All user input, including email addresses, is checked for valid format and/or
          stripped of special characters before being allowed to be assigned to fields
          in an attempt to stop possible SQL injection;  

      Some fields, such as phone number, have output formatted by getter for
          cleaner output and better user experience;  
