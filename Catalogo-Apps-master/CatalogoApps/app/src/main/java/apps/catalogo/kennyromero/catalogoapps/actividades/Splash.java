package apps.catalogo.kennyromero.catalogoapps.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import apps.catalogo.kennyromero.catalogoapps.R;
import apps.catalogo.kennyromero.catalogoapps.data.Post;

import static java.security.AccessController.getContext;

public class Splash extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
    private RequestQueue requestQueue;
    private String URL_BASE = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private JsonObjectRequest jsArrayRequest;
    private String[] categorias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(
                        Splash.this, MenuCategories.class);
                startActivity(mainIntent);

                // Close the activity so the user won't able to go back this
                // activity pressing Back button
                finish();
            }
        };



        if (!verificaConexion(this)) {
            SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            final String categorias = prefs.getString("arraycategories", "iTunes");
            String strJson = prefs.getString("json",null);

            if(categorias.equals("iTunes") || strJson == null){
                Toast.makeText(getBaseContext(),
                        "Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT)
                        .show();
                this.finish(); //valiar la existencia de datos guardados
            }else{
                Timer timer = new Timer();
                timer.schedule(task, SPLASH_SCREEN_DELAY);
            }

        }else{
            Toast.makeText(getApplicationContext(), "CONECTION SUCESSFULL", Toast.LENGTH_LONG).show();

            getData(this);

            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }

    }

    public void getData(Context context){


        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(context);

        // Nueva petición JSONObject
        jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE ,
                (String)null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        categorias = obtenerCategorias(response);
                        saveData(response, categorias);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error Respuesta en JSON: " + error.getMessage());
                    }
                }
        );

        // Añadir petición a la cola
        requestQueue.add(jsArrayRequest);

    }

    public  void saveData(JSONObject jsonObject, String[] strinArray){

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strinArray.length; i++) {
            sb.append(strinArray[i]).append(",");
        }

        prefsEditor.putString("arraycategories", sb.toString());
        prefsEditor.putString("json", jsonObject.toString());
        prefsEditor.commit();

    }






    public String[] obtenerCategorias(JSONObject jsonObject){
        // Variables locales
        List<Post> posts = new ArrayList();
        JSONObject feed = null;
        JSONArray entry = null;
        String[] categories = null;

        try {
            // Obtener el priemr objeto
            feed = jsonObject.getJSONObject("feed");
            entry = feed.getJSONArray("entry");
            categories = new String[entry.length()];

            for(int i=0; i<entry.length(); i++){

                try {
                    JSONObject objeto= entry.getJSONObject(i);
                    JSONObject name= objeto.getJSONObject("im:name");
                    JSONArray imagen = objeto.getJSONArray("im:image");
                    JSONObject img1 = imagen.getJSONObject(0);
                    JSONObject img2 = imagen.getJSONObject(2);
                    JSONObject summary = objeto.getJSONObject("summary");
                    JSONObject imPrice = objeto.getJSONObject("im:price");
                    JSONObject price = imPrice.getJSONObject("attributes");
                    JSONObject contentType = objeto.getJSONObject("im:contentType");
                    JSONObject attributesCt = contentType.getJSONObject("attributes");
                    JSONObject rights = objeto.getJSONObject("rights");
                    JSONObject title = objeto.getJSONObject("title");
                    JSONObject link = objeto.getJSONObject("link");
                    JSONObject attributesLk = link.getJSONObject("attributes");
                    JSONObject id = objeto.getJSONObject("id");
                    //JSONObject attributesId = id.getJSONObject("im:id");
                    JSONObject artist = objeto.getJSONObject("im:artist");
                    JSONObject linkArtist = artist.getJSONObject("attributes");
                    JSONObject category = objeto.getJSONObject("category");
                    JSONObject attributesCty = category.getJSONObject("attributes");
                    JSONObject releaseDate = objeto.getJSONObject("im:releaseDate");
                    JSONObject attributesRelease = releaseDate.getJSONObject("attributes");


                    Post post = new Post(
                            name.getString("label"),
                            img1.getString("label"),
                            img2.getString("label"),
                            summary.getString("label"),
                            price.getString("amount"),
                            price.getString("currency"),
                            attributesCt.getString("label"),
                            rights.getString("label"),
                            title.getString("label"),
                            attributesLk.getString("href"),
                            id.getString("label"),
                            artist.getString("label"),
                            linkArtist.getString("href"),
                            attributesCty.getString("label"),
                            attributesRelease.getString("label"));


                    //obtenemos categorias
                    String categoryNme;
                    boolean bool = false;

                    categories[i] = "category";

                    categoryNme = attributesCty.getString("label");
                    for (int j = 0; j < categories.length; j++){

                        if (categoryNme.equals(categories[j])) {
                            bool = true;
                            j = 25;
                        }else{
                            bool = false;
                        }
                    }
                    if (!bool){
                        categories[i] = categoryNme;
                    }

                    if(attributesCty.getString("label").equals("Social Networking")){
                        posts.add(post);
                    }

                } catch (JSONException e) {
                    Log.e("TAG", "Error de parsing: "+ e.getMessage());
                }
            }

            String aux ;

            for (int k =0; k <categories.length; k++){
                for (int j = 0 ; j < categories.length -1; j++){
                    if (categories[j].compareTo(categories[j+1])>0){
                        aux = categories[j];
                        categories[j] = categories[j+1];
                        categories[j+1] = aux;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return categories;
    }


    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }

}
