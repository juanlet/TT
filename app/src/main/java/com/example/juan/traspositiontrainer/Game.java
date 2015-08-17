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
import android.widget.Toast;

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
    int lastQuestionsIndex, correctAnswers, incorrectAnswers;
    Button startGameButton, restartGameButton, backToMenuButton, answerButton;
    SharedPreferences.Editor editor;
    KeyManager db;
    NumberPicker rootPicker,alterationPicker, chordTypePicker;
    String currentAnswer, chordType, currentNote;
    String[] roots = {" ","A","B","C","D","E","F","G"} ,alterations = {" ","#","♭","##","♭♭"}, chordTypesWith7ths={"maj7", "m7", "7", "m7b5", "-maj7", "maj7+", "dim7"}, chordTypesWithout7ths={"Major", "Minor", "Diminished"};
    static String[][]  enharmonicEquals={ {"B#","C","Dbb"},
                                        {"B##","C#","Db"},
                                        {"C##","D","Ebb"},
                                        {"D#","Eb","Fbb"},
                                        {"D##","E","Fb"},
                                        {"E#","F","Gbb"},
                                        {"E##","F#","Gb"},
                                        {"F##","G","Abb"},
                                        {"G#","Ab","0"},
                                        {"G##","A","Bbb"},
                                        {"A#","Bb","Cbb"},
                                        {"A##","B","Cb"}
        };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        pref= this.getSharedPreferences("Mypref", 0);
       //if the random repeats the number of the 3 last questions it will generate another one until the number is not in the array
        lastQuestionsSelected=new int[3];
        //tracks the last element inserted
        int lastQuestionsIndex=0;
        currentAnswer=null;
        editor = pref.edit();
        correctAnswers=0;
        incorrectAnswers=0;


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
        answerButton= (Button) findViewById(R.id.answerButton);


        rootPicker = (NumberPicker) findViewById(R.id.rootPicker);
        rootPicker.setMaxValue(0);
        rootPicker.setMaxValue(7);
        rootPicker.setDisplayedValues(roots);

        alterationPicker = (NumberPicker) findViewById(R.id.alterationPicker);
        alterationPicker.setMaxValue(0);
        alterationPicker.setMaxValue(4);
        alterationPicker.setDisplayedValues(alterations);

        chordTypePicker= (NumberPicker) findViewById(R.id.chordTypePicker);
        if(with7ths.equals("With 7ths") || gameDifficulty.equals("Hard"))
        {
            chordTypePicker.setMaxValue(0);
            chordTypePicker.setMaxValue(6);
            chordTypePicker.setDisplayedValues(chordTypesWith7ths);
        }
        else
        {
            chordTypePicker.setMaxValue(0);
            chordTypePicker.setMaxValue(2);
            chordTypePicker.setDisplayedValues(chordTypesWithout7ths);
        }



        //Me dice que botón presionó el usuario para poder definir que modo de juego corresponde
        intentExtras=this.getIntentExtras();
        //creo la base de datos
        db=new KeyManager(this);

        //según el botón que haya sido presionado en el menú voy a traer determinados datos
           quizList=this.getResourcesForGame(intentExtras, db);

            //Toast.makeText(this, quizList.size() + " registros devueltos", Toast.LENGTH_LONG).show();

       this.hideEverythingBeggining();

   }

    public void checkAnswer(View view) {
        Boolean isEnharmonic=false;
        String selectedAnswer="";
        String enharmonicSubstring="";

        if(intentExtras.equals("notes_quiz")) {
            selectedAnswer = roots[rootPicker.getValue()] + replaceBemolwithB(alterations[alterationPicker.getValue()]);
            isEnharmonic=this.isEnharmonicNoteEquivalent(selectedAnswer, currentAnswer);
        }
        else
        {
            if(with7ths.equals("With 7ths") || gameDifficulty.equals("Hard")) {
                selectedAnswer = roots[rootPicker.getValue()] + replaceBemolwithB(alterations[alterationPicker.getValue()]) + chordTypesWith7ths[chordTypePicker.getValue()];
                enharmonicSubstring=roots[rootPicker.getValue()] + replaceBemolwithB(alterations[alterationPicker.getValue()]);
                isEnharmonic = this.isEnharmonicChordEquivalent(enharmonicSubstring, currentNote, chordTypesWith7ths[chordTypePicker.getValue()]);
            }
            else
            {
             selectedAnswer = roots[rootPicker.getValue()] + replaceBemolwithB(alterations[alterationPicker.getValue()])+chordTypesWithout7ths[chordTypePicker.getValue()];
                enharmonicSubstring=roots[rootPicker.getValue()] + replaceBemolwithB(alterations[alterationPicker.getValue()]);
             isEnharmonic=this.isEnharmonicChordEquivalent(enharmonicSubstring, currentNote,chordTypesWithout7ths[chordTypePicker.getValue()]);
            }

        }

       //le saco los espacios en blanco para que compare bien las cadenas

        selectedAnswer= selectedAnswer.replaceAll("\\s+","");




        if(selectedAnswer.equals(currentAnswer) || isEnharmonic ){
            //sumar +1 a las respuestas correctas
            //cancelo el timer y después lo reseteo
            correctAnswers+=1;
               if(!isEnharmonic)
                Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();
               else
                   Toast.makeText(getApplicationContext(),"Enharmonically Correct",Toast.LENGTH_SHORT).show();
            answerTimer.cancel();
            startAnswerTimer(answerTime);

        }
        else
        {
            incorrectAnswers+=1;
            Toast.makeText(getApplicationContext(),"Incorrect",Toast.LENGTH_SHORT).show();
            answerTimer.cancel();
            startAnswerTimer(answerTime);

        }

    }

    private boolean isEnharmonicNoteEquivalent(String userAnswer, String quizAnswer) {

        int foundRow=-1, foundCol=-1;

        mainloop:for ( int i = 0; i < 12; ++i ) {
            for ( int j = 0; j < 3; ++j ) {
                if ( enharmonicEquals[i][j].equals(userAnswer) ) {
                    // Find the correct i
                    foundRow=i;
                    foundCol=j;
                    break mainloop;
                }
            }
        }

        if(foundRow==-1 || foundCol==-1){
            return false;
        }

        for(int x=0;x<3;++x){

        if(x!=foundCol && enharmonicEquals[foundRow][x].equals(quizAnswer))
          return true;
         }

        return false;
    }

    private boolean isEnharmonicChordEquivalent(String userAnswer, String noteName, String selectedChordType) {

        int foundRow=-1, foundCol=-1;

        mainloop:for ( int i = 0; i < 12; ++i ) {
            for ( int j = 0; j < 3; ++j ) {
                if ( enharmonicEquals[i][j].equals(userAnswer) ) {
                    // Find the correct i
                    foundRow=i;
                    foundCol=j;
                    break mainloop;
                }
            }
        }

        if(foundRow==-1 || foundCol==-1){
            return false;
        }

        for(int x=0;x<3;++x){

            if(x!=foundCol && enharmonicEquals[foundRow][x].equals(noteName))
                if(chordType.equals(selectedChordType))
                    return true;

        }

        return false;
    }



    public String replaceBemolwithB(String answer){

        answer = answer.replace('♭','b');

        return answer;
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
                //actualizar stats
                //cierro el cursor de selects para poder actualizar


                db.updateStats(correctAnswers,incorrectAnswers,intentExtras);

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

        //set answer depending on game mode
        if(intentExtras.equals("notes_quiz"))
        currentAnswer=question.getNoteName();
        else
        {    if(with7ths.equals("With 7ths") || gameDifficulty.equals("Hard")) {
            currentAnswer = question.getChordWith7();
            chordType = question.getChordTypeWith7();
           //I need the note dissociated from the chord tipe  to check for enharmonic chords
            currentNote=question.getNoteName();
        }
            else {

            currentAnswer = question.getChordWithout7();
            chordType= question.getChordTypeWithout7();
            currentNote=question.getNoteName();
        }
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



        if(intentExtras.equals("notes_quiz"))
        return  degreeNumber+" note of "+ question.getKeyName() + " "+ question.getScaleName();
        else
            return degreeNumber+" chord of "+ question.getKeyName() + " "+ question.getScaleName();

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
        //lo seteo en null porque cuando vuelve de la pausa cambia de pregunta, luego tengo que setear la answer a la nueva pregunta;
        currentAnswer=null;
        gameTimer.cancel();
        answerTimer.cancel();
        gameTimer=null;
        answerTimer=null;
    }



//funciones para ocultar o mostrar cosas


//pantalla de inicio cuando se viene del intent del menú principal
    public void hideEverythingBeggining(){
        gameCountDown.setVisibility(View.GONE);
        answerCountDown.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        pauseTextView.setVisibility(View.GONE);
        restartGameButton.setVisibility(View.GONE);
        backToMenuButton.setVisibility(View.GONE);
        answerButton.setVisibility(View.GONE);
        //the button stays visible
        startGameButton.setVisibility(View.VISIBLE);
        rootPicker.setVisibility(View.GONE);
        alterationPicker.setVisibility(View.GONE);
        chordTypePicker.setVisibility(View.GONE);
    }


// pantalla que aparece después de tocar "start game"
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

        if(intentExtras.equals("chord_quiz"))
            chordTypePicker.setVisibility(View.VISIBLE);

        answerButton.setVisibility(View.VISIBLE);

    }

