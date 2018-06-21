package id.co.japps.fasilkes.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Faskes {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("kelurahan")
    @Expose
    private String kelurahan;
    @SerializedName("kecamatan")
    @Expose
    private String kecamatan;
    @SerializedName("kategori")
    @Expose
    public String kategori;
    @SerializedName("hbuka")
    @Expose
    private String hbuka;
    @SerializedName("htutup")
    @Expose
    private String htutup;
    @SerializedName("jbuka")
    @Expose
    private String jbuka;
    @SerializedName("jtutup")
    @Expose
    private String jtutup;
    @SerializedName("pagi")
    @Expose
    private String pagi;
    @SerializedName("sore")
    @Expose
    private String sore;
    @SerializedName("pemilik")
    @Expose
    private String pemilik;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("foto")
    @Expose
    private String foto;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("jarak")
    @Expose
    private String jarak;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getKecamatan() {
        return kecamatan;
    }

    public void setKecamatan(String kecamatan) {
        this.kecamatan = kecamatan;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getHbuka() {
        return hbuka;
    }

    public void setHbuka(String hbuka) {
        this.hbuka = hbuka;
    }

    public String getHtutup() {
        return htutup;
    }

    public void setHtutup(String htutup) {
        this.htutup = htutup;
    }

    public String getJbuka() {
        return jbuka;
    }

    public void setJbuka(String jbuka) {
        this.jbuka = jbuka;
    }

    public String getJtutup() {
        return jtutup;
    }

    public void setJtutup(String jtutup) {
        this.jtutup = jtutup;
    }

    public String getPagi() {
        return pagi;
    }

    public void setPagi(String pagi) {
        this.pagi = pagi;
    }

    public String getSore() {
        return sore;
    }

    public void setSore(String sore) {
        this.sore = sore;
    }

    public String getPemilik() {
        return pemilik;
    }

    public void setPemilik(String pemilik) {
        this.pemilik = pemilik;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getJarak() {
        return jarak;
    }

    public void setJarak(String jarak) {
        this.jarak = jarak;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static final Comparator<Faskes> BY_JARAK = new Comparator<Faskes>() {
        @Override
        public int compare(Faskes o1, Faskes o2) {
            return o1.jarak.compareTo(o2.jarak);
        }
    };

    public Faskes(String kategori) {
        this.kategori = kategori;
    }
}