package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Adaptadores.AdaptadorChats;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos.Chats;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.FragmentChatsBinding;

import java.util.ArrayList;


public class FragmentChats extends Fragment {

    private FragmentChatsBinding binding;
    private FirebaseAuth firebaseAuth;
    private String miUid;
    private Context context;

    private ArrayList<Chats> chatsArrayList;
    private AdaptadorChats adaptadorChats;

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    public FragmentChats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        miUid = firebaseAuth.getUid();

        binding.etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence filtro, int start, int before, int count) {
                try {
                    String consulta = filtro.toString();
                    adaptadorChats.getFilter().filter(consulta);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cargarChats();

        return binding.getRoot();
    }

    private void cargarChats() {
        chatsArrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    String chatKey = ""+ds.getKey();

                    if (chatKey.contains(miUid)){
                        Chats modeloChats = new Chats();
                        modeloChats.setKeyChat(chatKey);

                        chatsArrayList.add(modeloChats);
                    }
                }

                adaptadorChats = new AdaptadorChats(context, chatsArrayList);
                binding.chatsRv.setAdapter(adaptadorChats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
