package aplicacionesmoviles.avanzado.todosalau.ejemplochat.data.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.data.dao.MessageDao;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.data.entity.MessageEntity;

@Database(entities = {MessageEntity.class}, version = 1)
public abstract class MessageDatabase extends RoomDatabase {

    public static volatile MessageDatabase INSTANCE;

    public static MessageDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (MessageDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "message_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
