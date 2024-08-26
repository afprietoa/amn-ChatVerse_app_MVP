package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos;

public class Chats {


    String imagen;
    String nombres;
    String keyChat;

    String uidRecibimos;

    String idMensaje;

    String tipoMensaje;

    String mensaje;

    String emisorUid;

    String receptorUid;

    long tiempo;

    public Chats() {
    }

    public Chats(String imagen, String nombres, String keyChat, String uidRecibimos, String idMensaje, String tipoMensaje, String mensaje, String emisorUid, String receptorUid, long tiempo) {
        this.imagen = imagen;
        this.nombres = nombres;
        this.keyChat = keyChat;
        this.uidRecibimos = uidRecibimos;
        this.idMensaje = idMensaje;
        this.tipoMensaje = tipoMensaje;
        this.mensaje = mensaje;
        this.emisorUid = emisorUid;
        this.receptorUid = receptorUid;
        this.tiempo = tiempo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getKeyChat() {
        return keyChat;
    }

    public void setKeyChat(String keyChat) {
        this.keyChat = keyChat;
    }

    public String getUidRecibimos() {
        return uidRecibimos;
    }

    public void setUidRecibimos(String uidRecibimos) {
        this.uidRecibimos = uidRecibimos;
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
