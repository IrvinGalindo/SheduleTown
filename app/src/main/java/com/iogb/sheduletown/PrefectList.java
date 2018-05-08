package com.iogb.sheduletown;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by L55-C5211R on 15/11/2017.
 */

public class PrefectList {
    private String nameTeacher;
    private int assistents;
    private Bitmap photo;
    private String classroom;
    private String idEmpleado;
    private int idHorario;

    public PrefectList(String idEmpleado,int idHorario,String nameTeacher,String classroom) {
        this.idEmpleado=idEmpleado;
        this.idHorario=idHorario;
        this.nameTeacher = nameTeacher;
        this.classroom=classroom;
        assistents=0;
    }

    public String getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(String idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getNameTeacher() {
        return nameTeacher;
    }

    public void setNameTeacher(String nameTeacher) {
        this.nameTeacher = nameTeacher;
    }

    public int isAssistents() {
        return assistents;
    }

    public void setAssistents(int assistents) {
        this.assistents = assistents;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(String data)
    {
        try {
            byte[] byteData = Base64.decode(data, Base64.DEFAULT);
            this.photo = BitmapFactory.decodeByteArray( byteData, 0,
                    byteData.length);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
