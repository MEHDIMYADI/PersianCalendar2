package ir.dimyadi.persiancalendar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;

public class ShapedArrayAdapter<String> extends ArrayAdapter<String> {
    private Utils utils;

    // preferred drop down list item padding, used for spinners and such
    private int padding;

    public ShapedArrayAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
        utils = Utils.getInstance(context);
        padding = (int)getContext().getResources().getDimension(R.dimen.listPreferredItemPadding);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        view = super.getView(position, convertView, parent);
        if (view instanceof TextView) utils.setFontShapeAndGravity((TextView) view);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        if (view instanceof TextView) utils.setFontShapeAndGravity((TextView) view);
        view.setPadding(padding, 0, padding, 0);
        return view;
    }
}
