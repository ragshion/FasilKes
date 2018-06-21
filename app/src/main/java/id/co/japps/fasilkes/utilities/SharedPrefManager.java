package id.co.japps.fasilkes.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fariz ramadhan.
 * website : www.farizdotid.com
 * github : https://github.com/farizdotid
 * linkedin : https://www.linkedin.com/in/farizramadhan/
 */


public class SharedPrefManager {

    public static final String SP_MAHASISWA_APP = "spMahasiswaApp";

    public static final String SP_NAMA = "spNama";
    public static final String SP_ID = "spID";
    public static final String SP_IMAGE = "spImage";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_STATUS = "spStatus";

    public static final String SP_LATITUDE = "spLatitude";
    public static final String SP_LONGITUDE = "spLongitude";

    public static final String SP_HISTORI = "spHistori";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_MAHASISWA_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpImage(){ return sp.getString(SP_IMAGE, "");}

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }
    public String getSPID(){
        return sp.getString(SP_ID, "");
    }
    public String getSpLatitude(){
        return sp.getString(SP_LATITUDE, "");
    }
    public String getSpLongitude(){
        return sp.getString(SP_LONGITUDE, "");
    }


    public String getSpHistori(){
        return sp.getString(SP_HISTORI, "");
    }

    public String getSpStatus(){return sp.getString(SP_STATUS,"");}

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }



    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}
