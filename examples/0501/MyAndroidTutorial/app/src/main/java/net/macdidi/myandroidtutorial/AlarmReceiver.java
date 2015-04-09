package net.macdidi.myandroidtutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 讀取記事標題
        String title = intent.getStringExtra("title");
        // 顯示訊息框
        Toast.makeText(context, title, Toast.LENGTH_LONG).show();
    }

}
