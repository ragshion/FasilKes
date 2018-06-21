package id.co.japps.fasilkes.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.adapter.FaskesAdapter;
import id.co.japps.fasilkes.api.Client;
import id.co.japps.fasilkes.api.Service;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.Faskes;
import id.co.japps.fasilkes.utilities.VerticalLineDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

/**
 * Created by OiX on 31/08/2017.
 */

public class ListFasKesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Faskes> movies;
    private List<Faskes> faskes = new ArrayList<>();
    FaskesAdapter adapter;
    String kategori = "", titlebar = "";
    Service api;
    String TAG = "MainActivity - ";
    Double latitude,longitude;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_faskes);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        movies = new ArrayList<>();
        kategori = getIntent().getStringExtra("kategori");
        if(kategori.equalsIgnoreCase("apotek")){
            titlebar = "Data Apotek";
        }else if(kategori.equalsIgnoreCase("bidan")){
            titlebar = "Data Bidan";
        }else if(kategori.equalsIgnoreCase("dokter")){
            titlebar = "Data Dokter Praktik";
        }else if(kategori.equalsIgnoreCase("puskesmas")){
            titlebar = "Data Puskesmas";
        }else if(kategori.equalsIgnoreCase("rumahsakit")){
            titlebar = "Data Rumah Sakit";
        }else{
            titlebar = "History Data";
        }
        this.setTitle(titlebar);

        GPSTracker gps = new GPSTracker(this);

        latitude = gps.getLatitude();
        longitude = gps.getLongitude();

        adapter = new FaskesAdapter(this, movies);
        adapter.setLoadMoreListener(new FaskesAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);
        api = Client.getClient();
        load(0);
    }

    private void load(int index){
        Call<List<Faskes>> call = api.getFaskes(kategori,latitude,longitude,index);
        call.enqueue(new Callback<List<Faskes>>() {
            @Override
            public void onResponse(Call<List<Faskes>> call, Response<List<Faskes>> response) {
                if(response.isSuccessful()){
                    List<Faskes> result = response.body();
                    if(result.size()<=0){
                        Toast.makeText(context,"Maaf, Saat Ini Semua "+kategori+" Sedang Tutup", Toast.LENGTH_SHORT).show();
                    }else{
                        movies.addAll(response.body());
                        adapter.notifyDataChanged();
                    }
                }else{
                    Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Faskes>> call, Throwable t) {
                Log.e(TAG,"Response Error "+t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void tampilspesialis(){
        Call<List<Faskes>> call = api.spesialis("spesialis",latitude,longitude);
        call.enqueue(new Callback<List<Faskes>>() {
            @Override
            public void onResponse(Call<List<Faskes>> call, Response<List<Faskes>> response) {
                if(response.isSuccessful()){
                    List<Faskes> result = response.body();
                    if(result.size()<=0){

                        //Toast.makeText(context,"Maaf, Saat Ini Semua "+kategori+" Sedang Tutup", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,response.body().toString(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG," Response Error "+String.valueOf(response.errorBody()));
                        Log.e(TAG," Response Error "+String.valueOf(response.message()));
                    }else{
                        movies.addAll(response.body());
                        FaskesAdapter faskesAdapter = new FaskesAdapter(context,movies);
                        faskesAdapter.notifyDataChanged();
                    }
                }else{
                    Log.e(TAG," Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Faskes>> call, Throwable t) {
                Log.e(TAG,"Response Error "+t.getMessage());
            }
        });
    }

    private void loadMore(int index){
        movies.add(new Faskes("load"));
        adapter.notifyItemInserted(movies.size()-1);

        Call<List<Faskes>> call = api.getFaskes(kategori,latitude,longitude,index);
        call.enqueue(new Callback<List<Faskes>>() {
            @Override
            public void onResponse(Call<List<Faskes>> call, Response<List<Faskes>> response) {
                if(response.isSuccessful()){

                    //remove loading view
                    movies.remove(movies.size()-1);

                    /*if(cek_load==1){
                        movies.remove(movies.size()-1);
                        cek_load = cek_load+1;
                    }*/


                    List<Faskes> result = response.body();
                    //Collections.sort(movies,Faskes.BY_JARAK);

                    if(result.size()>1){
                        //add loaded data

                        /*if(cek_load==1){
                            movies.remove(movies.size()-1);
                            cek_load = cek_load+1;
                        }*/
                        //movies.remove(movies.size()-1);
                        movies.remove(movies.size()-1);
                        movies.addAll(result);
                    }else{//result size 0 means there is no more data available at server

                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        //Toast.makeText(context,"Semua Data Sudah Dimuat",Toast.LENGTH_LONG).show();
                    }
                    //Collections.sort(movies,Faskes.BY_JARAK);

                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                    Log.e(TAG," Load More Response Error "+String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Faskes>> call, Throwable t) {
                Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }

    public static <T> List<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

}
