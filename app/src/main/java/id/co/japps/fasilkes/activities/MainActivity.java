package id.co.japps.fasilkes.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.gps.GPSTracker;
import id.co.japps.fasilkes.objek.MenuObject;
import id.co.japps.fasilkes.adapter.MenuViewAdapter;
import id.co.japps.fasilkes.utilities.SharedPrefManager;

/**
 * Created by OiX on 30/08/2017.
 */

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private SliderLayout sliderLayout;


    Drawer navDrawerLeft;
    AccountHeader headerNavLeft;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private GridLayoutManager lLayout;
    private boolean doubleBackToExitPressedOnce = false;

    String pEmail = "";
    ImageView imageView;



    SharedPrefManager sharedPrefManager;
    GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
        reqPermitGps(); //Request Permission GPS apabila belum diperbolehkan akses GPS
        reqPermitPhone(); //Request permission telepon
        GPSTracker gps = new GPSTracker(this);
        imageView = (ImageView) findViewById(R.id.material_drawer_account_header_current);

        sharedPrefManager = new SharedPrefManager(this);
        sharedPrefManager.saveSPString(sharedPrefManager.SP_HISTORI,"0");
        if(sharedPrefManager.getSPSudahLogin()){
            pEmail = sharedPrefManager.getSPNama();
        }

//        if(getIntent().getStringExtra("login")=="facebookLogin"){
//            FacebookSdk.sdkInitialize(this);
//            String name = getIntent().getStringExtra("name").toString();
//            String id = getIntent().getStringExtra("id").toString();
//            String imageUrl = getIntent().getStringExtra("imageUrl").toString();
//
//            navnama = name;
//            logins = true;
//
//        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    //=============GridLayoutManagerMenu RecyclerView=============
        List<MenuObject> rowListItem = getAllItemList();
        lLayout = new GridLayoutManager(MainActivity.this, 3);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recyclerView);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        MenuViewAdapter rcAdapter = new MenuViewAdapter(MainActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    //====================END OF GRID LAYOUT===========================

    //===================NAVIGATION DRAWER==============================================
        final PrimaryDrawerItem drawerBeranda = new PrimaryDrawerItem()
                .withIdentifier(1)
                .withName("Beranda")
                .withIcon(R.drawable.ic_home_black_24dp);
        PrimaryDrawerItem drawerGeoMap = new PrimaryDrawerItem()
                .withIdentifier(2)
                .withName("Geo Map")
                .withIcon(R.drawable.ic_map_black_24dp);
        PrimaryDrawerItem drawerNomorPenting = new PrimaryDrawerItem()
                .withIdentifier(3)
                .withName("Nomor Darurat")
                .withIcon(R.drawable.ic_contact_phone_black_24dp);
        PrimaryDrawerItem drawerTentang = new PrimaryDrawerItem()
                .withIdentifier(4)
                .withName("Tentang")
                .withIcon(R.drawable.ic_perm_contact_calendar_black_24dp);
        PrimaryDrawerItem drawerShare = new PrimaryDrawerItem()
                .withIdentifier(5)
                .withName("Share")
                .withIcon(R.drawable.ic_share_black_24dp);
        PrimaryDrawerItem drawerLogin = new PrimaryDrawerItem()
                .withIdentifier(6)
                .withName("Login")
                .withIcon(R.drawable.ic_login_24dp);
        PrimaryDrawerItem drawerLogout = new PrimaryDrawerItem()
                .withIdentifier(7)
                .withName("Logout")
                .withIcon(R.drawable.ic_logout_24dp);
        PrimaryDrawerItem drawerTambah = new PrimaryDrawerItem()
                .withIdentifier(8)
                .withName("Tambah Data")
                .withIcon(R.drawable.ic_tambah_24dp);
        PrimaryDrawerItem drawerHistory = new PrimaryDrawerItem()
                .withIdentifier(9)
                .withName("History Data")
                .withIcon(R.drawable.ic_history_black_24dp);
        PrimaryDrawerItem drawerBantuan = new PrimaryDrawerItem()
                .withIdentifier(10)
                .withName("Bantuan")
                .withIcon(R.drawable.ic_help);
        ProfileDrawerItem profil_login = new ProfileDrawerItem()
                .withIcon(getResources().getDrawable(R.drawable.icon_faskes))
                .withIdentifier(99)
                .withName("Pemetaan Fasilitas Kesehatan")
                .withEmail(pEmail);


        headerNavLeft = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.icon_header_background)
                .withHeightRes(R.dimen.HeaderMaterialDrawer)
                .withCurrentProfileHiddenInList(true)
                .withSelectionListEnabledForSingleProfile(false)
                .withSelectionListEnabled(false)
                .addProfiles(
                        profil_login
                )
                .build();

        navDrawerLeft = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(headerNavLeft)
                .addDrawerItems(
                    drawerBeranda,
                    drawerGeoMap,
                    drawerNomorPenting,
                    drawerTentang,
                    new DividerDrawerItem(),
                    drawerShare,
                    drawerBantuan,
                    new DividerDrawerItem(),
                    drawerLogin
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch (position){
                            case 2:
                                Intent intentGeoMap = new Intent(MainActivity.this, MapsActivity.class);
                                startActivity(intentGeoMap);
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 3:
                                Intent intentDarurat = new Intent(MainActivity.this, NomorDaruratActivity.class);
                                startActivity(intentDarurat);
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 4:
                                Intent intentTentang = new Intent(MainActivity.this, TentangActivity.class);
                                startActivity(intentTentang);
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 6:
                                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                String isiShare = "Aplikasi Pemetaan Fasilitas Kesehatan Kota Pekalongan \n\nAyo Unduh di http://bit.ly/faskesapp";
                                sharingIntent.putExtra(Intent.EXTRA_TEXT, isiShare);
                                startActivity(Intent.createChooser(sharingIntent, "Bagikan Via :"));
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 7:
                                Intent intentBantuan = new Intent(MainActivity.this, BantuanActivity.class);
                                startActivity(intentBantuan);
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 9:
                                if(sharedPrefManager.getSPSudahLogin()){
                                    if(sharedPrefManager.getSpStatus().equals("Google")){
                                        Intent gIntent = new Intent(MainActivity.this, LoginActivity.class);
                                        gIntent.putExtra("status","logout");
                                        gIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(gIntent);
                                    }else{
                                        FirebaseMessaging.getInstance().unsubscribeFromTopic(sharedPrefManager.getSPID());
                                        LoginManager.getInstance().logOut();
                                        sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, "spID");
                                        sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                                        startActivity(new Intent(MainActivity.this, MainActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                    finish();
                                }else{
                                    Intent logintIntent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(logintIntent);
                                }
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 10:
                                Intent tambahIntent = new Intent(MainActivity.this, TambahActivity.class);
                                startActivity(tambahIntent);
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                            case 11:
                                Intent historyIntent = new Intent(MainActivity.this, ListFasKesActivity.class);
                                sharedPrefManager.saveSPString(sharedPrefManager.SP_HISTORI, "1");
                                if(sharedPrefManager.getSpStatus().equalsIgnoreCase("Google")){
                                    String id = sharedPrefManager.getSPID().substring(0, sharedPrefManager.getSPID().indexOf("@")).toString();
                                    historyIntent.putExtra("kategori", id);
                                }else{
                                    historyIntent.putExtra("kategori", sharedPrefManager.getSPID());
                                }
                                startActivity(historyIntent);
                                navDrawerLeft.setSelection(drawerBeranda);
                                break;
                        }
                        navDrawerLeft.closeDrawer();
                        return true;
                    }
                })
                .build();

        if(sharedPrefManager.getSPSudahLogin()){
            navDrawerLeft.removeItem(6);
            navDrawerLeft.addItemAtPosition(drawerLogout,9);
            navDrawerLeft.addItemAtPosition(drawerTambah, 10);
            navDrawerLeft.addItemAtPosition(drawerHistory, 11);

            //headerNavLeft.removeProfileByIdentifier(1);
            //headerNavLeft.addProfile(profil_login,0);
            headerNavLeft.addProfile(profil_login,0);
            //initialize and create the image loader logic
            DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                    Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
                }

                @Override
                public void cancel(ImageView imageView) {
                    Picasso.with(imageView.getContext()).cancelRequest(imageView);
                }
            });
