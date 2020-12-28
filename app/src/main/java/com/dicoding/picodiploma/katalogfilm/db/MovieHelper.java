package com.dicoding.picodiploma.katalogfilm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.dicoding.picodiploma.katalogfilm.db.DatabaseContract.MovieColumns.ID;
import static com.dicoding.picodiploma.katalogfilm.db.DatabaseContract.TABLE_FAVORITE_MOVIE;

public class MovieHelper {
    private static String DATABASE_TABLE = TABLE_FAVORITE_MOVIE;
    private Context context;
    private DatabaseHelper dataBaseHelper;

    private SQLiteDatabase database;

    public MovieHelper(Context context){
        this.context = context;
    }

    public MovieHelper open() throws SQLException {
        dataBaseHelper = new DatabaseHelper(context);
        database = dataBaseHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dataBaseHelper.close();
    }

    public int delete(int id){
        return database.delete(TABLE_FAVORITE_MOVIE, ID + " = '"+id+"'", null);
    }

    public boolean isMovieFavorite(String id){

        String query = "SELECT * FROM "+DatabaseContract.TABLE_FAVORITE_MOVIE+" WHERE id like '"+id+"'";
        boolean isMovieFavorite;

        Cursor row  = database.rawQuery(query, null);
        if (row.moveToFirst() && row.getCount() > 0){
            isMovieFavorite = true;
        } else {
            isMovieFavorite = false;
        }

        return isMovieFavorite;
    }

    public Cursor queryByIdProvider(String id){
        return database.query(DATABASE_TABLE,null
                ,ID + " = ?"
                ,new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public Cursor queryProvider(){
        return database.query(DATABASE_TABLE
                ,null
                ,null
                ,null
                ,null
                ,null
                ,ID + " DESC");
    }

    public long insertProvider(ContentValues values){
        return database.insert(DATABASE_TABLE,null,values);
    }

    public int updateProvider(String id,ContentValues values){
        return database.update(DATABASE_TABLE,values,_ID +" = ?",new String[]{id} );
    }

    public int deleteProvider(String id){
        return database.delete(DATABASE_TABLE,ID + " = ?", new String[]{id});
    }
}
