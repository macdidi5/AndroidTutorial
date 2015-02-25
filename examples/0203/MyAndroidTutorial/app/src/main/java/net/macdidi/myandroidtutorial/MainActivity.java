package net.macdidi.myandroidtutorial;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private ListView item_list;
    private TextView show_app_name;

    private static final String[] data = {
            "關於Android Tutorial的事情",
            "一隻非常可愛的小狗狗!",
            "一首非常好聽的音樂！"};
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processViews();
        processControllers();

        int layoutId = android.R.layout.simple_list_item_1;
        adapter = new ArrayAdapter<String>(this, layoutId, data);
        item_list.setAdapter(adapter);
    }

    private void processViews() {
        item_list = (ListView)findViewById(R.id.item_list);
        show_app_name = (TextView) findViewById(R.id.show_app_name);
    }

    private void processControllers() {
        // 建立選單項目點擊監聽物件
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            // 第一個參數是使用者操作的ListView物件
            // 第二個參數是使用者選擇的項目
            // 第三個參數是使用者選擇的項目編號，第一個是0
            // 第四個參數在這裡沒有用途
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this,
                        data[position], Toast.LENGTH_LONG).show();
            }
        };

        // 註冊選單項目點擊監聽物件
        item_list.setOnItemClickListener(itemListener);

        // 建立選單項目長按監聽物件
        AdapterView.OnItemLongClickListener itemLongListener = new AdapterView.OnItemLongClickListener() {
            // 第一個參數是使用者操作的ListView物件
            // 第二個參數是使用者選擇的項目
            // 第三個參數是使用者選擇的項目編號，第一個是0
            // 第四個參數在這裡沒有用途
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(MainActivity.this,
                        "Long: " + data[position], Toast.LENGTH_LONG).show();
                return false;
            }
        };

        // 註冊選單項目長按監聽物件
        item_list.setOnItemLongClickListener(itemLongListener);

        // 建立長按監聽物件
        View.OnLongClickListener listener = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder dialog =
                        new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle(R.string.app_name)
                        .setMessage(R.string.about)
                        .show();
                return false;
            }

        };

        // 註冊長按監聽物件
        show_app_name.setOnLongClickListener(listener);
    }

    // 載入選單資源
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // 使用者選擇所有的選單項目都會呼叫這個方法
    public void clickMenuItem(MenuItem item) {
        // 使用參數取得使用者選擇的選單項目元件編號
        int itemId = item.getItemId();

        // 判斷該執行什麼工作，目前還沒有加入需要執行的工作
        switch (itemId) {
            case R.id.search_item:
                break;
            case R.id.add_item:
                break;
            case R.id.revert_item:
                break;
            case R.id.delete_item:
                break;
            case R.id.googleplus_item:
                break;
            case R.id.facebook_item:
                break;
        }

        // 測試用的程式碼，完成測試後記得移除
        AlertDialog.Builder dialog =
                new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("MenuItem Test")
                .setMessage(item.getTitle())
                .setIcon(item.getIcon())
                .show();
    }

    // 方法名稱與onClick的設定一樣，參數的型態是android.view.View
    public void aboutApp(View view) {
        // 顯示訊息框
        // Context：通常指定為「this」；如果在巢狀類別中使用，要加上這個Activity元件類別的名稱，例如「元件類別名稱.this」
        // String或int：設定顯示在訊息框裡面的訊息或文字資源
        // int：設定訊息框停留在畫面的時間，使用宣告在Toast類別中的變數，可以設定為「LENGTH_LONG」和「LENGTH_SHORT」
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_LONG).show();
    }

}


