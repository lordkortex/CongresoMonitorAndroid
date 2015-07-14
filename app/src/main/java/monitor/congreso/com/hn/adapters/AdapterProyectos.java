package monitor.congreso.com.hn.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import monitor.congreso.com.hn.congresomonitor.R;
import monitor.congreso.com.hn.interfaces.OnItemClickListener;

/**
 * Created by CortesMoncada on 25/01/2015.
 * <p/>
 * <xmlProyectos>
 * <id_proyecto>1</id_proyecto>
 * <proyecto_nombre>Prueba Militar</proyecto_nombre>
 * <id_sesiones>1</id_sesiones>
 * <sesion_nombre>20150125</sesion_nombre>
 * <id_sesion_proyecto>4</id_sesion_proyecto>
 * <legislacion_nombre>Manifestación</legislacion_nombre>
 * <diputado_nombre>Marisol</diputado_nombre>
 * <comision_dictaminadora_nombre>Comision 1</comision_dictaminadora_nombre>
 * <status_nombre>Rotundo No</status_nombre>
 * <fecha_creacion>2013-01-01T00:00:00-07:00</fecha_creacion>
 * <tipo_debate_nombre>Debate1</tipo_debate_nombre>
 * <partido_nombre>Republicano</partido_nombre>
 * </xmlProyectos>
 */
public class AdapterProyectos extends RecyclerView.Adapter<AdapterProyectos.ViewHolder> {
    private NodeList mDataset;
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView proyecto_id_sesion;
        public TextView proyecto_id_sesion_proyecto;
        public TextView proyecto_diputado_partido;
        public TextView proyecto_nombre;
        public TextView proyecto_debate;
        public TextView proyecto_fecha;
        public TextView proyecto_status;
        public TextView proyecto_legislacion;
        public TextView proyecto_descripcion;

        public ViewHolder(View v) {
            super(v);
            proyecto_id_sesion = (TextView) v.findViewById(R.id.proyecto_id_sesion);
            proyecto_id_sesion_proyecto = (TextView) v.findViewById(R.id.proyecto_id_sesion_proyecto);
            proyecto_diputado_partido = (TextView) v.findViewById(R.id.proyecto_diputado_partido);
            proyecto_nombre = (TextView) v.findViewById(R.id.proyecto_nombre);
            proyecto_debate = (TextView) v.findViewById(R.id.proyecto_debate);
            proyecto_fecha = (TextView) v.findViewById(R.id.proyecto_fecha);
            proyecto_status = (TextView) v.findViewById(R.id.proyecto_status);
            proyecto_legislacion= (TextView) v.findViewById(R.id.proyecto_legislacion);
            proyecto_descripcion= (TextView) v.findViewById(R.id.proyecto_descripcion);
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


    public AdapterProyectos(NodeList myDataset) {
        mDataset = myDataset;
    }

    @Override
    public AdapterProyectos.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_proyectos_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Node node = mDataset.item(position);
        NodeList venueChildNodes = node.getChildNodes();

        NodeList nodeItem;

        nodeItem = venueChildNodes.item(4).getChildNodes();
        String proyecto_id_sesion_proyecto ="";
        if(nodeItem.getLength()>0){
             proyecto_id_sesion_proyecto = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(3).getChildNodes();
        String proyecto_sesion_nombre ="";
        if(nodeItem.getLength()>0){
            proyecto_sesion_nombre = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(6).getChildNodes();
        String proyecto_diputado ="";
        if(nodeItem.getLength()>0){
            proyecto_diputado = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(11).getChildNodes();
        String proyecto_partido ="";
        if(nodeItem.getLength()>0){
            proyecto_partido = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(12).getChildNodes();
        String proyecto_descripcion ="";
        if(nodeItem.getLength()>0){
            proyecto_descripcion = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(1).getChildNodes();
        String proyecto_nombre ="";
        if(nodeItem.getLength()>0){
            proyecto_nombre = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(10).getChildNodes();
        String proyecto_debate ="";
        if(nodeItem.getLength()>0){
            proyecto_debate = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(8).getChildNodes();
        String proyecto_status ="";
        if(nodeItem.getLength()>0){
            proyecto_status = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(9).getChildNodes();
        String proyecto_fecha ="";
        if(nodeItem.getLength()>0){
            proyecto_fecha = nodeItem.item(0).getNodeValue();
        }

        nodeItem = venueChildNodes.item(5).getChildNodes();
        String proyecto_legislacion ="";
        if(nodeItem.getLength()>0){
            proyecto_legislacion = nodeItem.item(0).getNodeValue();
        }

        /*
                Acto: Proyecto
                Nombre: Contrato de Construcción
                Proponente: SESA / Partido Liberal
                Sesión: 5 de marzo de 2015
                Estatus: Aprobado
                Tercer debate
                * */

        holder.proyecto_id_sesion.setText("Sesion : " + proyecto_sesion_nombre);
        holder.proyecto_diputado_partido.setText("Proponente : " + proyecto_diputado  );
        holder.proyecto_nombre.setText(proyecto_nombre);// NO SE LE PUEDE AGREGAR MAS TEXTO
        holder.proyecto_debate.setText(proyecto_debate );// NO SE LE PUEDE AGREGAR MAS TEXTO
        holder.proyecto_fecha.setText("Sesion de Fecha: " + proyecto_fecha);
        holder.proyecto_id_sesion_proyecto.setText(proyecto_id_sesion_proyecto);// NO SE LE PUEDE AGREGAR MAS TEXTO
        holder.proyecto_status.setText("Estatus : " + proyecto_status);
        holder.proyecto_legislacion.setText("Tipo de Acto : " +proyecto_legislacion);
        holder.proyecto_descripcion.setText(proyecto_descripcion);


        /*holder.proyecto_id_sesion_proyecto.setOnClickListener(new View.OnClickListener() {
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