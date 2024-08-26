package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.ActivityOpcionesLoginBinding;

import java.util.HashMap;

public class OpcionesLoginActivity extends AppCompatActivity {

    private ActivityOpcionesLoginBinding binding;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOpcionesLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        comprobarSesion();

        binding.opcionEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OpcionesLoginActivity.this, LoginEmailActivity.class));
            }
        });

        binding.opcionGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarGoogle();
            }
        });
    }

    private void iniciarGoogle() {
        Intent googleSignInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInARL.launch(googleSignInIntent);
    }

    private ActivityResultLauncher<Intent> googleSignInARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult resultado) {
                    if (resultado.getResultCode() == Activity.RESULT_OK){
                        Intent data = resultado.getData();

                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount cuenta = task.getResult(ApiException.class);
                            autenticarCuentaGoogle(cuenta.getIdToken());
                        }catch (ApiException e){
                            Toast.makeText(
                                    OpcionesLoginActivity.this,
                                    ""+e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }else {
                        Toast.makeText(
                                OpcionesLoginActivity.this,
                                "Cancelado",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
    );

    private void autenticarCuentaGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        if (authResult.getAdditionalUserInfo().isNewUser()){
                            actualizarInfoUsuario();
                        }else {
                            startActivity(new Intent(OpcionesLoginActivity.this,
                                    MainActivity.class));
                            finishAffinity();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                OpcionesLoginActivity.this,
                                ""+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }

    private void actualizarInfoUsuario() {
        progressDialog.setMessage("Guardando información");

        String uidU = firebaseAuth.getUid();
        String nombresU = firebaseAuth.getCurrentUser().getDisplayName();
        String emailU = firebaseAuth.getCurrentUser().getEmail();
        long tiempoR = Constantes.obtenerTiempoD();

        HashMap<String, Object> datosUsuario = new HashMap<>();

        datosUsuario.put("uid", uidU);
        datosUsuario.put("nombres", nombresU);
        datosUsuario.put("email", emailU);
        datosUsuario.put("tiempoR", tiempoR);
        datosUsuario.put("proveedor", "Google");
        datosUsuario.put("estado", "Online");
        datosUsuario.put("imagen", "");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(uidU)
                .setValue(datosUsuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        startActivity(new Intent(OpcionesLoginActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                OpcionesLoginActivity.this,
                                "Falló el registro debido a : "+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });

    }

    private void comprobarSesion() {
        if (firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            finishAffinity();
        }
    }
}