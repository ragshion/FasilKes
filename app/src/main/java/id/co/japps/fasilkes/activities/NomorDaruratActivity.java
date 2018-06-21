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
import id.co.japps.fasilkes.adapter.MenuViewAdapter;
import id.co.japps.fasilkes.adapter.NomorDaruratAdapter;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.MenuObject;
import id.co.japps.fasilkes.objek.NomorObject;

/**
 * Created by OiX on 05/09/2017.
 */

public class NomorDaruratActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    private TextView alamatLokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomor_darurat);
        alamatLokasi = (TextView) findViewById(R.id.alamatLokasi);

        //=============LinearLayout RecyclerView=============

            List<NomorObject> rowListItem = getAllItemList();
            lLayout = new LinearLayoutManager(NomorDaruratActivity.this);

            RecyclerView rView = (RecyclerView)findViewById(R.id.recyclerView);
            rView.setHasFixedSize(true);
            rView.setLayoutManager(lLayout);

            NomorDaruratAdapter rcAdapter = new NomorDaruratAdapter(NomorDaruratActivity.this, rowListItem);
            rView.setAdapter(rcAdapter);

        //==================
        GPSTracker gps = new GPSTracker(this);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        Double lat = gps.getLatitude();
        Double log = gps.getLongitude();
        List<Address> adresses;
        try{
            adresses = geocoder.getFromLocation(lat,log,1);
            String alamat = adresses.get(0).getAddressLine(0);
            alamatLokasi.setText(alamat);
        }catch (Exception e){

        }
    }

    private List<NomorObject> getAllItemList(){

        List<NomorObject> allItems = new ArrayList<NomorObject>();
        allItems.add(new NomorObject("Kepolisian", R.drawable.ic_no_polisi));
        allItems.add(new NomorObject("Hotel", R.drawable.ic_no_hotel));
        allItems.add(new NomorObject("Rumah Sakit", R.drawable.ic_no_rs));
        allItems.add(new NomorObject("Umum", R.drawable.ic_no_umum));

        return allItems;
    }
}
