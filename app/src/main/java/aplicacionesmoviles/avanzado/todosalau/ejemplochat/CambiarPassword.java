package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.ActivityCambiarPasswordBinding;

public class CambiarPassword extends AppCompatActivity {

    private ActivityCambiarPasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCambiarPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.IbRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.btnCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarInformacion();
            }
        });
    }


    private String pass_actual = "";
    private String pass_nueva = "";
    private String r_pass_nueva = "";
    private void validarInformacion() {
        pass_actual = binding.etPassActual.getText().toString().trim();
        pass_nueva = binding.etPassNueva.getText().toString().trim();
        r_pass_nueva = binding.etRPassNueva.getText().toString().trim();

        if (pass_actual.isEmpty()){
            binding.etPassActual.setError("Ingrese contraseña actual");
            binding.etPassActual.requestFocus();
        }
        else if (pass_nueva.isEmpty()){
            binding.etPassNueva.setError("Ingrese contraseña nueva");
            binding.etPassNueva.requestFocus();
        }
        else if (r_pass_nueva.isEmpty()){
            binding.etRPassNueva.setError("Repita nueva contraseña");
            binding.etRPassNueva.requestFocus();
        }
        else if (!pass_nueva.equals(r_pass_nueva)){
            binding.etRPassNueva.setError("No coinciden las contraseñas");
            binding.etRPassNueva.requestFocus();
        }
        else {
            autenticarUsuario();
        }

    }

    private void autenticarUsuario() {
        progressDialog.setMessage("Autenticando usuario");
        progressDialog.show();

        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(), pass_actual);
        firebaseUser.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        actualizarPassword();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                CambiarPassword.this,
                                "Falló la autenticación debido a : "+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

    }

    private void actualizarPassword() {
        progressDialog.setMessage("Actualizando contraseña");
        progressDialog.dismiss();

        firebaseUser.updatePassword(pass_nueva)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                CambiarPassword.this,
                                "La contraseña se ha actualizado",
                                Toast.LENGTH_SHORT
                        ).show();
                        cerrarSesion();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                CambiarPassword.this,
                                "Falló la actualización de la contraseña debido a : "+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

    }

    private void cerrarSesion() {
        firebaseAuth.signOut();
        startActivity(new Intent(this, OpcionesLoginActivity.class));
        finishAffinity();
    }
}