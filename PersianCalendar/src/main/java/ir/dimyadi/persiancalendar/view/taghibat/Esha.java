package ir.dimyadi.persiancalendar.view.taghibat;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.Constants;
import ir.dimyadi.persiancalendar.R;

public class Esha extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taghibat, container, false);

        TextView textTaghib = (TextView) view.findViewById(R.id.athan_taghib);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), Constants.FONT_TAGHIBAT);
        textTaghib.setTypeface(type);
        textTaghib.setMovementMethod(new ScrollingMovementMethod());

        textTaghib.setText(getString(R.string.tisha));
        return view;
    }
}