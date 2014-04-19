package com.maps.paktebe;

import java.util.List;
import org.json.JSONObject;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.maps.paktebe.entity.TambalBan;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class PetaLokasi extends Activity implements OnInfoWindowClickListener {
	private GoogleMap map;
	private JSONParser json;
	private ProgressDialog pDialog;
	private LatLng myLocation;
	private List<TambalBan> listTambalBan;
	private final String url = "http://api.targetkerja.com/tambalban/all.php";
	public static final String KEY_NAMA = "nama";
	public static final String KEY_ALAMAT = "alamat";
	public static final String KEY_TELP = "telp";
	public static final String KEY_LAT = "lat";
	public static final String KEY_LNG = "lng";
	public static final String KEY_LAT_TUJUAN = "lat_tujuan";
	public static final String KEY_LNG_TUJUAN = "lng_tujuan";
	public static final String KEY_LAT_ASAL = "lat_asal";
	public static final String KEY_LNG_ASAL = "lng_asal";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.peta_lokasi);
		json = new JSONParser();

		if (isKonekInternet()) {
			new AsynTaskMain().execute();
			setupMapIfNeeded();

		} else {
			ShowAlert(this, "Warning", "Anda tidak tersambung dengan internet");

		}

	}

	@SuppressLint("NewApi")
	private void setupMapIfNeeded() {
		// TODO Auto-generated method stub
		if (map == null) {
			map = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.petaLokasi)).getMap();

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(-7.714564, 110.335386)).zoom(13).build();
			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			
			if (map != null) {
				setupMap();
			}
		}
	}

	private boolean isKonekInternet() {
		// TODO Auto-generated method stub
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void ShowAlert(Context context, String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		alertDialog.show();
	}

	private void setupMap() {
		// TODO Auto-generated method stub
		map.getUiSettings().setCompassEnabled(true);
		map.setMyLocationEnabled(true);
		map.setOnInfoWindowClickListener(this);
		moveToMyLocation();
	}

	private void moveToMyLocation() {
		// TODO Auto-generated method stub
		if (map.getMyLocation() != null) {
			Toast.makeText(this, "" + map.getMyLocation().getLatitude(),
					Toast.LENGTH_SHORT).show();
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(map
					.getMyLocation().getLatitude(), map.getMyLocation()
					.getLongitude()), 15));
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		int resCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getApplicationContext());
		if (resCode != ConnectionResult.SUCCESS) {
			GooglePlayServicesUtil.getErrorDialog(resCode, this, 1);
		}
	}

	public class AsynTaskMain extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPostExecute(Void result) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < listTambalBan.size(); i++) {
						if (listTambalBan.get(i).getJenis_tb().equals("a")) {
							map.addMarker(new MarkerOptions()
									.position(
											new LatLng(listTambalBan.get(i)
													.getLat(), listTambalBan
													.get(i).getLng()))
									.title(listTambalBan.get(i).getNama_tb())
									.snippet(
											listTambalBan.get(i).getAlamat_tb())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.motor)));

						} else if (listTambalBan.get(i).getJenis_tb()
								.equals("b")) {
							map.addMarker(new MarkerOptions()
									.position(
											new LatLng(listTambalBan.get(i)
													.getLat(), listTambalBan
													.get(i).getLng()))
									.title(listTambalBan.get(i).getNama_tb())
									.snippet(
											listTambalBan.get(i).getAlamat_tb())
									.icon(BitmapDescriptorFactory
											.fromResource(R.drawable.mobil)));
						}
					}
				}
			});
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PetaLokasi.this);
			pDialog.setMessage("Loading ....");
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			JSONObject jObject = json.getJSONFromURL(url);
			listTambalBan = json.getTambalBanAll(jObject);
			return null;
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		String id = marker.getId();
		id = id.substring(1);

		myLocation = new LatLng(map.getMyLocation().getLatitude(), map
				.getMyLocation().getLongitude());
		if (myLocation != null) {
			Bundle bundle = new Bundle();
			bundle.putString(KEY_NAMA, listTambalBan.get(Integer.parseInt(id))
					.getNama_tb());
			bundle.putString(KEY_ALAMAT, listTambalBan
					.get(Integer.parseInt(id)).getAlamat_tb());
			bundle.putString(KEY_TELP, listTambalBan.get(Integer.parseInt(id))
					.getTelp_tb());
			bundle.putDouble(KEY_LAT, listTambalBan.get(Integer.parseInt(id))
					.getLat());
			bundle.putDouble(KEY_LNG, listTambalBan.get(Integer.parseInt(id))
					.getLng());

			bundle.putDouble(KEY_LAT_TUJUAN, marker.getPosition().latitude);
			bundle.putDouble(KEY_LNG_TUJUAN, marker.getPosition().longitude);
			bundle.putDouble(KEY_LAT_ASAL, myLocation.latitude);
			bundle.putDouble(KEY_LNG_ASAL, myLocation.longitude);

			Intent i = new Intent(PetaLokasi.this, DetailTambalBan.class);
			i.putExtras(bundle);
			startActivity(i);

		} else {
			Toast.makeText(this, "Tidak dapat menemukan lokasi anda ",
					Toast.LENGTH_LONG).show();
		}
	}
}
