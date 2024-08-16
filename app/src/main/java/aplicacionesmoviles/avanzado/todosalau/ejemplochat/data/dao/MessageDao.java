package aplicacionesmoviles.avanzado.todosalau.ejemplochat.data.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.data.entity.MessageEntity;

    @Dao
    public interface MessageDao {


        @Insert
        void insert(MessageEntity messageEntity);

        @Query("SELECT * FROM messages WHERE (sender_id = :userId1 AND receiver_id = :userId2) OR (sender_id = :userId2 AND receiver_id = :userId1) ORDER BY timestamp ASC")
        List<MessageEntity> getConversation(String userId1, String userId2);


    }
