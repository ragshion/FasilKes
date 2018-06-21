package id.co.japps.fasilkes.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.activities.DetailFasKesActivity;
import id.co.japps.fasilkes.objek.Faskes;

/**
 * Created by OiX on 23/11/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    private ArrayList<Faskes> mArrayList;
    private ArrayList<Faskes> mFilteredList;

    public SearchAdapter(ArrayList<Faskes> arrayList){
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_searchview, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int i) {

        String kategori = mFilteredList.get(i).getKategori();
        String jam = "";

        holder.tv_search_nama.setText(mFilteredList.get(i).getNama());
        holder.tv_search_alamat.setText(mFilteredList.get(i).getAlamat());

        holder.tv_search_hari.setText(mFilteredList.get(i).getHbuka()+" - "+mFilteredList.get(i).getHtutup());

        if(kategori.equalsIgnoreCase("apotek") | kategori.equalsIgnoreCase("puskesmas") | kategori.equalsIgnoreCase("rumahsakit")){
            jam = mFilteredList.get(i).getJbuka()+" - "+mFilteredList.get(i).getJtutup();
        }else{
            jam = "Pagi : "+mFilteredList.get(i).getPagi()+"\nSore : "+mFilteredList.get(i).getSore();
        }
        holder.tv_search_jam.setText(jam);
        holder.tv_search_jarak.setText(mFilteredList.get(i).getJarak());



        holder.kelurahan = mFilteredList.get(i).getKelurahan();
        holder.kecamatan = mFilteredList.get(i).getKecamatan();
        holder.kategori = mFilteredList.get(i).getKategori();
        holder.hbuka = mFilteredList.get(i).getHbuka();
        holder.htutup = mFilteredList.get(i).getHtutup();
        holder.jbuka = mFilteredList.get(i).getJbuka();
        holder.jtutup = mFilteredList.get(i).getJtutup();
        holder.pagi = mFilteredList.get(i).getPagi();
        holder.sore = mFilteredList.get(i).getSore();
        holder.pemilik = mFilteredList.get(i).getPemilik();
        holder.latitude = mFilteredList.get(i).getLatitude();
        holder.longitude = mFilteredList.get(i).getLongitude();
        holder.foto = mFilteredList.get(i).getFoto();
    }

    @Override
    public int getItemCount(){
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<Faskes> filteredList = new ArrayList<>();

                    for (Faskes faskes : mArrayList) {

                        if (faskes.getNama().toLowerCase().contains(charString) || faskes.getAlamat().toLowerCase().contains(charString) || faskes.getJarak().toLowerCase().contains(charString)) {
                            filteredList.add(faskes);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<Faskes>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_search_nama, tv_search_alamat, tv_search_hari, tv_search_jam, tv_search_jarak;

        public String kelurahan,kecamatan,kategori,hbuka,htutup,jbuka,pagi,sore,jtutup,pemilik,latitude,longitude,foto;


        public ViewHolder(View view){
            super(view);

            tv_search_nama = (TextView) view.findViewById(R.id.tv_search_nama);
            tv_search_alamat = (TextView) view.findViewById(R.id.tv_search_alamat);
            tv_search_hari = (TextView) view.findViewById(R.id.tv_search_hari);
            tv_search_jam = (TextView) view.findViewById(R.id.tv_search_jam);
            tv_search_jarak = (TextView) view.findViewById(R.id.tv_search_jarak);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(v.getContext(), DetailFasKesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        intent.putExtra("nama",tv_search_nama.getText());
                        intent.putExtra("alamat",tv_search_alamat.getText());
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
                        intent.putExtra("jarak",tv_search_jarak.getText());

                        v.getContext().startActivity(intent);

                    }
                }
            });

        }
    }
}
