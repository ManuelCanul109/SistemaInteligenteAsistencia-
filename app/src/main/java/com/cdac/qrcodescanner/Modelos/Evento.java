package com.cdac.qrcodescanner.Modelos;

import android.os.Parcel;
import android.os.Parcelable;

public class Evento  implements Parcelable {

    private int id_evento;
    private String nombre_evento;
    private String descripcion_evento;
    private String url_foto_evento;
    private String fecha_evento;
    private String estado_evento;


    public Evento() {
    }

    public int getId_evento() {
        return id_evento;
    }

    public void setId_evento(int id_evento) {
        this.id_evento = id_evento;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public String getDescripcion_evento() {
        return descripcion_evento;
    }

    public void setDescripcion_evento(String descripcion_evento) {
        this.descripcion_evento = descripcion_evento;
    }

    public String getUrl_foto_evento() {
        return url_foto_evento;
    }

    public void setUrl_foto_evento(String url_foto_evento) {
        this.url_foto_evento = url_foto_evento;
    }

    public String getFecha_evento() {
        return fecha_evento;
    }

    public void setFecha_evento(String fecha_evento) {
        this.fecha_evento = fecha_evento;
    }

    public String getEstado_evento() {
        return estado_evento;
    }

    public void setEstado_evento(String estado_evento) {
        this.estado_evento = estado_evento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_evento);
        dest.writeString(nombre_evento);
        dest.writeString(descripcion_evento);
        dest.writeString(url_foto_evento);
        dest.writeString(fecha_evento);
        dest.writeString(estado_evento);
    }

    protected Evento(Parcel in) {
        id_evento = in.readInt();
        nombre_evento = in.readString();
        descripcion_evento = in.readString();
        url_foto_evento = in.readString();
        fecha_evento = in.readString();
        estado_evento = in.readString();
    }

    public static final Creator<Evento> CREATOR = new Creator<Evento>() {
        @Override
        public Evento createFromParcel(Parcel in) {
            return new Evento(in);
        }

        @Override
        public Evento[] newArray(int size) {
            return new Evento[size];
        }
    };
}
