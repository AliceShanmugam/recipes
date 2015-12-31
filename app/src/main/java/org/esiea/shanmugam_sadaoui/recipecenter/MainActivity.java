package org.esiea.shanmugam_sadaoui.recipecenter;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        GetRecipesServices.startActionRecipe(this);
        IntentFilter intentFilter=new IntentFilter(RECIPES_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new RecipeUpdate(),intentFilter);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public JSONArray getRecipesFromFile(){
        try{
            InputStream is = new FileInputStream(getCacheDir()+"/" + "recipes.json");
            byte[]buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer,"UTF-8"));
        }catch(IOException e){
            Context context = getApplicationContext();
            CharSequence text = "Fail to download";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            e.printStackTrace();
            return new JSONArray();
        }catch(JSONException e){
            Context context = getApplicationContext();
            CharSequence text = "Fail to download";
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            e.printStackTrace();
            return new JSONArray();
        }

    }

    public String ls () throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> execClass = Class.forName("android.os.Exec");
        Method createSubprocess = execClass.getMethod("createSubprocess", String.class, String.class, String.class, int[].class);
        int[] pid = new int[1];
        FileDescriptor fd = (FileDescriptor)createSubprocess.invoke(null, "/system/bin/ls", "/", null, pid);

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fd)));
        String output = "";
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                output += line + "\n";
            }
        }
        catch (IOException ignored) {}
        return output;
    }

    public void telecharger(View v){

        JSONArray recipes = getRecipesFromFile();

        Context context = getApplicationContext();
        CharSequence text = "Download started !";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        try {
            Log.d("RECIPE",recipes.getString(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void read(View v){
        startActivity(new Intent(MainActivity.this, ViewActivity.class));
    }


    public static final String RECIPES_UPDATE="com.RECIPE_UPDATE";
        public class RecipeUpdate extends BroadcastReceiver{
            @Override
            public void onReceive(Context context, Intent intent){
                CharSequence text = "Téléchargement terminé !";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }


}


