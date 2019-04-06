package ir.rojadev.maps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.rojadev.maps.Model.Map_info;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marker;

    ArrayList<Map_info> mapInfo = new ArrayList<>();

    private Map<Marker, Map_info> allMarkersMap = new HashMap<Marker, Map_info>();
    private HashMap<String, String> images=new HashMap<String, String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MapUtils.checkPlayServices(this)) {
            setContentView(R.layout.activity_marker);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            Button btn = (Button) findViewById(R.id.button2);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent GoActivity = new Intent (MainActivity.this,DriverActivity.class);
                    startActivity(GoActivity);

                }
            });

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        String url = "http://emadkeyvani.ir/volly/scmfgs/contractor/map_arry_driver.json";

        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("بارگذاری...");
        pDialog.setCancelable(false);
        pDialog.show();

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String mName = object.getString("name");
                        double mlat = object.getDouble("lat_json");
                        double mlng = object.getDouble("lng_json");
                        String mTell = object.getString("tell");
                        String mAdrs = object.getString("adrs");
                        String mPic = object.getString("pic");


                        //mapInfo.add(new Map_info(mName, mlat, mlng,mTell,mAdrs,mPic));
                        Map_info wInfo = new Map_info(mName, mlat, mlng,mTell,mAdrs,mPic);

                        LatLng Home = new LatLng(mlat, mlng);

                        Marker marker =  mMap.addMarker(new MarkerOptions().position(Home).title(mName));
                        // Changing marker icon
                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                        allMarkersMap.put(marker, wInfo);
                        images.put(marker.getId(), mPic);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Home, 15));
                    }
                } catch (Exception e) {

                }
                pDialog.dismiss();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);


        mMap.setInfoWindowAdapter(new PopupAdapter(MainActivity.this,
                getLayoutInflater(),
                images));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String msg = marker.getTitle() + " (" +
                        marker.getPosition().latitude + ", " +
                        marker.getPosition().longitude + ")";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder gc = new Geocoder(MainActivity.this);
                List<Address> list = null;
                try {
                    list = gc.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
                    if (list.size() > 0) {
                        Address add = list.get(0);
                        marker.setTitle(add.getLocality());
                        marker.setSnippet(add.getCountryName());
                        marker.showInfoWindow();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "onClick", Toast.LENGTH_SHORT).show();

                Map_info myDataObj = allMarkersMap.get(marker);
                String phoneNo = myDataObj.getTell();
                String dial = "tel:" + phoneNo;

                Uri uri = Uri.parse(dial);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(intent);
            }
        });

        mMap.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {
            }
        });

        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
            }
        });

    }




}
