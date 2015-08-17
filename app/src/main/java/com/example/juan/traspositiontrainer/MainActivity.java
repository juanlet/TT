package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
//comment3


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private Intent setIntentExtra(int idActivity, Intent intent) {

        switch (idActivity) {
            case R.id.notes_quiz:
                // do something
                intent.putExtra("ButtonPressed", "notes_quiz");
                break;
            case R.id.chords_quiz:
                intent.putExtra("ButtonPressed", "chord_quiz");

                break;
        }

          return intent;
    }

    public void goToGame(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Game.class);

       this.setIntentExtra(view.getId(),intent);

        startActivity(intent);

    }



    public void goToFaq(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Faq.class);

        startActivity(intent);

    }



}
