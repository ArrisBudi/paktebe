package com.maps.paktebe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Mobil extends ListFragment{
	public static final String TAG_TAMBALBAN = "mobil";
	public static final String TAG_ID = "id_tb";
	public static final String TAG_NAMA = "nama_tb";
	public static final String TAG_ALAMAT = "alamat_tb";
	public static final String TAG_TELP = "telp_tb";
	public static final String TAG_LAT = "lat";
	public static final String TAG_LNG = "lng";
	public static final String TAG_LAT_ASAL = "lat_asal";
	public static final String TAG_LNG_ASAL = "lng_asal";
	public static final String TAG_JARAK = "jarak";
	public static final String TAG_DIST = "dist";
	private SharedPreferences prefLocation;	
	private static final String url = "http://api.targetkerja.com/tambalban/mobil.php";
	JSONArray tambalban = null;
	ArrayList<HashMap<String, String>> listMobil = new ArrayList<HashMap<String, String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new AmbilDataJSON(getActivity()).execute();			
	}
	

	public class AmbilDataJSON extends
			AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {
		 Context ctx;
		 
		 public AmbilDataJSON(Context ctx){
			 this.ctx = ctx;			 
		 }
		
		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... params) {
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromURL(url);
						
			try {
				tambalban = json.getJSONArray(TAG_TAMBALBAN);

				for (int i = 0; i < tambalban.length(); i++) {
					JSONObject mtr = tambalban.getJSONObject(i);
					 String id_tb = mtr.getString(TAG_ID);
					 String nama_tb = mtr.getString(TAG_NAMA);
					 String alamat_tb = mtr.getString(TAG_ALAMAT);
					 String telp_tb = mtr.getString(TAG_TELP);
					 String lat = mtr.getString(TAG_LAT);
					 String lng = mtr.getString(TAG_LNG);					 
					 double lat_tujuan = Double.parseDouble(lat);
					 double lng_tujuan = Double.parseDouble(lng);
					 prefLocation = ctx.getSharedPreferences("tb", 0);
					 double lat_asal = Double.parseDouble(prefLocation.getString("userLat", "0"));
					 double lng_asal = Double.parseDouble(prefLocation.getString("userLng", "0"));
					 double distance = new Utils().hitungJarak(lat_asal, lng_asal, lat_tujuan, lng_tujuan);					 
					 distance = new Utils().RoundDecimal(distance, 3);
					 String jarak = Double.toString(distance);
					 String dist = Double.toString(distance)+" km";
					
					 HashMap<String, String> a = new HashMap<String, String>();
					 a.put(TAG_ID, id_tb);
					 a.put(TAG_NAMA, nama_tb);
					 a.put(TAG_ALAMAT, alamat_tb);
					 a.put(TAG_TELP, telp_tb);
					 a.put(TAG_LAT, lat);
					 a.put(TAG_LNG, lng);
					 a.put(TAG_JARAK, jarak);
					 a.put(TAG_DIST, dist);

					listMobil.add(a);
					
					Collections.sort(listMobil, new Comparator<HashMap<String, String>>() {
						@Override
						public int compare(HashMap<String, String> o1,
								HashMap<String, String> o2) {							
							return Double.compare(Double.parseDouble(o1.get(TAG_JARAK)), Double.parseDouble(o2.get(TAG_JARAK))); // error
						}					
					});
						
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return listMobil;
		}


		@Override
		protected void onPostExecute(
				ArrayList<HashMap<String, String>> listMobil) {
			// TODO Auto-generated method stub
			super.onPostExecute(listMobil);

			ListAdapter adapter = new SimpleAdapter(getActivity(), listMobil,
					R.layout.list_item, new String[] { TAG_NAMA, TAG_ALAMAT, TAG_DIST },
					new int[] { R.id.namatb, R.id.alamattb,R.id.jarak });
			setListAdapter(adapter);		
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String pilih = getListAdapter().getItem(position).toString();

		Intent i = new Intent(getActivity(), DetailDaftar.class);
		Bundle bundle = new Bundle();
		bundle.putString("Selected", pilih);		
		i.putExtras(bundle);
		startActivity(i); 
	}
}
