package com.example.pial_pc.instantbookreview.databaseHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Pial-PC on 2/11/2016.
 */
public class DBadapter {
    private Database database;
    private Context context;
    private SQLiteDatabase db;

    public DBadapter(Context context) {
        this.context = context;
        database = new Database(context);
        db = database.getWritableDatabase();
    }

    public void insertMsg(ArrayList<String> book) {
        ContentValues values = new ContentValues();
        values.put(database.Column_ISBN, book.get(0));
        values.put(database.Column_TITLE, book.get(1));
        values.put(database.Column_AUTHOR, book.get(2));
        values.put(database.Column_RATING, book.get(4));
        values.put(database.Column_RATINGSCOUNT, book.get(7));
        values.put(database.Column_REVIEWCOUNT, book.get(8));
        values.put(database.Column_LANGUAGE, book.get(5));
        values.put(database.Column_REVIEW, book.get(6));
        values.put(database.Column_FORMAT, book.get(9));
        values.put(database.Column_PAGE, book.get(10));
        values.put(database.Column_YEAR, book.get(11));
        values.put(database.Column_MONTH, book.get(12));
        values.put(database.Column_DAY, book.get(13));
        values.put(database.Column_PUBLISHER, book.get(14));
        values.put(database.Column_DESCRIPTION, book.get(3));
        values.put(database.Column_IMAGE_PATH, book.get(15));

        long insert = db.insert(database.Table_Name, null, values);

    }

    public void insertMsgINTOmap(String book_store, String isbn, String latitude, String longitude) {
        ContentValues values = new ContentValues();
        values.put(database.Column_BOOK_STORE, book_store);
        values.put(database.Column_BOOK_ISBN, isbn);
        values.put(database.Column_LAT, latitude);
        values.put(database.Column_LONG, longitude);

        long insert = db.insert(database.Table_Name_map, null, values);

    }

    public int MapTableSize() {
        String size_query = "SELECT DISTINCT (" + database.Column_BOOK_ISBN + ") FROM " + database.Table_Name_map;
        Cursor cursor = db.rawQuery(size_query, null);

        return cursor.getCount();

    }

    public ArrayList<String[]> getBookStore(String isbn) {
        ArrayList<String[]> book_store = new ArrayList<String[]>();
        String select_date_query = "SELECT " +
                database.Column_BOOK_STORE + "," + database.Column_LAT + "," + database.Column_LONG +
                " FROM " + database.Table_Name_map + " WHERE "
                + database.Column_BOOK_ISBN + "=" + "'" + isbn + "'";
        Cursor cursor = db.rawQuery(select_date_query, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String[] temp = new String[3];
                temp[0] = cursor.getString(cursor.getColumnIndex(database.Column_BOOK_STORE));
                temp[1] = cursor.getString(cursor.getColumnIndex(database.Column_LAT));
                temp[2] = cursor.getString(cursor.getColumnIndex(database.Column_LONG));

                book_store.add(temp);
                cursor.moveToNext();
            }
        }

