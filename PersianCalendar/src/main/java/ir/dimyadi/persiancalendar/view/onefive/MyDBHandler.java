package ir.dimyadi.persiancalendar.view.onefive;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "onefive.db";

    private final String TABLE_TRANSACTION = "transactions";
    final String COLUMN_ID = "_id";
    final String COLUMN_TITLE = "title";
    final String COLUMN_DESC = "description";
    final String COLUMN_TYPE = "type";
    final String COLUMN_CATEGORY = "category_id";
    final String COLUMN_AMOUNT = "amount";
    final String COLUMN_DATE = "tdate";

    private final String TABLE_CATEGORY = "category";
    final String COLUMN_CAT_NAME = "catname";
    private final String COLUMN_CAT_ID = "cat_id";

    private final String TABLE_TODOLIST = "todolist";

    private final String TABLE_NOTES = "notes";


    MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //expense manager tables
        ////////////
        //////////////////////////////
        ////////////////////////////////////////////
        String query = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT , "
                + COLUMN_DESC + " TEXT, "
                + COLUMN_TYPE + " TEXT, "
                + COLUMN_CATEGORY + " INTEGER, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DATE + " DATE"
                + "); ";

        db.execSQL(query);

        query = "create table " + TABLE_CATEGORY + "("
                + COLUMN_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CAT_NAME + " REAL );";

        db.execSQL(query);


        //addCategory("Misc",db);
        //addCategory("Automobile",db);
        //addCategory("Nice",db);

        //notes and to dolist table
        ///////
        /////////////////////
        ////////////////////////////////
        ///////////////////////////////////////////

        String COLUMN_TASK_ID = "_id";
        String COLUMN_TASK = "task";
        String COLUMN_DNT = "dnt";
        String COLUMN_STATUS = "status";
        query = "create table " + TABLE_TODOLIST + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK + " TEXT, "
                + COLUMN_DNT + " TEXT, "
                + COLUMN_STATUS + " INTEGER"
                + "); ";
        db.execSQL(query);

        String COLUMN_NOTE_ID = "_id";
        String COLUMN_NOTE_TITLE = "title";
        String COLUMN_NOTE = "note";
        String COLUMN_DT = "dnt";
        query = "create table " + TABLE_NOTES + " ("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOTE_TITLE + " TEXT, "
                + COLUMN_NOTE + " TEXT, "
                + COLUMN_DT + " TEXT"
                + "); ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    //no use double because it show 2E3 i like show 2000, so long is better
    Cursor addExpense(String title, String desc, String type, double amount, int cat, String date) {
        //Cursor addExpense(String title, String desc, String type, long amount, int cat, String date){

        Cursor c = null;
        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_DESC, desc);
            values.put(COLUMN_TYPE, type);
            values.put(COLUMN_AMOUNT, amount);
            values.put(COLUMN_CATEGORY, cat);
            values.put(COLUMN_DATE, date);

            long id = db.insert(TABLE_TRANSACTION, null, values);
            db.close();

            db = getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_TRANSACTION + " LEFT OUTER JOIN " + TABLE_CATEGORY + " "
                    + "ON " + COLUMN_CAT_ID + "=" + COLUMN_CATEGORY
                    + " WHERE " + COLUMN_ID + "=" + id;

            c = db.rawQuery(query, null);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    Cursor getQueryExpense(String s) {
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " LEFT OUTER JOIN " + TABLE_CATEGORY
                + " ON " + COLUMN_CAT_ID + "=" + COLUMN_CATEGORY + " "
                + s
                + " order by (substr(tdate,7,4)||'-'||substr(tdate,4,2)||'-'||substr(tdate,1,2)) desc";

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(query, null);
    }


    //no use double because it show 2E3 i like show 2000, so long is better
    boolean updateExpense(int searchid, String title, String desc, double amount, int cat, String date) {
        //boolean updateExpense(int searchid, String title, String desc, long amount, int cat, String date){
        boolean b;

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_DESC, desc);
            values.put(COLUMN_AMOUNT, amount);
            values.put(COLUMN_CATEGORY, cat);
            values.put(COLUMN_DATE, date);

            db.update(TABLE_TRANSACTION, values, COLUMN_ID + "=" + searchid, null);
            db.close();
            b = true;
        } catch (Exception e) {
            b = false;
        }

        return b;
    }

    boolean deleteExpense(int itemid) {
        boolean check;

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_TRANSACTION, COLUMN_ID + "=" + itemid, null);
            check = true;
        } catch (Exception e) {
            check = false;
        }

        return check;
    }

    Cursor getAllCategories() {
        String query = "SELECT " + COLUMN_CAT_ID + " as _id," + COLUMN_CAT_NAME + " FROM " + TABLE_CATEGORY;

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(query, null);
    }

    void addCategory(String cat, SQLiteDatabase db) {
        if (db == null) {
            db = getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_CAT_NAME, cat);
        db.insert(TABLE_CATEGORY, null, values);
    }

    boolean deleteCategory(int s) {
        boolean check;

        try {
            SQLiteDatabase db = getWritableDatabase();
            db.delete(TABLE_TRANSACTION, COLUMN_CATEGORY + "=" + s + "", null);
            db.delete(TABLE_CATEGORY, COLUMN_CAT_ID + "=" + s + "", null);
            check = true;
        } catch (Exception e) {
            check = false;
        }

        return check;
    }
}
