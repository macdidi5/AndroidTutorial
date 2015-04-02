package net.macdidi.myandroidtutorial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    // 應用程式儲存檔案的目錄
    public static final String APP_DIR = "androidtutorial";

    // 外部儲存設備是否可寫入
    public static boolean isExternalStorageWritable() {
        // 取得目前外部儲存設備的狀態
        String state = Environment.getExternalStorageState();

        // 判斷是否可寫入
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        return false;
    }

    // 外部儲存設備是否可讀取
    public static boolean isExternalStorageReadable() {
        // 取得目前外部儲存設備的狀態
        String state = Environment.getExternalStorageState();

        // 判斷是否可讀取
        if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }

    // 建立並傳回在公用相簿下參數指定的路徑
    public static File getPublicAlbumStorageDir(String albumName) {
        // 取得公用的照片路徑
        File pictures = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        // 準備在照片路徑下建立一個指定的路徑
        File file = new File(pictures, albumName);

        // 如果建立路徑不成功
        if (!file.mkdirs()) {
            Log.e("getAlbumStorageDir", "Directory not created");
        }

        return file;
    }

    // 建立並傳回在應用程式專用相簿下參數指定的路徑
    public static File getAlbumStorageDir(Context context, String albumName) {
        // 取得應用程式專用的照片路徑
        File pictures = context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        // 準備在照片路徑下建立一個指定的路徑
        File file = new File(pictures, albumName);

        // 如果建立路徑不成功
        if (!file.mkdirs()) {
            Log.e("getAlbumStorageDir", "Directory not created");
        }

        return file;
    }

    // 建立並傳回外部儲存媒體參數指定的路徑
    public static File getExternalStorageDir(String dir) {
        File result = new File(
                Environment.getExternalStorageDirectory(), dir);

        if (!isExternalStorageWritable()) {
            return null;
        }

        if (!result.exists() && !result.mkdirs()) {
            return null;
        }

        return result;
    }

    // 讀取指定的照片檔案名稱設定給ImageView元件
    public static void fileToImageView(String fileName, ImageView imageView) {
        if (new File(fileName).exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(fileName);
            imageView.setImageBitmap(bitmap);
        }
        else {
            Log.e("fileToImageView", fileName + " not found.");
        }
    }

    // 產生唯一的檔案名稱
    public static String getUniqueFileName() {
        // 使用年月日_時分秒格式為檔案名稱
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

}
