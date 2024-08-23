package aplicacionesmoviles.avanzado.todosalau.ejemplochat;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos.FragmentChats;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos.FragmentPerfil;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Fragmentos.FragmentUsuarios;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Iniciar con el fragmento Perfil por defecto
        verFragmentoPerfil();

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNV);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    verFragmentoChats();
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    verFragmentoPerfil();
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Configurar el botón flotante para redirigir al fragmento de usuariosFloatingActionButtonfab= findViewById(R.id.fab);
        FloatingActionButton fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
                    public void onClick(View v) {
                verFragmentoUsuarios();
            }
        });
    }

    private void verFragmentoPerfil() {
        FragmentPerfil fragment=new FragmentPerfil();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentosFL, fragment, "FragmentPerfil");
        fragmentTransaction.commit();
    }

    private void verFragmentoUsuarios() {
        FragmentUsuarios fragment=new FragmentUsuarios();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentosFL, fragment, "FragmentUsuarios");
        fragmentTransaction.commit();
    }

    private void verFragmentoChats() {
        FragmentChats fragment=new FragmentChats();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentosFL, fragment, "FragmentChats");
        fragmentTransaction.commit();
    }
}

