package aplicacionesmoviles.avanzado.todosalau.ejemplochat.view;

import android.content.Context;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.model.MessageModel;

public interface ChatConversationContract {

    interface View{

        /**
         * Muestra la lista de mensajes en la interfaz de usuario.
         * @param messages Lista de modelos de mensaje que representan los mensajes.
         */


        void displayMessages(List<MessageModel> messages);


        /**
         * Muestra un mensaje de error en la interfaz de usuario.
         * @param message Mensaje de error.
         */

        void showError(String message);


        /**
         * Indica que un mensaje ha sido enviado.
         */


        void messageSent();

        Context getContext();

    }
}
