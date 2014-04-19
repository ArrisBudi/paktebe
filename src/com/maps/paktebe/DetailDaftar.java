package com.maps.paktebe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailDaftar extends Activity {	
	private TextView tvNama, tvAlamat, tvTelp, tvLat, tvLng;	
	private Button btnGetDirection, btnTelp;
	private String nama, alamat, telp;
	private Double lat, lng;
	private String Selected;
	private String str[];
	Context ctx;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_daftar);
		ctx = this;
				
		initialize();
		Bundle bundle = getIntent().getExtras();
		
		Selected = bundle.getString("Selected");
		nama = bundle.getString("nama_tb");
		System.out.println("sel: "+Selected+"");
		
		str = Selected.split(",");
		
		int i;
		for (i = 0; i < str.length; i++) {
			int pos = str[i].indexOf("=");
			str[i] = str[i].replace("}", "");
			switch (i) {
			case 5: nama = str[i].substring(pos+1); break;
			case 7: alamat = str[i].substring(pos+1); break;
			case 1: telp = str[i].substring(pos+1); break;
			case 4: lat = Double.parseDouble(str[i].substring(pos+1)); break;
			case 3: lng = Double.parseDouble(str[i].substring(pos+1)); break;
			}
		}
		 
		setTeksView();		
	}

	private void setTeksView() {
		tvNama.setText(nama);
		tvAlamat.setText("Alamat : "+ alamat);
		tvTelp.setText("Telepon : "+ telp);
		tvLat.setText ("Latitude : "+ lat);
		tvLng.setText("Longitude : "+ lng);
	}

	private void initialize() {
		tvNama = (TextView) findViewById(R.id.nama_tb);
		tvAlamat = (TextView) findViewById(R.id.alamat_tb);
		tvTelp = (TextView) findViewById(R.id.telp_tb);
		tvLat = (TextView) findViewById(R.id.lat);
		tvLng = (TextView) findViewById(R.id.lng);
		
		btnGetDirection = (Button) findViewById(R.id.btnDirection);
		btnGetDirection.setOnClickListener(new OnClickListener() {

			SharedPreferences prefLocation = ctx.getSharedPreferences("tb", 0);
			double lat_asal = Double.parseDouble(prefLocation.getString("userLat", "0"));
			double lng_asal = Double.parseDouble(prefLocation.getString("userLng", "0"));
			
			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putDouble("lat", lat);
				bundle.putDouble ("lng", lng);
				bundle.putDouble("lat_asal", lat_asal);
				bundle.putDouble("lng_asal", lng_asal);
				bundle.putString("nama", nama);
				
				Intent intent = new Intent(getApplicationContext(), Direction.class);
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
