package com.maps.paktebe;

import com.google.android.gms.maps.model.LatLng;
import com.maps.paktebe.entity.TambalBan;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;


public class JSONParser {

	static InputStream is = null;
	static JSONObject jsonObject = null;
	static String json = "";	
	private final String TAG_TAMBALBAN = "tambalban";
	private final String TAG_ID = "id_tb";
	private final String TAG_NAMA = "nama_tb";
	private final String TAG_JENIS = "jenis_tb";
	private final String TAG_ALAMAT = "alamat_tb";
	private final String TAG_TELP = "telp_tb";
	private final String TAG_LAT = "lat";
	private final String TAG_LNG ="lng";
	private final String TAG_ROUTES = "routes";
	private final String TAG_LEGS ="legs";
	private final String TAG_STEPS ="steps";
	private final String TAG_POLYLINE = "polyline";
	private final String TAG_POINTS = "points";
	private final String TAG_START = "start_location";
	private final String TAG_END = "end_location";
		
	public JSONObject getJSONFromURL (String url){
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e){
			e.printStackTrace();
		} catch(ClientProtocolException e){
			e.printStackTrace();			
		} catch(IOException e){
			e.printStackTrace();
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine())!=null){
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		}catch (Exception e){
			Log.e("Buffer Error","Error converting result"+e.toString());
		}
		
		try {
			jsonObject = new JSONObject(json);			
		} catch (JSONException e){
			Log.e("JSON Parser","Error parsing data"+e.toString());
		}
		
		return jsonObject;
	}
	
	public ArrayList<TambalBan> getTambalBanAll(JSONObject jobj){
		ArrayList<TambalBan> listTambalBan = new ArrayList<TambalBan>();
		
		try {
			JSONArray arrayTambalBan = jobj.getJSONArray(TAG_TAMBALBAN);
			for (int i =0; i<arrayTambalBan.length();i++){
				JSONObject jobject = arrayTambalBan.getJSONObject(i);
				
				listTambalBan.add(new TambalBan(jobject.getInt(TAG_ID), jobject.getString(TAG_NAMA),
						jobject.getString(TAG_JENIS)
						,jobject.getString(TAG_ALAMAT),
						jobject.getString(TAG_TELP)
						,jobject.getDouble(TAG_LAT), jobject.getDouble(TAG_LNG)));			
				
			}
		} catch(Exception e){
			e.printStackTrace();
		}		
		return listTambalBan;
}
	
	

	/*
	 * Untuk decode Polyline
	 * 
	 * @params String
	 * 
	 * @return List<LatLng>
	 */
	
	private List <LatLng> decodePoly(String encoded){
		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len){
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b& 0x1f)<<shift;
				shift +=5;				
			} while (b>= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do
			{
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng(lat / 1E5, lng / 1E5);
			poly.add(position);
		}
		return poly;
	}
	
	/*
	 * Untuk mendapatkan direction
	 * 
	 * @params JSONObject
	 * 
	 * @return List<LatLng>
	 */
	public List<LatLng> getDirection(JSONObject jObj){
		List<LatLng> directions = new ArrayList<LatLng>();
		
		try {
			JSONObject objRoute = jObj.getJSONArray(TAG_ROUTES).getJSONObject(0);
			JSONObject objLegs = objRoute.getJSONArray(TAG_LEGS).getJSONObject(0);
			JSONArray arraySteps = objLegs.getJSONArray(TAG_STEPS);
			for (int i = 0; i< arraySteps.length(); i++){
				JSONObject step = arraySteps.getJSONObject(i);
				JSONObject objStart = step.getJSONObject(TAG_START);
				JSONObject objEnd = step.getJSONObject(TAG_END);
				double latStart = objStart.getDouble(TAG_LAT);
				double lngStart = objStart.getDouble(TAG_LNG);
				
				directions.add(new LatLng(latStart, lngStart));
				JSONObject poly = step.getJSONObject(TAG_POLYLINE);
				String encodedPoly = poly.getString(TAG_POINTS);
				
				List<LatLng>decodedPoly = decodePoly(encodedPoly);
				for (int a = 0; a< decodedPoly.size();a++ ){
					directions.add(new LatLng(decodedPoly.get(a).latitude, decodedPoly.get(a).longitude));
					
				}
				
				double latEnd = objEnd.getDouble(TAG_LAT);
				double lngEnd = objEnd.getDouble(TAG_LNG);
				directions.add(new LatLng(latEnd, lngEnd));
			}
		} catch (JSONException e){
			
		}
		
		return directions;
	}
	
}
