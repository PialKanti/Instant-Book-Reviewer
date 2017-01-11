package com.example.pial_pc.instantbookreview;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pial_pc.instantbookreview.databaseHandler.DBadapter;
import com.example.pial_pc.instantbookreview.databaseHandler.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pial-PC on 2/12/2016.
 */
public class ReadListFragment extends Fragment {
    ListView list;
    String[] bookISBN;
    String[] bookTitle,bookImagePath,bookRating;
    DBadapter dBadapter;
    Database db;
    SQLiteDatabase sql;
    ReadListAdapter readList;
    TextView error;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_readlist, container, false);
        error=(TextView)rootView.findViewById(R.id.textError);
        list=(ListView) rootView.findViewById(R.id.listView);

        db = new Database(getActivity());
        sql = db.getWritableDatabase();
        dBadapter = new DBadapter(getActivity());

        /*for (int i = 0; i < dBadapter.Size(); i++) {
            String[] temp = new String[3];
            temp[0] = bookISBN[i];
            Log.i("Test", bookISBN[i]);
            temp[1] = bookTitle[i];
            Log.i("Test",bookTitle[i]);
            temp[2] = bookImagePath[i];
            Log.i("Test",bookImagePath[i]);

            th.add(temp);
        }*/

//        Log.i("Test",th.get(0).toString());

        if(dBadapter.Size()!=0){
            bookISBN=new String[dBadapter.Size()];
            bookTitle=new String[dBadapter.Size()];
            bookImagePath=new String[dBadapter.Size()];
            bookRating=new String[dBadapter.Size()];

            bookISBN=dBadapter.getBookISBN();
            bookTitle=dBadapter.getBookTitle(bookISBN);
            bookImagePath=dBadapter.getBookImagePath(bookISBN);
            bookRating=dBadapter.getBookRatings(bookISBN);
            readList= new ReadListAdapter(getActivity(),bookTitle,bookISBN,bookImagePath,bookRating);
            list.setAdapter(readList);
        }else{
            error.setVisibility(View.VISIBLE);
            error.setText("No Books in Read List.");
            list.setVisibility(View.INVISIBLE);
        }




        return rootView;
    }
}
