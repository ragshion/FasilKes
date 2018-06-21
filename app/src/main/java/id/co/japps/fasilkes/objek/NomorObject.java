package id.co.japps.fasilkes.objek;

/**
 * Created by OiX on 05/09/2017.
 */

public class NomorObject {

    private String nama;
    private int photo;
    private String no_telp;

    public NomorObject(String nama, int photo) {
        this.nama = nama;
        this.photo = photo;
        this.no_telp = no_telp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }
}
