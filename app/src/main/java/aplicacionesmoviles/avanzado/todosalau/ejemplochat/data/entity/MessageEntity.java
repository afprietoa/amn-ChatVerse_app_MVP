package aplicacionesmoviles.avanzado.todosalau.ejemplochat.data.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "sender_id")
    public String senderId;

    @ColumnInfo(name = "receiver_id")
    public String receiverId;

    @ColumnInfo(name = "message")
    public String message;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "image_url")
    public String imageUrl;


    public MessageEntity(String senderId, String receiverId, String message, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }
}