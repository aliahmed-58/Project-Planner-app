package com.example.projectplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper( Context context) {
        super(context, "projectplanner", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE projects (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, date TEXT, link TEXT, status INTEGER DEFAULT 0)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS projects");
    }

    public Boolean insertData(String name, String desc, String date, String link) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("description", desc);
        contentValues.put("date", date);
        contentValues.put("link", link);

        long results = db.insert("projects", null, contentValues);
        return results != -1;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM projects", null);
    }

    public Cursor getSingleItem (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM projects where name = " + name, null);
    }

    public Integer deleteItem (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("projects", "name = ?", new String[] {name});
    }

    public Integer deleteData (){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("projects", null, null);
    }

    public Boolean setCompleted( String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 1);
        db.update("projects", contentValues, "name = ?", new String[] {name});
        return true;
    }

    public Boolean setUncompleted( String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", 0);
        db.update("projects", contentValues, "name=?", new String[] {name});
        return true;
    }



    public Cursor getProjectsCompleted() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT status FROM projects;", null );
    }

    public Cursor getLastProject() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM projects WHERE id=(SELECT max(id) FROM projects);", null );

    }
}
