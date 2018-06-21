package id.co.japps.fasilkes.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.objek.NomorPenting;

/**
 * Created by OiX on 23/11/2017.
 */

public class NomorPentingAdapter extends RecyclerView.Adapter<NomorPentingAdapter.ViewHolder> {
    private ArrayList<NomorPenting> mFilteredList;

    public NomorPentingAdapter(ArrayList<NomorPenting> arrayList){
        mFilteredList = arrayList;
    }

    @Override
    public NomorPentingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_nomor_penting, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NomorPentingAdapter.ViewHolder holder, int i) {

        String kategori = mFilteredList.get(i).getKategori();
        if(kategori.equalsIgnoreCase("Kepolisian")){
            holder.iv_icon.setImageResource(R.drawable.ic_no_polisi);
        }else if(kategori.equalsIgnoreCase("RumahSakit")){
            holder.iv_icon.setImageResource(R.drawable.ic_no_rs);
        }else if(kategori.equalsIgnoreCase("Hotel")){
            holder.iv_icon.setImageResource(R.drawable.ic_no_hotel);
        }else{
            holder.iv_icon.setImageResource(R.drawable.ic_no_umum);
        }

        holder.tv_nama.setText(mFilteredList.get(i).getNama());
        holder.tv_no_telp.setText(mFilteredList.get(i).getNoTelp());

    }

    @Override
    public int getItemCount(){
        return mFilteredList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_nama, tv_no_telp;
        private ImageView iv_icon;



        public ViewHolder(View view){
            super(view);


            tv_nama = (TextView) view.findViewById(R.id.textViewNamaPenting);
            tv_no_telp = (TextView) view.findViewById(R.id.textViewNomorPenting);
            iv_icon = (ImageView) view.findViewById(R.id.imageViewPenting);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        String nomor = "tel:"+tv_no_telp.getText().toString().trim();
                        Intent manggilIntent = new Intent(Intent.ACTION_CALL);
                        manggilIntent.setData(Uri.parse(nomor));
                        if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return;
                        }
                        v.getContext().startActivity(manggilIntent);
                    }
                }
            });
        }
    }
}
