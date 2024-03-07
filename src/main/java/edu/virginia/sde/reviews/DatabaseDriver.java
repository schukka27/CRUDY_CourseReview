package edu.virginia.sde.reviews;

import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;

public class DatabaseDriver { // used database driver from last assignment to base opening a database
        private String sqliteFilename;
        private Connection connection;

        public DatabaseDriver(String filename) throws SQLException {
            sqliteFilename = filename;
            if (!doesDatabaseExist()) {
                createDatabase();
            }
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
            } catch (SQLException error) {
                System.out.println("Connection to database failed. Please try again");
            }
        }
        private boolean doesDatabaseExist() {
            File file = new File(sqliteFilename);
            return file.exists();
        }
        public Connection connect() throws SQLException {
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            connection.setAutoCommit(false);
            return connection;
        }
        public void close(){
            try{
                if (connection != null && !connection.isClosed()) {
                    connection.commit();
                    connection.close();
                }
            } catch (SQLException e) {
                System.out.println("error closing" + e.getMessage());
            }
        }
        public void createDatabase() throws SQLException{ // chatgpt prompt: creating sql new database directly on intellij
            try{
                    connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
                    Statement sql = connection.createStatement();
                    String createUserTable = "CREATE TABLE IF NOT EXISTS credentials ("
                            + "username TEXT PRIMARY KEY,"
                            + "password TEXT NOT NULL"
                            + ");";

                    String createCourseTable = "CREATE TABLE IF NOT EXISTS courses ("
                            + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "mnemonic TEXT NOT NULL,"
                            + "courseNumber INTEGER NOT NULL,"
                            + "courseName TEXT NOT NULL,"
                            + "courseRating DOUBLE NOT NULL"
                            +");";
                    String createReviewsTable = "CREATE TABLE IF NOT EXISTS reviews ("
                            + "timestamp TEXT PRIMARY KEY,"
                            + "courseID INTEGER NOT NULL REFERENCES courses(ID) ON DELETE CASCADE,"
                            + "user TEXT NOT NULL REFERENCES credentials(username) ON DELETE CASCADE,"
                            + "courseRating DOUBLE NOT NULL,"
                            + "reviewComment TEXT"
                            +");";
                    //course, timestam(primary), rating, user, review comment

                    sql.execute(createUserTable);
                    sql.execute(createCourseTable);
                    sql.execute(createReviewsTable);
                    System.out.println("New Databases Created Successfully");

            } catch (SQLException error){
                System.out.println("Error creating SQL Database");
           }

        }




}

