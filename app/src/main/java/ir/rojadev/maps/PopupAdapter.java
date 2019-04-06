

package ir.rojadev.maps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

class PopupAdapter implements InfoWindowAdapter {
  private View popup=null;
  private LayoutInflater inflater=null;
  private HashMap<String, String> images=null;
  private Context ctxt=null;
  private int iconWidth=-1;
  private int iconHeight=-1;
  private Marker lastMarker=null;

  PopupAdapter(Context ctxt, LayoutInflater inflater,
               HashMap<String, String> images) {
    this.ctxt=ctxt;
    this.inflater=inflater;
    this.images=images;


  }

  @Override
  public View getInfoWindow(Marker marker) {
    return(null);
  }

  @SuppressLint("InflateParams")
  @Override
  public View getInfoContents(Marker marker) {
    if (popup == null) {
      popup=inflater.inflate(R.layout.info_window, null);
    }

    if (lastMarker == null
            || !lastMarker.getId().equals(marker.getId())) {
      lastMarker=marker;

      TextView txtLocality = (TextView) popup.findViewById(R.id.txtLocality);
      ImageView imgDriver = (ImageView) popup.findViewById(R.id.imgdriver);



      txtLocality.setText(marker.getTitle());

      String image=images.get(marker.getId());


      if (image == null) {
        imgDriver.setVisibility(View.GONE);
      }
      else {
        Picasso.with(ctxt)
                .load(image)
                .error(R.drawable.ic_error_black_24dp)
                .placeholder(R.drawable.driver)
                .transform(new CircleTransform())
                .noFade()
                .into(imgDriver, new MarkerCallback(marker));
      }
    }

    return(popup);
  }

  static class MarkerCallback implements Callback {
    Marker marker=null;

    MarkerCallback(Marker marker) {
      this.marker=marker;
    }

    @Override
    public void onError() {
      Log.e(getClass().getSimpleName(), "Error loading thumbnail!");
    }

    @Override
    public void onSuccess() {
      if (marker != null && marker.isInfoWindowShown()) {
        marker.showInfoWindow();


      }
    }
  }
}