package com.android.lv.kuawo;

/**
 * Created by lv on 2017/1/4.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        try {
            new MainActivity().prideMeTwice();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}