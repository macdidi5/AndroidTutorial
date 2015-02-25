package net.macdidi.myandroidtutorial;

import java.io.File;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ItemActivity extends Activity {
	
	private EditText title_text, content_text;
	
	private static final int START_CAMERA = 0;
	private static final int START_RECORD = 1;
	private static final int START_LOCATION = 2;
	private static final int START_ALARM = 3;
	private static final int START_COLOR = 4;
	
	private Item item;
	
	// 檔案名稱
	private String fileName;
	// 照片
	private ImageView picture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_item);
	    
	    processViews();
	    
	    Intent intent = getIntent();
	    String action = intent.getAction();
	    
	    if (action.equals("net.macdidi.myandroidtutorial.EDIT_ITEM")) {
	    	item = (Item) intent.getExtras().getSerializable(
					"net.macdidi.myandroidtutorial.Item");
	    	title_text.setText(item.getTitle());
	    	content_text.setText(item.getContent());
	    }
	    else {
	    	item = new Item();
		// 建立SharedPreferences物件
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		// 讀取設定的預設顏色
		int color = sharedPreferences.getInt("DEFAULT_COLOR", -1);
		item.setColor(getColors(color));
	    }
	     
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// 如果有檔案名稱
		if (item.getFileName() != null && item.getFileName().length() > 0) {
			// 照片檔案物件
			File file = configFileName("P", ".jpg");
			
			// 如果照片檔案存在
			if (file.exists()) {
				// 顯示照片元件
				picture.setVisibility(View.VISIBLE);
				// 設定照片
				FileUtil.fileToImageView(file.getAbsolutePath(), picture);
			}
		}	   		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			// 照像
			case START_CAMERA:
				// 設定照片檔案名稱
				item.setFileName(fileName);
				break;
			// 錄音
			case START_RECORD:
				// 設定錄音檔案名稱
				item.setFileName(fileName);
				break;
			case START_LOCATION:
				break;
			case START_ALARM:
				break;
			case START_COLOR:
				int colorId = data.getIntExtra(
						"colorId", Colors.LIGHTGREY.parseColor());
				item.setColor(getColors(colorId));
				break;
			}
		}
	}

	public static Colors getColors(int color) {
		Colors result = Colors.LIGHTGREY;
		
		if (color == Colors.BLUE.parseColor()) {
			result = Colors.BLUE;
		}
		else if (color == Colors.PURPLE.parseColor()) {
			result = Colors.PURPLE;
		} 
		else if (color == Colors.GREEN.parseColor()) {
			result = Colors.GREEN;
		} 
		else if (color == Colors.ORANGE.parseColor()) {
			result = Colors.ORANGE;
		} 
		else if (color == Colors.RED.parseColor()) {
			result = Colors.RED;
		} 
		
		return result;
	}	
	
	private void processViews() {
		title_text = (EditText) findViewById(R.id.title_text);
		content_text = (EditText) findViewById(R.id.content_text);
		picture = (ImageView) findViewById(R.id.picture);
	}

	public void onSubmit(View view) {		
		if (view.getId() == R.id.ok_teim) {
			String titleText = title_text.getText().toString();
			String contentText = content_text.getText().toString();
			
			item.setTitle(titleText);
			item.setContent(contentText);
			
		    if (getIntent().getAction().equals(
		    		"net.macdidi.myandroidtutorial.EDIT_ITEM")) {
		    	item.setLastModify(new Date().getTime());
		    }
		    else {
		    	item.setDatetime(new Date().getTime());
		    }

			Intent result = getIntent();
			result.putExtra("net.macdidi.myandroidtutorial.Item", item);
			setResult(Activity.RESULT_OK, result);
		}
		
		finish();
	}
	
	public void clickFunction(View view) {
		int id = view.getId();
		
		switch (id) {
		case R.id.take_picture:
			// 啟動相機元件用的Intent物件
			Intent intentCamera = 
					new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			
			// 照片檔案名稱
			File pictureFile = configFileName("P", ".jpg");
			Uri uri = Uri.fromFile(pictureFile);
			// 設定檔案名稱
			intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			// 啟動相機元件
			startActivityForResult(intentCamera, START_CAMERA);
			break;
		case R.id.record_sound:
			// 錄音檔案名稱
			final File recordFile = configFileName("R", ".mp3");
			
			if (recordFile.exists()) {
				// 詢問播放還是重新錄製的對話框
				AlertDialog.Builder d = new AlertDialog.Builder(this);
				
				d.setTitle(R.string.title_record)
				 .setCancelable(true);
				d.setPositiveButton(R.string.record_play, 
						new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, int which) {
						    	// 播放
						    	Intent playIntent = new Intent(
						    			ItemActivity.this, PlayActivity.class);
						    	playIntent.putExtra("fileName", 
						    			recordFile.getAbsolutePath());
								startActivity(playIntent);
						    }
						});
				d.setNegativeButton(R.string.record_new, 
						new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog, int which) {
						    	goToRecord(recordFile);
						    }
						});

				// 顯示對話框
				d.show();
			}
			else {
				goToRecord(recordFile);
			}
			
			break;
		case R.id.set_location:
			break;
		case R.id.set_alarm:
			break;
		case R.id.select_color:
			startActivityForResult(
					new Intent(this, ColorActivity.class), START_COLOR);
			break;
		}
		
	}
	
	private void goToRecord(File recordFile) {
		// 錄音
		Intent recordIntent = new Intent(this, RecordActivity.class);
		recordIntent.putExtra("fileName", recordFile.getAbsolutePath());
		startActivityForResult(recordIntent, START_RECORD);
	}
	
	private File configFileName(String prefix, String extension) {
		// 如果記事資料已經有檔案名稱
		if (item.getFileName() != null && item.getFileName().length() > 0) {
			fileName = item.getFileName();
		}
		// 產生檔案名稱
		else {
			fileName = FileUtil.getUniqueFileName();
		}
		
		return new File(FileUtil.getExternalStorageDir(FileUtil.APP_DIR),
				prefix + fileName + extension);
	}

}
