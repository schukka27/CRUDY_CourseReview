package edu.virginia.sde.reviews;

public class CurrentCourse {
    private static Course course;

    public static Course getCourse() {
        return course;
    }

    public static void setCourse(Course course) {
        CurrentCourse.course = course;
    }
}
