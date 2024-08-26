package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Chat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Adaptadores.AdaptadorChat;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Constantes;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos.Chat;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.databinding.ActivityChatBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    String miUid = "";
    String uid = "";

    String chatRuta = "";

    private Uri imagenUri = null;

    private String miNombre = "";
    private String recibimosToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        miUid = firebaseAuth.getUid();
        uid = getIntent().getStringExtra("uid");

        chatRuta = Constantes.chatRuta(uid, miUid);

        binding.adjuntarFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    imagenGaleria();
                }else {
                    solicitarPermisoAlmacenamiento.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        binding.IbRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.enviarFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarMensaje();
            }
        });

        cargarMiInfo();
        cargarInfo();
        cargarMensajes();
    }

    private void cargarMiInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(""+firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        miNombre = ""+snapshot.child("nombres").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void cargarMensajes() {
        ArrayList<Chat> chatArrayList= new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.child(chatRuta)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        chatArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            try {
                                Chat chat = ds.getValue(Chat.class);
                                chatArrayList.add(chat);
                            }catch (Exception e){

                            }
                        }

                        AdaptadorChat adaptadorChat = new AdaptadorChat(ChatActivity.this, chatArrayList);
                        binding.chatsRV.setAdapter(adaptadorChat);

                        binding.chatsRV.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
                        linearLayoutManager.setStackFromEnd(true);
                        binding.chatsRV.setLayoutManager(linearLayoutManager);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void validarMensaje() {
        String mensaje = binding.EtMensajeChat.getText().toString().trim();
        long tiempo = Constantes.obtenerTiempoD();

        if (mensaje.equals("")){
            Toast.makeText(this, "Ingrese un mensaje",Toast.LENGTH_SHORT).show();
        }else {
            enviarMensaje(Constantes.MENSAJE_TIPO_TEXTO , mensaje , tiempo);
        }
    }

    private void cargarInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
        reference.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String nombres = ""+snapshot.child("nombres").getValue();
                        String imagen = ""+snapshot.child("imagen").getValue();
                        String estado = ""+snapshot.child("estado").getValue();
                        recibimosToken = ""+snapshot.child("fcmToken").getValue();

                        binding.txtNombresUsuario.setText(nombres);
                        binding.txtEstadoChat.setText(estado);

                        try {
                            Glide.with(getApplicationContext())
                                    .load(imagen)
                                    .placeholder(R.drawable.perfil_usuario)
                                    .into(binding.toolbarIv);
                        }catch (Exception e){

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void imagenGaleria(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaARL.launch(intent);
    }

    private ActivityResultLauncher<Intent> galeriaARL = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult resultado) {
                    if (resultado.getResultCode() == Activity.RESULT_OK){
                        Intent data = resultado.getData();
                        imagenUri = data.getData();
                        subirImgStorage();
                    }else {
                        Toast.makeText(
                                ChatActivity.this,
                                "Cancelado",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<String> solicitarPermisoAlmacenamiento = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean esConcedido) {
                    if (esConcedido){
                        imagenGaleria();
                    }else {
                        Toast.makeText(
                                ChatActivity.this,
                                "El permiso de almacemiento no ha sido concedido",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
    );


    private void subirImgStorage(){
        progressDialog.setMessage("Subiendo imagen");
        progressDialog.show();

        long tiempo = Constantes.obtenerTiempoD();
        String nombreRutaImagen = "ImagenChat/"+tiempo;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(nombreRutaImagen);
        storageReference.putFile(imagenUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        String urlImagen = uriTask.getResult().toString();
                        if (uriTask.isSuccessful()){
                            enviarMensaje(Constantes.MENSAJE_TIPO_IMAGEN, urlImagen, tiempo);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void enviarMensaje (String tipoMensaje , String mensaje , long tiempo){
        progressDialog.setMessage("Enviar mensaje");
        progressDialog.show();

        DatabaseReference refChat = FirebaseDatabase.getInstance().getReference("Chats");
        String keyId = "" + refChat.push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("idMensaje", ""+keyId);
        hashMap.put("tipoMensaje",""+tipoMensaje);
        hashMap.put("mensaje", ""+mensaje);
        hashMap.put("emisorUid", ""+miUid);
        hashMap.put("receptorUid", ""+uid);
        hashMap.put("tiempo", tiempo);

        refChat.child(chatRuta)
                .child(keyId)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        binding.EtMensajeChat.setText("");

                        if (tipoMensaje.equals(Constantes.MENSAJE_TIPO_TEXTO)){
                            prepararNotificacion(mensaje);
                        }else {
                            prepararNotificacion("Se envió una imagen");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                ChatActivity.this,
                                "No se pudo enviar el mensaje debido a :"+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
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
        if (firebaseAuth.getCurrentUser()!=null){
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

    private void prepararNotificacion(String mensaje){
        JSONObject notificationJo = new JSONObject();
        JSONObject notificationDataJo = new JSONObject();
        JSONObject notificationNotificationJo = new JSONObject();

        try {
            notificationDataJo.put("notificationType", ""+Constantes.NOTIFICACION_DE_NUEVO_MENSAJE);
            notificationDataJo.put("senderUid", ""+firebaseAuth.getUid());

            notificationNotificationJo.put("title", ""+miNombre);
            notificationNotificationJo.put("body", ""+mensaje);
            notificationNotificationJo.put("sound", "default");

            notificationJo.put("to", ""+recibimosToken);
            notificationJo.put("notification", notificationNotificationJo);
            notificationJo.put("data",notificationDataJo);

        }catch (Exception e){

        }

        enviarNotificacion(notificationJo);
    }

    private void enviarNotificacion(JSONObject notificationJo) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                notificationJo,
                response -> {
                    //La notificación fue enviada
                },
                error -> {
                    //Por que la notificación no fue enviada
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key="+Constantes.FCM_SERVER_KEY);
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


}