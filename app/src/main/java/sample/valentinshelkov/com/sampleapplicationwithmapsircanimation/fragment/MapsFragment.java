package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment;


import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsFragment extends SupportMapFragment {
    private static final LatLng[] PINS = {
            new LatLng(50.27d, 30.30d),
            new LatLng(50.00d, 36.15d),
            new LatLng(46.28d, 30.44d),
            new LatLng(-17.82d, 31.05d),
            new LatLng(44.02d, 144.27d),
    };
    private GoogleMap map;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        map = getMap();
        map.setMyLocationEnabled(true);
        for (LatLng position : PINS) {
            map.addMarker(new MarkerOptions().position(position).visible(true));
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);
        Location location = locationManager.getLastKnownLocation(provider);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng myLocation = new LatLng(lat, lng);
        CameraUpdate center = CameraUpdateFactory.newLatLng(myLocation);
        map.moveCamera(center);
        super.onViewCreated(view, savedInstanceState);
    }
}
