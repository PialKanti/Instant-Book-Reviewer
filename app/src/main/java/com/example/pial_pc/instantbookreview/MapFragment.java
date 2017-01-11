package com.example.pial_pc.instantbookreview;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.pial_pc.instantbookreview.databaseHandler.DBadapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Pial-PC on 2/6/2016.
 */
public class MapFragment extends Fragment {
    MapView mMapView;
    private GoogleMap googleMap;
    MarkerOptions marker;
    Location lastKnownLocation;
    ArrayList<String> bookDetails;
    ArrayList<String[]> book;
    String[] book_store;
    DBadapter dBadapter;
    CameraPosition cameraPosition;
    LinearLayout errorLayout;
    String[] book_info=new String[3];
        Double lat,lng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        errorLayout=(LinearLayout)rootView.findViewById(R.id.Error);
        mMapView.onCreate(savedInstanceState);

        bookDetails = new ArrayList<String>();
        bookDetails = BookDetailsActivity.getRequiredData();

        dBadapter=new DBadapter(getActivity());
        book=new ArrayList<String[]>();
        book=dBadapter.getBookStore(bookDetails.get(0));
        final int size=book.size();


        if(size!=0){
            mMapView.onResume();// needed to get the map to display immediately
            try {
                MapsInitializer.initialize(getActivity().getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            googleMap = mMapView.getMap();

            googleMap.setMyLocationEnabled(true);
            // latitude and longitude
            double latitude = 22.817730;
            double longitude = 89.560043;


            Toast.makeText(getActivity(),size+"",Toast.LENGTH_SHORT).show();
            Log.i("Map",size+"");

            for(int i=0;i<size;i++){
                book_info = book.get(i);

                Log.i("Start",book_info[0]);
                Log.i("Start",book_info[1]);
                Log.i("Start",book_info[2]);

                lat=Double.parseDouble(book_info[1]);
                lng=Double.parseDouble(book_info[2]);

                // adding marker
                googleMap.addMarker(new MarkerOptions().position(
                        new LatLng(lat, lng)).title(book_info[0]).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(lat, lng)).zoom(16).build();
            }

            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));


            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    LocationManager locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
                    String locationProvider=LocationManager.NETWORK_PROVIDER;
                    lastKnownLocation=locationManager.getLastKnownLocation(locationProvider);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mylocation)));
                    return false;
                }
            });
        }else{
            errorLayout.setVisibility(View.VISIBLE);
            mMapView.setVisibility(View.INVISIBLE);
        }




        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
