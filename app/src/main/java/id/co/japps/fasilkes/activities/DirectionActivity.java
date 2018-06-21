package id.co.japps.fasilkes.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.dd.processbutton.FlatButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.gps.GPSTracker;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {

    private FlatButton btnRequestDirection;
    private GoogleMap googleMap;
    private String serverKey = "";
    private Double latDes = 0.0, lngDes = 0.0;
    private LatLng origin, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        serverKey = getResources().getString(R.string.mapsApiKey);
        btnRequestDirection = findViewById (R.id.btn_request_direction);
        btnRequestDirection.setOnClickListener(this);

        GPSTracker gps = new GPSTracker(this);

        Double latOri = gps.getLatitude();
        Double lngOri = gps.getLongitude();

        latDes = Double.parseDouble(getIntent().getStringExtra("latitude"));
        lngDes = Double.parseDouble(getIntent().getStringExtra("longitude"));

        origin = new LatLng(latOri, lngOri);
        destination = new LatLng(latDes, lngDes);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapDirect);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        requestDirection();

    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_request_direction) {

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + Double.valueOf(latDes) + ","
                    + Double.valueOf(lngDes));
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            try {
                v.getContext().startActivity(mapIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(DirectionActivity.this, "Smartphone anda tidak terdapat aplikasi google maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void requestDirection() {
        Snackbar.make(btnRequestDirection, "Mencari Rute...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        Snackbar.make(btnRequestDirection, "Rute Berhasil Dibuat : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            googleMap.addMarker(new MarkerOptions().position(origin));
            googleMap.addMarker(new MarkerOptions().position(destination));

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                boolean doNotMoveCameraToCenterMarker = true;
                @Override
                public boolean onMarkerClick(Marker marker) {
                    return  doNotMoveCameraToCenterMarker;
                }
            });

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.BLUE));
            setCameraWithCoordinationBounds(route);

        } else {
            Snackbar.make(btnRequestDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}