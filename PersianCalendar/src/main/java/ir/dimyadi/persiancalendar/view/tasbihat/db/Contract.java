package ir.dimyadi.persiancalendar.view.tasbihat.db;

import android.provider.BaseColumns;

public class Contract {

    //common columns names for the two dbs
    public static final String FREE_TASBIH = "free_tasbih";
    public static final String ALLAH_AKBAR = "allah_akbar";
    public static final String ALHAMDULELLAH = "alhamdulellah";
    public static final String SOBHAN_ALLAH = "sobhan_allah";
    public static final String LA_ELAH_ELLA_ALLAH = "la_elah_ella_allah";
    public static final String DATE_TASBIH = "date_tasbih";

    public static class Tasbiha implements BaseColumns {
        public static final String TABLE_NAME = "tasbihah";
    }

    public static class TempTasbiha implements BaseColumns {
        public static final String TABLE_NAME = "demotasbihah";
    }
}
