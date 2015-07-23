package com.example.juan.traspositiontrainer;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

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



    public Cursor getNotes(){

        Cursor notes=null;

        if(key.equals("Random")){

            notes = db.rawQuery("Select sd.description,k.degree,kd.description,nd.description " +
                    "from keys k, keys_description kd, note_description nd, scales_description sd " +
                    "where k.id_key=kd.id_key and k.id_note=nd.id_note and id_scale="+this.scaleSelectionDifficulty(gameDifficulty), null);

        }

        else{//el usuario seleccionó para practicar una escala en particular

            notes = db.rawQuery("Select sd.description,k.degree,kd.description,nd.description" +
                    " from keys k, keys_description kd, note_description nd, scales_description sd" +
                    " where k.id_key=kd.id_key and k.id_note=nd.id_note and k.id_scale=sd.id_scale and sd.description='"+scale+"'" +
                    " and kd.description='"+key+"'", null);

        }


        return notes;
    }

    public Cursor getChords(){
        Cursor chords=null;


        return chords;
    }

}
