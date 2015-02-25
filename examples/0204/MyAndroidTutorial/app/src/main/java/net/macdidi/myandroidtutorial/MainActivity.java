package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {
    private ListView item_list;
    private TextView show_app_name;

    // 換掉原來的字串陣列
    private ArrayList<String> data = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processViews();
        processControllers();

        // 加入範例資料
        data.add("關於Android Tutorial的事情");
        data.add("一隻非常可愛的小狗狗!");
        data.add("一首非常好聽的音樂！");

        int layoutId = android.R.layout.simple_list_item_1;
        adapter = new ArrayAdapter<String>(this, layoutId, data);
        item_list.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 如果被啟動的Activity元件傳回確定的結果
        if (resultCode == Activity.RESULT_OK) {
            String titleText = data.getStringExtra("titleText");

            // 如果是新增記事
            if (requestCode == 0) {
                // 加入標題項目
                this.data.add(titleText);
                // 通知資料已經改變，ListView元件才會重新顯示
                adapter.notifyDataSetChanged();
            }
            // 如果是修改記事
            else if (requestCode == 1) {
                // 讀取記事編號
                int position = data.getIntExtra("position", -1);

                if (position != -1) {
                    // 設定標題項目
                    this.data.set(position, titleText);
                    // 通知資料已經改變，ListView元件才會重新顯示
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void processViews() {
        item_list = (ListView)findViewById(R.id.item_list);
        show_app_name = (TextView) findViewById(R.id.show_app_name);
    }

    private void processControllers() {
        // 建立選單項目點擊監聽物件
        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 使用Action名稱建立啟動另一個Activity元件需要的Intent物件
                Intent intent = new Intent("net.macdidi.myandroidtutorial.EDIT_ITEM");

                // 設定記事編號與標題
                intent.putExtra("position", position);
                intent.putExtra("titleText", data.get(position));

                // 呼叫「startActivityForResult」，第二個參數「1」表示執行修改
                startActivityForResult(intent, 1);
            }
        };

        // 註冊選單項目點擊監聽物件
        item_list.setOnItemClickListener(itemListener);

        // 建立選單項目長按監聽物件
        AdapterView.OnItemLongClickListener itemLongListener = new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                // 換掉「data[position]」
                Toast.makeText(MainActivity.this,
                        "Long: " + data.get(position), Toast.LENGTH_LONG).show();
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
            // 使用者選擇新增選單項目
            case R.id.add_item:
                // 使用Action名稱建立啟動另一個Activity元件需要的Intent物件
                Intent intent = new Intent("net.macdidi.myandroidtutorial.ADD_ITEM");
                // 呼叫「startActivityForResult」，，第二個參數「0」表示執行新增
                startActivityForResult(intent, 0);
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

    }

    // 方法名稱與onClick的設定一樣，參數的型態是android.view.View
    public void aboutApp(View view) {
        // 建立啟動另一個Activity元件需要的Intent物件
        // 建構式的第一個參數：「this」
        // 建構式的第二個參數：「Activity元件類別名稱.class」
        Intent intent = new Intent(this, AboutActivity.class);
        // 呼叫「startActivity」，參數為一個建立好的Intent物件
        // 這行敘述執行以後，如果沒有任何錯誤，就會啟動指定的元件
        startActivity(intent);
    }

}


