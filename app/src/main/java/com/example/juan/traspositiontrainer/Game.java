package com.example.juan.traspositiontrainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;



public class Game extends ActionBarActivity {

private String intentExtras;
private ArrayList<MusicSQLRow> quizList;
    private TextView gameCountDown,answerCountDown,questionTextView, pauseTextView ;
   SharedPreferences pref;
    String gameDifficulty, key, scale,with7ths, answer;
    long gameTime,answerTime, millisLeftGameBeforePause;
    CountDownTimer gameTimer, answerTimer;
    boolean gameTimerIsRunning, answerTimerIsRunning;
    private int[] lastQuestionsSelected;
    int lastQuestionsIndex;
    Button startGameButton, restartGameButton, backToMenuButton;
    SharedPreferences.Editor editor;
    NumberPicker rootPicker,alterationPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pref= this.getSharedPreferences("Mypref", 0);
       //if the random repeats the number of the 3 last questions it will generate another one until the number is not in the array
        lastQuestionsSelected=new int[3];
        //tracks the last element inserted
        int lastQuestionsIndex=0;
        editor = pref.edit();

        rootPicker = (NumberPicker) findViewById(R.id.rootPicker);
        String[] roots = {" ","A","B","C","D","E","F","G"}; //etc
        rootPicker.setMaxValue(0);
        rootPicker.setMaxValue(7);
        rootPicker.setDisplayedValues(roots);

        alterationPicker = (NumberPicker) findViewById(R.id.alterationPicker);
        String[] alterations = {" ","#","♭","##","♭♭"}; //etc
        alterationPicker.setMaxValue(0);
        alterationPicker.setMaxValue(4);
        alterationPicker.setDisplayedValues(alterations);


        gameDifficulty=pref.getString("gameDifficulty", "");
        key=pref.getString("key","");
        scale=pref.getString("scale", "");
        with7ths=pref.getString("with7ths", "");
        gameTime=this.getMinutesInMilliseconds(pref.getString("gameTime", ""));
        answerTime=this.getSecondsInMilliseconds(pref.getString("answerTime", ""));

        gameCountDown=(TextView) findViewById(R.id.gameCountDown);
        answerCountDown= (TextView) findViewById(R.id.answerCountdown);
        questionTextView= (TextView) findViewById(R.id.question_TextView);
        pauseTextView=(TextView) findViewById(R.id.game_paused_textview);
        startGameButton= (Button) findViewById(R.id.start_game_button);
        restartGameButton=(Button) findViewById(R.id.restart_game_button);
        backToMenuButton= (Button) findViewById(R.id.back_to_menu_button);

        //Me dice que botón presionó el usuario para poder definir que modo de juego corresponde
        intentExtras=this.getIntentExtras();
        //creo la base de datos
        KeyManager db=new KeyManager(this);

        //según el botón que haya sido presionado en el menú voy a traer determinados datos
           quizList=this.getResourcesForGame(intentExtras, db);

            //Toast.makeText(this, quizList.size() + " registros devueltos", Toast.LENGTH_LONG).show();

       this.hideEverythingBeggining();

        rootPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // do something here



            }
        });

        alterationPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // do something here
            }
        });


    }



