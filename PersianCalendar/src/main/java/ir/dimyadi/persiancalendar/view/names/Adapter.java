package ir.dimyadi.persiancalendar.view.names;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.view.names.Adapter.Item;

public class Adapter extends ArrayAdapter<Item> {

    public Adapter(Context context, Item[] objects) {
        super(context, 0, objects);


    }


    @NonNull
    @Override
    public View getView(int pos, View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.names_item, parent, false);
            vh = new ViewHolder();
            vh.name = (TextView) convertView.findViewById(R.id.name);
            vh.arabicImg = (ImageView) convertView.findViewById(R.id.arabicImg);
            vh.arabic = (TextView) convertView.findViewById(R.id.arabicTxt);
            vh.meaning = (TextView) convertView.findViewById(R.id.meaning);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }


        Item i = getItem(pos);
        if (pos == 0) {
            vh.arabicImg.setVisibility(View.VISIBLE);
            vh.arabicImg.setImageResource(R.drawable.allah);
            vh.name.setVisibility(View.GONE);
            vh.arabic.setVisibility(View.GONE);
        } else {
            vh.arabicImg.setImageDrawable(null);
            vh.arabicImg.setVisibility(View.GONE);
            assert i != null;
            vh.arabic.setText(i.arabic);
            if (i.name == null) {
                vh.name.setVisibility(View.GONE);
            } else {
                vh.name.setText(i.name);
                vh.name.setVisibility(View.VISIBLE);
            }
            vh.arabic.setVisibility(View.VISIBLE);
        }

        assert i != null;
        if (i.desc == null) {
            vh.meaning.setVisibility(View.GONE);
        } else {
            vh.meaning.setText(i.desc);
        }

        return convertView;
    }

    public static class Item {
        public String arabic;
        public String name;
        public String desc;

        @Override
        public String toString() {
            return arabic + " " + name + " " + desc;
        }
    }

    private static class ViewHolder {
        TextView name;
        TextView meaning;
        TextView arabic;
        ImageView arabicImg;
    }
}