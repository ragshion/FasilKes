package id.co.japps.fasilkes.objek;

/**
 * Created by OiX on 05/09/2017.
 */

public class MenuObject {

    private String nama;
    private int photo;

    public MenuObject(String nama, int photo) {
        this.nama = nama;
        this.photo = photo;
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
}