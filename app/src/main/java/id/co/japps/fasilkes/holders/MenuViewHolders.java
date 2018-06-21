package id.co.japps.fasilkes.holders;

/**
 * Created by OiX on 05/09/2017.
 */

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.activities.DokterActivity;
import id.co.japps.fasilkes.activities.ListFasKesActivity;

public class MenuViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView textMenu;
    public ImageView imageMenu;

    public MenuViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        textMenu = (TextView)itemView.findViewById(R.id.textViewFaskes);
        imageMenu = (ImageView)itemView.findViewById(R.id.imageViewFaskes);

    }

    @Override
    public void onClick(View view) {
        int position = getPosition();
        Intent intent = new Intent(view.getContext(), ListFasKesActivity.class);
        if(position==0){
            intent.putExtra("kategori","apotek");
            view.getContext().startActivity(intent);
        }else if(position==1){
            intent.putExtra("kategori","bidan");
            view.getContext().startActivity(intent);
        }else if(position==2){
            //intent.putExtra("kategori","dokter");
            Intent xintent = new Intent(view.getContext(), DokterActivity.class);
            view.getContext().startActivity(xintent);
        }else if(position==3){
            intent.putExtra("kategori","puskesmas");
            view.getContext().startActivity(intent);
        }else if(position==4){
            intent.putExtra("kategori","rumahsakit");
            view.getContext().startActivity(intent);
        }
    }
}
