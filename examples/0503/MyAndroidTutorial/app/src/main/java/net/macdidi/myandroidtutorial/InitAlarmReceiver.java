package net.macdidi.myandroidtutorial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

public class InitAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // 建立資料庫物件
        ItemDAO itemDAO = new ItemDAO(context.getApplicationContext());
        // 讀取資料庫所有記事資料
        List<Item> items = itemDAO.getAll();

        // 讀取目前時間
        long current = Calendar.getInstance().getTimeInMillis();

        AlarmManager am = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        for (Item item : items) {
            long alarm = item.getAlarmDatetime();

            // 如果沒有設定提醒或是提醒已經過期
            if (alarm == 0 || alarm <= current) {
                continue;
            }

            // 設定提醒
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            //alarmIntent.putExtra("title", item.getTitle());

            // 加入記事編號
            intent.putExtra("id", item.getId());

            PendingIntent pi = PendingIntent.getBroadcast(
                    context, (int)item.getId(),
                    alarmIntent, PendingIntent.FLAG_ONE_SHOT);
            am.set(AlarmManager.RTC_WAKEUP, item.getAlarmDatetime(), pi);
        }
    }

}
