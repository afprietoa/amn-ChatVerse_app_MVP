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

public class Chat_Conversations_Fragments extends Fragment implements ChatConversationContract.View {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savadInstanceState){
        View view = inflater.inflate(R.layout.activity_chat, container, false);

        chatsRV = view.findViewById(R.id.chatsRV);
        etMensajeChat = view.findViewById(R.id.Et_mensaje_chat);
        adjuntarFoto = view.findViewById(R.id.adjuntarFAB);
        EnviarMensaje = view.findViewById(R.id.enviarFAB);
        txtNombresUsuario = view.findViewById(R.id.txt_nombres_usuario);
        txtEstadoChat = view.findViewById(R.id.txt_estado_chat);
        ibRegresar = view.findViewById(R.id.IbRegresar);
        toolbarIv = view.findViewById(R.id.toolbarIv);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverId = getArguments().getString("RECEIVER_ID");
        receiverName = getArguments().getString("RECEIVER_NAME");

        txtNombresUsuario.setText(receiverName);

        messageModelList = new ArrayList<>();
        chatAdapter = new ChatConversationAdapter(messageModelList, currentUserId);
        chatsRV.setAdapter(chatAdapter);


       presenter = new ChatConversationPresenterImpl();// necesita el this

        ibRegresar.setOnClickListener(v -> getActivity().onBackPressed());

        EnviarMensaje.setOnClickListener(v -> sendMessage());

        adjuntarFoto.setOnClickListener(v -> attachImage());

        presenter.loadMessages(currentUserId, receiverId);

        return view;

    }



    private void sendMessage() {

        String messageText = etMensajeChat.getText().toString().trim();
        if (!messageText.isEmpty()) {
            presenter.sendMessage(currentUserId, receiverId, messageText);
            etMensajeChat.setText("");
        }
    }
    private void attachImage() {

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

        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void messageSent() {
        chatsRV.scrollToPosition(messageModelList.size() - 1);

    }



}
