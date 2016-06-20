package apps.catalogo.kennyromero.catalogoapps.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import apps.catalogo.kennyromero.catalogoapps.Adapters.ItemAdapter;
import apps.catalogo.kennyromero.catalogoapps.Adapters.PostAdapter;
import apps.catalogo.kennyromero.catalogoapps.R;
import apps.catalogo.kennyromero.catalogoapps.data.Item;
import apps.catalogo.kennyromero.catalogoapps.data.Post;

import static apps.catalogo.kennyromero.catalogoapps.R.string.categories;


public class MenuCategories extends AppCompatActivity {

    private ListView listCategories;
    private ArrayAdapter gvAdapter;
    ArrayAdapter<String> arrayAdapter;
    private static String[] categorieslist;
    private ItemAdapter adapterlIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_categories);
        //gvAdapter = new PostAdapter(getApplicationContext());

        listCategories = (ListView)findViewById(R.id.listView_categories);


        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        final String categorias = prefs.getString("arraycategories", "iTunes");

        categorieslist = categorias.split(","); //obtener las categorias de las sharepreferences

        adapterlIST = new ItemAdapter(getApplicationContext(), categorieslist);


        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categorieslist);
        listCategories.setAdapter(itemsAdapter);


        listCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String categorie = parent.getItemAtPosition(position).toString();

                Intent intent = new Intent(MenuCategories.this, MainActivity.class);
                intent.putExtra("categorie",categorie);

                startActivity(intent);
            }
        });

    }

}


