package com.firewallsol.smartcollege.Funciones;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firewallsol.smartcollege.Aplicacion;
import com.firewallsol.smartcollege.SplashScreen;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;

/*import com.islaazul.android.Aplicacion;
import com.islaazul.android.IslaChat;
import com.islaazul.android.IslaChatConversaciones;
import com.islaazul.android.MainActivity;
import com.islaazul.android.SplashScreen;*/

/**
 * Created on 30/10/15.
 */
public class ParseNotificacion extends ParsePushBroadcastReceiver {

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        try {
            String jsonParse = intent.getExtras().getString("com.parse.Data");

            if (Aplicacion.isApplicationActivityVisible) {

              /*   if(MainActivity.posicion_activa_menu != 8) {

                    MainActivity.selectItem(8);
                    MainActivity.navList.setItemChecked(MainActivity.posicion_activa_menu, true);

                } else {

                    JSONObject json = new JSONObject(jsonParse);
                    String destinatario = json.getString("destinatario");
                    String groupId = json.getString("groupId");
                    String nombre_user = json.getString("nombre_user");

                    if (Aplicacion.isConversacionActivityVisible)
                        IslaChatConversaciones.activity.finish();

                    IslaChat.openChat(destinatario, groupId, nombre_user);

                }*/

            } else {
                Intent it = new Intent(context, SplashScreen.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                it.putExtra("ParsePush", jsonParse);
                context.startActivity(it);
            }

            Log.e("PushOpen", jsonParse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String jsonParse = intent.getExtras().getString("com.parse.Data");

            if (Aplicacion.isApplicationActivityVisible && Aplicacion.isConversacionActivityVisible) {

                JSONObject json = new JSONObject(jsonParse);
                    super.onReceive(context, intent);

            } else {
                    super.onReceive(context, intent);
            }

            Log.e("Receive", jsonParse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
