package id.co.japps.fasilkes.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.google.android.gms.maps.SupportMapFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import id.co.japps.fasilkes.R;
import id.co.japps.fasilkes.api.Client;
import id.co.japps.fasilkes.api.Service;
import id.co.japps.fasilkes.api.UploadService;
import id.co.japps.fasilkes.objek.BaseResponse;
import id.co.japps.fasilkes.utilities.FileUtils;
import id.co.japps.fasilkes.utilities.SharedPrefManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by OiX on 04/02/2018.
 */

public class TambahActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private static final int PERMISSION_REQUEST_STORAGE = 2;

    MaterialEditText nama,alamat,kelurahan,jambuka, jamtutup, pemilik, latitude,longitude;
    MaterialSpinner kategori,haribuka,haritutup,kecamatan;

    FlatButton btnSimpan,btnGetLatLong;
    MaterialDialog materialDialog, dialogEmpty, dialogSalah;
    ImageView imgThumb;
    String username;
    SharedPrefManager sharedPrefManager;

    private UploadService uploadService;
    private final ArrayList<String> mStrings = new ArrayList<>();

    String TYPE_1 = "gagal";

    private Uri uri;

    @Override
    public void onBackPressed() {
        Intent back = new Intent(TambahActivity.this,MainActivity.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(back);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        sharedPrefManager = new SharedPrefManager(this);
        username = sharedPrefManager.getSPID();

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        kelurahan = findViewById(R.id.kelurahan);

        jambuka= findViewById(R.id.jambuka);
        jamtutup = findViewById(R.id.jamtutup);
        pemilik = findViewById(R.id.pemilik);

        latitude = findViewById(R.id.latitude);
        latitude.setEnabled(false);
        longitude = findViewById(R.id.longitude);
        longitude.setEnabled(false);

        kecamatan = (MaterialSpinner) findViewById(R.id.kecamatan);
        kecamatan.setItems(mStrings);
        kecamatan.setItems("Pekalongan Utara", "Pekalongan Timur", "Pekalongan Selatan", "Pekalongan Barat");
        kecamatan.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        kategori = (MaterialSpinner) findViewById(R.id.kategori);
        kategori.setItems("Apotek", "Bidan", "Dokter", "Puskesmas", "RumahSakit");
        kategori.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        haribuka = (MaterialSpinner) findViewById(R.id.haribuka);
        haribuka.setItems("Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu");
        haribuka.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });

        haritutup = (MaterialSpinner) findViewById(R.id.haritutup);
        haritutup.setItems("Senin", "Selasa", "Rabu", "Kamis", "Jum'at", "Sabtu", "Minggu");
        haritutup.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                //Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });


        btnGetLatLong = (FlatButton) findViewById(R.id.btnGetLatLong);
        btnGetLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(TambahActivity.this, nama.getText(), Toast.LENGTH_LONG).show();
                Intent intentx = new Intent(TambahActivity.this, MapGetLatLongActivity.class);
                intentx.putExtra("nama",nama.getText().toString());
                intentx.putExtra("alamat", alamat.getText().toString());
                intentx.putExtra("kelurahan", kelurahan.getText().toString());
                intentx.putExtra("kecamatan", kecamatan.getText().toString());
                intentx.putExtra("kategori", kategori.getText().toString());
                intentx.putExtra("haribuka", haribuka.getText().toString());
                intentx.putExtra("haritutup", haritutup.getText().toString());
                intentx.putExtra("jambuka", jambuka.getText().toString());
                intentx.putExtra("jamtutup", jamtutup.getText().toString());
                intentx.putExtra("pemilik", pemilik.getText().toString());

                startActivityForResult(intentx, 1);
            }
        });

        imgThumb = findViewById(R.id.img_thumb);
        imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });



        btnSimpan = (FlatButton) findViewById(R.id.btnUpload);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int stringBuka = jambuka.getText().toString().length();
                int stringTutup = jamtutup.getText().toString().length();
                String ktg = kategori.getText().toString();

                if(ktg.equalsIgnoreCase("Apotek") | ktg.equalsIgnoreCase("Puskesmas") | ktg.equalsIgnoreCase("RumahSakit") && stringBuka!=5){
                    dialogSalah = new MaterialDialog.Builder(TambahActivity.this)
                            .iconRes(R.drawable.ic_warning_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title("Peringatan")
                            .content("Maaf, Format Penulisan Jam Buka / Pagi tidak tepat, mohon diperbaiki sesuai format penulisan yang diarahkan")
                            .positiveText("OK")
                            .show();
                }
                if(ktg.equalsIgnoreCase("Apotek") | ktg.equalsIgnoreCase("Puskesmas") | ktg.equalsIgnoreCase("RumahSakit") && stringTutup!=5){
                    dialogSalah = new MaterialDialog.Builder(TambahActivity.this)
                            .iconRes(R.drawable.ic_warning_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title("Peringatan")
                            .content("Maaf, Format Penulisan Jam Tutup / Sore tidak tepat, mohon diperbaiki sesuai format penulisan yang diarahkan")
                            .positiveText("OK")
                            .show();
                }
                if(ktg.equalsIgnoreCase("Bidan") | ktg.equalsIgnoreCase("Dokter") && stringBuka!=13){
                    dialogSalah = new MaterialDialog.Builder(TambahActivity.this)
                            .iconRes(R.drawable.ic_warning_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title("Peringatan")
                            .content("Maaf, Format Penulisan Jam Buka / Pagi tidak tepat, mohon diperbaiki sesuai format penulisan yang diarahkan")
                            .positiveText("OK")
                            .show();
                }
                if(ktg.equalsIgnoreCase("Bidan") | ktg.equalsIgnoreCase("Dokter") && stringTutup!=13){
                    dialogSalah = new MaterialDialog.Builder(TambahActivity.this)
                            .iconRes(R.drawable.ic_warning_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title("Peringatan")
                            .content("Maaf, Format Penulisan Jam Tutup / Sore tidak tepat, mohon diperbaiki sesuai format penulisan yang diarahkan")
                            .positiveText("OK")
                            .show();
                }

                if(nama.getText().toString().equalsIgnoreCase("") |
                        alamat.getText().toString().equalsIgnoreCase("") |
                        kelurahan.getText().toString().equalsIgnoreCase("") |
                        haribuka.getText().toString().equalsIgnoreCase("") |
                        haritutup.getText().toString().equalsIgnoreCase("") |
                        jambuka.getText().toString().equalsIgnoreCase("") |
                        jamtutup.getText().toString().equalsIgnoreCase("") |
                        pemilik.getText().toString().equalsIgnoreCase("") |
                        latitude.getText().toString().equalsIgnoreCase("0") |
                        longitude.getText().toString().equalsIgnoreCase("0")){
                    dialogEmpty = new MaterialDialog.Builder(TambahActivity.this)
                            .iconRes(R.drawable.ic_warning_black_24dp)
                            .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                            .title("Peringatan")
                            .content("Maaf, Pastikan semua field sudah diisi")
                            .positiveText("OK")
                            .show();
                }else{
                    materialDialog = new MaterialDialog.Builder(TambahActivity.this)
                            .content("Sedang Menyimpan...")
                            .progress(true, 0)
                            .cancelable(false)
                            .progressIndeterminateStyle(true)
                            .show();
                    if(uri != null) {
                        File file = FileUtils.getFile(TambahActivity.this, uri);
                        uploadMultipart(file);
                    }else{
                        Toast.makeText(TambahActivity.this, "You must choose the image", Toast.LENGTH_SHORT).show();
                        materialDialog.dismiss();
                    }
                }
            }
        });

    }

    private void uploadMultipart(File file) {

        TYPE_1 = nama.getText().toString();
        RequestBody photoBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo",
                file.getName(), photoBody);

        RequestBody action = RequestBody.create(MediaType.parse("text/plain"), TYPE_1);

        uploadService = new UploadService();
        uploadService.uploadPhotoMultipart(action, photoPart, new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                BaseResponse baseResponse = (BaseResponse) response.body();

                if(baseResponse != null) {
                    //Toast.makeText(TambahActivity.this, baseResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    //RESPON SUKSES A.K.A FOTO BERHASIL DI UPLOAD
                    String id ="";
                    if(sharedPrefManager.getSpStatus().equalsIgnoreCase("Google")){
                        id = sharedPrefManager.getSPID().substring(0, sharedPrefManager.getSPID().indexOf("@")).toString();
                    }else{
                        id = sharedPrefManager.getSPID().toString();
                    }

                    simpanfaskes(nama.getText().toString(),alamat.getText().toString(),kelurahan.getText().toString(),kecamatan.getText().toString()
                            ,kategori.getText().toString(),haribuka.getText().toString(),haritutup.getText().toString(),jambuka.getText().toString(),jamtutup.getText().toString(),pemilik.getText().toString()
                            ,latitude.getText().toString(),longitude.getText().toString(),nama.getText().toString(),id);


                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                materialDialog.dismiss();
                t.printStackTrace();
                Toast.makeText(TambahActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    void simpanfaskes(String nama,
                      String alamat,
                      String kelurahan,
                      String kecamatan,
                      String kategori,
                      String haribuka,
                      String haritutup,
                      String jambuka,
                      String jamtutup,
                      String pemilik,
                      String latitude,
                      String longitude,
                      String foto,
                      String username){


        Service serviceApi = Client.getClient();
        Call<ResponseBody> simpanFaskes = serviceApi.simpanFaskes(nama,alamat,kelurahan,kecamatan,kategori,haribuka,haritutup,jambuka,jamtutup,pemilik,latitude,longitude,foto,username);
        simpanFaskes.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(TambahActivity.this, "Berhasil Simpan", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                    materialDialog.dismiss();
                }else{
                    materialDialog.dismiss();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                onBackPressed();
            }
        });

    }

    private void choosePhoto() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_STORAGE);

        }else{
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                uri = data.getData();
                imgThumb.setImageURI(uri);
                latitude.setText(sharedPrefManager.getSpLatitude());
                longitude.setText(sharedPrefManager.getSpLongitude());
            }
        }
        if(resultCode == RESULT_OK){
//                nama.setText(data.getStringExtra("nama"));
//                alamat.setText(data.getStringExtra("alamat"));
//                kelurahan.setText(data.getStringExtra("kelurahan"));
//                kecamatan.setText(data.getStringExtra("kecamatan"));
//                kategori.setText(data.getStringExtra("kategori"));
//                haribuka.setText(data.getStringExtra("haribuka"));
//                haritutup.setText(data.getStringExtra("haritutup"));
//                jambuka.setText(data.getStringExtra("jambuka"));
//                jamtutup.setText(data.getStringExtra("jamtutup"));
//                pemilik.setText(data.getStringExtra("pemilik"));
            latitude.setText(sharedPrefManager.getSpLatitude());
            longitude.setText(sharedPrefManager.getSpLongitude());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                }

                return;
            }
        }
    }
}
