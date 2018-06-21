package id.co.japps.fasilkes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

import id.co.japps.fasilkes.R;

/**
 * Created by OiX on 22/11/2017.
 */

public class StreetViewActivity extends AppCompatActivity {
    private StreetViewPanorama mStreetViewPanorama;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streetview);

        final Double latitude = Double.valueOf(getIntent().getStringExtra("latitude"));
        final Double longitude = Double.valueOf(getIntent().getStringExtra("longitude"));

        final LatLng lokasi = new LatLng(latitude,longitude);


        SupportStreetViewPanoramaFragment supportStreetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment) getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);

        supportStreetViewPanoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(StreetViewPanorama streetViewPanorama) {
                mStreetViewPanorama = streetViewPanorama;
                mStreetViewPanorama.setStreetNamesEnabled(true);
                mStreetViewPanorama.setUserNavigationEnabled(true);
                mStreetViewPanorama.setZoomGesturesEnabled(true);
                mStreetViewPanorama.setPanningGesturesEnabled(true);

                if(savedInstanceState == null){
                    mStreetViewPanorama.setPosition(lokasi);
                }else{
                    Toast.makeText(StreetViewActivity.this, "Maaf, tidak dapat mengambil data lokasi streetview", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
