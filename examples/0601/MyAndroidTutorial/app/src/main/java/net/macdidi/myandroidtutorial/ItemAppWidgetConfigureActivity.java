package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;


public class ItemAppWidgetConfigureActivity extends Activity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private static final String PREFS_NAME =
            "net.macdidi.myandroidtutorial.ItemAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    // 選擇小工具使用的記事項目
    private ListView item_list;
    private ItemAdapter itemAdapter;
    private List<Item> items;
    private ItemDAO itemDAO;

    public ItemAppWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setResult(RESULT_CANCELED);

        // 改為使用應用程式主畫面
        setContentView(R.layout.activity_main);

        // 建立與設定選擇小工具使用的記事項目需要的物件
        item_list = (ListView)findViewById(R.id.item_list);
        itemDAO = new ItemDAO(getApplicationContext());
        items = itemDAO.getAll();
        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        item_list.setAdapter(itemAdapter);
        item_list.setOnItemClickListener(itemListener);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

    }

    // 選擇記事項目
    AdapterView.OnItemClickListener itemListener =
            new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            final Context context = ItemAppWidgetConfigureActivity.this;

            // 讀取與儲存選擇的記事物件
            Item item = itemAdapter.getItem(position);
            saveItemPref(context, mAppWidgetId, item.getId());

            AppWidgetManager appWidgetManager =
                    AppWidgetManager.getInstance(context);
            ItemAppWidget.updateAppWidget(
                    context, appWidgetManager, mAppWidgetId);
            Intent resultValue = new Intent();
            resultValue.putExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);

            finish();
        }
    };

    // 儲存選擇的記事編號
    static void saveItemPref(Context context, int appWidgetId, long id) {
        SharedPreferences.Editor prefs =
                context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putLong(PREF_PREFIX_KEY + appWidgetId, id);
        prefs.commit();
    }

    // 讀取記事編號
    static long loadItemPref(Context context, int appWidgetId) {
        SharedPreferences prefs =
                context.getSharedPreferences(PREFS_NAME, 0);
        long idValue = prefs.getLong(PREF_PREFIX_KEY + appWidgetId, 0);

        return idValue;
    }

    // 刪除記事編號
    static void deleteItemPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs =
                context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.commit();
    }

}



