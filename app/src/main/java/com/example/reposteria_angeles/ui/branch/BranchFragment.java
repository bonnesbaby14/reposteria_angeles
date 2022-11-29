package com.example.reposteria_angeles.ui.branch;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.reposteria_angeles.R;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class BranchFragment extends Fragment {
    private GoogleMap mMap;
    private final int PERMISSION_LOCATION = 999, DEFAULT_ZOOM = 10;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location lastKnownLocation;
    private LatLng defaultLocation;
    private boolean selectedLocation = false, locationEnabled = false;
    private String key = "AIzaSyCngJvHlT2HUyjALQdzVIiJik58zZY1o7U";
    private LatLng branch1, branch2, branch3;
    private int position;
    private ArrayList<LatLng> branches;
    private ArrayList<String> brancheNames;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            //MarkerOptions marker = new MarkerOptions().position(sydney).title("Hello Maps");
           // marker.icon(bitmapDescriptor(getContext(),R.drawable.bakery));
            mMap = googleMap;
            locationEnabled = false;
            mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,DEFAULT_ZOOM));
            if(ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED)
            {
                mMap.clear();
                mMap.setMyLocationEnabled(true);
                locationEnabled  = true;
                getDeviceLocation();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION

                        },
                        PERMISSION_LOCATION);
                mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,DEFAULT_ZOOM));

                Toast.makeText(getContext(), "Active la ubicación", Toast.LENGTH_SHORT).show();
            }
            setBranches();
            if(locationEnabled == false){
                mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,DEFAULT_ZOOM));
                Toast.makeText(getContext(), "Ubicación desactivada", Toast.LENGTH_SHORT).show();

            }
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    for(int i=0; i<branches.size();i++){
                        if(marker.getPosition().longitude==branches.get(i).longitude &&marker.getPosition().latitude==branches.get(i).latitude) {
                            if(selectedLocation==true||locationEnabled==true)
                                direction(defaultLocation,branches.get(i),brancheNames.get(i));
                            else
                                Toast.makeText(getContext(), "Seleccione ubicación", Toast.LENGTH_SHORT).show();

                        }
                    }
                    return false;
                }
            });

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(latLng.equals(branch1)||latLng.equals(branch2)||latLng.equals(branch3)){

                    }else{
                        mMap.clear();
                        setBranches();
                        mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación Seleccionada"));
                        defaultLocation = latLng;
                        Log.e(TAG, "HI I DIDNT ENTERED THE IF");
                        selectedLocation = true;
                    }
                }//

            });

        }//onMapReady
    };
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationEnabled  = true;

                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                mMap.addMarker(new MarkerOptions().position(mMap.getCameraPosition().target).title("Ubicación Actual"));
                                defaultLocation = mMap.getCameraPosition().target;

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                        }
                    }
                });
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }//getDeviceLocation
    private BitmapDescriptor bitmapDescriptor(Context applicationContext, int vectorId){
        Drawable drawable = ContextCompat.getDrawable(applicationContext, vectorId);
        drawable.setBounds(0,0,drawable.getIntrinsicHeight(),drawable.getIntrinsicWidth());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }//BitmapDescriptor

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        branch1 = new LatLng(20.681634187181032, -103.36564065137496);
        branch2 = new LatLng(20.68237458517441, -103.33410124718523);
        branch3 = new LatLng(20.664431687170165, -103.39607636807966);
        branches = new ArrayList<>();
        brancheNames = new ArrayList<>();
        brancheNames.add("Sucursal Santa Tere");
        brancheNames.add("Sucursal La Perla");
        brancheNames.add("Sucursal Chapalita");
        branches.add(branch1);
        branches.add(branch2);
        branches.add(branch3);
        //dowtown
        defaultLocation = new LatLng(20.679505604723303, -103.34955953569988);

        return inflater.inflate(R.layout.fragment_branch, container, false);
    }//onCreateView

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
    }//onViewCreated
    private void setBranches(){
        //Branches
        mMap.addMarker(new MarkerOptions().position(branch1).title("Sucursal Santa Tere").icon(bitmapDescriptor(getContext(),R.drawable.bakery)));
        mMap.addMarker(new MarkerOptions().position(branch2).title("Sucursal La Perla").icon(bitmapDescriptor(getContext(),R.drawable.bakery)));
        mMap.addMarker(new MarkerOptions().position(branch3).title("Sucursal Chapalita").icon(bitmapDescriptor(getContext(),R.drawable.bakery)));
    }//setBranches


    private void direction(LatLng origin, LatLng destiny,String branch){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String url = Uri.parse("https://maps.googleapis.com/maps/api/directions/json")
                .buildUpon()
                .appendQueryParameter("destination", destiny.latitude +", "+destiny.longitude)
                .appendQueryParameter("origin", origin.latitude+", "+origin.longitude)
                .appendQueryParameter("mode", "driving")
                .appendQueryParameter("key", key)
                .toString();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.equals("OK")) {
                        JSONArray routes = response.getJSONArray("routes");

                        ArrayList<LatLng> points;
                        PolylineOptions polylineOptions = null;

                        for (int i=0;i<routes.length();i++){
                            points = new ArrayList<>();
                            polylineOptions = new PolylineOptions();
                            JSONArray legs = routes.getJSONObject(i).getJSONArray("legs");

                            for (int j=0;j<legs.length();j++){
                                JSONArray steps = legs.getJSONObject(j).getJSONArray("steps");

                                for (int k=0;k<steps.length();k++){
                                    String polyline = steps.getJSONObject(k).getJSONObject("polyline").getString("points");
                                    List<LatLng> list = PolyUtil.decode(polyline);

                                    for (int l=0;l<list.size();l++){
                                        LatLng position = new LatLng((list.get(l)).latitude, (list.get(l)).longitude);
                                        points.add(position);
                                    }
                                }
                            }
                            polylineOptions.addAll(points);
                            polylineOptions.width(10);
                            polylineOptions.color(ContextCompat.getColor(getContext(), R.color.purple_500));
                            polylineOptions.geodesic(true);
                        }
                        mMap.addPolyline(polylineOptions);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(origin.latitude, origin.longitude)).title("Ubicación seleccionada"));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(destiny.latitude, destiny.longitude)).title(branch).icon(bitmapDescriptor(getContext(),R.drawable.bakery)));

                        LatLngBounds bounds = new LatLngBounds.Builder()
                                .include(new LatLng(origin.latitude, origin.longitude))
                                .include(new LatLng(destiny.latitude, destiny.longitude)).build();
                        Point point = new Point();
                        getActivity().getWindowManager().getDefaultDisplay().getSize(point);
                       // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, point.x, 150, 30));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RetryPolicy retryPolicy = new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(jsonObjectRequest);
    }//direction


    private List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                try {
                    b = encoded.charAt(index++) - 63;
                }catch (Exception e){
                    e.printStackTrace();
                }
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b > 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }//poly
}