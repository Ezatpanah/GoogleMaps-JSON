package ir.rojadev.maps;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.rojadev.maps.Adapter.CustomAdapter_MapInfoDriver;
import ir.rojadev.maps.Model.Map_info;


public class DriverActivity extends Activity {


    ArrayList<Map_info> info_driver = new ArrayList<>();
    CustomAdapter_MapInfoDriver customAdapter;
    ListView lstDriver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        lstDriver = (ListView) findViewById(R.id.lstContacts);
        customAdapter = new CustomAdapter_MapInfoDriver(DriverActivity.this, info_driver);
        lstDriver.setAdapter(customAdapter);
        lstDriver.setVisibility(View.GONE);

        info_driver.clear();
        sendJsonArrayRequest();

    }




    private void sendJsonArrayRequest()
    {
        String url = "http://rojadev.ir/volly/scmfgs/contractor/map_arry_driver.json";

        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(DriverActivity.this);
        pDialog.setMessage("در حال بارگذاری...");
        pDialog.setCancelable(false);
        pDialog.show();

        Response.Listener<JSONArray> listener = new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject object = response.getJSONObject(i);
                        String mName = object.getString("name");
                        double mlat = object.getDouble("lat_json");
                        double mlng = object.getDouble("lng_json");
                        String mTell = object.getString("tell");
                        String mAdrs = object.getString("adrs");
                        String mPic = object.getString("pic");

                        info_driver.add(new Map_info(mName, mlat, mlng,mTell,mAdrs,mPic));
                    }
                }
                catch (Exception e)
                {

                }
                pDialog.dismiss();
                lstDriver.setVisibility(View.VISIBLE);
                ((BaseAdapter) lstDriver.getAdapter()).notifyDataSetChanged();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(DriverActivity.this, "خطا در دریافت اطلاعات", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        };

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, listener, errorListener);
        RequestQueue queue= Volley.newRequestQueue(DriverActivity.this);
        queue.add(request);

    }

}