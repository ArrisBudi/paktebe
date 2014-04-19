package com.maps.paktebe;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener { 
Button panduan, daftarTB, petaLokasi,tentangApp;
public SharedPreferences prefLocation;
private LocationManager locationManager= null;
private double userLat = 0;
private double userLng = 0;
private static final long JARAK_MINIMAL_UNTUK_UPDATE = 10; // dalam meter
private static final long WAKTU_MINIMUM_UNTUK_UPDATE= 1000; // dalam detik
Context mContext;
boolean isGPSEnabled = false;
boolean isNetworkEnabled = false;
boolean canGetLocation = false;
Location loc;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		initLocationManager();
				
		daftarTB = (Button)findViewById(R.id.daftartb);
		daftarTB.setOnClickListener(this);
		
		petaLokasi =(Button) findViewById(R.id.petaLokasi);
		petaLokasi.setOnClickListener(this);
		
		panduan = (Button) findViewById(R.id.panduan);
		panduan.setOnClickListener(this);
		
		tentangApp =(Button) findViewById(R.id.tentangApp);
		tentangApp.setOnClickListener(this);			
	}

	

	private void initLocationManager() {
		try{		
			locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			
			//getting GPS status
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			
			//getting network status
			isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			//all provider disabled
			if(!isGPSEnabled && !isNetworkEnabled){
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);				
				alertDialog.setTitle("GPS Settings");	
				alertDialog.setMessage("GPS tidak aktif. Apakah Anda ingin menyettingnya?");
		        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		                mContext.startActivity(intent);
		            }
		        });
		  
		        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int which) {
		            dialog.cancel();
		            }
		        });
		  
		        alertDialog.show();	
				
			} else {
				this.canGetLocation = true;
				// first get location from network provider
				if(isNetworkEnabled){
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, WAKTU_MINIMUM_UNTUK_UPDATE, JARAK_MINIMAL_UNTUK_UPDATE, new MyLocationListener());
					if (locationManager != null){
						loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (loc != null){
							setUserLocation(loc);
						}
					}
				}
				
				if (isGPSEnabled){
					if (loc == null){
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, WAKTU_MINIMUM_UNTUK_UPDATE, JARAK_MINIMAL_UNTUK_UPDATE, new MyLocationListener());
					
						if(locationManager != null){
							loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (loc != null){
								setUserLocation(loc);
							}
						}
					}					
				}				
			}
		} catch(Exception e){
			e.printStackTrace();
		}		
	}

	private void setUserLocation(Location location) {
		userLat = location.getLatitude();
		userLng = location.getLongitude();
		prefLocation = getSharedPreferences("tb", 0);
		String lat= Double.toString(userLat);
		String lon = Double.toString(userLng);		
		Editor ed = prefLocation.edit();
		ed.putString("userLat", lat);
		ed.putString("userLng", lon);
		ed.commit();		
	}

	private class MyLocationListener implements LocationListener{
		@Override
		public void onLocationChanged(Location location) {
			String message = String.format(
					"Lokasi berubah \nLng: %1$s \nLat: %2$s", location.getLongitude(), location.getLatitude());
			Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
			setUserLocation(location);			
		}

		@Override
		public void onProviderDisabled(String provider) {
			Toast.makeText(mContext, "Provider disabled oleh user. GPS offline", Toast.LENGTH_LONG).show();					
		}

		@Override
		public void onProviderEnabled(String provider) {
			Toast.makeText(mContext, "Provider enabled oleh user. GPS online", Toast.LENGTH_LONG).show();			
		}

		@Override
		public void onStatusChanged(String s, int i, Bundle b) {
			Toast.makeText(mContext, "Status Provider Berubah", Toast.LENGTH_LONG).show();			
		}		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.daftartb:
			startActivity(new Intent(this,DaftarTB.class));
			break;
		
		case R.id.petaLokasi:
			startActivity(new Intent(this,PetaLokasi.class));
			break;
			
		case R.id.panduan:
			startActivity(new Intent(this,Panduan.class));
			break;
			
		case R.id.tentangApp:
			startActivity(new Intent(this,TentangApp.class));
			break;			
		}		
	}
	
	
		public void onBackPressed(){
			new AlertDialog.Builder(this)
			.setTitle ("Keluar dari Pak Tebe")
			.setMessage("Apakah Anda Ingin Keluar?")
			.setNegativeButton ("Tidak",null)
			.setPositiveButton("Ya", new DialogInterface.OnClickListener(){			
				@Override 
				public void onClick(DialogInterface dialog,int which){
					finish();
				}
				}).show();
		}
		
		protected void onDestroy(){
			locationManager = null;
			super.onDestroy();
		}
}
