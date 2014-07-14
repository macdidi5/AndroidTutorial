package net.macdidi.myandroidtutorial;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

public class ItemActivity extends Activity {
	
	private EditText title_text, content_text;
	
	private static final int START_CAMERA = 0;
	private static final int START_RECORD = 1;
	private static final int START_LOCATION = 2;
	private static final int START_ALARM = 3;
	private static final int START_COLOR = 4;
	
	private Item item;
	
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
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case START_CAMERA:
				break;
			case START_RECORD:
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

	// 改為public static
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
		    // 新增記事
		    else {
		    	item.setDatetime(new Date().getTime());
		    	// 建立SharedPreferences物件
		    	SharedPreferences sharedPreferences =
						PreferenceManager.getDefaultSharedPreferences(this);
		    	// 讀取設定的預設顏色
				int color = sharedPreferences.getInt("DEFAULT_COLOR", -1);
				item.setColor(getColors(color));
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
			break;
		case R.id.record_sound:
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

}
