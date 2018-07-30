package ir.dimyadi.persiancalendar.view.testament;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ir.dimyadi.persiancalendar.R;

public class TestamentSOSActivity extends AppCompatActivity {

    private TestamentAdapter adapter;
    private List<MyTestament> notesList;
    public MyDBHandler dbHandler;
    private int flag;
    public int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testament);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        notesList = new ArrayList<>();


        adapter = new TestamentAdapter(this,notesList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        dbHandler = new MyDBHandler(TestamentSOSActivity.this,null,null,1);
        //addNotes();

        //Toast.makeText(getContext(), "initiating", Toast.LENGTH_SHORT).show();
        try{
            //Toast.makeText(getContext(), "inside try", Toast.LENGTH_SHORT).show();
            Cursor c = dbHandler.getAllNotes();
            //Toast.makeText(getContext(), "value of c = ", Toast.LENGTH_SHORT).show();

            if(c!=null)
            {
                //Toast.makeText(getContext(), "c!=Null", Toast.LENGTH_SHORT).show();
                if(c.moveToFirst()){
                    do{
                        String db_title = c.getString(c.getColumnIndex(dbHandler.COLUMN_NOTE_TITLE));
                        String db_note = c.getString(c.getColumnIndex(dbHandler.COLUMN_NOTE));
                        String db_dt = c.getString(c.getColumnIndex(dbHandler.COLUMN_DT));
                        addNotes(db_note, db_title, db_dt, 0);
                    }while (c.moveToNext());
                }
            }
        }catch (SQLException se){
            Log.e(getClass().getSimpleName(), "Could not create or delete database");
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(TestamentSOSActivity.this, recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Movie movie = movieList.get(position);
                MyTestament note = notesList.get(position);
                Intent i = new Intent(TestamentSOSActivity.this, TestamentSOSDisplay.class);
                i.putExtra("TITLE",note.getTitle());
                i.putExtra("NOTE", note.getNote());
                i.putExtra("POSITION",Integer.toString(position));
                startActivityForResult(i, 2);
                //Toast.makeText(getContext(), note.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
//long press
            }
        }));

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                //none action
                                break;
                            case R.id.action_item2:
                                //none action
                                break;
                            case R.id.action_item3:
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }

    public int getPosition()
    {
        return pos;
    }

    private void addNotes(String note, String title, String dt, int flag){
        if(flag==1) {
            boolean check = dbHandler.add_dbNote(title, note, dt);
            if (check) {
                //Toast.makeText(getContext(), "Data Added", Toast.LENGTH_LONG).show();
                MyTestament myNote = new MyTestament(title.toUpperCase(), note, dt);
                notesList.add(myNote);
            } else {
                Toast.makeText(TestamentSOSActivity.this, getString(R.string.testament_add_error), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            MyTestament myNote = new MyTestament(title.toUpperCase(), note, dt);
            notesList.add(myNote);
        }




        /*myNote = new MyTestament("Title2", "huvbjwdsmkcnjeidwml smcjfehiwjdkns mcjbfehijdskm cjbdbl,.d ,vl,vdfehijksncjfehij", DateFormat.getDateTimeInstance().format(new Date()));
        notesList.add(myNote);

        myNote = new MyTestament("Title3", "huvbjwdsmkcnjeidwmkcl,.xl smcjfehiwjdkns mcjbfehijdskm cjbdbl,.d ,vl,vdfehijksncjfehij", DateFormat.getDateTimeInstance().format(new Date()));
        notesList.add(myNote);

        myNote = new MyTestament("Title4", "qwertyuiopasdfghjklzxcvbnm", DateFormat.getDateTimeInstance().format(new Date()));
        notesList.add(myNote);

        myNote = new MyTestament("Title5", "huvbjwdsmkcnjmx,eidwml smcjfehiwjdkns mcjbfehijdskm cjbdbl,.d ,vl,vdfehijksncjfehij", DateFormat.getDateTimeInstance().format(new Date()));
        notesList.add(myNote);*/

        adapter.notifyDataSetChanged();


    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    //@Override
    //public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    //    super.onViewCreated(view, savedInstanceState);
    //    this.setTitle("Notes");
    //    registerForContextMenu(recyclerView);
    //}

    /*public static class AlertBoxFragment extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.ic_delete_note)
                    .setTitle("Kar dein fir delete ???")
                    .setMessage("Are you sure a**hole ?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something else
                        }
                    }).create();

            }
        }*/
    }

