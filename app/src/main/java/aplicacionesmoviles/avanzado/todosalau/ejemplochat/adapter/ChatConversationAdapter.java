package aplicacionesmoviles.avanzado.todosalau.ejemplochat.adapter;

import android.os.UserHandle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.model.MessageModel;

public class ChatConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private List<MessageModel> messageList;
    private String currentUserId;


    public ChatConversationAdapter(List<MessageModel> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_right, parent, false);
            return new SentMessageViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_left, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel message = messageList.get(position);

        if(holder.getItemViewType() == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).bind(message);
        }else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }



      class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMensaje, tvTiempoMensaje;
        ShapeableImageView ivMensaje;


        SentMessageViewHolder(@NonNull View itemView){
            super(itemView);
            tvMensaje = itemView.findViewById(R.id.Tv_mensaje);
            tvTiempoMensaje = itemView.findViewById(R.id.Tv_tiempo_mensaje);
            ivMensaje = itemView.findViewById(R.id.Iv_mensaje);
        }

        public void bind(MessageModel message) {

            tvMensaje.setText(message.getMessageText());
            tvTiempoMensaje.setText(formatTimestamp(message.getTimestamp().getTime()));
        }

    }


        class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{
        TextView tvMensaje, tvTiempoMensaje;
        ShapeableImageView ivMensaje;

        ReceivedMessageViewHolder(@NonNull View itemView){
            super(itemView);

            tvMensaje = itemView.findViewById(R.id.Tv_mensaje);
            tvTiempoMensaje = itemView.findViewById(R.id.Tv_tiempo_mensaje);
            ivMensaje = itemView.findViewById(R.id.Iv_mensaje);
        }
            void bind(MessageModel message) {
                tvMensaje.setText(message.getMessageText());
                tvTiempoMensaje.setText(formatTimestamp(message.getTimestamp().getTime()));

        }

    }

    private String formatTimestamp(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }










}
