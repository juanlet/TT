package com.example.juan.traspositiontrainer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Juan on 16/07/2015.
 */
//Este es el popup que va a saltar cuando la persona toque en el icono Settings de la barra superior

public class MyDialogFragment  extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.settings_layout, null);
        final SharedPreferences pref = getActivity().getSharedPreferences("Mypref",0);

        //creo las variables de los spinners
        final Spinner game_time_spinner=(Spinner)layout.findViewById(R.id.game_time_spinner);
        final Spinner game_difficulty_spinner=(Spinner)layout.findViewById(R.id.game_difficulty_spinner);
        final Spinner answer_time_spinner=(Spinner)layout.findViewById(R.id.answer_time_spinner);
        final Spinner with_7ths_spinner=(Spinner)layout.findViewById(R.id.w_7th_spinner);
        final Spinner key_spinner=(Spinner)layout.findViewById(R.id.key_spinner);
        final Spinner scale_spinner=(Spinner)layout.findViewById(R.id.scale_spinner);

        final TextView scaleText= (TextView) layout.findViewById(R.id.scale_text);

        //setear los spinners al valor que tenga SharedPreferences

        game_time_spinner.setSelection(pref.getInt("gameTimePosition",0));
        game_difficulty_spinner.setSelection(pref.getInt("gameDifficultyPosition",0));
        answer_time_spinner.setSelection(pref.getInt("answerTimePosition",0));
        with_7ths_spinner.setSelection(pref.getInt("with7thsPosition",0));
        key_spinner.setSelection(pref.getInt("keyPosition",0));
        scale_spinner.setSelection(pref.getInt("scalePosition",0));


        // Se crea el popUp
        // getActivity() returna a que pantalla esta asociada este popup(tambi�n llamado fragment)
        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());

        // T�tulo del popup
        //theDialog.setTitle("Game Settings");

        // Contenido del popup
        //theDialog.setMessage("en este lugar van los spinners con las preferencias de");

        theDialog.setView(layout);

        key_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

  //Si el spinner de Key esta seteado en Random esconde el titulo del spinner de Scale y el spinner de Scale
                    if (position == 0){
                        scale_spinner.setVisibility(View.GONE);
                        scaleText.setVisibility(View.GONE);
                    } else {
                        scale_spinner.setVisibility(View.VISIBLE);
                        scaleText.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }
        });


        // Bot�n ok
        theDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //que hacer si el usuario toca OK

                String gameTime = (String) game_time_spinner.getSelectedItem().toString();
                String gameDifficulty = (String) game_difficulty_spinner.getSelectedItem().toString();
                String answerTime = (String) answer_time_spinner.getSelectedItem().toString();
                String with7ths=(String)with_7ths_spinner.getSelectedItem().toString();
                String key = (String) key_spinner.getSelectedItem().toString();
                String scale = (String) scale_spinner.getSelectedItem().toString();


                int gameTimePosition= game_time_spinner.getSelectedItemPosition();
                int gameDifficultyPosition= game_difficulty_spinner.getSelectedItemPosition();
                int answerTimePosition= answer_time_spinner.getSelectedItemPosition();
                int with7thsPosition=with_7ths_spinner.getSelectedItemPosition();
                int keyPosition= key_spinner.getSelectedItemPosition();
                int scalePosition= scale_spinner.getSelectedItemPosition();

                //guardo en sharedPreferences los valores que quedaron seteados en los spinners cuando el usuario tocó OK
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("gameTime", gameTime);
                editor.putString("gameDifficulty", gameDifficulty);
                editor.putString("answerTime", answerTime);
                editor.putString("key", key);
                editor.putString("with7ths",with7ths);
                editor.putString("scale", scale);
                editor.putString("scale", scale);


                editor.putInt("gameTimePosition", gameTimePosition);
               editor.putInt("gameDifficultyPosition",gameDifficultyPosition);
               editor.putInt("answerTimePosition",answerTimePosition);
                editor.putInt("with7thsPosition",with7thsPosition);
               editor.putInt("keyPosition",keyPosition);
               editor.putInt("scalePosition",scalePosition);

                editor.commit();

//prueba consultas base
                KeyManager db=new KeyManager(getActivity().getApplicationContext());

                Cursor c=db.getNotes();



                Toast.makeText(getActivity().getApplicationContext(), c.getCount()+ " registros devueltos" , Toast.LENGTH_LONG).show();



                Toast.makeText(getActivity(), "Settings Saved ", Toast.LENGTH_SHORT).show();

            }
        });

        // Texto para el bot�n de cancelar
        theDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //que hacer si el usuario toca cancel, no guardo nada, las preferencias quedan como estaban
                Toast.makeText(getActivity(), "Clicked Cancel", Toast.LENGTH_SHORT).show();

            }
        });


        return theDialog.create();

    }

 }