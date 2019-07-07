package com.mycode.appforenp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mycode.appforenp.models.MyModel;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String DATABASE_NAME = "list.db";
    private static final String TABLE_NAME = "list_table";
    private static final String ID = "ID";
    private static final String HEADER = "HEADER";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String BITMAP = "BITMAP";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " +
                TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                HEADER + " TEXT, " +
                DESCRIPTION + " TEXT, " +
                BITMAP + " TEXT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean addData(MyModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HEADER, model.getHeadder());
        contentValues.put(DESCRIPTION, model.getDis());
        contentValues.put(BITMAP, model.getBitmap());

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    public boolean updateItem(MyModel model, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HEADER, model.getHeadder());
        contentValues.put(DESCRIPTION, model.getDis());
        contentValues.put(BITMAP, model.getBitmap());


        int update = db.update(TABLE_NAME, contentValues, ID + " = ? ", new String[] {String.valueOf(id)} );

        if(update != 1) {
            return false;
        }
        else{
                return true;
            }
    }



    public Integer deleteItem(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {String.valueOf(id)});
    }

}







