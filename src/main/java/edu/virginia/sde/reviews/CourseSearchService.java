package edu.virginia.sde.reviews;

import javafx.scene.control.TableRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseSearchService {
    private DatabaseDriver databaseDriver;

    public CourseSearchService() throws SQLException {
        try{
            databaseDriver = new DatabaseDriver("userinfo");
        } catch(SQLException error) {
            System.out.println("Error connecting to database");
        }

    }
    public boolean addCourse(String department, int courseNumber, String courseName) throws SQLException {

        try{
            if (department == null ||  courseNumber == 0 || courseName == null){
                return false;
            }
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatementInitial = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic COLLATE NOCASE = ? AND courseNumber = ? AND courseName COLLATE NOCASE = ?");
            preparedStatementInitial.setString(1,department);
            preparedStatementInitial.setInt(2,courseNumber);
            preparedStatementInitial.setString(3,courseName);
            var row = preparedStatementInitial.executeQuery();
            if (row.next()) {
                return false;
            }
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO courses (mnemonic,courseNumber,courseName,courseRating) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, department);
            preparedStatement.setInt(2, courseNumber);
            preparedStatement.setString(3, courseName);
            preparedStatement.setDouble(4, 0.0);
            preparedStatement.executeUpdate();
        } catch (SQLException error){
            System.out.println("Error adding course");

        }
        finally {
            databaseDriver.close();
        }
        return true;
    }
    public ArrayList<Course> getCourseDepartment(String department) throws SQLException {
        ArrayList<Course> output = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic COLLATE NOCASE = ?");
            preparedStatement.setString(1, department);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                output.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return output;
    }

    public ArrayList<Course> getCourseNumber(int coursenumber) throws SQLException {
        ArrayList<Course> output = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE courseNumber = ?");
            preparedStatement.setInt(1, coursenumber);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                output.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return output;
    }

    public ArrayList<Course> getCourseName(String name) throws SQLException {
        ArrayList<Course> output = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE courseName COLLATE NOCASE LIKE '%' || ? || '%'");
            preparedStatement.setString(1, name);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                output.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return output;
    }

    public ArrayList<Course> getAllCourses() {
        ArrayList<Course> output = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses");
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                output.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return output;
    }

    public ArrayList<Course> getMnemonicCourseNum(String department, int coursenumber) {
        ArrayList<Course> values = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic COLLATE NOCASE = ? AND courseNumber = ?");
            preparedStatement.setString(1, department);
            preparedStatement.setInt(2, coursenumber);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                values.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return values;
    }

    public ArrayList<Course> getCourseNumCourseName(int coursenumber, String name) {
        ArrayList<Course> values = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE courseNumber = ? AND courseName COLLATE NOCASE LIKE '%' || ? || '%'");
            preparedStatement.setInt(1, coursenumber);
            preparedStatement.setString(2, name);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                values.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return values;
    }

    public ArrayList<Course> getMnemonicandName(String mnemonic, String name) {
        ArrayList<Course> values = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic COLLATE NOCASE = ? AND courseName COLLATE NOCASE LIKE '%' || ? || '%'");
            preparedStatement.setString(1, mnemonic);
            preparedStatement.setString(2, name);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                values.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return values;
    }

    public ArrayList<Course> getCourse(String department, int coursenumber, String name) {
        ArrayList<Course> values = new ArrayList<>();
        try {
            databaseDriver = new DatabaseDriver("userinfo");
            Connection connection = databaseDriver.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM courses WHERE mnemonic COLLATE NOCASE = ? AND courseNumber = ? AND courseName COLLATE NOCASE LIKE '%' || ? || '%'");
            preparedStatement.setString(1, department);
            preparedStatement.setInt(2, coursenumber);
            preparedStatement.setString(3, name);
            ResultSet row = preparedStatement.executeQuery();
            while (row.next()) {
                Course course = new Course(row.getString("mnemonic"), row.getInt("courseNumber"), row.getString("courseName"), row.getDouble("courseRating"));
                values.add(course);
            }
        } catch (SQLException error) {
            System.out.println("Error parsing courses");
        } finally {
            databaseDriver.close();
        }
        return values;
    }
}
