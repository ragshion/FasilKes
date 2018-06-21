package id.co.japps.fasilkes.api;

import com.google.gson.JsonArray;

import java.util.List;

import id.co.japps.fasilkes.objek.BaseResponse;
import id.co.japps.fasilkes.objek.Faskes;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by delaroy on 7/3/17.
 */

public interface Service {

    @GET("datafaskes/api_kat/all")
    Call<JsonArray> xTeam(

    );


    @Multipart
    @POST(Config.API_UPLOAD)
    Call<BaseResponse> uploadPhotoMultipart(
            @Part("action") RequestBody action,
            @Part MultipartBody.Part photo);


    @GET("datafaskes/api/{kategori}/{latitude}/{longitude}/loadall")
    Call<JsonArray> readTeamArray(
            @Path("kategori") String kategori,
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );

    @GET("datafaskes/apispesialis/{latitude}/{longitude}")
    Call<JsonArray> getSpesialis(
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );

    @GET("datafaskes/apispesialis/{kategori}/{latitude}/{longitude}")
    Call<List<Faskes>> spesialis(
            @Path("kategori") String kategori,
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );

    @GET("datafaskes/api/{kategori}/{latitude}/{longitude}/loadmore")
    Call<List<Faskes>> getFaskes(@Path("kategori") String kategori,
                                 @Path("latitude") Double latitude,
                                 @Path("longitude") Double longitude,
                                 @Query("index") int index);

    @GET("nomorpenting/api/{kategori}")
    Call<JsonArray> getNoPe(
            @Path("kategori") String kategori
    );

    @FormUrlEncoded
    @POST("datafaskes_android/simpan")
    Call<ResponseBody> simpanFaskes(
        @Field("nama") String nama,
        @Field("alamat") String alamat,
        @Field("kelurahan") String kelurahan,
        @Field("kecamatan") String kecamatan,
        @Field("kategori") String kategori,
        @Field("hbuka") String hbuka,
        @Field("htutup") String htutup,
        @Field("jbuka") String jbuka,
        @Field("jtutup") String jtutup,
        @Field("pemilik") String pemilik,
        @Field("latitude") String latitude,
        @Field("longitude") String longitude,
        @Field("foto") String foto,
        @Field("username") String username
    );


}
