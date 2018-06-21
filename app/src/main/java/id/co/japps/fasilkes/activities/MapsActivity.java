package id.co.japps.fasilkes.activities;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.api.Client;
import id.co.japps.fasilkes.api.Service;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.Faskes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    BitmapDescriptor icon;
    private List<Faskes> faskes = new ArrayList<>();
    String kategoriApi = "all", kategoriWaktu = "", kategori="";

    int i = 1;

    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        tampilDataLokasi();
    }

    private void tampilDataLokasi(){
        GPSTracker gps = new GPSTracker(MapsActivity.this);
        materialDialog = new MaterialDialog.Builder(this)
                .content(R.string.dialog_progress)
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();
        materialDialog.show();

        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.readTeamArray(kategoriApi,gps.getLatitude(),gps.getLongitude() );

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                materialDialog.dismiss();

                String faskesString = response.body().toString();

                Type listType = new TypeToken<List<Faskes>>() {}.getType();
                faskes = getTeamListFromJson(faskesString, listType);


                String snippetAlamat = "";

                GPSTracker gps = new GPSTracker(MapsActivity.this);


                Double lat = gps.getLatitude();
                Double log = gps.getLongitude();

                LatLng lokasisekarang = new LatLng(lat,log);
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                List<Address> adresses;

                try{
                    adresses = geocoder.getFromLocation(lat,log,1);
                    snippetAlamat = adresses.get(0).getAddressLine(0);
                }catch (Exception e){
                    snippetAlamat = "Alamat Tidak Diketahui";
                }

                mMap.addMarker(new MarkerOptions().position(lokasisekarang).title("Lokasi Saya").snippet(snippetAlamat));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(),gps.getLongitude()), 16.0f));

                for (i=0; i<faskes.size(); i++){
                    Double latitude = Double.parseDouble(faskes.get(i).getLatitude());
                    Double longitude = Double.parseDouble(faskes.get(i).getLongitude());
                    LatLng location = new LatLng(latitude, longitude);

                    kategori= faskes.get(i).getKategori();

                    if(kategori.equalsIgnoreCase("apotek")){
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_apotek);
                    }else if(kategori.equalsIgnoreCase("bidan")){
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_bidan);
                    }else if(kategori.equalsIgnoreCase("dokter")){
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_dokter);
                    }else if(kategori.equalsIgnoreCase("puskesmas")){
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_puskesmas);
                    }else{
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.marker_rumahsakit);
                    }

                    if(kategori.equalsIgnoreCase("apotek") | kategori.equalsIgnoreCase("puskesmas") | kategori.equalsIgnoreCase("rumahsakit")){
                        String snippetInfo = faskes.get(i).getHbuka()+" - "+faskes.get(i).getHtutup()+" | "+faskes.get(i).getJbuka()+" - "+faskes.get(i).getJtutup();
                        String titleInfo = faskes.get(i).getNama();
                        mMap.addMarker(new MarkerOptions().position(location).title(titleInfo).snippet(snippetInfo).icon(icon));
                    }else if(kategori.equalsIgnoreCase("dokter") | kategori.equalsIgnoreCase("bidan")){
                        String snippetInfo = faskes.get(i).getHbuka()+" - "+faskes.get(i).getHtutup()+" | "+faskes.get(i).getPagi()+" | "+faskes.get(i).getSore();
                        String titleInfo = faskes.get(i).getNama();
                        mMap.addMarker(new MarkerOptions().position(location).title(titleInfo).snippet(snippetInfo).icon(icon));
                    }

//                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//                        @Override
//                        public void onInfoWindowClick(Marker marker) {
//
//                            Intent intent = new Intent(MapsActivity.this, DetailFasKesActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//                            intent.putExtra("nama",faskes.get(i).getNama());
//                            intent.putExtra("alamat",faskes.get(i).getAlamat());
//                            intent.putExtra("kelurahan",faskes.get(i).getKelurahan());
//                            intent.putExtra("kecamatan",faskes.get(i).getKecamatan());
//                            intent.putExtra("kategori",kategori);
//                            intent.putExtra("hbuka",faskes.get(i).getHbuka());
//                            intent.putExtra("htutup",faskes.get(i).getHtutup());
//                            intent.putExtra("jbuka",faskes.get(i).getJbuka());
//                            intent.putExtra("jtutup",faskes.get(i).getJtutup());
//                            intent.putExtra("pagi",faskes.get(i).getPagi());
//                            intent.putExtra("sore",faskes.get(i).getSore());
//                            intent.putExtra("pemilik",faskes.get(i).getPemilik());
//                            intent.putExtra("latitude",faskes.get(i).getLatitude());
//                            intent.putExtra("longitude",faskes.get(i).getLongitude());
//                            intent.putExtra("foto",faskes.get(i).getFoto());
//                            intent.putExtra("jarak",faskes.get(i).getJarak());
//
//                            startActivity(intent);
//                        }
//                    });
                }
                   /*
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    boolean doNotMoveCameraToCenterMarker = true;
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.showInfoWindow();
                        mMap.getUiSettings().setMapToolbarEnabled(true);
                        return  doNotMoveCameraToCenterMarker;
                    }
                });
                */
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(MapsActivity.this, "Maaf Terjadi Kesalahan Dalam Pengambilan Data", Toast.LENGTH_SHORT).show();
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