//pantalla que aparece cuando se toca el botón de pausa
    private void hidePause(){
        gameCountDown.setVisibility(View.GONE);
        answerCountDown.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        rootPicker.setVisibility(View.GONE);
        alterationPicker.setVisibility(View.GONE);

        if(intentExtras.equals("chord_quiz"))
            chordTypePicker.setVisibility(View.GONE);

        restartGameButton.setVisibility(View.GONE);
        backToMenuButton.setVisibility(View.GONE);
        startGameButton.setVisibility(View.GONE);
        pauseTextView.setVisibility(View.VISIBLE);
        answerButton.setVisibility(View.GONE);
    }


//pantalla que aparece cuando se vuelve al juego después de la pausa
    public void showEverythingDepaused(){

        gameCountDown.setVisibility(View.VISIBLE);
        answerCountDown.setVisibility(View.VISIBLE);
        questionTextView.setVisibility(View.VISIBLE);
        pauseTextView.setVisibility(View.GONE);
        rootPicker.setVisibility(View.VISIBLE);
        alterationPicker.setVisibility(View.VISIBLE);

        if(intentExtras.equals("chord_quiz"))
            chordTypePicker.setVisibility(View.VISIBLE);

        answerButton.setVisibility(View.VISIBLE);
    }



//pantalla que aparece cuando se acaba el timer principal
    private void showButtonsEndOfGame() {

        rootPicker.setVisibility(View.GONE);
        alterationPicker.setVisibility(View.GONE);
        if(intentExtras.equals("chord_quiz"))
            chordTypePicker.setVisibility(View.GONE);
        gameCountDown.setVisibility(View.GONE);
        answerCountDown.setVisibility(View.GONE);
        questionTextView.setVisibility(View.GONE);
        pauseTextView.setVisibility(View.GONE);
        restartGameButton.setVisibility(View.VISIBLE);
        backToMenuButton.setVisibility(View.VISIBLE);
        startGameButton.setVisibility(View.GONE);
        answerButton.setVisibility(View.GONE);
    }


}
