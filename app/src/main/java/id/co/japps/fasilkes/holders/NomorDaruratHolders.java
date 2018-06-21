package id.co.japps.fasilkes.holders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.activities.NomorPentingActivity;

public class NomorDaruratHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mNama;
    public TextView mNo_Telp;
    public ImageView mPhoto;

    public NomorDaruratHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mNama = (TextView)itemView.findViewById(R.id.textViewNamaDarurat);
        mNo_Telp = (TextView)itemView.findViewById(R.id.textViewNomorDarurat);
        mPhoto = (ImageView)itemView.findViewById(R.id.imageViewDarurat);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked Country Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        int position = getPosition();
        Intent intent = new Intent(view.getContext(), NomorPentingActivity.class);
        if(position==0){
            intent.putExtra("kategori","Kepolisian");
        }else if(position==1){
            intent.putExtra("kategori","Hotel");
        }else if(position==2){
            intent.putExtra("kategori","RumahSakit");
        }else if(position==3){
            intent.putExtra("kategori","Umum");
        }


        view.getContext().startActivity(intent);

    }
}