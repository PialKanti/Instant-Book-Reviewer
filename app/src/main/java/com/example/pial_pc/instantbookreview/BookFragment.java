package com.example.pial_pc.instantbookreview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.pial_pc.instantbookreview.databaseHandler.DBadapter;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.Toast;

/**
 * Created by Pial-PC on 1/3/2016.
 */
public class BookFragment extends Fragment {

    ImageView bookCover;
    ImageButton addTOreadList;
    RatingBar bookRatings;
    TextView bookTitle, bookAuthor, RatingScore, bookLanguage, bookDescription, lanText, Destext, RatingsCount, ReviewCount, PageDes, Publication;
    View lineView;
    ArrayList<String> bookDetails;
    String url;
    KProgressHUD hud;
    DBadapter dBadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);


        hud = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("getting book from goodreads...")
                .setMaxProgress(100)
                .setDimAmount(0.5f)
                .show();

        addTOreadList = (ImageButton) rootView.findViewById(R.id.readlist);
        bookCover = (ImageView) rootView.findViewById(R.id.bookCover);
        bookTitle = (TextView) rootView.findViewById(R.id.bookTitle);
        bookAuthor = (TextView) rootView.findViewById(R.id.bookAuthor);
        bookRatings = (RatingBar) rootView.findViewById(R.id.RatingBar);
        RatingScore = (TextView) rootView.findViewById(R.id.ratingScore);
        RatingsCount = (TextView) rootView.findViewById(R.id.ratingCount);
        ReviewCount = (TextView) rootView.findViewById(R.id.reviewCount);
        PageDes = (TextView) rootView.findViewById(R.id.page);
        Publication = (TextView) rootView.findViewById(R.id.Publication);
        bookLanguage = (TextView) rootView.findViewById(R.id.language);
        bookDescription = (TextView) rootView.findViewById(R.id.bookDescription);
        /*moreDescription = (TextView) rootView.findViewById(R.id.MoreDescription);*/
        lanText = (TextView) rootView.findViewById(R.id.lanText);
        Destext = (TextView) rootView.findViewById(R.id.Destext);
        lineView = (View) rootView.findViewById(R.id.innerLine);

        bookDetails = new ArrayList<String>();
        bookDetails = BookDetailsActivity.getRequiredData();
        //image path
        File folder=new File(Environment.getExternalStorageDirectory()+"/ReadList_books_cover/");
        String title=bookDetails.get(1);
        title=title.replaceAll(" ","_");
        String author=bookDetails.get(2);
        author=author.replaceAll(" ","_");
        String oriImageName=title+"_"+author+"_"+bookDetails.get(0)+".jpg";
        String imageOriginalFile = folder.getPath() +"/"+ oriImageName;

        bookDetails.add(15,imageOriginalFile);

        dBadapter = new DBadapter(getActivity());

        if (dBadapter.CheckBook(bookDetails.get(0))) {
            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.item_checked);
            BitmapDrawable bit = new BitmapDrawable(getActivity().getResources(), bitmap);
            addTOreadList.setBackgroundDrawable(bit);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.item_check);
            BitmapDrawable bit = new BitmapDrawable(getActivity().getResources(), bitmap);
            addTOreadList.setBackgroundDrawable(bit);
        }


        addTOreadList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String tagName = (String) addTOreadList.getTag();
                if (tagName.equalsIgnoreCase("item_check.png")) {
                    Toast.makeText(getActivity(), "Click to add into Readlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Already added to into Readlist", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        addTOreadList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagName = (String) addTOreadList.getTag();
                if (tagName.equals("item_check.png")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.item_checked);
                    BitmapDrawable bit = new BitmapDrawable(getActivity().getResources(), bitmap);
                    addTOreadList.setBackgroundDrawable(bit);
                    DownloadImageTask imageTask=new DownloadImageTask(getActivity());
                    imageTask.execute(bookDetails.get(1), bookDetails.get(2), bookDetails.get(0));
                    addTOreadList.setTag("item_checked.png");
                    dBadapter.insertMsg(bookDetails);
                } else if (tagName.equals("item_checked.png")) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.item_check);
                    BitmapDrawable bit = new BitmapDrawable(getActivity().getResources(), bitmap);
                    addTOreadList.setBackgroundDrawable(bit);
                    addTOreadList.setTag("item_check.png");
                    dBadapter.deleteBook(bookDetails.get(0));

                }

            }
        });


        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.TTF");
        bookTitle.setTypeface(font, Typeface.BOLD);
        bookAuthor.setTypeface(font, Typeface.ITALIC);
        RatingScore.setTypeface(font, Typeface.NORMAL);
        lanText.setTypeface(font, Typeface.BOLD);
        bookLanguage.setTypeface(font, Typeface.NORMAL);
        Destext.setTypeface(font, Typeface.BOLD);
        bookDescription.setTypeface(font, Typeface.NORMAL);

        /*Bundle bundle = this.getArguments();
        if (bundle.getSerializable("bookMap") != null) {
            bookDetails = (ArrayList<String>) bundle.getSerializable("bookMap");
        }*/

        /*moreDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moreDescription.getText().toString().equals("(...more)")) {
                    int length = 5000;
                    bookDescription.setMaxLines(50);
                    bookDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
                    bookDescription.setEllipsize(null);
                    moreDescription.setText("(...less)");
                } else if (moreDescription.getText().toString().equals("(...less)")) {
                    bookDescription.setMaxLines(3);
                    bookDescription.setEllipsize(TextUtils.TruncateAt.END);
                    moreDescription.setText("(...more)");
                }

            }
        });*/

        BookDetailsTask bookDetailsTask = new BookDetailsTask();
        bookDetailsTask.execute("");


        return rootView;
    }

    public class BookDetailsTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            bookTitle.setVisibility(View.INVISIBLE);
            bookAuthor.setVisibility(View.INVISIBLE);
            bookRatings.setVisibility(View.INVISIBLE);
            lanText.setVisibility(View.INVISIBLE);
            Destext.setVisibility(View.INVISIBLE);
            RatingScore.setVisibility(View.INVISIBLE);
            RatingsCount.setVisibility(View.INVISIBLE);
            ReviewCount.setVisibility(View.INVISIBLE);
            PageDes.setVisibility(View.INVISIBLE);
            Publication.setVisibility(View.INVISIBLE);
            lineView.setVisibility(View.INVISIBLE);

            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap myBitmap = null;
            try {
                url = "http://covers.openlibrary.org/b/isbn/" + bookDetails.get(0) + "-M.jpg";
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            hud.dismiss();
            bookTitle.setVisibility(View.VISIBLE);
            bookAuthor.setVisibility(View.VISIBLE);
            bookRatings.setVisibility(View.VISIBLE);
            RatingScore.setVisibility(View.VISIBLE);
            lanText.setVisibility(View.VISIBLE);
            Destext.setVisibility(View.VISIBLE);
            RatingsCount.setVisibility(View.VISIBLE);
            ReviewCount.setVisibility(View.VISIBLE);
            PageDes.setVisibility(View.VISIBLE);
            Publication.setVisibility(View.VISIBLE);
            lineView.setVisibility(View.VISIBLE);

            //Converting Bitmap to Bitmap drawable
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            float rating = Float.parseFloat(bookDetails.get(4));

            // This code is for review iframe
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
//            String url = "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head> <body style=\"background:white;margin:0 0 0 0; padding:0 0 0 0;\"> <iframe style=\"background:white;\" width=' "+width+"' height='"+height+"' src=\""+bookDetails.get(6)+"\" frameborder=\"0\"></iframe> </body> </html> ";
            bookCover.setBackgroundDrawable(bitmapDrawable);
            bookTitle.setText(bookDetails.get(1));
            bookAuthor.setText(Html.fromHtml("By <b>" + bookDetails.get(2) + "</b>"));
            bookRatings.setRating((float) rating);
            RatingScore.setText(bookDetails.get(4) + "/5");
            RatingsCount.setText("\u2022 " + bookDetails.get(7) + " Ratings");
            ReviewCount.setText("\u2022 " + bookDetails.get(8) + " Reviews");
            PageDes.setText(bookDetails.get(9) + ", " + bookDetails.get((10)) + " pages");
            if (bookDetails.get(13) != null) {
                if (bookDetails.get(13) == "1")
                    Publication.setText("Published " + bookDetails.get(12) + " " + bookDetails.get(13) + "st " + bookDetails.get(11) + " by " + bookDetails.get(14));
                if (bookDetails.get(13) == "2")
                    Publication.setText("Published " + bookDetails.get(12) + " " + bookDetails.get(13) + "nd " + bookDetails.get(11) + " by " + bookDetails.get(14));
                if (bookDetails.get(13) == "1")
                    Publication.setText("Published " + bookDetails.get(12) + " " + bookDetails.get(13) + "rd " + bookDetails.get(11) + " by " + bookDetails.get(14));
            } else {
                Publication.setText("Published " + bookDetails.get(12) + " " + bookDetails.get(11) + " by " + bookDetails.get(14));
            }
            bookLanguage.setText(bookDetails.get(5));
            bookDescription.setText(Html.fromHtml(bookDetails.get(3)));
            /*bookReview.getSettings().setJavaScriptEnabled(true);
            bookReview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            bookReview.getSettings().setPluginState(WebSettings.PluginState.ON);
            bookReview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            bookReview.getSettings().setLoadWithOverviewMode(true);
            bookReview.getSettings().setUseWideViewPort(true);
            bookReview.getSettings().setBuiltInZoomControls(true);
            bookReview.setWebChromeClient(new WebChromeClient());
            bookReview.setHorizontalScrollBarEnabled(true);
            bookReview.setVerticalScrollBarEnabled(true);
            bookReview.setWebViewClient(new WebViewClient());
            bookReview.loadUrl(bookDetails.get(6));*/

        }
    }


}
