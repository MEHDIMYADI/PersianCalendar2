package ir.dimyadi.persiancalendar.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.help.widget.AccordionView;


public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        final Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.help), "");

        final AccordionView v = (AccordionView) view.findViewById(R.id.accordion_view);

        //LinearLayout ll = (LinearLayout) v.findViewById(R.id.help_0);
        //TextView tv = new TextView(getActivity());
        //tv.setText("Added in runtime...");
        //ll.addView(tv);

        return view;
    }
}