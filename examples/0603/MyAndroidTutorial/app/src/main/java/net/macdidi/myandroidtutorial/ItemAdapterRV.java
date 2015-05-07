package net.macdidi.myandroidtutorial;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ItemAdapterRV extends RecyclerView.Adapter<ItemAdapterRV.ViewHolder>{

    // 包裝的記事資料
    private List<Item> items;

    public ItemAdapterRV(List<Item> items) {
        this.items = items;
    }

    @Override
    public ItemAdapterRV.ViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.single_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item item = items.get(position);

        // 設定記事顏色
        GradientDrawable background = (GradientDrawable)
                holder.typeColor.getBackground();
        background.setColor(item.getColor().parseColor());

        // 設定標題與日期時間
        holder.titleView.setText(item.getTitle());
        holder.dateView.setText(item.getLocaleDatetime());

        // 設定是否已選擇
        holder.selectedItem.setVisibility(
                item.isSelected() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void add(Item item) {
        items.add(item);
        notifyItemInserted(items.size());
    }

    // 一定要使用ViewHolder包裝畫面元件
    public class ViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout typeColor;
        protected ImageView selectedItem;
        protected TextView titleView;
        protected TextView dateView;

        protected View rootView;

        public ViewHolder(View view) {
            super(view);

            typeColor = (RelativeLayout) itemView.findViewById(R.id.type_color);
            selectedItem = (ImageView) itemView.findViewById(R.id.selected_item);
            titleView = (TextView) itemView.findViewById(R.id.title_text);
            dateView = (TextView) itemView.findViewById(R.id.date_text);

            rootView = view;
        }

    }

}
