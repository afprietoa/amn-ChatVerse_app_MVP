package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Chat.ChatActivity;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Modelos.Usuario;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;

import java.util.List;

public class AdaptadorUsuario extends RecyclerView.Adapter<AdaptadorUsuario.ViewHolder>{

    private Context context;
    private List<Usuario> usuarioList;

    public AdaptadorUsuario(Context context, List<Usuario> usuarioList) {
        this.context = context;
        this.usuarioList = usuarioList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_usuario,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String uid = usuarioList.get(position).getUid();
        String nombres = usuarioList.get(position).getNombres();
        String email = usuarioList.get(position).getEmail();
        String imagen = usuarioList.get(position).getImagen();

        holder.uid.setText(uid);
        holder.nombres.setText(nombres);
        holder.email.setText(email);

        try {
            Glide.with(context)
                    .load(imagen)
                    .placeholder(R.drawable.ic_imagen_perfil)
                    .into(holder.imagen);
        }catch (Exception e){

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uid", holder.uid.getText());
                Toast.makeText(context, "Has seleccionado al usuario: "+holder.nombres.getText(),
                        Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usuarioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView uid , nombres , email;
        ImageView imagen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            uid = itemView.findViewById(R.id.item_uid);
            email = itemView.findViewById(R.id.item_email);
            nombres = itemView.findViewById(R.id.item_nombre);
            imagen = itemView.findViewById(R.id.item_perfil);
        }
    }
}
