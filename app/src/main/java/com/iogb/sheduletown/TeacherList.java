package com.iogb.sheduletown;

/**
 * Created by L55-C5211R on 08/11/2017.
 */

public class TeacherList {
    private String classroom;
    private String subject;
    private String hour;

    public TeacherList(String classroom, String subject, String hour) {
        this.classroom = classroom;
        this.subject = subject;
        this.hour = hour;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
