package edu.birzeit.zamilihotal.adabter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import edu.birzeit.androidprojectzamili.R;
import edu.birzeit.zamilihotal.model.SpinnerItem;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    private final Context context;
    private final List<SpinnerItem> data;

    public SpinnerAdapter(Context context, List<SpinnerItem> data) {
        super(context, R.layout.spinner_item, data);
        this.context = context;
        this.data = data;
    }



    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, parent);
    }

    private View createItemView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.spinner_item, parent, false);

        ImageView imageView = view.findViewById(R.id.icon);
        TextView textView = view.findViewById(R.id.text);

        imageView.setImageResource(data.get(position).getDrawable());
        textView.setText(data.get(position).getText());

        return view;
    }

}
