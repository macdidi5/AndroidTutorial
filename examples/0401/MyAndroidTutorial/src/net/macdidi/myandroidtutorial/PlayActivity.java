package net.macdidi.myandroidtutorial;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

public class PlayActivity extends Activity {
	
    private SeekBar control;
    private MediaPlayer mediaPlayer;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        
        processViews();
        processControllers();
        
        Intent intent = getIntent();
        String fileName = intent.getStringExtra("fileName");
        
        // 建立指定資源的MediaPlayer物件
        Uri uri = Uri.parse(fileName);
        mediaPlayer = MediaPlayer.create(this, uri);
        // 註冊播放完畢監聽事件
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                // 切換按鈕為可播放
                clickStop(null);
            }
        });
        
        // 設定SeekBar元件的最大值為音樂的總時間（毫秒）
        control.setMax(mediaPlayer.getDuration());        
	}
	
	@Override
	protected void onPause() {
		mediaPlayer.stop();
		super.onPause();
	}

	public void onSubmit(View view) {
		if (view.getId() == R.id.ok_add_teim) {
			
		}
		
		finish();
	}
	
    private void processViews() {
    	control = (SeekBar) findViewById(R.id.control);
    }
    
    private void processControllers() {
        // 註冊SeekBar元件進度改變事件
        control.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, 
                        int progress, boolean fromUser) {
                    // 一定要判斷是使用者的操作，因為播放過程也會更改進度
                    if (fromUser) {
                        // 移動音樂到指定的進度
                        mediaPlayer.seekTo(progress);
                    }
                }
        
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
        
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}    
            });    	
    }
    
    public void clickPlay(View view) {
        // 開始播放
        mediaPlayer.start();
        // 建立並執行顯示播放進度的AsyncTask物件
        new MyPlayTask().execute();
    }

    public void clickPause(View view) {
        // 暫停播放
        mediaPlayer.pause();
    }

    public void clickStop(View view) {
        // 停止播放
        mediaPlayer.stop();

        try {
            // 重新設定
            mediaPlayer.prepare();
        }
        catch (IOException e) {
            Log.d("PlayActivity", e.toString());
        }

        // 回到開始的位置
        mediaPlayer.seekTo(0);
        control.setProgress(0);
    }
    
    // 播放完成監聽類別
    private class MyCompletion implements OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer player) {
            // 清除MediaPlayer物件
            player.release();
        }
    }
    
    // 在播放過程中顯示播放進度
    private class MyPlayTask extends AsyncTask<Void, Void, Void> {
    
        @Override
        protected Void doInBackground(Void... args) {
            while (mediaPlayer.isPlaying()) {
                // 設定播放進度
                control.setProgress(mediaPlayer.getCurrentPosition());
    
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    Log.d("PlayActivity", e.toString());
                }
            }
    
            return null;
        }
    
    }    

}
