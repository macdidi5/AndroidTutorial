package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 為ListView元件設定三筆資料
        String[] data = {
                "關於Android Tutorial的事情",
                "一隻非常可愛的小狗狗!",
                "一首非常好聽的音樂！"};
        int layoutId = android.R.layout.simple_list_item_1;
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, layoutId, data);
        ListView item_list = (ListView) findViewById(R.id.item_list);
        item_list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}