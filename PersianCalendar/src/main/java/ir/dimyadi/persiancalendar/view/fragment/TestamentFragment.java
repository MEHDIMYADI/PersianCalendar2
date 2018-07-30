package ir.dimyadi.persiancalendar.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ir.dimyadi.persiancalendar.R;
import ir.dimyadi.persiancalendar.util.Utils;
import ir.dimyadi.persiancalendar.view.tasbihat.CurrentShamsidate;
import ir.dimyadi.persiancalendar.view.testament.DisplayTestament;
import ir.dimyadi.persiancalendar.view.testament.MyDBHandler;
import ir.dimyadi.persiancalendar.view.testament.MyTestament;
import ir.dimyadi.persiancalendar.view.testament.RecyclerTouchListener;
import ir.dimyadi.persiancalendar.view.testament.TestamentAdapter;


public class TestamentFragment extends Fragment {

    private RecyclerView recyclerView;
    private TestamentAdapter adapter;
    private List<MyTestament> notesList;
    public MyDBHandler dbHandler;
    private int flag;
    public int pos;

    Calendar calendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_testament, container, false);
        Utils utils = Utils.getInstance(getContext());
        utils.setActivityTitleAndSubtitle(getActivity(), getString(R.string.testament), "");

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        notesList = new ArrayList<>();


        adapter = new TestamentAdapter(getActivity(),notesList);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        dbHandler = new MyDBHandler(getContext(),null,null,1);
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

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Movie movie = movieList.get(position);
                MyTestament note = notesList.get(position);
                Intent i = new Intent(TestamentFragment.this.getActivity(), DisplayTestament.class);
                i.putExtra("TITLE",note.getTitle());
                i.putExtra("NOTE", note.getNote());
                i.putExtra("POSITION",Integer.toString(position));
                startActivityForResult(i, 2);
                //Toast.makeText(getContext(), note.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(TestamentFragment.this.getActivity());

                // Setting Dialog Title
                alertDialog.setTitle("  " + getString(R.string.delete_note));

                // Setting Dialog Message
                alertDialog.setMessage(getString(R.string.delete_note_sure));

                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_delete);
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
                        int check = dbHandler.deleteNote(notesList.get(getPosition()).getDt());
                        if(check==0)
                            Toast.makeText(getActivity(), getString(R.string.cannot_delete), Toast.LENGTH_SHORT).show();
                        else{
                            //Toast.makeText(getActivity(), "Task deleted !", Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(recyclerView, notesList.get(getPosition()).getTitle()+" "+getString(R.string.deleted), Snackbar.LENGTH_SHORT);
                            snackbar.show();

                        }
                        notesList.remove(getPosition());
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                        //AlertDialog
                        dialog.dismiss();
                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        }));


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.action_testament_add, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_testament:
                Intent i = new Intent(TestamentFragment.this.getActivity(), DisplayTestament.class);
                i.putExtra("TITLE","init");
                i.putExtra("NOTE", "init");
                startActivityForResult(i, 1);
                //startActivityForResult(new Intent(TestamentFragment.this.getActivity(), DisplayTestament.class),1);
                return false;
            default:
                break;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String response = data.getStringExtra("IsDELETED");
        final Utils utils = Utils.getInstance(getContext());

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        CurrentShamsidate ct = new CurrentShamsidate(year,month,day);
        String currentTimeString = DateFormat.getTimeInstance().format(new Date());

        if(requestCode == 1) //create new note
            {
                if(response.equals("n")){
                    String fnote = data.getStringExtra("NOTE");
                    String ftitle = data.getStringExtra("TITLE");
                    addNotes(fnote, ftitle, currentTimeString + " " +  utils.formatNumber(ct.getIranianDate()), 1);
                }
                if(response.equals("y")){
                //do nothing
                }
            }
            if(requestCode == 2)  //update existing note
            {
                int position = Integer.parseInt(data.getStringExtra("POSITION"));
                if(response.equals("n")){
                    String fnote = data.getStringExtra("NOTE");
                    String ftitle = data.getStringExtra("TITLE");
                    int check = dbHandler.updateNote(ftitle.toUpperCase(),fnote, notesList.get(position).getDt());
                    if(check==0)
                        Toast.makeText(getActivity(), getString(R.string.cannot_update), Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(getActivity(), getString(R.string.will_updated), Toast.LENGTH_SHORT).show();
                        notesList.set(position, new MyTestament(ftitle.toUpperCase(), fnote, currentTimeString + " " + utils.formatNumber(ct.getIranianDate())));
                        adapter.notifyDataSetChanged();
                    }
                }
                if(response.equals("y")){
                    pos = position;
                    int check = dbHandler.deleteNote(notesList.get(position).getDt());
                    if(check==0)
                        Toast.makeText(getActivity(),  getString(R.string.cannot_delete), Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(getActivity(), getString(R.string.will_deleted), Toast.LENGTH_SHORT).show();
                        Snackbar snackbar = Snackbar.make(recyclerView, notesList.get(position).getTitle()+" "+getString(R.string.deleted), Snackbar.LENGTH_SHORT);
                        snackbar.show();

                    }
                    notesList.remove(position);
                    adapter.notifyDataSetChanged();
                }
        }
    }

    public int getPosition()
    {
        return pos;
    }


    private void deleteNotes(int position) {
        int check = dbHandler.deleteNote(notesList.get(position).getDt());
        if (check == 0) {
            Toast.makeText(getActivity(), getString(R.string.cannot_delete), Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "Task deleted !", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(recyclerView, notesList.get(position).getTitle()+" "+getString(R.string.deleted), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        notesList.remove(position);
        adapter.notifyDataSetChanged();
    }

    private void addNotes(String note, String title, String dt, int flag){
        if(flag==1) {
            boolean check = dbHandler.add_dbNote(title, note, dt);
            if (check) {
                //Toast.makeText(getContext(), "Data Added", Toast.LENGTH_LONG).show();
                MyTestament myNote = new MyTestament(title.toUpperCase(), note, dt);
                notesList.add(myNote);
            } else {
                Toast.makeText(getContext(), getString(R.string.testament_add_error), Toast.LENGTH_LONG).show();
            }
        } else {
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.testament));
        registerForContextMenu(recyclerView);
    }

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

