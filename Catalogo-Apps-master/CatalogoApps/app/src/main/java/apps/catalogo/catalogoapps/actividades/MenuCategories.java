package apps.catalogo.catalogoapps.actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import apps.catalogo.catalogoapps.Adapters.ItemAdapter;
import apps.catalogo.catalogoapps.R;


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

        listCategories = (ListView)findViewById(R.id.listView_categories);


        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        final String categorias = prefs.getString("arraycategories", "iTunes");

        //obtener las categorias de las sharepreferences
        categorieslist = categorias.split(",");

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


