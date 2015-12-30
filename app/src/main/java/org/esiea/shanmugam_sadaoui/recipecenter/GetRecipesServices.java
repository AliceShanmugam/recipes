package org.esiea.shanmugam_sadaoui.recipecenter;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GetRecipesServices extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_RECIPES = "org.esiea.shanmugam_sadaoui.recipecenter.action.getRecipesServices";


    public GetRecipesServices() {
        super("GetRecipesServices");
    }


    public static void startActionRecipe(Context context) {
        Intent intent = new Intent(context, GetRecipesServices.class);
        intent.setAction(ACTION_RECIPES);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

        }
    }


    private void copyInputStreamToFile(InputStream in,File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[]buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    private void handleActionRecipe() {
        Log.d("","Thread service name:" + Thread.currentThread().getName());
        URL url = null;
        try {
            url = new URL("http://www.elias-berriache.me/recipes/recipes.json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                copyInputStreamToFile(conn.getInputStream(),
                        new File(getCacheDir(), "recipes.json"));
                LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(MainActivity.RECIPES_UPDATE));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
            }
        }
    }





