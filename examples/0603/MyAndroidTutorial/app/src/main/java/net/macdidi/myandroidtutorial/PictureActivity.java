package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class PictureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picture);

        // 取得照片元件
        ImageView picture_view = (ImageView) findViewById(R.id.picture_view);

        // 讀取照片檔案名稱
        Intent intent = getIntent();
        String pictureName = intent.getStringExtra("pictureName");

        if (pictureName != null) {
            // 設定照片元件
            FileUtil.fileToImageView(pictureName, picture_view);
        }
    }

    public void clickPicture(View view) {
        // 如果裝置的版本是LOLLIPOP
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }
        else {
            finish();
        }
    }

}