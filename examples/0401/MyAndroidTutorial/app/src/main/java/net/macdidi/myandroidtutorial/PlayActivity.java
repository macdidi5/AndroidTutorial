package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class PlayActivity extends Activity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");

        // 建立指定資源的MediaPlayer物件
        Uri uri = Uri.parse(fileName);
        mediaPlayer = MediaPlayer.create(this, uri);
    }

    @Override
    protected void onStop() {
        if (mediaPlayer.isPlaying()) {
            // 停止播放
            mediaPlayer.stop();
        }

        // 清除MediaPlayer物件
        mediaPlayer.release();
        super.onStop();
    }

    public void onSubmit(View view) {
        // 結束Activity元件
        finish();
    }

    public void clickPlay(View view) {
        // 開始播放
        mediaPlayer.start();
    }

    public void clickPause(View view) {
        // 暫停播放
        mediaPlayer.pause();
    }

    public void clickStop(View view) {
        // 停止播放
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        // 回到開始的位置
        mediaPlayer.seekTo(0);
    }

}