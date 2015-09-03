package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
//comment3

    MediaPlayer introSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //to make sure if the user turn downs volume is only multimedia volume and not call volume
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        introSong=MediaPlayer.create(MainActivity.this,R.raw.intromusic);
        introSong.setLooping(true);
        introSong.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //add this to be sure that resources of previous mediaplayers are released
        if (introSong != null) {
            introSong.release();
        }
        introSong=MediaPlayer.create(MainActivity.this,R.raw.intromusic);
        introSong.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        introSong.release();
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
