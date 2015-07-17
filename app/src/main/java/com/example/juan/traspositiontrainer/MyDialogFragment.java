package com.example.juan.traspositiontrainer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Juan on 16/07/2015.
 */
//Este es el popup que va a saltar cuando la persona toque en el icono Settings de la barra superior
    // se va a elegir a)El timer de 5 o 10 segundos
   //  b)La tonalidad a elegir(una de las opciones va a ser modo random, que va a ser el modo por default)
   //  c)Nivel de dificultad(Facil:solo escala mayor, Normal: Escala Mayor y menor natural,
   //  Dificil: Escala Mayor, Escala Menor natural, Escala Menor Armónica, Escala Menor Melódica)
public class MyDialogFragment extends DialogFragment{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Se crea el popUp
        // getActivity() returna a que pantalla esta asociada este popup(también llamado fragment)
        AlertDialog.Builder theDialog = new AlertDialog.Builder(getActivity());

        // Título del popup
        theDialog.setTitle("Game Settings");

        // Contenido del popup
        theDialog.setMessage("en este lugar van los spinners con las preferencias de usuario");

        // Botón ok
        theDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //que hacer si el usuario toca OK, guardo en TinyDB las preferencias de usuario
                Toast.makeText(getActivity(), "Clicked OK", Toast.LENGTH_SHORT).show();

            }
        });

        // Texto para el botón de cancelar
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