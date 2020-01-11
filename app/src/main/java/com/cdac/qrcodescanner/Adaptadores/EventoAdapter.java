package com.cdac.qrcodescanner.Adaptadores;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.cdac.qrcodescanner.ConfirmEventActivity;
import com.cdac.qrcodescanner.Modelos.Evento;
import com.cdac.qrcodescanner.R;

import java.util.List;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder>{

    private static final int DURATION = 250;
    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //List to store all plants
    List<Evento> evento_list;
    int previousPosition = 0;

    //Constructor of this class
    public EventoAdapter(List<Evento> lista_evento, Context context) {
        super();
        //Getting all plant
        this.evento_list = lista_evento;
        this.context = context;
    }

    private static int currentPosition = -1;

    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_evento, parent, false);
        EventoAdapter.ViewHolder viewHolder = new EventoAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final EventoAdapter.ViewHolder holder, final int position) {

        //Getting the particular item from the list
        final Evento evento = evento_list.get(position);
        //Loading image from url

        //Showing data on the views

        holder.id_evento.setText(Integer.toString(evento.getId_evento()));

        holder.nombre_evento.setText(evento.getNombre_evento());

        holder.descripcion_evento.setText(evento.getDescripcion_evento());

        holder.fecha_evento.setText(evento.getFecha_evento());

        if(evento.getEstado_evento().equals("0")){
            holder.estado_evento.setText("Estado: A tiempo para inscribirte");
        }else if(evento.getEstado_evento().equals("1")){
            holder.estado_evento.setText("Estado: Ya paso el tiempo de registro");
            holder.estado_evento.setEnabled(false);
        }

        Glide.with(context).load(evento.getUrl_foto_evento()).into(holder.imagen_evento);

        //Picasso.with(context).load(tip.getImagen_tip()).into(holder.imageViewFotoTip);
        holder.cardEve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 //Creo la actividad y envio los datos de forma serializable

                Intent actividad = new Intent(context, ConfirmEventActivity.class);
               // actividad.putExtra("EVENTO", evento);
                context.startActivity(actividad);


                // Toast.makeText(context, "Folio: " + holder.tvFolioPasajeroTicket.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return evento_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //Views
        public TextView id_evento;
        public TextView nombre_evento;
        public TextView descripcion_evento;
        public TextView estado_evento;
        public TextView fecha_evento;
        public ImageView imagen_evento;
        public CardView cardEve;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            id_evento = (TextView) itemView.findViewById(R.id.id_evento);
            nombre_evento = (TextView) itemView.findViewById(R.id.nombre_evento);
            descripcion_evento = (TextView) itemView.findViewById(R.id.descripcion_evento);
            estado_evento = (TextView) itemView.findViewById(R.id.estado_evento);
            fecha_evento = (TextView) itemView.findViewById(R.id.fecha_evento);
            imagen_evento = (ImageView) itemView.findViewById(R.id.imagen_evento);

            cardEve = (CardView) itemView.findViewById(R.id.card_evento);
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {

                    Intent intent = new Intent(v.getContext(), PassengerActivity.class);
                    intent.putExtra("ID_CORRIDA", tvIdCorrida.getText());
                    intent.putExtra("HORA_CORRIDA", tvHoraCorrida.getText());
                    intent.putExtra("RUTA_CORRIDA", tvrutaCorrida.getText());
                    v.getContext().startActivity(intent);

                }
           });
*/
        }
    }

}

