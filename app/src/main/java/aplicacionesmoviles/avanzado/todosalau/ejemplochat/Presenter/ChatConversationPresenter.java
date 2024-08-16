package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter;

import com.google.android.gms.common.internal.StringResourceValueReader;

public interface ChatConversationPresenter {

    void loadMessages(String currentUserId, String receiverId);
    void sendMessage(String currentUserId, String receiverId, String message);
}
