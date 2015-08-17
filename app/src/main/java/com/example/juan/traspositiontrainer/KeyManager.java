package com.example.juan.traspositiontrainer;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

/**
 * Created by Juan on 11/07/2015.
 */
public class KeyManager extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "Transpo";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db = getWritableDatabase();
    SharedPreferences pref;
    String gameDifficulty, key, scale,with7ths;

    public KeyManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       //recupero las SharedPreferences para hacer las preguntas acorde a esto
        pref= context.getSharedPreferences("Mypref", 0);
        gameDifficulty=pref.getString("gameDifficulty", "");
        key=pref.getString("key","");
        scale=pref.getString("scale","");
        with7ths=pref.getString("with7ths","");
    }

    //Easy - Escala Mayor
    //Normal - Escala Mayor, Escala Menor Natural
    //Hard - Escala Mayor, Escala Menor Natural, Escala Menor Armónica, Escala Menor Melódica

    //recibe un nivel de dificultad y devuelve que escalas sacar de la base
    private String scaleSelectionDifficulty(String difficulty){

        String resultado;

        if(difficulty.equals("Easy"))
            resultado="1";
        else if(difficulty.equals("Normal"))
            resultado="1,2";
        else //modo Hard
            resultado="1,2,3,4";
        return resultado;

    }


//returns notes
public ArrayList<MusicSQLRow> getNotes(){

    Cursor notes=null;
    ArrayList<MusicSQLRow> result;

    if(key.equals("Random")){

        notes = db.rawQuery("Select sd.description scale_description,k.degree degree,kd.description key_description,nd.description note_description " +
                "from keys k, keys_description kd, note_description nd, scales_description sd " +
                "where k.id_key=kd.id_key and k.id_note=nd.id_note and k.id_scale=sd.id_scale and k.id_scale in ("+this.scaleSelectionDifficulty(gameDifficulty)+")", null);

        result=this.buildArrayListQuizNotes(notes);

    }

    else{//el usuario seleccionó para practicar una escala en particular

        notes = db.rawQuery("Select sd.description scale_description,k.degree degree,kd.description key_description,nd.description note_description" +
                " from keys k, keys_description kd, note_description nd, scales_description sd" +
                " where k.id_key=kd.id_key and k.id_note=nd.id_note and k.id_scale=sd.id_scale and sd.description='"+scale+"'" +
                " and kd.description='"+key+"'", null);

        result=this.buildArrayListQuizNotes(notes);
    }

    notes.close();
    return result;
}

    //build the ArrayList to return to the client Class Game
    private ArrayList<MusicSQLRow> buildArrayListQuizNotes(Cursor notes) {

        ArrayList<MusicSQLRow> packedArrayList = new ArrayList<MusicSQLRow>();

        while (notes.moveToNext())
        {
            String scale = notes.getString(notes.getColumnIndex("scale_description"));
            String degree = notes.getString(notes.getColumnIndex("degree"));
            String key = notes.getString(notes.getColumnIndex("key_description"));
            String note = notes.getString(notes.getColumnIndex("note_description"));


            try
            {
                MusicSQLRow musicRow = new MusicSQLRow(scale,degree,key,note);
                packedArrayList.add(musicRow);
            }
            catch (Exception e) {
                Log.e("Error", "Error " + e.toString());
            }

        }
       //packedArrayList.size();
        return packedArrayList;

    }

    public ArrayList<MusicSQLRow> getChords(){
        Cursor chords=null;
        ArrayList<MusicSQLRow> result;

        if(key.equals("Random")){

            chords = db.rawQuery("Select sd.description scale_description,k.degree degree,kd.description key_description,nd.description note_description,  ctwo7.description chordTypeWithout7, ctw7.symbol chordSymbol "+
                    "from keys k "+
                    "left join keys_description kd on  k.id_key=kd.id_key "+
                    "left join note_description nd on  k.id_note=nd.id_note "+
                    "left join scales_description sd on  k.id_scale=sd.id_scale "+
                    "left join  scale_degree_chord_types sdct on k.id_scale=sdct.id_scale and k.degree=sdct.degree "+
                    "left join  chord_types_with_7th ctw7 on sdct.id_chord_type_with_7=ctw7.id_chord_type "+
                    "left join  chord_types_without_7th ctwo7 on sdct.id_chord_type_without_7=ctwo7.id_chord_type "+
                    "where "+
                    "k.id_scale in ("+this.scaleSelectionDifficulty(gameDifficulty)+")", null);

            result=this.buildArrayListQuizChords(chords);

        }

        else{//el usuario seleccionó para practicar una escala en particular

            chords = db.rawQuery("Select sd.description scale_description,k.degree degree,kd.description key_description,nd.description note_description,  " +
                    "ctwo7.description chordTypeWithout7, ctw7.symbol chordSymbol "+
                    "from keys k "+
                    "left join keys_description kd on  k.id_key=kd.id_key "+
                    "left join note_description nd on  k.id_note=nd.id_note "+
                    "left join scales_description sd on  k.id_scale=sd.id_scale "+
                    "left join  scale_degree_chord_types sdct on k.id_scale=sdct.id_scale and k.degree=sdct.degree "+
                    "left join  chord_types_with_7th ctw7 on sdct.id_chord_type_with_7=ctw7.id_chord_type "+
                    "left join  chord_types_without_7th ctwo7 on sdct.id_chord_type_without_7=ctwo7.id_chord_type "+
                    " where " +
                    " sd.description='"+scale+"' " +
                    " and kd.description='"+key+"'", null);

            result=this.buildArrayListQuizChords(chords);
        }

        chords.close();
        return result;
    }


    //build the ArrayList to return to the client Class Game
    private ArrayList<MusicSQLRow> buildArrayListQuizChords(Cursor chords) {

        ArrayList<MusicSQLRow> packedArrayList = new ArrayList<MusicSQLRow>();

        while (chords.moveToNext())
        {
            String scale = chords.getString(chords.getColumnIndex("scale_description"));
            String degree = chords.getString(chords.getColumnIndex("degree"));
            String key = chords.getString(chords.getColumnIndex("key_description"));
            String note = chords.getString(chords.getColumnIndex("note_description"));
            String chordTypeWithout7= chords.getString(chords.getColumnIndex("chordTypeWithout7"));
            String chordSymbol=chords.getString(chords.getColumnIndex("chordSymbol"));

            try
            {
                MusicSQLRow musicRow = new MusicSQLRow(scale,degree,key,note, chordTypeWithout7, chordSymbol);
                packedArrayList.add(musicRow);
            }
            catch (Exception e) {
                Log.e("Error", "Error " + e.toString());
            }

        }
        //packedArrayList.size();
        return packedArrayList;

    }

    public void updateStats(int right_ammount, int wrong_ammount, String game_type){
/*
        ContentValues args = new ContentValues();

        args.put("right_answers",right_ammount);
        args.put("wrong_answers",wrong_ammount);

        db.update("user_stats",args,"game_type='Chord_Quiz'" , null);
*/
    }



}
