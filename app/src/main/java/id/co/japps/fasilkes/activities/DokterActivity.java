package id.co.japps.fasilkes.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.adapter.DokterAdapter;
import id.co.japps.fasilkes.adapter.MenuViewAdapter;
import id.co.japps.fasilkes.adapter.NomorDaruratAdapter;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.MenuObject;
import id.co.japps.fasilkes.objek.NomorObject;

/**
 * Created by OiX on 05/09/2017.
 */

public class DokterActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    private TextView alamatLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokter);
        this.setTitle("Data Dokter");
        //=============LinearLayout RecyclerView=============

        List<NomorObject> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(DokterActivity.this);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recDocter);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        DokterAdapter rcAdapter = new DokterAdapter(DokterActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }

    private List<NomorObject> getAllItemList(){

        List<NomorObject> allItems = new ArrayList<NomorObject>();
        allItems.add(new NomorObject("Dokter Umum", R.drawable.ic_dokter));
        allItems.add(new NomorObject("Dokter Specialis", R.drawable.ic_rumahsakit));

        return allItems;
    }
}
