package ir.dimyadi.persiancalendar.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.names.Adapter;
import ir.dimyadi.persiancalendar.view.names.Adapter.Item;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Names extends Fragment implements OnQueryTextListener {

    private ListView listView;
    private Item[] values;

    @SuppressLint("NewApi")
    private static String normalize(CharSequence str) {
        String string = Normalizer.normalize(str, Normalizer.Form.NFD);
        //string = string.replaceAll("[^\\p{ASCII}]", "");
        return string.toLowerCase(Locale.ENGLISH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_names, container, false);
        Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.asmaallah), "");
        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(android.R.id.list);
        values = new Item[99];

        String[] ar = getResources().getStringArray(R.array.names_ar);
        String[] name = getResources().getStringArray(R.array.names_fa);
        String[] desc = getResources().getStringArray(R.array.names_en);

        for (int i = 0; i < 99; i++) {
            Item item = new Item();
            item.arabic = ar[i];
            if (name.length > i) item.name = name[i];
            if (desc.length > i) item.desc = desc[i];
            values[i] = item;
        }
        listView.setAdapter(new Adapter(getActivity(), values));
        listView.setFastScrollEnabled(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<Item> values = new ArrayList<>();
        for (Item val : this.values) {
            if (normalize(val.toString()).contains(normalize(newText))) {
                values.add(val);
            }
        }

        listView.setAdapter(new Adapter(getActivity(), values.toArray(new Item[values.size()])));
        return false;
    }


}
