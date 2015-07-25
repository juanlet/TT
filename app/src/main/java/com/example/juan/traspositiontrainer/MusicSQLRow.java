package com.example.juan.traspositiontrainer;

/**
 * Created by Juan on 24/07/2015.
 */
public class MusicSQLRow {

    private String scaleName;
    private String degreeNumber;
    private String keyName;
    private String noteName;


    public MusicSQLRow(String scale,String degree, String key, String note) {

        scaleName=scale;
        degreeNumber=degree;
        keyName=key;
        noteName=note;

    }


    public String getScaleName() {
        return scaleName;
    }

    public String getDegreeNumber() {
        return degreeNumber;
    }

    public String getKeyName() {
        return keyName;
    }

    public String getNoteName() {
        return noteName;
    }
}
