package id.co.japps.fasilkes.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dd.processbutton.FlatButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.utilities.SharedPrefManager;

/**
 * Created by OiX on 05/02/2018.
 */

public class MapGetLatLongActivity  extends FragmentActivity implements GoogleMap.OnMapClickListener,OnMapReadyCallback {
    private GoogleMap googleMap;
    private FlatButton btnOk;
    private TextView latitude, longitude,lokasi;
    String snippetAlamat = "";
    SharedPrefManager sharedPrefManager;

    String nama,alamat,kelurahan,kecamatan,jambuka, jamtutup, pemilik, kategori, haribuka, haritutup;
    String lati,longi;

    @Override
    public void onBackPressed() {
        sharedPrefManager = new SharedPrefManager(MapGetLatLongActivity.this);
        Intent intent = new Intent();
        intent.putExtra("nama",nama);
        intent.putExtra("alamat", alamat);
        intent.putExtra("kelurahan", kelurahan);
        intent.putExtra("kecamatan", kecamatan);
        intent.putExtra("kategori", kategori);
        intent.putExtra("haribuka", haribuka);
        intent.putExtra("haritutup", haritutup);
        intent.putExtra("jambuka", jambuka);
        intent.putExtra("jamtutup", jamtutup);
        intent.putExtra("pemilik", pemilik);

        sharedPrefManager.saveSPString(SharedPrefManager.SP_LATITUDE,lati);
        sharedPrefManager.saveSPString(SharedPrefManager.SP_LONGITUDE,longi);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_getlatlong);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latitude = findViewById(R.id.textLatitude);
        longitude = findViewById(R.id.textLongitude);
        lokasi = findViewById(R.id.textAlamat);
        nama = getIntent().getStringExtra("nama");
        alamat = getIntent().getStringExtra("alamat");
        kelurahan = getIntent().getStringExtra("kelurahan");
        kecamatan = getIntent().getStringExtra("kecamatan");
        kategori = getIntent().getStringExtra("kategori");
        haribuka = getIntent().getStringExtra("haribuka");
        haritutup = getIntent().getStringExtra("haritutup");
        jambuka = getIntent().getStringExtra("jambuka");
        jamtutup = getIntent().getStringExtra("jamtutup");
        pemilik = getIntent().getStringExtra("pemilik");

        //latitude.setText(nama);

        btnOk = findViewById(R.id.btnOKLatLong);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
        googleMap.setOnMapClickListener(this);
        GPSTracker gps = new GPSTracker(MapGetLatLongActivity.this);
        Double lat = gps.getLatitude();
        Double log = gps.getLongitude();

        //googleMap.addMarker(new MarkerOptions().position(lokasisekarang).title("Lokasi Saya").snippet(snippetAlamat));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude()), 16.0f));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        //googleMap.clear();

        String latlng = latLng.toString();
        longi = latlng.substring(latlng.lastIndexOf(",")+1);
        int iend = latlng.indexOf(",");
        lati = latlng.substring(0,iend);
        lati = lati.substring(lati.lastIndexOf("(")+1);
        longi = longi.substring(0, longi.length() - 1);

        Geocoder geocoder = new Geocoder(MapGetLatLongActivity.this, Locale.getDefault());
        List<Address> adresses;

        try{
            adresses = geocoder.getFromLocation(Double.valueOf(lati),Double.valueOf(longi),1);
            snippetAlamat = adresses.get(0).getAddressLine(0);
        }catch (Exception e){
            snippetAlamat = "Tidak Diketahui";
        }

        latitude.setText("Latitude : "+lati);
        longitude.setText("Longitude : "+longi);
        lokasi.setText("Lokasi : "+snippetAlamat);

    }

}
