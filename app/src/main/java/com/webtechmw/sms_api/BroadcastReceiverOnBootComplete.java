/**
 * Created by mangochiman on 8/20/17.
 */
package com.webtechmw.sms_api;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverOnBootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, SMSServiceStartOnBoot.class);
            context.startService(serviceIntent);
        }
    }
}
