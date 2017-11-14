package com.iogb.sheduletown;

import java.io.Serializable;

/**
 * Created by Irvin Omar Galindo Becerra on 31/10/2017.
 */

public class Employee implements Serializable {
    private String idEmployee;
    private String password;
    private String fullName;
    private int type;
    private String[] shedule;

    public Employee() {
        idEmployee="";
        password="";
        type=0;

    }

    public String getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(String idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String[] getShedule() {
        return shedule;
    }

    public void setShedule(String[] shedule) {
        this.shedule = shedule;
    }


}
