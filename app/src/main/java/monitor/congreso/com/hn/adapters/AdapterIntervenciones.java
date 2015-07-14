package monitor.congreso.com.hn.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;

/**
 * Created by CortesMoncada on 25/01/2015.
 */
public class AdapterIntervenciones extends RecyclerView.Adapter<AdapterIntervenciones.ViewHolder> {
    private NodeList mDataset;
    OnItemClickListener mItemClickListener;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView intervencion_diputado,intervencion_diputado_imagen,intervencion_sesion_proyecto,intervencion_partido_imagen,intervencion_fecha,intervencion_texto;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            intervencion_diputado = (TextView) v.findViewById(R.id.intervencion_diputado);
            intervencion_diputado_imagen = (TextView) v.findViewById(R.id.intervencion_diputado_imagen);
            intervencion_sesion_proyecto = (TextView) v.findViewById(R.id.intervencion_sesion_proyecto);
            intervencion_partido_imagen = (TextView) v.findViewById(R.id.intervencion_partido_imagen);
            intervencion_fecha = (TextView) v.findViewById(R.id.intervencion_fecha);
            intervencion_texto = (TextView) v.findViewById(R.id.intervencion_texto);

            imageView = (ImageView) v.findViewById(R.id.list_image);



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



    public AdapterIntervenciones(NodeList myDataset) {
        mDataset = myDataset;
    }

    @Override
    public AdapterIntervenciones.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_intervenciones_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Node node = mDataset.item(position);
        NodeList venueChildNodes = node.getChildNodes();

        String intervencion_partido = venueChildNodes.item(4).getChildNodes().item(0).getNodeValue();
        String intervencion_fecha = venueChildNodes.item(7).getChildNodes().item(0).getNodeValue();
        String intervencion_texto = venueChildNodes.item(6).getChildNodes().item(0).getNodeValue();
        String intervencion_diputado = venueChildNodes.item(8).getChildNodes().item(0).getNodeValue();
        String intervencion_sesion = venueChildNodes.item(12).getChildNodes().item(0).getNodeValue();
        String intervencion_proyecto = venueChildNodes.item(13).getChildNodes().item(0).getNodeValue();
        String intervencion_diputado_imagen = venueChildNodes.item(9).getChildNodes().item(0).getNodeValue();
        String intervencion_partido_imagen = "";//venueChildNodes.item(5).getChildNodes().item(0).getNodeValue();

        holder.intervencion_diputado.setText(intervencion_diputado +" / "+ intervencion_partido);
        holder.intervencion_fecha.setText("Sesion de Fecha : " + intervencion_fecha);
        //holder.intervencion_sesion_proyecto.setText(intervencion_sesion +" / "+ intervencion_proyecto);
        holder.intervencion_sesion_proyecto.setText(intervencion_proyecto);
        holder.intervencion_texto.setText(intervencion_texto);
        //holder.intervencion_partido_imagen.setText(intervencion_partido_imagen);
        //holder.intervencion_diputado_imagen.setText(intervencion_diputado_imagen);

        Picasso.with(context)
                .load(intervencion_diputado_imagen)
                .resize(100, 100)
                .placeholder(R.drawable.ico_diputados)
                .error(R.drawable.ic_launcher)
                .into(holder.imageView);


        /*holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return mDataset.getLength();
    }

}