        return book_store;
    }


    public boolean CheckBook(String isbn) {
        String size_query = "SELECT DISTINCT (" + database.Column_TITLE + ") FROM " + database.Table_Name + " WHERE " + database.Column_ISBN + "=" + "'" + isbn + "'";
        Cursor cursor = db.rawQuery(size_query, null);

        if (cursor.getCount() > 0)
            return true;
        return false;

    }

    public void deleteBook(String isbn) {
        db.execSQL("DELETE FROM " + database.Table_Name + " WHERE " + database.Column_ISBN + "=" + "'" + isbn + "'");
    }

    public String[] getBookISBN() {
        String[] DistinctISBN = null;
        String select_number_query = "SELECT DISTINCT (" + database.Column_ISBN + ") FROM " + database.Table_Name + " ORDER BY " + database.Column_ID + " DESC ";
        ;
        Cursor cursor = db.rawQuery(select_number_query, null);
        if (cursor != null && cursor.getCount() > 0) {
            int size = cursor.getCount();
            DistinctISBN = new String[size];
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                DistinctISBN[i] = cursor.getString(cursor.getColumnIndex(database.Column_ISBN));
                cursor.moveToNext();
            }
        }
        return DistinctISBN;
    }

    public String[] getBookTitle(String[] isbn) {
        String[] bookTitle = new String[isbn.length];
        for (int count = 0; count < isbn.length; count++) {
            String select_date_query = "SELECT " +
                    database.Column_TITLE +
                    " FROM " + database.Table_Name +
                    " WHERE " + database.Column_ISBN + "=" + "'" +
                    isbn[count] + "'" + " ORDER BY " + database.Column_ID + " DESC ";
            Cursor cursor = db.rawQuery(select_date_query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                bookTitle[count] = cursor.getString(cursor.getColumnIndex(database.Column_TITLE));
            }

        }
        return bookTitle;
    }

    public String[] getBookRatings(String[] isbn) {
        String[] bookRating = new String[isbn.length];
        for (int count = 0; count < isbn.length; count++) {
            String select_date_query = "SELECT " +
                    database.Column_RATING +
                    " FROM " + database.Table_Name +
                    " WHERE " + database.Column_ISBN + "=" + "'" +
                    isbn[count] + "'" + " ORDER BY " + database.Column_ID + " DESC ";
            Cursor cursor = db.rawQuery(select_date_query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                bookRating[count] = cursor.getString(cursor.getColumnIndex(database.Column_RATING));
            }

        }
        return bookRating;
    }

    public String[] getBookImagePath(String[] isbn) {
        String[] bookImagePath = new String[isbn.length];
        for (int count = 0; count < isbn.length; count++) {
            String select_date_query = "SELECT " +
                    database.Column_IMAGE_PATH +
                    " FROM " + database.Table_Name +
                    " WHERE " + database.Column_ISBN + "=" + "'" +
                    isbn[count] + "'" + " ORDER BY " + database.Column_ID + " DESC ";
            Cursor cursor = db.rawQuery(select_date_query, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                bookImagePath[count] = cursor.getString(cursor.getColumnIndex(database.Column_IMAGE_PATH));
            }

        }
        return bookImagePath;
    }

    public int Size() {
        String size_query = "SELECT DISTINCT (" + database.Column_ISBN + ") FROM " + database.Table_Name;
        Cursor cursor = db.rawQuery(size_query, null);

        return cursor.getCount();

    }

    public ArrayList<String> getBookDetails(String isbn) {
        ArrayList<String> book = new ArrayList<String>();
        ;
        String select_date_query = "SELECT " +
                database.Column_ISBN + "," + database.Column_TITLE + "," + database.Column_AUTHOR + "," + database.Column_RATING + "," + database.Column_RATINGSCOUNT +
                "," + database.Column_REVIEWCOUNT + "," + database.Column_LANGUAGE + "," + database.Column_REVIEW + "," + database.Column_FORMAT +
                "," + database.Column_PAGE + "," + database.Column_YEAR + "," + database.Column_MONTH + "," + database.Column_DAY +
                "," + database.Column_PUBLISHER + "," + database.Column_DESCRIPTION + "," + database.Column_IMAGE_PATH + " FROM " + database.Table_Name +
                " WHERE " + database.Column_ISBN + "=" + "'" + isbn + "'";
        Cursor cursor = db.rawQuery(select_date_query, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String bookIsbn = cursor.getString(cursor.getColumnIndex(database.Column_ISBN));
            String bookTitle = cursor.getString(cursor.getColumnIndex(database.Column_TITLE));
            String bookAuthor = cursor.getString(cursor.getColumnIndex(database.Column_AUTHOR));
            String bookRating = cursor.getString(cursor.getColumnIndex(database.Column_RATING));
            String bookRatingsCount = cursor.getString(cursor.getColumnIndex(database.Column_RATINGSCOUNT));
            String bookReviewCount = cursor.getString(cursor.getColumnIndex(database.Column_REVIEWCOUNT));
            String bookLanguage = cursor.getString(cursor.getColumnIndex(database.Column_LANGUAGE));
            String bookReview = cursor.getString(cursor.getColumnIndex(database.Column_REVIEW));
            String bookFormat = cursor.getString(cursor.getColumnIndex(database.Column_FORMAT));
            String bookPage = cursor.getString(cursor.getColumnIndex(database.Column_PAGE));
            String bookYear = cursor.getString(cursor.getColumnIndex(database.Column_YEAR));
            String bookMonth = cursor.getString(cursor.getColumnIndex(database.Column_MONTH));
            String bookDay = cursor.getString(cursor.getColumnIndex(database.Column_DAY));
            String bookPublisher = cursor.getString(cursor.getColumnIndex(database.Column_PUBLISHER));
            String bookDescription = cursor.getString(cursor.getColumnIndex(database.Column_DESCRIPTION));
            String bookImage = cursor.getString(cursor.getColumnIndex(database.Column_IMAGE_PATH));


            book.add(bookIsbn);
            book.add(bookTitle);
            book.add(bookAuthor);
            book.add(bookDescription);
            book.add(bookRating);
            book.add(bookLanguage);
            book.add(bookReview);
            book.add(bookRatingsCount);
            book.add(bookReviewCount);
            book.add(bookFormat);
            book.add(bookPage);
            book.add(bookYear);
            book.add(bookMonth);
            book.add(bookDay);
            book.add(bookPublisher);
            book.add(bookImage);


        }

        return book;
    }


}
