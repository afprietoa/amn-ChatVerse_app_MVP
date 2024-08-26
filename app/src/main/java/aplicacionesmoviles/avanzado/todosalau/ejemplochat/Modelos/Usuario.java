package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos;

public class Usuario {

    String uid , nombres , email , imagen;

    public Usuario() {

    }

    public Usuario(String uid, String nombres, String email, String imagen) {
        this.uid = uid;
        this.nombres = nombres;
        this.email = email;
        this.imagen = imagen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
