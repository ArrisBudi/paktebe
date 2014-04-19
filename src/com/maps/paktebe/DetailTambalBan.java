package com.maps.paktebe;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailTambalBan extends Activity  {
	private TextView tvNama ;
	private TextView tvAlamat ;
	private TextView tvTelp;
	private TextView tvLat;
	private TextView tvLng;
	private TextView tvJarak;	
	private Button btnGetDirection;
	private Button btnTelp;	
	private String nama;
	private String alamat;
	private String telp;
	private Double lat;
	private Double lng;
	private Double jarak;
	private Double latAsal;
	private Double lngAsal;
	private Double latTuj;
	private Double lngTuj;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_tb);
		
		initialize();
		Bundle bundle = getIntent().getExtras();
		if(bundle != null){
			nama = bundle.getString(PetaLokasi.KEY_NAMA);
			alamat = bundle.getString(PetaLokasi.KEY_ALAMAT);
			telp = bundle.getString(PetaLokasi.KEY_TELP);
			lat = bundle.getDouble(PetaLokasi.KEY_LAT);
			lng = bundle.getDouble(PetaLokasi.KEY_LNG);
			latAsal = bundle.getDouble(PetaLokasi.KEY_LAT_ASAL);
			lngAsal = bundle.getDouble(PetaLokasi.KEY_LNG_ASAL);
			latTuj = bundle.getDouble(PetaLokasi.KEY_LAT_TUJUAN);
			lngTuj = bundle.getDouble(PetaLokasi.KEY_LNG_TUJUAN);			
		}
		jarak = new Utils().hitungJarak(latAsal, lngAsal, latTuj, lngTuj);
		jarak = new Utils().RoundDecimal(jarak, 2);
		
		setTeksView();	
	}

	private void setTeksView() {
		tvNama.setText(nama);
		tvAlamat.setText("Alamat : "+ alamat);
		tvTelp.setText("Telepon  : "+ telp);
		tvLat.setText ("Latitude : "+ lat);
		tvLng.setText("Longitude : "+ lng);
		tvJarak.setText("Jarak   : " + jarak + " KM");
	}
	

	private void initialize() {
		tvNama = (TextView) findViewById(R.id.nama_tb);
		tvAlamat = (TextView) findViewById(R.id.alamat_tb);
		tvTelp = (TextView) findViewById(R.id.telp_tb);
		tvLat = (TextView) findViewById(R.id.lat);
		tvLng = (TextView) findViewById(R.id.lng);
		tvJarak = (TextView) findViewById(R.id.jarak);
		
		btnGetDirection = (Button) findViewById(R.id.btnDirection);
		btnGetDirection.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putDouble(PetaLokasi.KEY_LAT_ASAL, latAsal);
				bundle.putDouble(PetaLokasi.KEY_LNG_ASAL, lngAsal);
				bundle.putDouble(PetaLokasi.KEY_LAT_TUJUAN, latTuj);
				bundle.putDouble(PetaLokasi.KEY_LNG_TUJUAN, lngTuj);
				bundle.putString(PetaLokasi.KEY_NAMA, nama);
				bundle.putDouble("jarak", jarak);
				
				Intent intent = new Intent(getApplicationContext(), DirectionActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);				
			}
		});
		
		btnTelp = (Button) findViewById(R.id.btnTelp);
		btnTelp.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				isTelephonyEnabled();
				Intent telpon = new Intent(Intent.ACTION_CALL);
				telpon.setData(Uri.parse("tel:" + telp));
				startActivity(telpon);				
			}

			private boolean isTelephonyEnabled(){
			    TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			    return tm != null && tm.getSimState()==TelephonyManager.SIM_STATE_READY;
			}
		});
		
	}
}
