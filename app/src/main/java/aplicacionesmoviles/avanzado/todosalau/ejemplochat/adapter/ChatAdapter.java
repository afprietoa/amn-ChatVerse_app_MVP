package aplicacionesmoviles.avanzado.todosalau.ejemplochat.adapter;

import android.content.Context;
import android.location.GnssAntennaInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.common.util.concurrent.Service;
import com.google.firebase.firestore.auth.User;

import java.util.List;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Chat;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.model.UserModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private List<UserModel> userModelList;
    private static OnChatClickListener listener;


    public ChatAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_chats, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
        UserModel user = userModelList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }


    static class ChatViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivperfil;
        TextView tvNombres, tvUltimoMensaje, tvFecha;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            ivperfil = itemView.findViewById(R.id.IvPerfil);
            tvNombres = itemView.findViewById(R.id.tv_nombres);
            tvUltimoMensaje = itemView.findViewById(R.id.tvUltimoMensaje);
            tvFecha = itemView.findViewById(R.id.tvFecha);


            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && itemView.getContext() instanceof Chat) {
                    UserModel user = ((ChatAdapter) itemView.getTag()).userModelList.get(position);
                    ((Chat) itemView.getContext()).openConversation(user);
                    listener.openConversation(user);
                }
            });
        }

        void bind(UserModel user) {
            tvNombres.setText(user.getName());
            tvUltimoMensaje.setText(user.getLastMessage());
            tvFecha.setText(user.getLastMessageTime());
        }
    }

    public interface OnChatClickListener {
        void openConversation(UserModel user);
    }

    public void setOnChatClickListener(OnChatClickListener listener) {
        this.listener = listener;
    }

    public void updateUserList(List<UserModel> newUserList) {
        userModelList = newUserList;
        notifyDataSetChanged();
    }
}
