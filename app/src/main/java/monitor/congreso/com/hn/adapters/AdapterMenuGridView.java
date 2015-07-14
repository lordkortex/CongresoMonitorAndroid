package monitor.congreso.com.hn.adapters;

/**
 * Created by CortesMoncada on 08/02/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.dto.MenuItem;

/**
 *
 * @author manish.s
 *
 */
public class AdapterMenuGridView extends ArrayAdapter<MenuItem> {
    Context context;
    int layoutResourceId;
    ArrayList<MenuItem> data = new ArrayList<MenuItem>();
    //UtilsImageLoader imageLoader;



    public AdapterMenuGridView(Context context, int layoutResourceId, ArrayList<MenuItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

        //imageLoader=new UtilsImageLoader(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RecordHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new RecordHolder();
            holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
            holder.imageItem = (ImageView) row.findViewById(R.id.item_image);
            row.setTag(holder);
        } else {
            holder = (RecordHolder) row.getTag();
        }

        MenuItem item = data.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.imageItem.setImageBitmap(item.getImage());

        //imageLoader.DisplayImage(item.getImage().toString(), holder.imageItem);
        //imageLoader.DisplayImage("http://www.mipotra.com/Images/MipotraImageIsotipo.png", holder.imageItem);
        return row;

    }

    static class RecordHolder {
        TextView txtTitle;
        ImageView imageItem;

    }
}