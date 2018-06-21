package id.co.japps.fasilkes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;

import id.co.japps.fasilkes.R;

/**
 * Created by OiX on 17/11/2017.
 */

public class DetailFasKesActivity extends AppCompatActivity {
    public CoordinatorLayout coordinatorLayout;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public Toolbar toolbar;
    public TextView nama,alamat,kategori,hari,jam,pemilik,jarak;
    public ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_faskes);

        nama = (TextView) findViewById(R.id.txtDtNama);
        alamat = (TextView) findViewById(R.id.txtDtAlamat);
        kategori = (TextView) findViewById(R.id.txtDtKategori);
        hari = (TextView) findViewById(R.id.txtDtBuka);
        jam = (TextView) findViewById(R.id.txtDtJam);
        pemilik = (TextView) findViewById(R.id.txtDtPemilik);
        jarak= (TextView) findViewById(R.id.txtDtJarak);
        foto = (ImageView) findViewById(R.id.mainbackdrop);

        final String xnama,xalamat,xkelurahan,xkecamatan,xkategori,xhbuka,xhtutup,xjbuka,xjtutup,xpagi,xsore,xpemilik,xlatitude,xlongitude,xfoto,xjarak;
        xnama = getIntent().getStringExtra("nama");
        xalamat = getIntent().getStringExtra("alamat");
        xkelurahan = getIntent().getStringExtra("kelurahan");
        xkecamatan = getIntent().getStringExtra("kecamatan");
        xkategori = getIntent().getStringExtra("kategori");
        xhbuka = getIntent().getStringExtra("hbuka");
        xhtutup = getIntent().getStringExtra("htutup");
        xjbuka = getIntent().getStringExtra("jbuka");
        xjtutup = getIntent().getStringExtra("jtutup");
        xpagi = getIntent().getStringExtra("pagi");
        xsore = getIntent().getStringExtra("sore");
        xpemilik = getIntent().getStringExtra("pemilik");
        xlatitude = getIntent().getStringExtra("latitude");
        xlongitude = getIntent().getStringExtra("longitude");
        xfoto = getIntent().getStringExtra("foto");
        xjarak = getIntent().getStringExtra("jarak");

        nama.setText(xnama);
        alamat.setText(xalamat+" "+xkelurahan+" "+xkecamatan);
        kategori.setText(xkategori);
        hari.setText(xhbuka+" - "+xhtutup);
        if(xkategori.equalsIgnoreCase("apotek") | xkategori.equalsIgnoreCase("puskesmas") | xkategori.equalsIgnoreCase("rumahsakit")){
            jam.setText(xjbuka+" - "+xjtutup);
        }else{
            jam.setText("Pagi : "+xpagi+"\nSore : "+xsore);
        }

        pemilik.setText(xpemilik);
        jarak.setText(xjarak);
        String thumbnail = getResources().getString(R.string.pathFoto)+xfoto;

        Glide.with(DetailFasKesActivity.this)
                .load(thumbnail)
                .thumbnail(Glide.with(DetailFasKesActivity.this).load(R.drawable.elipalt))
                .fitCenter()
                .crossFade()
                .into(foto);

        toolbar = (Toolbar) findViewById(R.id.maintoolbar);
        toolbar.setTitle(xnama);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final FloatingActionsMenu floatingActionsMenu = findViewById(R.id.fabMultiAct);

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent imgIntent = new Intent(DetailFasKesActivity.this, DetailImageViewActivity.class);
                    //imgIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    imgIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    imgIntent.putExtra("detailImage", getIntent().getStringExtra("foto"));
                    startActivity(imgIntent);
            }
        });

        final FloatingActionButton fabStreet = (FloatingActionButton) findViewById(R.id.fabStreet);
        fabStreet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent street = new Intent(DetailFasKesActivity.this, StreetViewActivity.class);
                //street.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                street.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                street.putExtra("latitude",xlatitude);
                street.putExtra("longitude",xlongitude);
                startActivity(street);
                floatingActionsMenu.collapse();
            }
        });

        final FloatingActionButton fabNav = (FloatingActionButton) findViewById(R.id.fabNav);
        fabNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent directionIntent= new Intent(DetailFasKesActivity.this, DirectionActivity.class);
                directionIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                directionIntent.putExtra("latitude",xlatitude);
                directionIntent.putExtra("longitude",xlongitude);
                startActivity(directionIntent);
                floatingActionsMenu.collapse();
            }
        });

        coordinatorLayout =(CoordinatorLayout) findViewById(R.id.coordinator);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitle(xnama);
    }


}
