package net.macdidi.myandroidtutorial;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


public class ItemAppWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    @Override
         public void onDeleted(Context context, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {
            // 刪除小工具已經儲存的記事編號
            ItemAppWidgetConfigureActivity.deleteItemPref(
                    context, appWidgetIds[i]);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // 讀取小工具儲存的記事編號
        long id = ItemAppWidgetConfigureActivity.loadItemPref(
                context, appWidgetId);
        // 建立小工具畫面元件
        RemoteViews views = new RemoteViews(
                context.getPackageName(), R.layout.item_app_widget);
        // 讀取指定編號的記事物件
        ItemDAO itemDAO = new ItemDAO(context.getApplicationContext());
        Item item = itemDAO.get(id);

        // 設定小工具畫面顯示記事標題
        views.setTextViewText(R.id.appwidget_text,
                item != null ? item.getTitle() : "NA");

        // 點選小工具畫面的記事標題後，啟動記事應用程式
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(
                context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pending);

        // 更新小工具
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}


