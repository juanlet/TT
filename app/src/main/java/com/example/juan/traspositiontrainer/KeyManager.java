package com.example.juan.traspositiontrainer;


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

    private static final String DATABASE_NAME = "TranspositionT";
    private static final int DATABASE_VERSION = 1;
    SQLiteDatabase db = getReadableDatabase();
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

            result=this.buildArrayListQuiz(notes);

        }

        else{//el usuario seleccionó para practicar una escala en particular

            notes = db.rawQuery("Select sd.description scale_description,k.degree degree,kd.description key_description,nd.description note_description" +
                    " from keys k, keys_description kd, note_description nd, scales_description sd" +
                    " where k.id_key=kd.id_key and k.id_note=nd.id_note and k.id_scale=sd.id_scale and sd.description='"+scale+"'" +
                    " and kd.description='"+key+"'", null);

             result=this.buildArrayListQuiz(notes);
        }


        return result;
    }

    //build the ArrayList to return to the client Class Game
    private ArrayList<MusicSQLRow> buildArrayListQuiz(Cursor notes) {

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
        packedArrayList.size();
        return packedArrayList;

    }

    public ArrayList<MusicSQLRow> getChords(){
        ArrayList<MusicSQLRow>  chords=null;


        return chords;
    }

}
