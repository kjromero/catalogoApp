package apps.catalogo.catalogoapps.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import apps.catalogo.catalogoapps.R;


/**
 * Created by hebertromero on 6/11/16.
 */

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private String[] items;

    public ItemAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null){
            //Create a new view into  the list
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item, parent, false);
        }

        TextView category = (TextView)v.findViewById(R.id.category);

        String item = items[position];
        category.setText(item);

        return v;
    }
}
