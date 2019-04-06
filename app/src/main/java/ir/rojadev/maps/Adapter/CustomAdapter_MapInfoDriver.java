package ir.rojadev.maps.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ir.rojadev.maps.CircleTransform;
import ir.rojadev.maps.Model.Map_info;
import ir.rojadev.maps.R;


public class CustomAdapter_MapInfoDriver extends BaseAdapter
{
    private Context context;
    private ArrayList<Map_info> data = new ArrayList<>();
    private ArrayList<Map_info> tmpData = new ArrayList<>();
    private LayoutInflater inflater = null;

    public CustomAdapter_MapInfoDriver(Context ctx, ArrayList<Map_info> data)
    {
        this.context = ctx;
        this.data = data;
        this.tmpData = data;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public static class ViewHolder
    {
        public TextView txtName;
        public TextView txtPhone;
        public ImageView img;


    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null)
        {
            vi = inflater.inflate(R.layout.row_driver, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) vi.findViewById(R.id.rowItemTxtName);
            holder.txtPhone = (TextView) vi.findViewById(R.id.rowItemTxtPhone);

            holder.img = (ImageView) vi.findViewById(R.id.driverimg);

            vi.setTag(holder);
        }
        else
            holder = (ViewHolder) vi.getTag();

        if (data.size() != 0)
        {
            holder.txtName.setText(data.get(position).getName());
            holder.txtPhone.setText(data.get(position).getTell());

            Picasso.with(context)
                    .load(data.get(position).getPic())
                    .error(R.drawable.ic_error_black_24dp)
                    .placeholder(R.drawable.driver)
                    .transform(new CircleTransform())
                    .into(holder.img);



          //  holder.img.setImageDrawable(Drawable.createFromPath((data.get(position).getPic())));


            holder.txtPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String phoneNo = data.get(position).getTell();
                    String dial = "tel:" + phoneNo;

                    Uri uri = Uri.parse(dial);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    context.startActivity(intent);
                }
            });
        }
        return vi;
    }

}