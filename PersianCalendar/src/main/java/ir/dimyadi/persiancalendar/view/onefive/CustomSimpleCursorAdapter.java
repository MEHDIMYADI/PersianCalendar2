package ir.dimyadi.persiancalendar.view.onefive;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;


class CustomSimpleCursorAdapter extends SimpleCursorAdapter {

    //private Cursor cur;
    CustomSimpleCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        //cur = c;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //if(cur.moveToPosition(position)){
            //String type = cur.getString(cur.getColumnIndex("type"));
            //if(type.equals("Expense")){
            //    v.setBackgroundColor(Color.parseColor("#ff8080"));
            //}
            //else{
            //    v.setBackgroundColor(Color.parseColor("#00b300"));
            //}
        //}
        return super.getView(position, convertView, parent);
    }

    @Override
    public void changeCursor(Cursor cursor) {
        //cur = cursor;
        super.changeCursor(cursor);
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}
