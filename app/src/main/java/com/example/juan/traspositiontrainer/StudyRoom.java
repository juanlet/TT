package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class StudyRoom extends AppCompatActivity {
    Menu activityMenu;
    KeyManager db;
    ArrayList<MusicSQLRow> retrievedNotes=null,retrievedChords=null;
    Spinner findKeySpinner,findScaleSpinner;
    String selectedKey=null,selectedScale=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        findKeySpinner = (Spinner) findViewById(R.id.find_key);
        ArrayAdapter<CharSequence> findKeySpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.key_spinner_search, R.layout.spinner_layout_studyroom);
        findKeySpinnerAdapter.setDropDownViewResource(R.layout.spinner_layout_studyroom);
        findKeySpinner.setAdapter(findKeySpinnerAdapter);

        findScaleSpinner = (Spinner) findViewById(R.id.find_scale);
        ArrayAdapter<CharSequence> findScaleSpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.scale_spinner, R.layout.spinner_layout_studyroom);
        findScaleSpinnerAdapter.setDropDownViewResource(R.layout.spinner_layout_studyroom);
        findScaleSpinner.setAdapter(findScaleSpinnerAdapter);

        db=new KeyManager(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        activityMenu=menu;
        hideItemMenu();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            DialogFragment myFragment = new MyDialogFragment();

            myFragment.show(getFragmentManager(), "theDialog");

            return true;
        }else if (id== R.id.exit_app){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideItemMenu(){


        //escondo botón de pausa
        MenuItem pauseMenuItem = activityMenu.findItem(R.id.action_settings);
        pauseMenuItem.setVisible(false);

    }

    public void searchNotesAndChords(View view) {

        selectedKey=findKeySpinner.getSelectedItem().toString();
        selectedScale=findScaleSpinner.getSelectedItem().toString();

        //search Chords and notes and show it on the 14 textViews
        retrievedNotes=null;
        retrievedChords=null;
        retrievedNotes=db.getNotes(selectedKey,selectedScale);
        retrievedChords=db.getChords(selectedKey,selectedScale);

      //cycle through both array list and fill the 14 textviews for the 7 notes and the 7 chords of that particular scale in that particular key


        for (int i = 0; i < retrievedNotes.size(); i++) {
// fill the textviews with just one loop for the two lists retrievedNotes and retrievedChords
        /* retrievedNotes.get(i);
         retrievedChords.get(i);*/


        }
    }
}
