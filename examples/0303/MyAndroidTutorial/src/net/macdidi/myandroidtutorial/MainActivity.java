package net.macdidi.myandroidtutorial;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends Activity {
	
	private ListView item_list;	
	private TextView show_app_name;
	
	private ItemAdapter itemAdapter;
	private List<Item> items;
	
	private MenuItem add_item, search_item, revert_item, share_item, delete_item;
	
	private int selectedCount = 0;
	
	private ItemDAO itemDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		processViews();
		processControllers();
		
		// 建立資料庫物件
		itemDAO = new ItemDAO(getApplicationContext());
		
		// 如果資料庫是空的，就建立一些範例資料
		// 這是為了方便測試用的，完成應用程式以後可以拿掉		
		if (itemDAO.getCount() == 0) {
			itemDAO.sample();
		}
		
		// 取得所有記事資料
		items = itemDAO.getAll();

		itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
		item_list.setAdapter(itemAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			Item item = (Item) data.getExtras().getSerializable(
					"net.macdidi.myandroidtutorial.Item");
			
			if (requestCode == 0) {
				// 新增記事資料到資料庫
    			item = itemDAO.insert(item);
    			
				items.add(item);
				itemAdapter.notifyDataSetChanged();
			}
			else if (requestCode == 1) {
				int position = data.getIntExtra("position", -1);
				
				if (position != -1) {
					// 修改資料庫中的記事資料
        			itemDAO.update(item);
        			
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
                        	// 取得最後一個元素的編號
                        	int index = itemAdapter.getCount() - 1;
                        	
                        	while (index > -1) {
                        		Item item = itemAdapter.get(index);
                        		
                        		if (item.isSelected()) {
                        			itemAdapter.remove(item);
                        			// 刪除資料庫中的記事資料
                        			itemDAO.delete(item.getId());
                        		}
                        		
                        		index--;
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
	
	public void clickPreferences(MenuItem item) {
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
