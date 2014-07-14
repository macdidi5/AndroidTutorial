package net.macdidi.myandroidtutorial;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ListView item_list;	
	private TextView show_app_name;
	
	private ItemAdapter itemAdapter;
	private List<Item> items;
	
	private MenuItem add_item, search_item, revert_item, share_item, delete_item;
	
	private int selectedCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		processViews();
		processControllers();
						
		items = new ArrayList<Item>();
        
        items.add(new Item(1, new Date().getTime(), Colors.RED, "關於Android Tutorial的事情.", "Hello content", "", 0, 0, 0));
        items.add(new Item(2, new Date().getTime(), Colors.BLUE, "一隻非常可愛的小狗狗!", "她的名字叫「大熱狗」，又叫\n作「奶嘴」，是一隻非常可愛\n的小狗。", "", 0, 0, 0));
        items.add(new Item(3, new Date().getTime(), Colors.GREEN, "一首非常好聽的音樂！", "Hello content", "", 0, 0, 0));
        
        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        item_list.setAdapter(itemAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Item item = (Item) data.getExtras().getSerializable(
					"net.macdidi.myandroidtutorial.Item");
			
			if (requestCode == 0) {
				item.setId(items.size() + 1);
				items.add(item);
				itemAdapter.notifyDataSetChanged();
			}
			else if (requestCode == 1) {
				int position = data.getIntExtra("position", -1);
				
				if (position != -1) {
					items.set(position, item);
					itemAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private void processViews() {
    	item_list = (ListView)findViewById(R.id.item_list);
    	show_app_name = (TextView) findViewById(R.id.show_app_name);
    }

    private void processControllers() {
    	
		OnItemClickListener itemListener = new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, 
		            int position, long id) {
                Item item = itemAdapter.getItem(position);
		        
                if (selectedCount > 0) { 
                    processMenu(item);
                    itemAdapter.set(position, item);
                }
                else {    				
    				Intent intent = new Intent(
    						"net.macdidi.myandroidtutorial.EDIT_ITEM");
    				intent.putExtra("position", position);
    				intent.putExtra("net.macdidi.myandroidtutorial.Item", item);    				
    				startActivityForResult(intent, 1);
                }
		    }
		};
		
		item_list.setOnItemClickListener(itemListener);
		
		OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> parent, View view, 
		            int position, long id) {
                Item item = itemAdapter.getItem(position);
                processMenu(item);
                itemAdapter.set(position, item);
                return true;
		    }
		};
		
		item_list.setOnItemLongClickListener(itemLongListener);
		
		OnLongClickListener listener = new OnLongClickListener() {

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
		
		show_app_name.setOnLongClickListener(listener);	
    }	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater menuInflater = getMenuInflater();
	    menuInflater.inflate(R.menu.main_menu, menu);
	    
	    add_item = menu.findItem(R.id.add_item);
        search_item = menu.findItem(R.id.search_item);
        revert_item = menu.findItem(R.id.revert_item);
        share_item = menu.findItem(R.id.share_item);
        delete_item = menu.findItem(R.id.delete_item);
	    
        processMenu(null);
        
	    return true;
	}
	
	public void clickMenuItem(MenuItem item) {
		int itemId = item.getItemId();
		
		switch (itemId) {
		case R.id.search_item:
			break;
		case R.id.add_item:
			Intent intent = new Intent("net.macdidi.myandroidtutorial.ADD_ITEM");
			startActivityForResult(intent, 0);
			break;
		case R.id.revert_item:
            for (int i = 0; i < itemAdapter.getCount(); i++) {
                Item ri = itemAdapter.getItem(i);
                
                if (ri.isSelected()) {
                    ri.setSelected(false);
                    itemAdapter.set(i, ri);
                }
            }
            
            selectedCount = 0;
            processMenu(null);          
		    
			break;
		case R.id.delete_item:
		    if (selectedCount == 0) {
		        break;
		    }
		    
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            String message = getString(R.string.delete_item);
            d.setTitle(R.string.delete)
             .setMessage(String.format(message, selectedCount));
            d.setPositiveButton(android.R.string.yes, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < itemAdapter.getCount(); i++) {
                                Item item = itemAdapter.get(i);
                                
                                if (item.isSelected()) {
                                    itemAdapter.remove(item);
                                }
                            }
                            
                            itemAdapter.notifyDataSetChanged();
                        }
                    });
            d.setNegativeButton(android.R.string.no, null);
            d.show();
		    
			break;
		case R.id.googleplus_item:
			break;
		case R.id.facebook_item:
			break;
		}		
	}
	
	// 設定
	public void clickPreferences(MenuItem item) {
		// 啟動設定元件
		startActivity(new Intent(this, PrefActivity.class));
	}
	
	private void processMenu(Item item) {
    	if (item != null) {
	    	item.setSelected(!item.isSelected());
			
			if (item.isSelected()) {
				selectedCount++;
			}
			else {
				selectedCount--;
			}
    	}
		
    	add_item.setVisible(selectedCount == 0);
		search_item.setVisible(selectedCount == 0);
		revert_item.setVisible(selectedCount > 0);
		share_item.setVisible(selectedCount > 0);
		delete_item.setVisible(selectedCount > 0);
	}
	
	public void aboutApp(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
}
