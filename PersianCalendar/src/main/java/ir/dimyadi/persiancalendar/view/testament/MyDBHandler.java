package ir.dimyadi.persiancalendar.view.testament;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.util.Date;


public class MyDBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION =1;
    private static final String DATABASE_NAME ="testament.db";

    private final String TABLE_TRANSACTION ="transactions";

    private final String TABLE_CATEGORY ="category";

    private final String TABLE_TODOLIST = "todolist";

    private final String TABLE_NOTES = "notes";
    public final String COLUMN_NOTE_TITLE = "title";
    public final String COLUMN_NOTE = "note";
    public final String COLUMN_DT = "dnt";


    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //expense manager tables
        ////////////
        //////////////////////////////
        ////////////////////////////////////////////
        String COLUMN_ID = "_id";
        String COLUMN_TITLE = "title";
        String COLUMN_DESC = "description";
        String COLUMN_TYPE = "type";
        String COLUMN_CATEGORY = "category_id";
        String COLUMN_AMOUNT = "amount";
        String COLUMN_DATE = "tdate";
        String query = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT , "
                + COLUMN_DESC + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_CATEGORY + " INTEGER, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DATE + " DATE"
                +"); "

                ;

        db.execSQL(query);

        String COLUMN_CAT_ID = "cat_id";
        String COLUMN_CAT_NAME = "catname";
        query = "create table "+TABLE_CATEGORY + "("
                + COLUMN_CAT_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CAT_NAME + " REAL );";

        db.execSQL(query);


       // addCategory("Misc",db);
       // addCategory("Automobile",db);
       // addCategory("Nice",db);

        //notes and to dolist table
        ///////
        /////////////////////
        ////////////////////////////////
        ///////////////////////////////////////////

        String COLUMN_TASK_ID = "_id";
        String COLUMN_TASK = "task";
        String COLUMN_DNT = "dnt";
        String COLUMN_STATUS = "status";
        query = "create table "+TABLE_TODOLIST + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK + " TEXT, "
                + COLUMN_DNT + " TEXT, "
                + COLUMN_STATUS + " INTEGER"
                +"); ";
        db.execSQL(query);

        String COLUMN_NOTE_ID = "_id";
        query = "create table "+TABLE_NOTES+" ("
                + COLUMN_NOTE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +COLUMN_NOTE_TITLE+" TEXT, "
                +COLUMN_NOTE+" TEXT, "
                +COLUMN_DT+" TEXT"
                +"); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_TODOLIST);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTES);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }

    public boolean add_dbNote(String title, String note, String dt){
        boolean b;
        try{
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_NOTE, note);
            values.put(COLUMN_NOTE_TITLE, title);
            values.put(COLUMN_DT, dt);

            db.insert(TABLE_NOTES, null, values);
            db.close();
            b = true;
        }catch (Exception e){
            b = false;
        }
        return b;
    }

    public Cursor getAllNotes(){
        String query = "SELECT * FROM "+TABLE_NOTES;
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);
    }

    public int deleteNote(String dt)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NOTES, COLUMN_DT+" =?", new String[]{dt});
    }

    public int updateNote(String title, String note, String dt){
        int result;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, title);
        values.put(COLUMN_NOTE, note);
        values.put(COLUMN_DT, DateFormat.getDateTimeInstance().format(new Date()));
        result = db.update(TABLE_NOTES, values, COLUMN_DT+" =?", new String[]{dt});
        return result;
    }

}
