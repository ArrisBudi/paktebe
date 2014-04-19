package com.maps.paktebe;


import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class DetailPanduan extends Activity {	
	private TextView tvNama, tvDetail ;	
	private String nama, detail;
	private String str[];
	private String Select;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_panduan);
		initialize();
		Bundle bundle = getIntent().getExtras();		
		Select = bundle.getString("Select");	
			 
		 str = Select.split(",");
		 int i;
		 for (i = 0; i < str.length; i++) {
				int pos = str[i].indexOf("=");
				str[i] = str[i].replace("}","");
				switch (i) {
				case 0: nama = str[i].substring(pos+1); break;
				case 1: detail = str[i].substring(pos+1); break;
				}
		 }
		setTeksView();	
	}



	private void setTeksView() {
		tvNama.setText(nama);
		tvDetail.setText(detail);		
	}



	private void initialize() {
		tvNama = (TextView) findViewById(R.id.nama_panduan);
		tvDetail = (TextView) findViewById(R.id.detail_panduan);		
	}



	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
