package id.co.japps.fasilkes.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.adapter.NomorPentingAdapter;
import id.co.japps.fasilkes.api.Client;
import id.co.japps.fasilkes.api.Service;
import id.co.japps.fasilkes.objek.NomorPenting;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by OiX on 08/01/2018.
 */

public class NomorPentingActivity extends AppCompatActivity{
    private RecyclerView mRecyclerView;
    private ArrayList<NomorPenting> mArrayList;
    private NomorPentingAdapter mAdapter;
    String kategori = "", titlebar = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomor_penting);
        kategori = getIntent().getStringExtra("kategori");
        if(kategori.equalsIgnoreCase("Kepolisian")){
            titlebar = "Kepolisian";
        }else if(kategori.equalsIgnoreCase("Hotel")){
            titlebar = "Hotel";
        }else if(kategori.equalsIgnoreCase("Umum")){
            titlebar = "Umum";
        }else {
            titlebar = "Rumah Sakit";
        }
        this.setTitle(titlebar);
        mRecyclerView = findViewById(R.id.recDarurat);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        loadJson(kategori);
    }

    private void loadJson(String kategori){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadNomor = serviceAPI.getNoPe(kategori);
        loadNomor.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String faskesString = response.body().toString();

                Type listType = new TypeToken<ArrayList<NomorPenting>>() {}.getType();
                mArrayList = getTeamListFromJson(faskesString, listType);


                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mAdapter = new NomorPentingAdapter(mArrayList);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public static <T> ArrayList<T> getTeamListFromJson(String jsonString, Type type) {
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
