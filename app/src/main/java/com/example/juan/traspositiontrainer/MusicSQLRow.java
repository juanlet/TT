package com.example.juan.traspositiontrainer;

/**
 * Created by Juan on 24/07/2015.
 */
public class MusicSQLRow {

    private String scaleName;
    private String degreeNumber;
    private String keyName;
    private String noteName;
    private String chordTypeWithout7;
    private String chordSymbol;


    public MusicSQLRow(String scale,String degree, String key, String note) {

        scaleName=scale;
        degreeNumber=degree;
        keyName=key;
        noteName=note;

    }

    public MusicSQLRow(String scale,String degree, String key, String note,String chordTypeWo7,String symbol){
        scaleName=scale;
        degreeNumber=degree;
        keyName=key;
        noteName=note;
        chordTypeWithout7=chordTypeWo7;
        chordSymbol=symbol;

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

    public String getChordWithout7() {
        return noteName+" "+chordTypeWithout7;
    }

    public String getChordWith7() {return noteName+" "+chordSymbol;}

    public String getChordTypeWithout7(){
    return chordTypeWithout7;
    }

    public String getChordTypeWith7(){
     return chordSymbol;
    }
}
