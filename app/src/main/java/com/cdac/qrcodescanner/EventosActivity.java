package com.cdac.qrcodescanner;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cdac.qrcodescanner.Adaptadores.EventoAdapter;
import com.cdac.qrcodescanner.Modelos.Evento;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventosActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private List<Evento> EventoList;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RelativeLayout coordinatorLayout;
    //Volley Request Queue
    private RequestQueue requestQueue;


    private RelativeLayout contenedor_errores;

    private Button recargar;

    private SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;

    public static final String MY_PREFERENCES = "MyPrefs";
    String id_usu_tic;

    Button btn1,btn2,btn3,btn4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerMyEventos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout_evento);
        swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        showToolbar("Eventos de UniModelo", false);

        //TODO INICIALIZAMOS NUESTRA LISTA DE TIPS
        EventoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //TODO LLAMAMOS LA METODO QUE REALIZA LA BAJADA DE DATOS Y LOS COLOCA EN EL RECYCLER VIEW
        getData();

        //TODO INICIALIZAMOS  EL ADATADOR
        adapter = new EventoAdapter(EventoList, this);

        //TODO AÑADIMOS EL ADATER AL RECYCLER VIEW
        recyclerView.setAdapter(adapter);

        //TODO INICIALIZAMOS LOS COMPONENTES DE LA VISTA
        contenedor_errores = (RelativeLayout)findViewById(R.id.contenedor_error);

        //TODO EN CASO DE FALLO ESTE BOTON FUNCIONARA COMO RECARCAGADOR DE DATOS

    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
    }

    /*
     *TODO PROCESAMOS LA PETICION HTTP Y CAPTURAMOS LOS ELEMENTOS JSON RECIBIDOS
     *
     **/
    private JsonArrayRequest getDataFromServer() {
        //TODO REALIZAMOS LA LECTURA DEL JSONARRAY CON VOLLEY
        String urlGetPlants = "https://taximanfcp.com/sadcum/services/getEventos.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlGetPlants,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //TODO LLAMAMOS AL METODO PARSEDATA SI HUBO CAPTURA DE DATOS
                        parseData(response);
                        //TODO DE IGUAL FORMA OCULTAMOS EL PROGRESS BAR Y EL TEXTO DE CARGA

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO SI HAY ERROR OCULTAMOS EL PROGRESS BAR Y EL TEXTO DE CARGA Y MOSTRAMOS EL CONTENEDOR QUE MARCA ERROR
                        //TODO Y EL BOTON PARA RECARGAR
                        contenedor_errores.setVisibility(View.VISIBLE);
                        lanzarError();
                    }
                });
        //TODO RETORNAMOS LO CAPTURADO SEA ERROR O DATOS
        return jsonArrayRequest;
    }

    //TODO ESTE METODO ES EL QUE HACE QUE BAJEMOS LOS DATOS DE LA RED
    private void getData() {
        //TODO AÑADIENDO AL  queue LLAMANDO AL METODO getDataFromServer
        EventoList.clear();
        swipeRefreshLayout.setRefreshing(true);
        requestQueue.add(getDataFromServer());
    }

    public void recargaDatos(){
        contenedor_errores.setVisibility(View.GONE);
        requestQueue.add(getDataFromServer());
    }


    //TODO ESTE METODO PASA LOS DATOS A SUS GET Y SET CORRESPONDIENTES PARA SU USO
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //TODO CREAMOS EL OBJETO TIP
            Evento evento = new Evento();
            JSONObject json = null;
            try {
                //TODO OBTENEMOS EL JSON
                json = array.getJSONObject(i);

                //TODO AÑADIMOS DATOS AL OBJETO TIP
                evento.setId_evento(json.getInt("id_evento"));
                evento.setNombre_evento(json.getString("nombre_evento"));
                evento.setDescripcion_evento(json.getString("descripcion_evento"));
                evento.setUrl_foto_evento(json.getString("url_foto_evento"));
                evento.setFecha_evento(json.getString("fecha_evento"));

                evento.setEstado_evento(json.getString("estado_evento"));
            } catch (JSONException e) {

                e.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
            }
            //TODO AÑADIMOS EL OBJETO TIP A LA LISTA TIP
            EventoList.add(evento);
        }

        //TODO NOTIFICAMOS AL ADAPTADOR PARA QUE HAGA CAMBIOS
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
    private void lanzarError(){
        Toast.makeText(getApplicationContext(), "Error al bajar los eventos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getData();
    }

}
