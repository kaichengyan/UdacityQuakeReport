package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by studentuser on 9/9/16.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {


    public EarthquakeAdapter(Context context, List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_earthquake, parent, false);
        }

        Earthquake earthquake = getItem(position);

        TextView tvMagnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        TextView tvLocationOffset = (TextView) listItemView.findViewById(R.id.location_offset);
        TextView tvPrimaryLocation = (TextView) listItemView.findViewById(R.id.primary_location);

        TextView tvDate = (TextView) listItemView.findViewById(R.id.date);
        TextView tvTime = (TextView) listItemView.findViewById(R.id.time);

        double mag = earthquake.getMagnitude();
        tvMagnitude.setText(formatMagnitude(mag));
        GradientDrawable magnitudeCircle = (GradientDrawable) tvMagnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(mag);
        magnitudeCircle.setColor(magnitudeColor);

        String location = earthquake.getLocation();
        tvLocationOffset.setText(formatLocationOffset(location));
        tvPrimaryLocation.setText(formatPrimaryLocation(location));

        Date date = new Date(earthquake.getTimeInMilliseconds());
        tvDate.setText(formatDate(date));
        tvTime.setText(formatTime(date));

        return listItemView;
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(date);
    }

    private String formatTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(date);
    }

    private String formatLocationOffset(String location) {
        if (location.contains(" of ")) {
            return location.substring(0, location.indexOf(" of ") + 3);
        }
        return "Near the";
    }

    private String formatPrimaryLocation(String location) {
        if (location.contains(" of ")) {
            return location.substring(location.indexOf(" of ") + 4);
        } else {
            return location;
        }
    }

    private String formatMagnitude(double mag) {
        DecimalFormat decFormat = new DecimalFormat("0.0");
        return decFormat.format(mag);
    }

    private int getMagnitudeColor(double mag) {
        int magFloor = (int) mag;
        int colorResId;
        switch (magFloor) {
            case 0:
            case 1:
                colorResId = R.color.magnitude1;
                break;
            case 2:
                colorResId = R.color.magnitude2;
                break;
            case 3:
                colorResId = R.color.magnitude3;
                break;
            case 4:
                colorResId = R.color.magnitude4;
                break;
            case 5:
                colorResId = R.color.magnitude5;
                break;
            case 6:
                colorResId = R.color.magnitude6;
                break;
            case 7:
                colorResId = R.color.magnitude7;
                break;
            case 8:
                colorResId = R.color.magnitude8;
                break;
            case 9:
                colorResId = R.color.magnitude9;
                break;
            default:
                colorResId = R.color.magnitude10plus;
        }
        return ContextCompat.getColor(getContext(), colorResId);
    }


}
