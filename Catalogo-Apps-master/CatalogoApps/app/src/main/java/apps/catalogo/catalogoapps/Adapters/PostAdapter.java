package apps.catalogo.catalogoapps.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import apps.catalogo.catalogoapps.R;
import apps.catalogo.catalogoapps.actividades.MainActivity;
import apps.catalogo.catalogoapps.data.Post;


public class PostAdapter extends ArrayAdapter {

    // Atributos
    private String URL_BASE = "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";
    private static final String TAG = "PostAdapter";
    private JsonObjectRequest jsArrayRequest ;
    private RequestQueue requestQueue;
    private String[] categories;


    //List<Post> items;

    public PostAdapter(Context context, final String categoria, JSONObject response) {
        super(context,0);

        // Crear nueva cola de peticiones
        requestQueue= Volley.newRequestQueue(context);

        MainActivity.items = parseJson(response, categoria);
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return MainActivity.items != null ? MainActivity.items.size() : 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        // Referencia del view procesado
        View listItemView;

        //Comprobando si el View no existe
        listItemView = null == convertView ? layoutInflater.inflate(
                R.layout.grid_item,
                parent,
                false) : convertView;


        // Obtener el item actual
        Post item = MainActivity.items.get(position);

        // Obtener Views
        TextView textoTitulo = (TextView) listItemView.
                findViewById(R.id.nameItem_app);

        final ImageView imagenPost = (ImageView) listItemView.
                findViewById(R.id.imgItem_app);

        // Actualizar los Views
        textoTitulo.setText(item.getIm_name());

        // Petición para obtener la imagen
        ImageRequest request = new ImageRequest(
                item.getIm_image(), //url img icon
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imagenPost.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imagenPost.setImageResource(R.drawable.box);
                        Log.d(TAG, "Error en respuesta Bitmap: "+ error.getMessage());
                    }
                });

        // Añadir petición a la cola
        requestQueue.add(request);


        return listItemView;
    }


    public List<Post> parseJson(JSONObject jsonObject, String categorie){
        // Variables locales
        List<Post> posts = new ArrayList();
        JSONObject feed = null;
        JSONArray entry = null;

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


                    if(attributesCty.getString("label").equals(categorie)){
                        posts.add(post);
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "Error de parsing: "+ e.getMessage());
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

        return posts;
    }


}