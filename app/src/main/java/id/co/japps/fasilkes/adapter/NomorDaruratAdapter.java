package id.co.japps.fasilkes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.holders.MenuViewHolders;
import id.co.japps.fasilkes.holders.NomorDaruratHolders;
import id.co.japps.fasilkes.objek.MenuObject;
import id.co.japps.fasilkes.objek.NomorObject;

public class NomorDaruratAdapter extends RecyclerView.Adapter<NomorDaruratHolders> {

    private List<NomorObject> itemList;
    private Context context;

    public NomorDaruratAdapter(Context context, List<NomorObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public NomorDaruratHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_nomor_darurat, null);
        NomorDaruratHolders ncv = new NomorDaruratHolders(layoutView);
        return ncv;
    }

    @Override
    public void onBindViewHolder(NomorDaruratHolders holder, int position) {
        holder.mNama.setText(itemList.get(position).getNama());
        holder.mNo_Telp.setText(itemList.get(position).getNo_telp());
        holder.mPhoto.setImageResource(itemList.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
