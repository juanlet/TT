package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends AppCompatActivity {
//comment3
private static SoundPool mySounds;
    MediaPlayer introSong;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String music,sound;
    int buttonClickSoundID;

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

        String gameDifficulty = pref.getString("gameDifficulty", null);
        String key=pref.getString("key", null);
        String scale=pref.getString("scale", null);
        String with7ths=pref.getString("with7ths", null);
        String gameTimePref=pref.getString("gameTime",null);
        String answerTimePref=pref.getString("answerTime",null);

        music=pref.getString("music", null);
        sound=pref.getString("sound", null);

        if(gameDifficulty == null)
        {
            editor.putString("music", "Yes");
            editor.putString("sound", "Yes");
            editor.putString("gameTime", "5 minutes");
            editor.putString("gameDifficulty", "Easy");
            editor.putString("answerTime", "30 seconds");
            editor.putString("with7ths","Without 7ths");
            editor.putString("key", "Random");
            editor.putString("scale", "C");
            editor.commit();

            music="Yes";
            sound="Yes";
            gameDifficulty="Easy";
            key="Random";
            with7ths="Without 7ths";
            gameTimePref="5 minutes";
            answerTimePref="30 seconds";
        }

    playMusic();
    loadSounds();

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

        music=pref.getString("music", null);
        playMusic();


    }



    @Override
    protected void onPause() {
        super.onPause();
        killMusic();
        introSong=null;

    }

    private void killMusic(){
        if (introSong != null) {//chequear si con chquear que sea null es suficiente o si agrego un boolean para chequear que la música esté corriendo

            introSong.release();
        }
    }

    private void playMusic(){
        if(music.equals("Yes")) {
            if (introSong == null) {
                setVolumeControlStream(AudioManager.STREAM_MUSIC);
                introSong = MediaPlayer.create(MainActivity.this, R.raw.intromusic);
                introSong.setLooping(true);
                introSong.start();
            }
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

            reproduceSound();

        // Do something in response to button
        Intent intent = new Intent(this, Game.class);


        this.setIntentExtra(view.getId(),intent);

        startActivity(intent);


    }



    public void goToFaq(View view) {


            reproduceSound();


        Intent intent = new Intent(this, Faq.class);

        startActivity(intent);


    }


    public void goToStudyRoom(View view) {


        reproduceSound();


        // Do something in response to button
        Intent intent = new Intent(this, StudyRoom.class);

        startActivity(intent);

    }



    private void loadSounds(){
        if(sound.equals("Yes")) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

                AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN).setUsage(AudioAttributes.USAGE_GAME).build();

                mySounds = new SoundPool.Builder().
                        setMaxStreams(10).
                        setAudioAttributes(audioAttributes).
                        build();

                buttonClickSoundID = mySounds.load(this, R.raw.menubtnsound, 1);

            } else {
                mySounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
                buttonClickSoundID = mySounds.load(this, R.raw.menubtnsound, 1);
            }
        }


    }

    private void reproduceSound(){

        if(sound.equals("Yes")) {
            mySounds.play(buttonClickSoundID, 1, 1, 1, 0, 1);
        }
    }





}
