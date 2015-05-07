package net.macdidi.myandroidtutorial;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.IOException;

public class RecordActivity extends Activity {

    private ImageButton record_button;
    private boolean isRecording = false;
    private ProgressBar record_volumn;

    private MyRecoder myRecoder;

    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        processViews();

        // 讀取檔案名稱
        Intent intent = getIntent();
        fileName = intent.getStringExtra("fileName");
    }

    public void onSubmit(View view) {
        if (isRecording) {
            // 停止錄音
            myRecoder.stop();
        }

        // 確定
        if (view.getId() == R.id.record_ok) {
            Intent result = getIntent();
            setResult(Activity.RESULT_OK, result);
        }

        finish();
    }

    private void processViews() {
        record_button = (ImageButton) findViewById(R.id.record_button);
        record_volumn = (ProgressBar) findViewById(R.id.record_volumn);
        // 隱藏狀態列ProgressBar
        setProgressBarIndeterminateVisibility(false);
    }

    public void clickRecord(View view) {
        // 切換
        isRecording = !isRecording;

        // 開始錄音
        if (isRecording) {
            // 設定按鈕圖示為錄音中
            record_button.setImageResource(R.drawable.record_red_icon);
            // 建立錄音物件
            myRecoder = new MyRecoder(fileName);
            // 開始錄音
            myRecoder.start();
            // 建立並執行顯示麥克風音量的AsyncTask物件
            new MicLevelTask().execute();
        }
        // 停止錄音
        else {
            // 設定按鈕圖示為停止錄音
            record_button.setImageResource(R.drawable.record_dark_icon);
            // 麥克風音量歸零
            record_volumn.setProgress(0);
            // 停止錄音
            myRecoder.stop();
        }
    }

    // 在錄音過程中顯示麥克風音量
    private class MicLevelTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            while (isRecording) {
                publishProgress();

                try {
                    Thread.sleep(200);
                }
                catch (InterruptedException e) {
                    Log.d("RecordActivity", e.toString());
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            record_volumn.setProgress((int) myRecoder.getAmplitudeEMA());
        }

    }

    // 執行錄音並且可以取得麥克風音量的錄音物件
    private class MyRecoder {

        private static final double EMA_FILTER = 0.6;
        private MediaRecorder recorder = null;
        private double mEMA = 0.0;
        private String output;

        // 建立錄音物件，參數為錄音儲存的位置與檔名
        MyRecoder(String output) {
            this.output = output;
        }

        // 開始錄音
        public void start() {
            if (recorder == null) {
                // 建立錄音用的MediaRecorder物件
                recorder = new MediaRecorder();
                // 設定錄音來源為麥克風，必須在setOutputFormat方法之前呼叫
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                // 設定輸出格式為3GP壓縮格式，必須在setAudioSource方法之後，
                // 在prepare方法之前呼叫
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                // 設定錄音的編碼方式，必須在setOutputFormat方法之後，
                // 在prepare方法之前呼叫
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // 設定輸出的檔案名稱，必須在setOutputFormat方法之後，
                // 在prepare方法之前呼叫
                recorder.setOutputFile(output);

                try {
                    // 準備執行錄音工作，必須在所有設定之後呼叫
                    recorder.prepare();
                }
                catch (IOException e) {
                    Log.d("RecordActivity", e.toString());
                }

                // 開始錄音
                recorder.start();
                mEMA = 0.0;
            }
        }

        // 停止錄音
        public void stop() {
            if (recorder != null) {
                // 停止錄音
                recorder.stop();
                // 清除錄音資源
                recorder.release();
                recorder = null;
            }
        }

        public double getAmplitude() {
            if (recorder != null)
                return (recorder.getMaxAmplitude() / 2700.0);
            else
                return 0;
        }

        // 取得麥克風音量
        public double getAmplitudeEMA() {
            double amp = getAmplitude();
            mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
            return mEMA;
        }
    }

}