package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
//comment3

    MediaPlayer introSong;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String music,sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_main);
        //to make sure if the user turn downs volume is only multimedia volume and not call volume, only if music is set to yes

        introSong=null;

        pref= this.getSharedPreferences("Mypref", 0);

        editor = pref.edit();

        music=pref.getString("music", null);
        sound=pref.getString("sound", null);

        if(music == null || sound == null)
        {
            editor.putString("music", "Yes");
            editor.commit();

            music="Yes";
        }

        if(music.equals("Yes")) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            introSong = MediaPlayer.create(MainActivity.this, R.raw.intromusic);
            introSong.setLooping(true);
            introSong.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        music=pref.getString("music", null);

        if(music.equals("No")){
            killMusic();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMusic();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //add this to be sure that resources of previous mediaplayers are released
        killMusic();
        if(music.equals("Yes")) {
        introSong=MediaPlayer.create(MainActivity.this,R.raw.intromusic);
        introSong.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        killMusic();
    }

    private void killMusic(){
        if (introSong != null) {

            introSong.release();
        }
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
            killMusic();
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

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                // the context of the activity
                MainActivity.this,null);
        ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());

      //  startActivity(intent);

    }



    public void goToFaq(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Faq.class);

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                // the context of the activity
                MainActivity.this,null);
        ActivityCompat.startActivity(MainActivity.this, intent, options.toBundle());

       // startActivity(intent);

    }



}
