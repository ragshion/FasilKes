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
import id.co.japps.fasilkes.activities.ListFasKesActivity;
import id.co.japps.fasilkes.activities.NomorPentingActivity;
import id.co.japps.fasilkes.activities.SpesialisActivity;

public class DokterHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView mNama;
    public TextView mNo_Telp;
    public ImageView mPhoto;

    public DokterHolders(View itemView) {
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

        if(position==0){
            Intent intent = new Intent(view.getContext(), ListFasKesActivity.class);
            intent.putExtra("kategori","dokter");
            view.getContext().startActivity(intent);
        }else if(position==1) {
            Intent intentx = new Intent(view.getContext(), SpesialisActivity.class);
            view.getContext().startActivity(intentx);
        }


    }
}