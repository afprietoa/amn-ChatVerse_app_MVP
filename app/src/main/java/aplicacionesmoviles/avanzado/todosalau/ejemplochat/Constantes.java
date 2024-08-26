package aplicacionesmoviles.avanzado.todosalau.ejemplochat;

import android.text.format.DateFormat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class Constantes {

    public static final String MENSAJE_TIPO_TEXTO = "TEXTO";
    public static final String MENSAJE_TIPO_IMAGEN = "IMAGEN";

    public static final String NOTIFICACION_DE_NUEVO_MENSAJE = "NOTIFICACION_DE_NUEVO_MENSAJE";
    public static final String FCM_SERVER_KEY = "AAAAbKNEbb4:APA91bGvUD3yZngCkVMbxAyinrCGTAP1LRayQWY6Nd_wGfBdbJ4_LDSw-w-cSi-Ado2HsYAFuqqLRYhS9-x5gyanuQsVJ0gNXusZRIUO3qdkPntdx_HIFY7OJa6NL0zDQz__fc93VUPA";

    public static long obtenerTiempoD(){
        return System.currentTimeMillis();
    }

    public static String formatoFecha(Long tiempo){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(tiempo);

        String fecha = DateFormat.format("dd/MM/yyyy", calendar).toString();
        return fecha;
    }

    public static String formatoFechaHora(Long tiempo){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(tiempo);

        String fecha = DateFormat.format("dd/MM/yyyy hh:mm:a", calendar).toString();
        return fecha;
    }

    public static String chatRuta (String receptorUid, String emisorUid){
        String[] arrayUid = new String[]{receptorUid,emisorUid};
        Arrays.sort(arrayUid);
        //Nuestro uid [emisor] = 1plNxwsL2wgjsEsyoZWt1TeD1cf1
        //Uid del usuario con quien entablamos la conversación [receptor] = BqxDDpcD4BbhSWVGRkndTDkqt2o2
        //La ruta seria = 1plNxwsL2wgjsEsyoZWt1TeD1cf1_BqxDDpcD4BbhSWVGRkndTDkqt2o2
        String chatRuta = arrayUid[0]+"_"+arrayUid[1];
        return chatRuta;
    }

}
