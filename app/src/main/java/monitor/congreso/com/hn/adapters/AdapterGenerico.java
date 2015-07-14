package monitor.congreso.com.hn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;

/**
 * Created by CortesMoncada on 25/01/2015.
 */
public class AdapterGenerico extends RecyclerView.Adapter<AdapterGenerico.ViewHolder> {

    private NodeList mDataset;
    private NodeList mDatasetOriginal;

    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> originaldata;


    OnItemClickListener mItemClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView generic_field_id, generic_field_1, generic_field_2, generic_field_3, generic_field_4,generic_field_5;
        public ImageView imageView;



        public ViewHolder(View v) {
            super(v);
            generic_field_id = (TextView) v.findViewById(R.id.generic_field_id);
            generic_field_1 = (TextView) v.findViewById(R.id.generic_field_1);
            generic_field_2 = (TextView) v.findViewById(R.id.generic_field_2);
            generic_field_3 = (TextView) v.findViewById(R.id.generic_field_3);
            generic_field_4 = (TextView) v.findViewById(R.id.generic_field_4);
            generic_field_5= (TextView) v.findViewById(R.id.generic_field_5);
            imageView = (ImageView) v.findViewById(R.id.generic_image);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getPosition());
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public AdapterGenerico(NodeList myDataset) {
        mDataset = myDataset;
        mDatasetOriginal = myDataset;
    }

    @Override
    public AdapterGenerico.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_generico_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Node node = mDataset.item(position);
        NodeList venueChildNodes = node.getChildNodes();

        int cantidadTotal = venueChildNodes.getLength();

        String generic_field_id = "";
        String generic_field_1 = "";
        String generic_field_2 = "";
        String generic_field_3 = "";
        String generic_field_4 = "";
        String generic_imagen = "";

        for (int i = 0; i < cantidadTotal; i++) {

            Node nodeItem = venueChildNodes.item(i).getChildNodes().item(0);
            if (nodeItem != null){
                switch (i) {
                    case 0:
                        generic_field_id = nodeItem.getNodeValue();
                        break;
                    case 1:
                        generic_field_1 = nodeItem.getNodeValue();
                        break;
                    case 2:
                        generic_field_2 = nodeItem.getNodeValue();
                        break;
                    case 3:
                        generic_field_3 = nodeItem.getNodeValue();
                        break;
                    case 4:
                        generic_field_4 = nodeItem.getNodeValue();
                        break;
                    case 5:
                        generic_imagen = nodeItem.getNodeValue();
                        break;
                }
            }

        }

        holder.generic_field_id.setText(generic_field_id);
        holder.generic_field_1.setText(generic_field_1);
        holder.generic_field_2.setText(generic_field_2);
        holder.generic_field_3.setText(generic_field_3);
        holder.generic_field_4.setText(generic_field_4);
        holder.generic_field_5.setText(generic_imagen);

        if(!"".equals(generic_imagen)){
            Picasso.with(context)
                    .load(generic_imagen)
                    .resize(100, 100)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.imageView);

        }else{
            Picasso.with(context)
                    .load(R.drawable.ic_launcher)
                    .resize(100, 100)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.ic_launcher)
                    .into(holder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.getLength();
    }

    public void getFirstFilter(NodeList newValues){
        mDataset = newValues;
        getFilter().filter("");
    }


    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                NodeList filteredArrayNames = mDataset;

                results.count = filteredArrayNames.getLength();
                results.values = filteredArrayNames;
                return results;
            }
        };

        return filter;

    }
}