package id.co.japps.fasilkes.adapter;

/**
 * Created by OiX on 05/09/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.holders.MenuViewHolders;
import id.co.japps.fasilkes.objek.MenuObject;

public class MenuViewAdapter extends RecyclerView.Adapter<MenuViewHolders> {

    private List<MenuObject> itemList;
    private Context context;

    public MenuViewAdapter(Context context, List<MenuObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public MenuViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view, null);
        MenuViewHolders rcv = new MenuViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MenuViewHolders holder, int position) {
        holder.textMenu.setText(itemList.get(position).getNama());
        holder.imageMenu.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}