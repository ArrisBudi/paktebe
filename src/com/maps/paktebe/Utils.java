package com.maps.paktebe;

import java.math.BigDecimal;

public class Utils {
	
	public Double RoundDecimal(Double jarak2, int i) {
		BigDecimal bd = new BigDecimal(jarak2);
		bd = bd.setScale(i, 6);		
		return bd.doubleValue();
	}
	

	public Double hitungJarak(Double latAsal, Double lngAsal,
			Double latTuj, Double lngTuj) {
		double dist;
		double radian = 6371;
		double dLat = (latTuj - latAsal) * Math.PI / 180;
		double dLon = (lngTuj- lngAsal) * Math.PI / 180;
		
		double a = Math.sin(dLat/2) * Math.sin(dLat/2)
				+ Math.cos(latAsal * (Math.PI / 180))
				* Math.cos(latTuj * (Math.PI /180))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		dist = radian * c;
		return dist;		
	}		
	
}

