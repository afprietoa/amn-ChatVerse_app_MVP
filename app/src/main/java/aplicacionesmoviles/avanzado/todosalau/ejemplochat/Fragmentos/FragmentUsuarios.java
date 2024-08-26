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
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Adaptadores.AdaptadorUsuario;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos.Usuario;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.FragmentUsuariosBinding;

import java.util.ArrayList;
import java.util.List;


public class FragmentUsuarios extends Fragment {

    private FragmentUsuariosBinding binding;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    AdaptadorUsuario adaptadorUsuario;
    List<Usuario> usuarioList;

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    public FragmentUsuarios() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsuariosBinding.inflate(LayoutInflater.from(mContext),container,false);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.RVUsuarios.setHasFixedSize(true);
        binding.RVUsuarios.setLayoutManager(new GridLayoutManager(getActivity(),1));

        usuarioList = new ArrayList<>();


        binding.etBuscarUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence usuario, int start, int before, int count) {
                buscarUsuario(usuario.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listarUsuarios();

        return binding.getRoot();
    }

    private void listarUsuarios() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.orderByChild("nombres").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuarioList.clear();
                if (binding.etBuscarUsuario.getText().toString().isEmpty()){
                    for (DataSnapshot ds : snapshot.getChildren()){
                        Usuario usuario = ds.getValue(Usuario.class);
                        assert usuario != null;
                        assert firebaseUser != null;
                        if (!usuario.getUid().equals(firebaseUser.getUid())){
                            usuarioList.add(usuario);
                        }
                        adaptadorUsuario = new AdaptadorUsuario(getActivity(), usuarioList);
                        binding.RVUsuarios.setAdapter(adaptadorUsuario);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void buscarUsuario(String usuario){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.orderByChild("nombres").startAt(usuario).endAt(usuario+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usuarioList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            Usuario user = ds.getValue(Usuario.class);
                            assert user !=null;
                            assert firebaseUser != null;
                            if (!user.getUid().equals(firebaseUser.getUid())){
                                usuarioList.add(user);
                            }
                            adaptadorUsuario = new AdaptadorUsuario(getActivity(), usuarioList);
                            binding.RVUsuarios.setAdapter(adaptadorUsuario);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}