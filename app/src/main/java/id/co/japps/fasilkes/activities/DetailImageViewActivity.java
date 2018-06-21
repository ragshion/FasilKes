package id.co.japps.fasilkes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import id.co.japps.fasilkes.R;

/**
 * Created by OiX on 29/12/2017.
 */

public class DetailImageViewActivity extends AppCompatActivity {
    ImageView imgDetail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_image);
        imgDetail = (ImageView) findViewById(R.id.detailImageView);
        //imgDetail.setImageResource(R.drawable.common_google_signin_btn_icon_dark);
        String thumbnail = getResources().getString(R.string.pathFoto)+getIntent().getStringExtra("detailImage");
        Glide.with(DetailImageViewActivity.this)
                .load(thumbnail)
                .thumbnail(Glide.with(DetailImageViewActivity.this).load(R.drawable.elip))
                .fitCenter()
                .crossFade()
                .into(imgDetail);
    }
}
