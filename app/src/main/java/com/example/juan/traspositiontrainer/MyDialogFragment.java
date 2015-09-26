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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Juan on 16/07/2015.
 */
//Este es el popup que va a saltar cuando la persona toque en el icono Settings de la barra superior

public class MyDialogFragment  extends DialogFragment {

 String music;
    View divisoryLine7ths;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.settings_layout, null);
        final SharedPreferences pref = getActivity().getSharedPreferences("Mypref",0);

        //creo las variables de los spinners
        final Spinner music_spinner=(Spinner) layout.findViewById(R.id.music_spinner);
        final Spinner sound_spinner=(Spinner) layout.findViewById(R.id.sounds_spinner);
        final Spinner game_time_spinner=(Spinner)layout.findViewById(R.id.game_time_spinner);
        final Spinner game_difficulty_spinner=(Spinner)layout.findViewById(R.id.game_difficulty_spinner);
        final Spinner answer_time_spinner=(Spinner)layout.findViewById(R.id.answer_time_spinner);
        final Spinner with_7ths_spinner=(Spinner)layout.findViewById(R.id.w_7th_spinner);
        final Spinner key_spinner=(Spinner)layout.findViewById(R.id.key_spinner);
        final Spinner scale_spinner=(Spinner)layout.findViewById(R.id.scale_spinner);

        //divisory line to hide from style if hard mode get selected to avoid stacking of divisory lines on the design
        divisoryLine7ths=(View) layout.findViewById(R.id.divisoryLine7ths);

        final TextView scaleText= (TextView) layout.findViewById(R.id.scale_text);
        final TextView w_7_text=(TextView) layout.findViewById(R.id.w_7_text);
        //setear los spinners al valor que tenga SharedPreferences

        music_spinner.setSelection(pref.getInt("musicPosition",0));
        sound_spinner.setSelection(pref.getInt("soundPosition",0));
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

        key_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Si el spinner de Key esta seteado en Random esconde el titulo del spinner de Scale y el spinner de Scale
                if (position == 0) {
                    scale_spinner.setVisibility(View.GONE);
                    scaleText.setVisibility(View.GONE);
                } else {
                    scale_spinner.setVisibility(View.VISIBLE);
                    scaleText.setVisibility(View.VISIBLE);

                    //le limito las opciones que va a ver en el spinner de escalas según lo que seleccionó:
                    //si gameDifficulty  easy+ Key!=random--->Scale solo mayor
                    //si gameDifficulty  medium+ Key!=random--->Scale solo mayor y menor
                    //si gameDifficulty  hard+ Key!=random--->Scale Las 4 opciones, quedan las opciones que se setean en el layout desde el archivo string, aparecen todas las opciones, no es necesario especificarlo acá

                    String difficulty = game_difficulty_spinner.getSelectedItem().toString();

                    if(difficulty.equals("Easy"))
                    {
                        ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scale_spinner_easy));
                        scale_spinner.setAdapter(scaleSpinnerAdapter);

                    }else if(difficulty.equals("Normal")){
                        ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scale_spinner_medium));
                        scale_spinner.setAdapter(scaleSpinnerAdapter);
                    } else if(difficulty.equals("Hard")){
                        ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scale_spinner));
                        scale_spinner.setAdapter(scaleSpinnerAdapter);

                    }


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }


        });

        game_difficulty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Si el spinner de Key esta seteado en Hard esconde el Spinner de con 7mas o sin séptimas
                if (position == 2) {
                    with_7ths_spinner.setVisibility(View.GONE);
                    w_7_text.setVisibility(View.GONE);
                } else {
                    with_7ths_spinner.setVisibility(View.VISIBLE);
                    w_7_text.setVisibility(View.VISIBLE);

                }

                String difficulty = game_difficulty_spinner.getSelectedItem().toString();

                if(difficulty.equals("Easy"))
                {
                    ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scale_spinner_easy));
                    scale_spinner.setAdapter(scaleSpinnerAdapter);
                    divisoryLine7ths.setVisibility(View.VISIBLE);
                }else if(difficulty.equals("Normal")){
                    ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scale_spinner_medium));
                    scale_spinner.setAdapter(scaleSpinnerAdapter);
                    divisoryLine7ths.setVisibility(View.VISIBLE);
                } else if(difficulty.equals("Hard")){
                    ArrayAdapter<String> scaleSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scale_spinner));
                    scale_spinner.setAdapter(scaleSpinnerAdapter);
                    divisoryLine7ths.setVisibility(View.GONE);

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

                music=(String) music_spinner.getSelectedItem().toString();
                String sounds=(String) sound_spinner.getSelectedItem().toString();
                String gameTime = (String) game_time_spinner.getSelectedItem().toString();
                String gameDifficulty = (String) game_difficulty_spinner.getSelectedItem().toString();
                String answerTime = (String) answer_time_spinner.getSelectedItem().toString();
                String with7ths=(String)with_7ths_spinner.getSelectedItem().toString();
                String key = (String) key_spinner.getSelectedItem().toString();
                String scale = (String) scale_spinner.getSelectedItem().toString();

                int musicPosition= music_spinner.getSelectedItemPosition();
                int soundPosition= sound_spinner.getSelectedItemPosition();
                int gameTimePosition= game_time_spinner.getSelectedItemPosition();
                int gameDifficultyPosition= game_difficulty_spinner.getSelectedItemPosition();
                int answerTimePosition= answer_time_spinner.getSelectedItemPosition();
                int with7thsPosition=with_7ths_spinner.getSelectedItemPosition();
                int keyPosition= key_spinner.getSelectedItemPosition();
                int scalePosition= scale_spinner.getSelectedItemPosition();

                //guardo en sharedPreferences los valores que quedaron seteados en los spinners cuando el usuario tocó OK
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("music", music);
                editor.putString("sound", sounds);
                editor.putString("gameTime", gameTime);
                editor.putString("gameDifficulty", gameDifficulty);
                editor.putString("answerTime", answerTime);
               editor.putString("with7ths",with7ths);
                editor.putString("key", key);
                editor.putString("scale", scale);

                editor.putInt("musicPosition",musicPosition);
                editor.putInt("soundPosition",soundPosition);
                editor.putInt("gameTimePosition", gameTimePosition);
               editor.putInt("gameDifficultyPosition",gameDifficultyPosition);
               editor.putInt("answerTimePosition",answerTimePosition);
                editor.putInt("with7thsPosition",with7thsPosition);
               editor.putInt("keyPosition",keyPosition);
               editor.putInt("scalePosition",scalePosition);

                editor.commit();

//prueba consultas base


                Toast.makeText(getActivity(), R.string.settingsSavedMsg, Toast.LENGTH_SHORT).show();

            }
        });

        // Texto para el bot�n de cancelar
        theDialog.setNegativeButton(R.string.cancelSettingsFragmText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //que hacer si el usuario toca cancel, no guardo nada, las preferencias quedan como estaban
                Toast.makeText(getActivity(), "Clicked Cancel", Toast.LENGTH_SHORT).show();

            }
        });


        return theDialog.create();

    }


}