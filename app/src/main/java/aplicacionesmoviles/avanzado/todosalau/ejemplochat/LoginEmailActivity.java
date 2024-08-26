package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.ActivityLoginEmailBinding;

public class LoginEmailActivity extends AppCompatActivity {

    private ActivityLoginEmailBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarInformacion();
            }
        });

        binding.tvRecuperarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailActivity.this, OlvidePassword.class));
            }
        });

        binding.tvRegistrarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginEmailActivity.this, RegistroEmailActivity.class));
            }
        });
    }

    private String email , password;
    private void validarInformacion() {
        email = binding.etEmail.getText().toString().trim();
        password = binding.etPassword.getText().toString().trim();

        if (email.isEmpty()){
            binding.etEmail.setError("Ingrese email");
            binding.etEmail.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.setError("Email inválido");
            binding.etEmail.requestFocus();
        }
        else if (password.isEmpty()){
            binding.etPassword.setError("Ingrese password");
            binding.etPassword.requestFocus();
        }
        else {
            loginUsuario();
        }

    }

    private void loginUsuario() {
        progressDialog.setMessage("Ingresando");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginEmailActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                LoginEmailActivity.this,
                                "No se pudo logear debido a : "+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });


    }
}