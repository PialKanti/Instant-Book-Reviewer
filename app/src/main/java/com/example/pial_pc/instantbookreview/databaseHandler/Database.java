package com.example.pial_pc.instantbookreview.databaseHandler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Pial-PC on 2/9/2016.
 */
public class Database extends SQLiteOpenHelper {
    SQLiteDatabase database;
    Context context;
    private static final String Logtag = "Message";

    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String Database_Name = "book_review.db";
    //  table name
    public static final String Table_Name = "readlist_table";
    public static final String Table_Name_map = "location_table";
    //table columnsDatabase_Name
    public static final String Column_ID = "_id";
    public static final String Column_ISBN = "isbn";
    public static final String Column_TITLE = "title";
    public static final String Column_AUTHOR = "author";
    public static final String Column_RATING = "rating_score";
    public static final String Column_RATINGSCOUNT = "ratings_count";
    public static final String Column_REVIEWCOUNT = "review_count";
    public static final String Column_LANGUAGE = "language";
    public static final String Column_REVIEW = "review";
    public static final String Column_FORMAT = "format";
    public static final String Column_PAGE = "page";
    public static final String Column_YEAR = "year";
    public static final String Column_MONTH = "month";
    public static final String Column_DAY = "day";
    public static final String Column_PUBLISHER = "publisher";
    public static final String Column_DESCRIPTION = "description";
    public static final String Column_IMAGE_PATH = "image_path";

    //Database Create Query
    private static final String CREATE_QUERY = "CREATE TABLE " + Table_Name + " (" +
            Column_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Column_ISBN + " TEXT, " +
            Column_TITLE + " TEXT, " +
            Column_AUTHOR + " TEXT, " +
            Column_RATING + " TEXT, " +
            Column_RATINGSCOUNT + " TEXT, " +
            Column_REVIEWCOUNT + " TEXT, " +
            Column_LANGUAGE + " TEXT, " +
            Column_REVIEW + " TEXT, " +
            Column_FORMAT + " TEXT, " +
            Column_PAGE + " TEXT, " +
            Column_YEAR + " TEXT, " +
            Column_MONTH + " TEXT, " +
            Column_DAY + " TEXT, " +
            Column_PUBLISHER + " TEXT, " +
            Column_DESCRIPTION + " TEXT, " +
            Column_IMAGE_PATH + " TEXT);";

    //coluumns names for Map Table
    public static final String Column_ID_MAP = "_id";
    public static final String Column_BOOK_STORE = "book_store";
    public static final String Column_BOOK_ISBN = "book_isbn";
    public static final String Column_LAT = "latitude";
    public static final String Column_LONG = "longitude";



    private static final String CREATE_QUERY_MAP = "CREATE TABLE " + Table_Name_map + " (" +
            Column_ID_MAP + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Column_BOOK_STORE + " TEXT, " +
            Column_BOOK_ISBN + " TEXT, " +
            Column_LAT + " TEXT, " +
            Column_LONG + " TEXT);";

    public Database(Context context) {
        super(context, Database_Name, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY); //Create table
        db.execSQL(CREATE_QUERY_MAP);
        /*db.execSQL(Password_Table_QUERY);*/
        Log.i(Logtag, "Table has been created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
