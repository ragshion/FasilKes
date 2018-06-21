package id.co.japps.fasilkes.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.activities.DetailFasKesActivity;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.Faskes;
import id.co.japps.fasilkes.utilities.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;


public class FaskesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<Faskes> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
    SharedPrefManager sharedPrefManager;

    public FaskesAdapter(Context context, List<Faskes> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        sharedPrefManager = new SharedPrefManager(context);
        if(viewType==TYPE_MOVIE){
            if(sharedPrefManager.getSpHistori().equalsIgnoreCase("1")){
                return new FaskesHolder(inflater.inflate(R.layout.card_history_place,parent,false));
            }else{
                return new FaskesHolder(inflater.inflate(R.layout.card_list_place,parent,false));
            }

        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((FaskesHolder)holder).bindData(movies.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(movies.get(position).kategori.equalsIgnoreCase("apotek") | movies.get(position).kategori.equalsIgnoreCase("bidan") | movies.get(position).kategori.equalsIgnoreCase("dokter") | movies.get(position).kategori.equalsIgnoreCase("puskesmas") | movies.get(position).kategori.equalsIgnoreCase("rumahsakit")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class FaskesHolder extends RecyclerView.ViewHolder{
        TextView nama;
        String alamat,kelurahan,kecamatan,kategori,hbuka,htutup,jbuka,pagi,sore,jtutup,pemilik,latitude,longitude,foto,stats="1";
        TextView hari,jarak;
        TextView jam;
        TextView status;
        ImageView image;

        public FaskesHolder(View view){

            super(view);
            nama = (TextView) view.findViewById(R.id.textViewNamaTempat);
            image = (ImageView) view.findViewById(R.id.imageTempatFaskes);
            hari = (TextView) view.findViewById(R.id.textViewHariBuka);
            jam = (TextView) view.findViewById(R.id.textViewJamBuka);
            jarak = (TextView) view.findViewById(R.id.textViewJarak);

            SharedPrefManager sharedPrefManager = new SharedPrefManager(view.getContext());
            if(sharedPrefManager.getSpHistori().equalsIgnoreCase("1")){
                status = (TextView) view.findViewById(R.id.textStatus);
            }

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if(stats.equalsIgnoreCase("1")){
                            Intent intent = new Intent(v.getContext(), DetailFasKesActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("nama",nama.getText());
                            intent.putExtra("alamat",alamat);
                            intent.putExtra("kelurahan",kelurahan);
                            intent.putExtra("kecamatan",kecamatan);
                            intent.putExtra("kategori",kategori);
                            intent.putExtra("hbuka",hbuka);
                            intent.putExtra("htutup",htutup);
                            intent.putExtra("jbuka",jbuka);
                            intent.putExtra("jtutup",jtutup);
                            intent.putExtra("pagi",pagi);
                            intent.putExtra("sore",sore);
                            intent.putExtra("pemilik",pemilik);
                            intent.putExtra("latitude",latitude);
                            intent.putExtra("longitude",longitude);
                            intent.putExtra("foto",foto);
                            intent.putExtra("jarak",jarak.getText());

                            v.getContext().startActivity(intent);
                        }else{
                            Toast.makeText(v.getContext(), "Maaf, Fasilitas Kesehatan yang anda ajukan sedang dalam proses verifikasi, Mohon untuk Ditunggu", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

        void bindData(Faskes faskes){

            nama.setText(faskes.getNama());
            hari.setText(faskes.getHbuka()+" - "+faskes.getHtutup());
            if(faskes.getKategori().equalsIgnoreCase("apotek") | faskes.getKategori().equalsIgnoreCase("puskesmas") | faskes.getKategori().equalsIgnoreCase("rumahsakit")){
                jam.setText(faskes.getJbuka()+" - "+faskes.getJtutup());
            }else{
                jam.setText("Pagi : "+faskes.getPagi()+"\nSore : "+faskes.getSore());
            }
            jarak.setText(faskes.getJarak());
            String thumbnail = itemView.getContext().getResources().getString(R.string.pathFoto)+faskes.getFoto();

            Glide.with(itemView.getContext()).load(thumbnail)
                    .thumbnail(Glide.with(itemView.getContext()).load(R.drawable.elip))
                    .fitCenter()
                    .crossFade()
                    .into(image);

            SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
            if(sharedPrefManager.getSpHistori().equalsIgnoreCase("1")){
                if(faskes.getStatus().equalsIgnoreCase("1")){
                    status.setText("Status : Diterima");
                    status.setTextColor(Color.parseColor("#009688"));
                }else{
                    stats = "0";
                    status.setText("Status : Sedang Diproses");
                    status.setTextColor(Color.parseColor("#FF8F00"));
                }
            }


            //For intent ke detail===========================================
            alamat = faskes.getAlamat();
            kelurahan = faskes.getKelurahan();
            kecamatan = faskes.getKecamatan();
            kategori = faskes.getKategori();
            hbuka = faskes.getHbuka();
            htutup = faskes.getHtutup();
            jbuka = faskes.getJbuka();
            jtutup = faskes.getJtutup();
            pagi = faskes.getPagi();
            sore = faskes.getSore();
            pemilik = faskes.getPemilik();
            latitude = faskes.getLatitude();
            longitude = faskes.getLongitude();
            foto = faskes.getFoto();
            //END FOR INTENT DETAIL=====================================


        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}
