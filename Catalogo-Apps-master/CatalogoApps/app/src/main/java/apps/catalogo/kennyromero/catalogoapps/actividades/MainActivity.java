package apps.catalogo.kennyromero.catalogoapps.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import apps.catalogo.kennyromero.catalogoapps.Adapters.PostAdapter;
import apps.catalogo.kennyromero.catalogoapps.R;
import apps.catalogo.kennyromero.catalogoapps.data.Post;

public class MainActivity extends AppCompatActivity {

    private GridView gvApps;
    private ArrayAdapter gvAdapter;
    public static List<Post> items;
    private String categoria;
    private JSONObject jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String strJson = prefs.getString("json",null);
        if(strJson != null) try {
            jsonData = new JSONObject(strJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        categoria = extras.getString("categorie");

        // obtenemos preferencias y se las mandamos a posadapte ()

        gvApps = (GridView)findViewById(R.id.grid);

        // Crear y setear adaptador

        gvAdapter = new PostAdapter(getApplicationContext(), categoria , jsonData);
        gvApps.setAdapter(gvAdapter);

        gvApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // Obtener el item actual
                Post item = items.get(position);
                item.getArtist();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("titulo", item.getTitle());
                intent.putExtra("image", item.getIm_image2());
                intent.putExtra("descrip", item.getSummary());
                intent.putExtra("artist", item.getArtist());
                intent.putExtra("category", item.getCategory());
                intent.putExtra("link", item.getLink());
                startActivity(intent);



            }
        });


    }
}