package com.example.juan.traspositiontrainer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Juan on 16/07/2015.
 */
//Este es el popup que va a saltar cuando la persona toque en el icono Settings de la barra superior
    // se va a elegir a)El timer de 5 o 10 segundos
   //  b)La tonalidad a elegir(una de las opciones va a ser modo random, que va a ser el modo por default)
   //  c)Nivel de dificultad(Facil:solo escala mayor, Normal: Escala Mayor y menor natural,
   //  Dificil: Escala Mayor, Escala Menor natural, Escala Menor Arm�nica, Escala Menor Mel�dica)
public class MyDialogFragment  extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.settings_layout, null);

        Spinner game_time_spinner=(Spinner)layout.findViewById(R.id.game_time_spinner);
        Spinner game_difficulty_spinner=(Spinner)layout.findViewById(R.id.game_difficulty_spinner);
        Spinner answer_time_spinner=(Spinner)layout.findViewById(R.id.answer_time_spinner);
        Spinner key_spinner=(Spinner)layout.findViewById(R.id.key_spinner);
        final Spinner scale_spinner=(Spinner)layout.findViewById(R.id.scale_spinner);

        // Se crea el popUp
        // getActivity() returna a que pantalla esta asociada este popup(tambi�n llamado fragment)
        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());

        // T�tulo del popup
        theDialog.setTitle("Game Settings");

        // Contenido del popup
        //theDialog.setMessage("en este lugar van los spinners con las preferencias de");

        theDialog.setView(layout);

        key_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position == 0){
                        scale_spinner.setVisibility(View.GONE);
                    } else {
                        scale_spinner.setVisibility(View.VISIBLE);
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
                //que hacer si el usuario toca OK, guardo en TinyDB las preferencias de usuario
                Toast.makeText(getActivity(), "Clicked OK", Toast.LENGTH_SHORT).show();

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