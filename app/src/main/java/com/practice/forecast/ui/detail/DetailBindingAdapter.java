package com.practice.forecast.ui.detail;

import android.widget.ImageView;
import android.widget.TextView;

import com.practice.forecast.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.databinding.BindingAdapter;

public class DetailBindingAdapter {

    private static final String TIME_PATTERN = "HH:mm";
    private static final String DAY_PATTERN = "EEEE";

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat();

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, String icon) {
        if (icon != null) {
            switch (icon) {
                case "01d":
                    imageView.setImageResource(R.drawable.icon_01d);
                    break;
                case "02d":
                    imageView.setImageResource(R.drawable.icon_02d);
                    break;
                case "03d":
                    imageView.setImageResource(R.drawable.icon_03d);
                    break;
                case "04d":
                    imageView.setImageResource(R.drawable.icon_04d);
                    break;
                case "09d":
                    imageView.setImageResource(R.drawable.icon_09d);
                    break;
                case "10d":
                    imageView.setImageResource(R.drawable.icon_10d);
                    break;
                case "11d":
                    imageView.setImageResource(R.drawable.icon_11d);
                    break;
                case "13d":
                    imageView.setImageResource(R.drawable.icon_13d);
                    break;
                case "50d":
                    imageView.setImageResource(R.drawable.icon_50d);
                    break;

                case "01n":
                    imageView.setImageResource(R.drawable.icon_01n);
                    break;
                case "02n":
                    imageView.setImageResource(R.drawable.icon_02n);
                    break;
                case "03n":
                    imageView.setImageResource(R.drawable.icon_03n);
                    break;
                case "04n":
                    imageView.setImageResource(R.drawable.icon_04n);
                    break;
                case "09n":
                    imageView.setImageResource(R.drawable.icon_09n);
                    break;
                case "10n":
                    imageView.setImageResource(R.drawable.icon_10n);
                    break;
                case "11n":
                    imageView.setImageResource(R.drawable.icon_11n);
                    break;
                case "13n":
                    imageView.setImageResource(R.drawable.icon_13n);
                    break;
                case "50n":
                    imageView.setImageResource(R.drawable.icon_50n);
                    break;

                case "label":
                    imageView.setImageResource(R.drawable.icon_01d);
                    break;
            }
        }
    }

    @BindingAdapter("android:text")
    public static void setConvertedTime(TextView view, int millis) {
        if (view.getId() == R.id.tvTime) {
            setTextToView(view, R.string.time_label, TIME_PATTERN, millis);
        }

        if (view.getId() == R.id.tvDate) {
            setTextToView(view, R.string.date_label, DAY_PATTERN, millis);
        }
    }

    private static void setTextToView(TextView view, int labelResource, String timePattern, int millis) {
        if (millis == 0) {
            view.setText(labelResource);
        } else {
            SIMPLE_DATE_FORMAT.applyPattern(timePattern);
            SIMPLE_DATE_FORMAT.format(new Date(millis * 1000L));
            view.setText(SIMPLE_DATE_FORMAT.format(new Date(millis * 1000L)));
        }
    }


}