//            try{
//                new MainActivity.DownloadImage(imageView).execute(sharedPrefManager.getSpImage());
//            }catch (Exception e){
//
//            }
        }
    //=================END OF NAVIGATION Drawer===================================

        //====================Image Slider============================================
            sliderLayout = (SliderLayout) findViewById(R.id.imageslider);

            HashMap<String,String> url_maps = new HashMap<String, String>();

            url_maps.put("Museum Batik Pekalongan", "http://www.faskespekalongan.xyz/assets/imageslider/museum_batik.jpg");
            url_maps.put("Tugu Pekalongan", "http://www.faskespekalongan.xyz/assets/imageslider/tugu_pekalongan.jpg");
            url_maps.put("Taman Sorogenen", "http://www.faskespekalongan.xyz/assets/imageslider/sorogenen_depan.jpg");
            url_maps.put("Masjid Al-Ikhlas", "http://www.faskespekalongan.xyz/assets/imageslider/masjid_depan.jpg");

            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                sliderLayout.addSlider(textSliderView);
            }

            // you can change animasi, time page and anythink.. read more on github
            sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
            sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            sliderLayout.setCustomAnimation(new DescriptionAnimation());
            sliderLayout.setDuration(5000);
            sliderLayout.startAutoCycle();
        //====================END OF IMAGE SLIDER===============================================



    } //====END OF ON CREATE METHOD


    //==================================Override ImageSlider================================
            @Override
            protected void onStop() {
                sliderLayout.stopAutoCycle();
                super.onStop();
            }

            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Slider Demo", "Page Changed: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
    //===================end of overade image slider===================================


    private List<MenuObject> getAllItemList(){

        List<MenuObject> allItems = new ArrayList<MenuObject>();
        allItems.add(new MenuObject("Apotek", R.drawable.ic_apotek));
        allItems.add(new MenuObject("Bidan", R.drawable.ic_bidan));
        allItems.add(new MenuObject("Dokter Praktik", R.drawable.ic_dokter));
        allItems.add(new MenuObject("Puskesmas", R.drawable.ic_puskesmas));
        allItems.add(new MenuObject("Rumah Sakit", R.drawable.ic_rumahsakit));

        return allItems;
    }



    @TargetApi(Build.VERSION_CODES.M)
    public boolean reqPermitGps(){
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                requestPermissions(new String[] {permission}, 101);
            }
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean reqPermitPhone(){
        String permission = Manifest.permission.CALL_PHONE;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                requestPermissions(new String[] {permission}, 101);
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem cari = menu.findItem(R.id.search);
        cari.setVisible(false);
        MenuItem search = menu.findItem(R.id.btnSearch);
        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent cari = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(cari);
                return true;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        confirmQuit();
    }

    private void confirmQuit() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this,"Tekan kembali sekali lagi untuk Keluar",Toast.LENGTH_SHORT).show();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        sharedPrefManager.saveSPString(sharedPrefManager.SP_HISTORI,"0");
        startActivity(getIntent());
    }
}
