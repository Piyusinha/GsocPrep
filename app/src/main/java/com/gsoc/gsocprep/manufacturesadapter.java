package com.gsoc.gsocprep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gsoc.gsocprep.model.manufacturers;

import java.util.List;

public class manufacturesadapter extends ArrayAdapter<String> {
    private  final LayoutInflater minflator;
    private final Context mContext;
    private final List<manufacturers> cars;
    private final int mResource;

    public manufacturesadapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        minflator = LayoutInflater.from(context);
        mResource = resource;
        cars = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }
    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = minflator.inflate(mResource, parent, false);

        TextView carname = (TextView) view.findViewById(R.id.carmodel);

       String cardata= cars.get(position).getName();

        carname.setText(cardata);


        return view;
    }
}
