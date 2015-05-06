package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    // 移除原來的ListView元件
    //private ListView item_list;

    // 加入下列需要的元件
    private RecyclerView item_list;
    private RecyclerView.Adapter itemAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    private TextView show_app_name;

    // 移除原來的ItemAdapter
    //private ItemAdapter itemAdapter;

    // 儲存所有記事本的List物件
    private List<Item> items;

    // 選單項目物件
    private MenuItem add_item, search_item, revert_item, share_item, delete_item;

    // 已選擇項目數量
    private int selectedCount = 0;

    // 宣告資料庫功能類別欄位變數
    private ItemDAO itemDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processViews();
        // 移除註冊監聽事件的工作，要移到下面執行
        // processControllers();

        itemDAO = new ItemDAO(getApplicationContext());

        if (itemDAO.getCount() == 0) {
            itemDAO.sample();
        }

        items = itemDAO.getAll();

        // 移除原來ListView元件執行的工作
        //itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        //item_list.setAdapter(itemAdapter);

        // 執行RecyclerView元件的設定
        item_list.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        item_list.setLayoutManager(rvLayoutManager);

        // 在這裡執行註冊監聽事件的工作
        processControllers();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Item item = (Item) data.getExtras().getSerializable(
                    "net.macdidi.myandroidtutorial.Item");

            boolean updateAlarm = false;

            if (requestCode == 0) {
                item = itemDAO.insert(item);

                items.add(item);
                itemAdapter.notifyDataSetChanged();
            }
            else if (requestCode == 1) {
                int position = data.getIntExtra("position", -1);

                if (position != -1) {
                    Item ori = itemDAO.get(item.getId());
                    updateAlarm = (item.getAlarmDatetime() != ori.getAlarmDatetime());

                    itemDAO.update(item);

                    items.set(position, item);
                    itemAdapter.notifyDataSetChanged();
                }
            }

            if (item.getAlarmDatetime() != 0 && updateAlarm) {
                Intent intent = new Intent(this, AlarmReceiver.class);
                intent.putExtra("id", item.getId());
                PendingIntent pi = PendingIntent.getBroadcast(
                        this, (int)item.getId(),
                        intent, PendingIntent.FLAG_ONE_SHOT);
                AlarmManager am = (AlarmManager)
                        getSystemService(Context.ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, item.getAlarmDatetime(), pi);
            }
        }
    }

    private void processViews() {
        item_list = (RecyclerView) findViewById(R.id.item_list);
        show_app_name = (TextView) findViewById(R.id.show_app_name);
    }

    private void processControllers() {

        itemAdapter = new ItemAdapterRV(items) {
            @Override
            public void onBindViewHolder(final ViewHolder holder, final int position) {
                super.onBindViewHolder(holder, position);

                // 建立與註冊項目點擊監聽物件
                holder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 讀取選擇的記事物件
                        Item item = items.get(position);

                        // 如果已經有勾選的項目
                        if (selectedCount > 0) {
                            // 處理是否顯示已選擇項目
                            processMenu(item);
                            // 重新設定記事項目
                            items.set(position, item);
                        } else {
                            Intent intent = new Intent(
                                    "net.macdidi.myandroidtutorial.EDIT_ITEM");

                            // 設定記事編號與記事物件
                            intent.putExtra("position", position);
                            intent.putExtra("net.macdidi.myandroidtutorial.Item", item);

                            // 依照版本啟動Acvitity元件
                            startActivityForVersion(intent, 1);
                        }
                    }
                });

                // 建立與註冊項目長按監聽物件
                holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // 讀取選擇的記事物件
                        Item item = items.get(position);
                        // 處理是否顯示已選擇項目
                        processMenu(item);
                        // 重新設定記事項目
                        items.set(position, item);
                        return true;
                    }
                });
            }
        };

        item_list.setAdapter(itemAdapter);
    }

    // 處理是否顯示已選擇項目
    private void processMenu(Item item) {
        if (item != null) {
            item.setSelected(!item.isSelected());

            if (item.isSelected()) {
                selectedCount++;
            }
            else {
                selectedCount--;
            }
        }

        add_item.setVisible(selectedCount == 0);
        search_item.setVisible(selectedCount == 0);
        revert_item.setVisible(selectedCount > 0);
        share_item.setVisible(selectedCount > 0);
        delete_item.setVisible(selectedCount > 0);

        // 通知項目勾選狀態改變
        itemAdapter.notifyDataSetChanged();
    }

    // 載入選單資源
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        // 取得選單項目物件
        add_item = menu.findItem(R.id.add_item);
        search_item = menu.findItem(R.id.search_item);
        revert_item = menu.findItem(R.id.revert_item);
        share_item = menu.findItem(R.id.share_item);
        delete_item = menu.findItem(R.id.delete_item);

        // 設定選單項目
        processMenu(null);

        return true;
    }

    // 使用者選擇所有的選單項目都會呼叫這個方法
    public void clickMenuItem(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.search_item:
                break;
            case R.id.add_item:
                Intent intent = new Intent("net.macdidi.myandroidtutorial.ADD_ITEM");
                startActivityForVersion(intent, 0);
                break;
            // 取消所有已勾選的項目
            case R.id.revert_item:
                for (int i = 0; i < items.size(); i++) {
                    Item ri = items.get(i);

                    if (ri.isSelected()) {
                        ri.setSelected(false);
                        // 移除
                        //itemAdapter.set(i, ri);
                    }
                }

                selectedCount = 0;
                processMenu(null);

                break;
            // 刪除
            case R.id.delete_item:
                if (selectedCount == 0) {
                    break;
                }

                AlertDialog.Builder d = new AlertDialog.Builder(this);
                String message = getString(R.string.delete_item);
                d.setTitle(R.string.delete)
                        .setMessage(String.format(message, selectedCount));
                d.setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = items.size() - 1;

                                while (index > -1) {
                                    // 改為使用items物件
                                    Item item = items.get(index);

                                    if (item.isSelected()) {
                                        // 改為使用items物件
                                        items.remove(item);
                                        itemDAO.delete(item.getId());
                                    }

                                    index--;
                                }

                                // 移除
                                //itemAdapter.notifyDataSetChanged();
                                selectedCount = 0;
                                processMenu(null);
                            }
                        });
                d.setNegativeButton(android.R.string.no, null);
                d.show();

                break;
            case R.id.googleplus_item:
                break;
            case R.id.facebook_item:
                break;
        }

    }

    public void aboutApp(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void clickPreferences(MenuItem item) {
        // 依照版本啟動Acvitity元件
        startActivityForVersion(new Intent(this, PrefActivity.class));
    }

    private void startActivityForVersion(Intent intent, int requestCode) {
        // 如果裝置的版本是LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 加入畫面轉換設定
            startActivityForResult(intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(
                            MainActivity.this).toBundle());
        }
        else {
            startActivityForResult(intent, requestCode);
        }
    }

    private void startActivityForVersion(Intent intent) {
        // 如果裝置的版本是LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 加入畫面轉換設定
            startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(
                            MainActivity.this).toBundle());
        }
        else {
            startActivity(intent);
        }
    }

}


