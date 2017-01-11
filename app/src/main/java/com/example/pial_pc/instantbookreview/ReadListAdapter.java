package com.example.pial_pc.instantbookreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pial_pc.instantbookreview.databaseHandler.DBadapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pial-PC on 2/12/2016.
 */
public class ReadListAdapter extends BaseAdapter {
    Activity context;
    Bitmap bitmap1;
    BitmapDrawable bit1;
    String[] bookTitle, bookISBN, bookImage, bookRating;
    Float ratingScore;
    DBadapter dBadapter;
    ArrayList<String> bookDetails;
    String isbn;


    public ReadListAdapter(Activity context, String[] bookTitle, String[] bookISBN, String[] bookImage, String[] bookRating) {
        this.context = context;
        this.bookTitle = bookTitle;
        this.bookISBN = bookISBN;
        this.bookImage = bookImage;
        this.bookRating = bookRating;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bookISBN.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public static class Holder  //holder class containing view components/widgets
    {
        ImageView image1;
        TextView text1, isbn1, empty,ratingScore;
        Button moreButton1;
        ImageButton closeButton1;
        RatingBar rating;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.readlist_row, null, false);
        holder.empty = (TextView) view.findViewById(R.id.EmptyReadList);
        holder.image1 = (ImageView) view.findViewById(R.id.image1);
        holder.text1 = (TextView) view.findViewById(R.id.text1);
        holder.isbn1 = (TextView) view.findViewById(R.id.isbn1);
        holder.moreButton1 = (Button) view.findViewById(R.id.moreButton1);
        holder.closeButton1 = (ImageButton) view.findViewById(R.id.closeButton1);
        holder.rating = (RatingBar) view.findViewById(R.id.ratingBar);
        holder.ratingScore=(TextView) view.findViewById(R.id.rating_score);

        holder.text1.setText(bookTitle[position]);
        Log.i("PiAL", bookTitle[position]);
        holder.isbn1.setText(bookISBN[position]);
        Log.i("PiAL", bookISBN[position]);
        bitmap1 = BitmapFactory.decodeFile(bookImage[position]);

        Log.i("PiAL", bookImage[position]);
        bit1 = new BitmapDrawable(context.getResources(), bitmap1);
        holder.image1.setBackgroundDrawable(bit1);

        Log.i("PiAL", bookTitle[0]);
        ratingScore=Float.parseFloat(bookRating[position]);
        holder.rating.setRating((float)ratingScore);
        holder.ratingScore.setText(bookRating[position]);

        dBadapter=new DBadapter(context);

        holder.moreButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbn=holder.isbn1.getText().toString();

                bookDetails=new ArrayList<String>();
                bookDetails=dBadapter.getBookDetails(isbn);
                Intent intent=new Intent(context,BookDetailsActivity.class);
                intent.putExtra("bookMap", bookDetails);
                context.startActivity(intent);
            }
        });

        holder.closeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbn=holder.isbn1.getText().toString();
                dBadapter.deleteBook(isbn);
            }
        });

        return view;
    }
}
