package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos;

public class Chat {

    String idMensaje;
    String tipoMensaje;
    String mensaje;
    String emisorUid;
    String receptorUid;
    long tiempo;

    public Chat() {


    }

    public Chat(String idMensaje, String tipoMensaje, String mensaje, String emisorUid, String receptorUid, long tiempo) {
        this.idMensaje = idMensaje;
        this.tipoMensaje = tipoMensaje;
        this.mensaje = mensaje;
        this.emisorUid = emisorUid;
        this.receptorUid = receptorUid;
        this.tiempo = tiempo;
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEmisorUid() {
        return emisorUid;
    }

    public void setEmisorUid(String emisorUid) {
        this.emisorUid = emisorUid;
    }

    public String getReceptorUid() {
        return receptorUid;
    }

    public void setReceptorUid(String receptorUid) {
        this.receptorUid = receptorUid;
    }

    public long getTiempo() {
        return tiempo;
    }

    public void setTiempo(long tiempo) {
        this.tiempo = tiempo;
    }
}
