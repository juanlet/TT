package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class Faq extends ActionBarActivity {

    TextView textFaq;
    Menu activityMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        TextView t2 = (TextView) findViewById(R.id.imageButton);
        t2.setMovementMethod(LinkMovementMethod.getInstance());



        textFaq=(TextView) findViewById(R.id.faqtextTextView);

        textFaq.setText(Html.fromHtml(getString(R.string.faqText)));

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


}