//this is the method that triggers when the user pushes the Start Game Button
    public void startGame(View view){
        showEverythingBeginning();
        startGameTimer(gameTime);
        startAnswerTimer(answerTime);
    }



    public void startGameTimer(long time){
     
        gameTimer = new CountDownTimer(time, 500){

            @Override public void onTick(long millisUntilFinished) {
                millisLeftGameBeforePause=millisUntilFinished;
                gameCountDown.setText("Time Remaining: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            @Override public void onFinish() {
                gameTimerIsRunning = false;
              if(answerTimerIsRunning) answerTimer.cancel();
                endOfTheGame();
            }

        };
        gameTimerIsRunning = true;
        gameTimer.start();
    }

    private void endOfTheGame() {

        showButtonsEndOfGame();

    }

    public void restartGame(View view){

        this.startGame(view);
    }

    public void backtoMenu(View view){
        finish();
     }



    public void startAnswerTimer(long time) {
        //load the first question
        loadNewQuestion();


            answerTimer = new CountDownTimer(time, 100) {

                @Override
                public void onTick(long millisUntilFinished) {
                    answerCountDown.setText("Next Question in: "+ TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) );
                }

                @Override
                public void onFinish() {
                    answerTimerIsRunning = false;

                    if (gameTimerIsRunning) {
                             //load the next question
                             loadNewQuestion();
                             answerTimer.start();
                    }
                }


            };
            answerTimerIsRunning = true;
            answerTimer.start();

    }

    private void loadNewQuestion() {

        String text=this.pickQuestion();

        questionTextView.setText(text);


    }

    private String pickQuestion() {

        //generate random number


        int generatedQuestionNumber = this.generateRandomNumber();

        //create MusicSQLRow object

        MusicSQLRow question=null;

        //fill the last 5 picked numbers android, if it's already in the array pick another one

        while(Arrays.asList(lastQuestionsSelected).contains(generatedQuestionNumber)) {

            generatedQuestionNumber = this.generateRandomNumber();


        }


         //generate question
            question = quizList.get(generatedQuestionNumber);
         //check if the array is full, it it's not I use the last index, otherwise I use the first first position which is the oldest
            if(lastQuestionsIndex<=2){

                lastQuestionsSelected[lastQuestionsIndex]=generatedQuestionNumber;
                lastQuestionsIndex++;
                if(lastQuestionsIndex==3)
                    lastQuestionsIndex=0;

            }

        //generate question text

        String questionResponse= this.generateQuestionText(question);
        //return the question

        return questionResponse;


    }

    private int generateRandomNumber(){

        int min = 0;
        int max = quizList.size();

        Random random = new Random();

        return random.nextInt(max - min) + min;

    }

    private String generateQuestionText(MusicSQLRow question){
//this is just for notes, change word note for chord in chord game
        String degreeNumber;

        switch (question.getDegreeNumber()) {
            case "1":  degreeNumber = "1st";
                break;
            case "2":  degreeNumber = "2nd";
                break;
            case "3":  degreeNumber = "3rd";
                break;
            case "4":  degreeNumber = "4th";
                break;
            case "5":  degreeNumber = "5th";
                break;
            case "6":  degreeNumber = "6th";
                break;
            case "7":  degreeNumber = "7th";
                break;
            default: degreeNumber = "Invalid degree";
                break;
        }




        return  degreeNumber+" note of "+ question.getKeyName() + " "+ question.getScaleName();

    }


    private long getMinutesInMilliseconds(String time) {
        //the time is inicially in minutes
        String firstWord = null;
        long timeInMilliseconds;


        if(time.contains(" ")){
            firstWord= time.substring(0, time.indexOf(" "));
        }

        timeInMilliseconds= TimeUnit.MINUTES.toMillis(Integer.parseInt(firstWord));


        return timeInMilliseconds;
    }

    private long getSecondsInMilliseconds(String time) {
        //the time is inicially in minutes
        String firstWord = null;
        long timeInMilliseconds;


        if(time.contains(" ")){
            firstWord= time.substring(0, time.indexOf(" "));
        }

        timeInMilliseconds= TimeUnit.SECONDS.toMillis(Integer.parseInt(firstWord));


        return timeInMilliseconds;
    }

    private ArrayList<MusicSQLRow> getResourcesForGame(String intentExtras,KeyManager db) {

        ArrayList<MusicSQLRow> arrayListRowsFromDb=null;

        if(intentExtras.equals("notes_quiz"))
            arrayListRowsFromDb=db.getNotes();
        else if(intentExtras.equals("chord_quiz"))
            arrayListRowsFromDb=db.getChords();
        else if (intentExtras.equals("chord_progressions_quiz"))
            arrayListRowsFromDb=db.getChords();

        return arrayListRowsFromDb;
    }


    private String getIntentExtras() {

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            intentExtras = extras.getString("ButtonPressed");
        }
        return intentExtras;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.pause_game) {
           if(gameTimerIsRunning)
           {//cancel timers

            this.gamePaused();

           //hide everything and show pause message and change icon to play icon
            this.hidePause();

           } else
           {
                this.onResumeGame();


           }


            return true;
        }else if (id== R.id.exit_app){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onResumeGame() {


        //recreate Timers and assing miliseconds remaning stored in SharedPreferences
        this.startGameTimer(millisLeftGameBeforePause);
       this.startAnswerTimer(answerTime);
        //show everything when resume game
        this.showEverythingDepaused();

    }


    public void gamePaused(){
        gameTimerIsRunning = false;
        answerTimerIsRunning=false;
        gameTimer.cancel();
        answerTimer.cancel();
        gameTimer=null;
        answerTimer=null;
    }



//funciones para ocultar o mostrar cosas

    public void showEverythingDepaused(){

        gameCountDown.setVisibility(View.VISIBLE);
        answerCountDown.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        pauseTextView.setVisibility(View.GONE);
        rootPicker.setVisibility(View.VISIBLE);
        alterationPicker.setVisibility(View.VISIBLE);
    }

    public void hideEverythingBeggining(){
        gameCountDown.setVisibility(View.GONE);
        answerCountDown.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        pauseTextView.setVisibility(View.GONE);
        restartGameButton.setVisibility(View.GONE);
        backToMenuButton.setVisibility(View.GONE);

        //the button stays visible
        startGameButton.setVisibility(View.VISIBLE);
        rootPicker.setVisibility(View.GONE);
        alterationPicker.setVisibility(View.GONE);
    }

    public void showEverythingBeginning(){

        gameCountDown.setVisibility(View.VISIBLE);
        answerCountDown.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        pauseTextView.setVisibility(View.GONE);
        restartGameButton.setVisibility(View.GONE);
        backToMenuButton.setVisibility(View.GONE);
        //the button stays visible
        startGameButton.setVisibility(View.GONE);
        rootPicker.setVisibility(View.VISIBLE);
        alterationPicker.setVisibility(View.VISIBLE);

    }

    private void showButtonsEndOfGame() {

        rootPicker.setVisibility(View.GONE);
        alterationPicker.setVisibility(View.GONE);
        gameCountDown.setVisibility(View.GONE);
        answerCountDown.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        pauseTextView.setVisibility(View.GONE);
        restartGameButton.setVisibility(View.VISIBLE);
        backToMenuButton.setVisibility(View.VISIBLE);
        startGameButton.setVisibility(View.GONE);


    }

    private void hidePause(){
        gameCountDown.setVisibility(View.GONE);
        answerCountDown.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        rootPicker.setVisibility(View.GONE);
        alterationPicker.setVisibility(View.GONE);
        restartGameButton.setVisibility(View.GONE);
        backToMenuButton.setVisibility(View.GONE);
        startGameButton.setVisibility(View.GONE);
        pauseTextView.setVisibility(View.VISIBLE);
    }




}
