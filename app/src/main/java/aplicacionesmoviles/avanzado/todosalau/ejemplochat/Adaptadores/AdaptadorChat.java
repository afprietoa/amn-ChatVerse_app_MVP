package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Adaptadores;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Constantes;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos.Chat;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;

import java.util.ArrayList;

public class AdaptadorChat extends RecyclerView.Adapter<AdaptadorChat.HolderChat>{

    private Context context;
    private ArrayList<Chat> chatArrayList;

    private static final int MENSAJE_IZQUIERDO = 0;
    private static final int MENSAJE_DERECHO = 1;

    private FirebaseUser firebaseUser;

    String chatRuta = "";

    Dialog dialog;

    public AdaptadorChat(Context context, ArrayList<Chat> chatArrayList) {
        this.context = context;
        this.chatArrayList = chatArrayList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public HolderChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MENSAJE_DERECHO){
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_derecho,parent,false);
            return new HolderChat(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_izquierdo,parent,false);
            return new HolderChat(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderChat holder, int position) {
        Chat chat = chatArrayList.get(position);

        String mensaje = chat.getMensaje();
        String tipoMensaje = chat.getTipoMensaje();
        long tiempo = chat.getTiempo();

        String formatoFechaHora = Constantes.formatoFechaHora(tiempo);

        /*IMAGENES DE TIPO TEXTO*/
        if (tipoMensaje.equals(Constantes.MENSAJE_TIPO_TEXTO)){
            holder.Tv_mensaje.setVisibility(View.VISIBLE);
            holder.Iv_mensaje.setVisibility(View.GONE);

            holder.Tv_mensaje.setText(mensaje);

            if (chat.getEmisorUid().equals(firebaseUser.getUid())){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String [] opciones = {"Eliminar mensaje", "Cancelar"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("¿Qué desea realizar?");
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0){
                                    eliminarMensaje(position, holder , chat);
                                }
                            }
                        });

                        builder.show();
                    }
                });
            }

        }

        /*IMAGEN DE TIPO IMAGEN*/
        else {
            holder.Tv_mensaje.setVisibility(View.GONE);
            holder.Iv_mensaje.setVisibility(View.VISIBLE);

            try {

                Glide.with(context)
                        .load(mensaje)
                        .placeholder(R.drawable.imagen_enviada)
                        .error(R.drawable.imagen_chat_falla)
                        .into(holder.Iv_mensaje);

            }catch (Exception e){

            }

            if (chat.getEmisorUid().equals(firebaseUser.getUid())){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String [] opciones = {"Eliminar imagen", "Ver imagen", "Cancelar"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("¿Qué desea realizar?");
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0 ){
                                    eliminarMensaje(position, holder, chat);
                                }else if (which == 1){
                                    visualizadorImagen(chat.getMensaje());
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            else if (!chat.getEmisorUid().equals(firebaseUser.getUid())){
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String [] opciones = {"Ver imagen", "Cancelar"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                        builder.setTitle("¿Qué desea realizar?");
                        builder.setItems(opciones, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0 ){
                                    visualizadorImagen(chat.getMensaje());
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        }

        holder.Tv_tiempo_mensaje.setText(formatoFechaHora);
    }

    private void eliminarMensaje(int position, HolderChat holder, Chat chat) {
        chatRuta = Constantes.chatRuta(chat.getReceptorUid(), chat.getEmisorUid());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.child(chatRuta).child(chatArrayList.get(position).getIdMensaje())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(
                                holder.itemView.getContext(),
                                "El mensaje se ha eliminado",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(
                                holder.itemView.getContext(),
                                "No se ha eliminado el mensaje debido a "+e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });


    }

    @Override
    public int getItemCount() {
        return chatArrayList.size();
    }

    private void visualizadorImagen(String imagen){
        PhotoView Pv;
        MaterialButton btnCerrar;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.cuadro_d_visualizador_img);

        Pv = dialog.findViewById(R.id.Pv_img);
        btnCerrar = dialog.findViewById(R.id.BtnCerrarVisualizador);

        try {
            Glide.with(context)
                    .load(imagen)
                    .placeholder(R.drawable.imagen_enviada)
                    .into(Pv);
        }catch (Exception e){

        }

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getItemViewType(int position) {
        if (chatArrayList.get(position).getEmisorUid().equals(firebaseUser.getUid())){
            return MENSAJE_DERECHO;
        }else {
            return MENSAJE_IZQUIERDO;
        }
    }

    class HolderChat extends RecyclerView.ViewHolder{
        TextView Tv_mensaje, Tv_tiempo_mensaje;
        ShapeableImageView Iv_mensaje;

        public HolderChat(@NonNull View itemView) {
            super(itemView);
            Tv_mensaje = itemView.findViewById(R.id.Tv_mensaje);
            Tv_tiempo_mensaje = itemView.findViewById(R.id.Tv_tiempo_mensaje);
            Iv_mensaje = itemView.findViewById(R.id.Iv_mensaje);
        }
    }
}
