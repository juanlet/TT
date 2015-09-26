package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class StudyRoom extends AppCompatActivity {
    Menu activityMenu;
    KeyManager db;
    ArrayList<MusicSQLRow> retrievedNotes=null,retrievedChords=null;
    Spinner findKeySpinner,findScaleSpinner,findWith7thspinner;
    String selectedKey=null,selectedScale=null, selectedW7ths=null;
    TextView note1,note2,note3,note4,note5,note6,note7,chord1,chord2,chord3,chord4,chord5,chord6,chord7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyroom);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        note1=(TextView) findViewById(R.id.note1);
                note2=(TextView) findViewById(R.id.note2);
                note3=(TextView) findViewById(R.id.note3);
                note4=(TextView) findViewById(R.id.note4);
                note5=(TextView) findViewById(R.id.note5);
                note6=(TextView) findViewById(R.id.note6);
                note7=(TextView) findViewById(R.id.note7);
                chord1=(TextView) findViewById(R.id.chord1);
                chord2=(TextView) findViewById(R.id.chord2);
                chord3=(TextView) findViewById(R.id.chord3);
                chord4=(TextView) findViewById(R.id.chord4);
                chord5=(TextView) findViewById(R.id.chord5);
                chord6=(TextView) findViewById(R.id.chord6);
                chord7=(TextView) findViewById(R.id.chord7);


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

        findWith7thspinner= (Spinner) findViewById(R.id.find_chords_w_7ths);
        ArrayAdapter<CharSequence> findWith7thsSpinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.key_spinner_search_7ths, R.layout.spinner_layout_studyroom);
        findWith7thsSpinnerAdapter.setDropDownViewResource(R.layout.spinner_layout_studyroom);
        findWith7thspinner.setAdapter(findWith7thsSpinnerAdapter);

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
        selectedW7ths=findWith7thspinner.getSelectedItem().toString();

        //search Chords and notes and show it on the 14 textViews
        retrievedNotes=null;
        retrievedChords=null;
        retrievedNotes=db.getNotes(selectedKey,selectedScale);
        retrievedChords=db.getChords(selectedKey,selectedScale);

      //cycle through both array list and fill the 14 textviews for the 7 notes and the 7 chords of that particular scale in that particular key



// fill the textviews with just one loop for the two lists retrievedNotes and retrievedChords
        /* retrievedNotes.get(i);
         retrievedChords.get(i);*/

        note1.setText(retrievedNotes.get(0).getNoteName());
        note2.setText(retrievedNotes.get(1).getNoteName());
        note3.setText(retrievedNotes.get(2).getNoteName());
        note4.setText(retrievedNotes.get(3).getNoteName());
        note5.setText(retrievedNotes.get(4).getNoteName());
        note6.setText(retrievedNotes.get(5).getNoteName());
        note7.setText(retrievedNotes.get(6).getNoteName());


if(selectedW7ths.equals("7ths")) {
    chord1.setText(retrievedChords.get(0).getChordWith7().replaceAll("\\s+",""));
    chord2.setText(retrievedChords.get(1).getChordWith7().replaceAll("\\s+", ""));
    chord3.setText(retrievedChords.get(2).getChordWith7().replaceAll("\\s+", ""));
    chord4.setText(retrievedChords.get(3).getChordWith7().replaceAll("\\s+", ""));
    chord5.setText(retrievedChords.get(4).getChordWith7().replaceAll("\\s+", ""));
    chord6.setText(retrievedChords.get(5).getChordWith7().replaceAll("\\s+", ""));
    chord7.setText(retrievedChords.get(6).getChordWith7().replaceAll("\\s+",""));
}
        else{
    chord1.setText(retrievedChords.get(0).getChordWithout7().replace("null", ""));
    chord2.setText(retrievedChords.get(1).getChordWithout7().replace("null", ""));
    chord3.setText(retrievedChords.get(2).getChordWithout7().replace("null", ""));
    chord4.setText(retrievedChords.get(3).getChordWithout7().replace("null", ""));
    chord5.setText(retrievedChords.get(4).getChordWithout7().replace("null", ""));
    chord6.setText(retrievedChords.get(5).getChordWithout7().replace("null", ""));
    chord7.setText(retrievedChords.get(6).getChordWithout7().replace("null", ""));
        }
    }
}
