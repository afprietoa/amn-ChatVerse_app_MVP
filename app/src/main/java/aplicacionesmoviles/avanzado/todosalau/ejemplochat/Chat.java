package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter.ChatPresenter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter.ChatPresenterImpl;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter.UserListPresenter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Presenter.UserListPresenterImpl;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.adapter.ChatAdapter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.adapter.MessageAdapter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.adapter.UserAdapter;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.model.MessageModel;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.model.UserModel;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.view.ChatContract;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.view.UserListContract;
/**
 * Esta clase representa la actividad de chat de la aplicación.
 */
public class Chat extends AppCompatActivity implements UserListContract, ChatContract, ChatAdapter.OnChatClickListener {

    private RecyclerView chatsRv;
    private EditText etBuscar;
    private ChatAdapter chatAdapter;
    private List<UserModel> userList;

    // Variables de Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    // Variables de presentador
    private ChatPresenter chatPresenter;
    private UserListPresenter presenter;

    private List<UserModel> usersList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chats);

        // Inicialización de Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //inicializacion de componentes
        chatsRv= findViewById(R.id.chatsRv);
        etBuscar = findViewById(R.id.etBuscar);

        chatPresenter= new ChatPresenterImpl(this);
        presenter = new UserListPresenterImpl(this);

        userList = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, userList);
        chatAdapter.setOnChatClickListener(this);
        chatsRv.setLayoutManager(new LinearLayoutManager(this));
        chatsRv.setAdapter(chatAdapter);

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        presenter.loadUsers();


    }

    private void searchUsers(String query) {
        String nametoSearch = query.toLowerCase().trim();
        List<UserModel> foundUsers = new ArrayList<>();

        for (UserModel user : usersList){
            if (user.getName().toLowerCase().contains(nametoSearch)){
                foundUsers.add(user);
            }
        }
        chatAdapter.updateUserList(foundUsers);

    }

    @Override
    public void showConversations(List<MessageModel> conversations) {

    }

    @Override
    public void showMessageSentConfirmation() {

    }

    @Override
    public void displayUsers(List<UserModel> users) {

        usersList.clear();
        usersList.addAll(users);
        chatAdapter.updateUserList(usersList);

    }

    @Override
    public void showError(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void openConversation(UserModel user){
        Intent intent = new Intent(this, Chat_Conversations_Fragments.class);
        intent.putExtra("RECEIVER_ID", user.getUserId());
        intent.putExtra("RECEIVER_NAME", user.getName());
        startActivity(intent);
        finish();

    }
}
