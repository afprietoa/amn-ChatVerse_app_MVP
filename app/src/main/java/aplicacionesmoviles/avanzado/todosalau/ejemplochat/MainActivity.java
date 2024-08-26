package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.messaging.FirebaseMessaging;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos.FragmentChats;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos.FragmentPerfil;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos.FragmentUsuarios;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        comprobarSesion();

        verFragmentoPerfil();

        binding.bottomNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.item_perfil){
                    //Visualizar el fragmento perfil
                    verFragmentoPerfil();
                    return true;
                }
                else if (itemId == R.id.item_usuarios){
                    //Visualizar el fragmento usuarios
                    verFragmentoUsuarios();
                    return true;
                }
                else if (itemId == R.id.item_chats){
                    //Visualizar el fragmento chats
                    verFragmentoChats();
                    return true;
                }
                else {
                    return false;
                }

            }
        });
    }

    private void comprobarSesion() {
        if (firebaseAuth.getCurrentUser() == null){
            startActivity(new Intent(this, OpcionesLoginActivity.class));
            finishAffinity();
        }else {
            agregarToken();
            solicitarPermisoNotificacion();
        }

    }

    private void verFragmentoPerfil(){
        //binding.tvTitulo.setText("Perfil");

        FragmentPerfil fragment = new FragmentPerfil();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentosFL.getId(), fragment , "FragmentPerfil");
        fragmentTransaction.commit();
    }

    private void verFragmentoUsuarios(){
        //binding.tvTitulo.setText("Usuarios");

        FragmentUsuarios fragment = new FragmentUsuarios();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentosFL.getId(), fragment , "FragmentUsuarios");
        fragmentTransaction.commit();
    }

    private void verFragmentoChats(){
        //binding.tvTitulo.setText("Chats");

        FragmentChats fragment = new FragmentChats();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(binding.fragmentosFL.getId(), fragment , "FragmentChats");
        fragmentTransaction.commit();
    }

    private void actualizarEstado(String estado){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios")
                .child(firebaseAuth.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("estado", estado);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth.getCurrentUser() != null){
            actualizarEstado("Online");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth.getCurrentUser()!=null){
            actualizarEstado("Offline");

        }
    }

    private void agregarToken(){
        String miUid = firebaseAuth.getUid();

        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("fcmToken", token);
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
                        reference.child(miUid).updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        /*Se agregó el token con éxito*/
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void solicitarPermisoNotificacion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_DENIED){
                concederPermiso.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private ActivityResultLauncher<String> concederPermiso = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean o) {
                    //Se ha concedido el permiso
                }
            }
    );
}