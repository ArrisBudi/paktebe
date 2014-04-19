package com.maps.paktebe;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.FragmentTransaction;

@SuppressLint("NewApi")
public class DaftarTB extends FragmentActivity implements ActionBar.TabListener {
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daftar_tb);		
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText("Daftar Motor").setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("Daftar Mobil").setTabListener(this));				
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)){
			getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar().getSelectedNavigationIndex());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		if(tab.getPosition()==0){
		Motor motor = new Motor();
		getSupportFragmentManager().beginTransaction().replace(R.id.container, motor).commit();
		} else {
			Mobil mobil = new Mobil();
			getSupportFragmentManager().beginTransaction().replace(R.id.container, mobil).commit();			
		}		
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		
	}
}