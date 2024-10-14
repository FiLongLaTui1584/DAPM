package com.example.dapm.model;

import java.io.Serializable;

public class TinDang implements Serializable {
    private int maTin;

    private String TinhTrang;

    private String ChinhSachSD;

    private String XuatXu;

    private String HDSD;

    private String TieuDeTinDang;

    private String MoTaChiTiet;

    private String Gia;

    private int hinhanh;

    public TinDang() {
    }

    public TinDang(int maTin, String tinhTrang, String chinhSachSD, String xuatXu, String HDSD, String tieuDeTinDang, String moTaChiTiet, String gia, int hinhanh) {
        this.maTin = maTin;
        TinhTrang = tinhTrang;
        ChinhSachSD = chinhSachSD;
        XuatXu = xuatXu;
        this.HDSD = HDSD;
        TieuDeTinDang = tieuDeTinDang;
        MoTaChiTiet = moTaChiTiet;
        Gia = gia;
        this.hinhanh = hinhanh;
    }

    public int getMaTin() {
        return maTin;
    }

    public void setMaTin(int maTin) {
        this.maTin = maTin;
    }

    public String getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        TinhTrang = tinhTrang;
    }

    public String getChinhSachSD() {
        return ChinhSachSD;
    }

    public void setChinhSachSD(String chinhSachSD) {
        ChinhSachSD = chinhSachSD;
    }

    public String getXuatXu() {
        return XuatXu;
    }

    public void setXuatXu(String xuatXu) {
        XuatXu = xuatXu;
    }

    public String getHDSD() {
        return HDSD;
    }

    public void setHDSD(String HDSD) {
        this.HDSD = HDSD;
    }

    public String getTieuDeTinDang() {
        return TieuDeTinDang;
    }

    public void setTieuDeTinDang(String tieuDeTinDang) {
        TieuDeTinDang = tieuDeTinDang;
    }

    public String getMoTaChiTiet() {
        return MoTaChiTiet;
    }

    public void setMoTaChiTiet(String moTaChiTiet) {
        MoTaChiTiet = moTaChiTiet;
    }

    public String getGia() {
        return Gia;
    }

    public void setGia(String gia) {
        Gia = gia;
    }

    public int getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(int hinhanh) {
        this.hinhanh = hinhanh;
    }
}