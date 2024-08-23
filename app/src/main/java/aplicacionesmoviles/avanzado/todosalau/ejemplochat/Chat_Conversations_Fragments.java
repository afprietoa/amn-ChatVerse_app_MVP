package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter.ChatConversationPresenter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter.ChatConversationPresenterImpl;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.adapter.ChatConversationAdapter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.model.MessageModel;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.view.ChatConversationContract;
import de.hdodenhof.circleimageview.CircleImageView;

public class Chat_Conversations_Fragments extends AppCompatActivity implements ChatConversationContract.View {

    private RecyclerView chatsRV;
    private EditText etMensajeChat;
    private FloatingActionButton adjuntarFoto, EnviarMensaje;
    private TextView txtNombresUsuario, txtEstadoChat;
    private ImageButton ibRegresar;
    private CircleImageView toolbarIv;


    private ChatConversationAdapter chatAdapter;
    private List<MessageModel> messageModelList;

    private String currentUserId;
    private String receiverId;
    private String receiverName;


    private ChatConversationPresenter presenter;
    private Chat.ChatConversationCallback chatConversationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
// Inicializar las vistas
        chatsRV = findViewById(R.id.chatsRV);
        etMensajeChat = findViewById(R.id.Et_mensaje_chat);
        adjuntarFoto = findViewById(R.id.adjuntarFAB);
        EnviarMensaje = findViewById(R.id.enviarFAB);
        txtNombresUsuario = findViewById(R.id.txt_nombres_usuario);
        txtEstadoChat = findViewById(R.id.txt_estado_chat);
        ibRegresar = findViewById(R.id.IbRegresar);
        toolbarIv = findViewById(R.id.toolbarIv);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Configurar el nombre del receptor en la interfaz de usuario
        txtNombresUsuario.setText(receiverName);

        // Configurar la lista de mensajes y el adaptador
        messageModelList = new ArrayList<>();
        chatAdapter = new ChatConversationAdapter(messageModelList, currentUserId);
        chatsRV.setAdapter(chatAdapter);

        // Inicializar el presentador
        presenter = new ChatConversationPresenterImpl(this);

        // Configurar los listeners de los botones
        ibRegresar.setOnClickListener(v -> onBackPressed());
        EnviarMensaje.setOnClickListener(v -> sendMessage());
        adjuntarFoto.setOnClickListener(v -> attachImage());

        // Cargar los mensajes
        presenter.loadMessages(currentUserId, receiverId);
    }

    public void setChatConversationCallback(Chat.ChatConversationCallback callback) {
        this.chatConversationCallback = callback;
    }

    private void sendMessage() {
        String messageText = etMensajeChat.getText().toString().trim();
        if (!messageText.isEmpty()) {
            presenter.sendMessage(currentUserId, receiverId, messageText);
            etMensajeChat.setText("");
            if (chatConversationCallback != null) {
                chatConversationCallback.onChatMessageSent();
            }
        }
    }

    private void attachImage() {
        // Implementar la funcionalidad de adjuntar imagen
    }

    @Override
    public void displayMessages(List<MessageModel> messages) {
        messageModelList.clear();
        messageModelList.addAll(messages);
        chatAdapter.notifyDataSetChanged();
        chatsRV.scrollToPosition(messageModelList.size() - 1);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void messageSent() {
        chatsRV.scrollToPosition(messageModelList.size() - 1);
    }
}




