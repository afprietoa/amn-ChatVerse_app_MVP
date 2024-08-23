package aplicacionesmoviles.avanzado.todosalau.ejemplochat.model;

import java.util.List;

public class UserModel {
    private String userId;
    private String email;
    private String name;
    private List<String> messageIds; // Lista de IDs de los mensajes enviados por el usuario
    private String lastMessage;
    private String lastMessageTime;

    // Constructor vacío necesario para Firebase
    public UserModel() {
    }

    // Constructor para inicializar un UserModel con ID, correo electrónico y nombre
    public UserModel(String userId, String email, String name) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }

    // Getter y setter para la lista de IDs de los mensajes enviados por el usuario
    public List<String> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(List<String> messageIds) {
        this.messageIds = messageIds;
    }

    // Getters y setters para los demás atributos
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}