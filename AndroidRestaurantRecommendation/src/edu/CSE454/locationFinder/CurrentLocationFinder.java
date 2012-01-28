package edu.CSE454.locationFinder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class CurrentLocationFinder extends Observable{
	
	private Context context;
	private double longitude;
	private double latitude;
	private LocationResult locationResult;
	
	public CurrentLocationFinder(Context context){
		this.context = context;
		longitude = 0;
		latitude = 0;
        locationResult = new MLocationResult();
	}
	
	public void getLocation(){
    	MyLocation myLocation = new MyLocation();
        myLocation.getLocation(context.getApplicationContext(), locationResult);
	}
	
	
	private class MLocationResult extends LocationResult{

		@Override
		public void gotLocation(Location location) {

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Log.d("locationResult", "long: " + longitude + " lat: " + latitude);
//    			Geocoder gcd = new Geocoder(context, Locale.getDefault());
			ReverseGeocode gcd = new ReverseGeocode();
			List<Address> addresses = gcd.getFromLocation(latitude, longitude, 2);
			if (addresses.size() > 0){
//    				Log.d("location finder", "place: " + addresses.get(0).getLocality());
				Address address = addresses.get(0);
				String city = address.getLocality();
				if(city == null) city = "";
				else city += ", ";
				String state = address.getAdminArea();
				if(state == null) state = "";
				setChanged();
				notifyObservers(city + state);
			}
		}
	}
}
