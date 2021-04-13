package pl.wsiz.przypominajka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperProfile extends SQLiteOpenHelper {

    private static final String Table_Name = "person_info";
    private static final String COL2 = "name";
    private static final String COL3 = "surname";
    private static final String COL4 = "email";
    private static final String COL5 = "phone";
    private static final String COL6 = "bank";
    private static final String COL7 = "account";

    DatabaseHelperProfile(Context context) {
        super(context, Table_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTbl = "CREATE TABLE " + Table_Name + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " TEXT, " + COL4 + " TEXT, " + COL5 + " TEXT, " + COL6 + " TEXT, " + COL7 + " TEXT)";
        sqLiteDatabase.execSQL(createTbl);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int j) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Table_Name);
        onCreate(sqLiteDatabase);
    }

    boolean addData(String i2, String i3, String i4, String i5, int i6, String i7) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("delete from " + Table_Name);
        ContentValues values = new ContentValues();
        values.put(COL2, i2);
        values.put(COL3, i3);
        values.put(COL4, i4);
        values.put(COL5, i5);
        values.put(COL6, i6);
        values.put(COL7, i7);
        long results = database.insert(Table_Name, null, values);
        return results != -1;
    }

    Cursor getPersonalData() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM " + Table_Name;
        return database.rawQuery(query, null);
    }
}