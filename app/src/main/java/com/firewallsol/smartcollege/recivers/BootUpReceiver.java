package com.firewallsol.smartcollege.recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bluejamesbond.text.Console;
import com.firewallsol.smartcollege.MainActivity;

/**
 * Created by Darkuz on 09/02/2017.
 */

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Console.log("iniciado", "ok");
    }
}