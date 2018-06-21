package id.co.japps.fasilkes.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;


import id.co.japps.fasilkes.R;

/**
 * Created by OiX on 01/03/2018.
 */

public class BantuanActivity extends IntroActivity {
    int REQUEST_CODE_INTRO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(new SimpleSlide.Builder()
                .title("Pilih Kategori  Fasilitas Kesehatan")
                .description("Masyarakat dapat memilih 5 kategori fasilitas kesehatan, Setelah dipilih maka akan muncul menu daftar fasilitas kesehatan berdasarkan kategori yang dipilih")
                .image(R.drawable.intro_1)
                .background(R.color.primary)
                .backgroundDark(R.color.primary_dark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Cari Fasilitas Kesehatan")
                .description("Apabila memilih menu / icon cari, maka masyarakat dapat mencari data suatu fasilitas kesehatan berdasarkan nama, alamat, kategori")
                .image(R.drawable.intro_2)
                .background(R.color.accent)
                .backgroundDark(R.color.primary_dark)
                .scrollable(false)
                .build());
        addSlide(new SimpleSlide.Builder()
                .title("Navigation Drawer")
                .description("Menu navigation drawer terdapat beberapa sub menu antara lain, Geo Map (Peta Fasilitas Kesehatan Kota Pekalongan), Nomor Telepon Darurat, Bantuan, Login")
                .image(R.drawable.intro_3)
                .background(R.color.colorAccent3)
                .backgroundDark(R.color.primary_dark)
                .scrollable(false)
                .build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "OKE", Toast.LENGTH_SHORT).show();
                Log.d("-Bantuan Actvity", "onActivityResult: ");
            } else {
                // Cancelled the intro. You can then e.g. finish this activity too.
                Log.d("-Bantuan Actvity", "canceled: ");
                finish();
            }
        }
    }
}
