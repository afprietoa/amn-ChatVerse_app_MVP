package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;

public class ActivityChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Encuentra la vista de animación Lottie
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_animation_view2);

        // Configura la animación (esto es opcional si ya está configurada en XML)
        lottieAnimationView.setAnimation("checkpasswordbluelight.json");
        lottieAnimationView.setRepeatCount(LottieDrawable.INFINITE); // Repetir la animación
        lottieAnimationView.playAnimation(); // Reproducir la animación
        // Encuentra el TextView
        TextView textView = findViewById(R.id.Txt_cambiar_pass);

        // Obtener el texto de los recursos
        String text = getString(R.string.Txt_chatverse);
        SpannableString spannableString = new SpannableString(text);

        // Aplicar el color negro a la palabra "Chat"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#0D141C")), 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Aplicar el color azul a la palabra "Verse"
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#005ADB")), 4, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
    }
}