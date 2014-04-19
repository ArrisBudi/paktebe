package com.maps.paktebe.entity;

public class TambalBan {

	private int id_tb;
	private String nama_tb;
	private String jenis_tb;
	private String alamat_tb;
	private String telp_tb;
	private double lat;
	private double lng;
	
	public TambalBan(){
		//do nothing
	}
	
	public TambalBan (int id_tb, String nama_tb, String jenis_tb, String alamat_tb, String telp_tb, double lat, double lng){
		super();
		this.id_tb= id_tb;
		this.nama_tb=nama_tb;
		this.jenis_tb=jenis_tb;
		this.alamat_tb=alamat_tb;
		this.telp_tb=telp_tb;
		this.lat=lat;
		this.lng=lng;		
	}

	public int getId_tb() {
		return id_tb;
	}

	public void setId_tb(int id_tb) {
		this.id_tb = id_tb;
	}

	public String getNama_tb() {
		return nama_tb;
	}

	public void setNama_tb(String nama_tb) {
		this.nama_tb = nama_tb;
	}

	public String getJenis_tb() {
		return jenis_tb;
	}

	public void setJenis_tb(String jenis_tb) {
		this.jenis_tb = jenis_tb;
	}

	public String getAlamat_tb() {
		return alamat_tb;
	}

	public void setAlamat_tb(String alamat_tb) {
		this.alamat_tb = alamat_tb;
	}

	public String getTelp_tb() {
		return telp_tb;
	}

	public void setTelp_tb(String telp_tb) {
		this.telp_tb = telp_tb;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
}
