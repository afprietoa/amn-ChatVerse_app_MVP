package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.CambiarPassword;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Constantes;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.EditarInformacion;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.OpcionesLoginActivity;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.FragmentPerfilBinding;

import java.util.HashMap;
import java.util.Map;


public class FragmentPerfil extends Fragment {

    private FragmentPerfilBinding binding;
    private Context mContext;
    private FirebaseAuth firebaseAuth;
    private CountDownTimer timer;

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    public FragmentPerfil() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPerfilBinding.inflate(LayoutInflater.from(mContext), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        cargarInformacion();

        binding.btnActualizarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, EditarInformacion.class));
            }
        });

        binding.btnCambiarPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CambiarPassword.class));
            }
        });

        binding.btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarEstado();
                cerrarSesion();
            }
        });
    }

    private void cerrarSesion(){
        timer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                firebaseAuth.signOut();
                startActivity(new Intent(mContext, OpcionesLoginActivity.class));
                getActivity().finishAffinity();
            }
        }.start();
    }

    private void actualizarEstado(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseAuth.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("estado", "Offline");
        reference.updateChildren(hashMap);
    }
    private void cargarInformacion() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombres = ""+snapshot.child("nombres").getValue();
                        String email = ""+snapshot.child("email").getValue();
                        String proveedor = ""+snapshot.child("proveedor").getValue();
                        String t_registro = ""+snapshot.child("tiempoR").getValue();
                        String imagen = ""+snapshot.child("imagen").getValue();

                        if (t_registro.equals("null")){
                            t_registro = "0";
                        }

                        String fecha = Constantes.formatoFecha(Long.parseLong(t_registro));

                        /*Setear la información en las vistas*/
                        binding.tvNombres.setText(nombres);
                        binding.tvEmail.setText(email);
                        binding.tvProveedor.setText(proveedor);
                        binding.tvTRegistro.setText(fecha);

                        /*Setear la imagen*/
                        Glide.with(mContext.getApplicationContext())
                                .load(imagen)
                                .placeholder(R.drawable.ic_img_perfil)
                                .into(binding.ivPerfil);

                        if (proveedor.equals("Email")){
                            binding.btnCambiarPass.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}