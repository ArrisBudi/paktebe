package com.maps.paktebe;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Panduan extends ListActivity{	
	public static final String TAG_PANDUAN = "panduan";
	public static final String TAG_ID= "id_panduan";
	public static final String TAG_NAMA= "nama_panduan";
	public static final String TAG_DETAIL = "detail_panduan";
	private static final String url = "http://api.targetkerja.com/tambalban/panduan.php";
	JSONArray panduan = null;	
	ArrayList<HashMap<String, String>> listPanduan = new ArrayList<HashMap<String, String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new AmbilDataJSON().execute();					
	}
	
	public class AmbilDataJSON extends
	AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(
				String... arg0) {
			
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromURL(url);
			
			try{
				panduan = json.getJSONArray(TAG_PANDUAN);
				
				for (int i= 0; i < panduan.length();i++){
					JSONObject pdn = panduan.getJSONObject(i);
					
					String id_panduan= pdn.getString(TAG_ID);
					String nama_panduan=pdn.getString(TAG_NAMA);
					String detail_panduan = pdn.getString(TAG_DETAIL);					

					HashMap<String, String> a = new HashMap<String, String>();
					a.put(TAG_ID, id_panduan);
					a.put(TAG_NAMA, nama_panduan);
					a.put(TAG_DETAIL, detail_panduan);
					
					listPanduan.add(a);
				}
				
			} catch (JSONException e){
				e.printStackTrace();				
			}
			return listPanduan;			
		}
		
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> listPanduan) {
			// TODO Auto-generated method stub
			super.onPostExecute(listPanduan);

			ListAdapter adapter = new SimpleAdapter(getApplicationContext(), listPanduan, R.layout.list_panduan,
				new String[]{TAG_NAMA}, new int[]{R.id.namaPanduan});
			setListAdapter(adapter);
		}		
	}
		
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String pilih = getListAdapter().getItem(position).toString();		
		
		Intent i = new Intent(Panduan.this, DetailPanduan.class);
		Bundle bundle = new Bundle();
		bundle.putString("Select", pilih);		
		i.putExtras(bundle);
		startActivity(i); 				
	}
}
