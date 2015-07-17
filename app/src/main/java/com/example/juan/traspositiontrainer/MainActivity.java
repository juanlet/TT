package com.example.juan.traspositiontrainer;

import android.app.DialogFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class MainActivity extends ActionBarActivity {
//comment3

    TinyDB tinydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        /*llamo a la base de datos*/
       DBManager db=new DBManager(this);

       Cursor c=db.getKeys();



        Toast.makeText(this, c.getCount()+ " registros devueltos" , Toast.LENGTH_LONG).show();

//

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
}
