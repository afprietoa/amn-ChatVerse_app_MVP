package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.ActivityOlvidePasswordBinding;

public class OlvidePassword extends AppCompatActivity {

    private ActivityOlvidePasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOlvidePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.IbRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarInformacion();
            }
        });

    }

    private String email = "";
    private void validarInformacion() {
        email = binding.etEmail.getText().toString().trim();

        if (email.isEmpty()){
            binding.etEmail.setError("Ingrese email");
            binding.etEmail.requestFocus();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.etEmail.setError("Email no válido");
            binding.etEmail.requestFocus();
        }
        else {
            enviarInstrucciones();
        }

    }

    private void enviarInstrucciones() {
        progressDialog.setMessage("Enviando las instrucciones a : "+email);
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                OlvidePassword.this,
                                "Instrucciones enviadas",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                OlvidePassword.this,
                                "Falló el envío de instrucciones debido a "+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });


    }
}