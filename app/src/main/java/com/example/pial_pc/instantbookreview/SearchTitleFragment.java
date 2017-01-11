package com.example.pial_pc.instantbookreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Pial-PC on 1/1/2016.
 */
public class SearchTitleFragment extends Fragment {
    TextView searchText;
    EditText title;
    ImageButton search_button;
    String book_title, xml, url;
    Document document;
    String developer_key = "2LbbmGUeNFCEVdeZaux6hw";  //Developer Key for Goodreads API
    ArrayList<String> book;
    KProgressHUD hud;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_title, container, false);
        searchText = (TextView) rootView.findViewById(R.id.searchText);
        title = (EditText) rootView.findViewById(R.id.title_editText);
        search_button = (ImageButton) rootView.findViewById(R.id.search);
        final Animation animAlpha = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_alpha);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animAlpha);
                if (title.getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please insert a book name.", Toast.LENGTH_SHORT).show();
                } else {
                    final InternetConnection internet = new InternetConnection();
                    if (internet.Checking_Internet_Connection(getActivity()) == true) {
                        UrlTask urlTask = new UrlTask();
                        urlTask.execute(url);
                        hud = KProgressHUD.create(getActivity())
                                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                                .setLabel("Searching book title...")
                                .setMaxProgress(100)
                                .setDimAmount(0.5f)
                                .show();
                    } else if (internet.Checking_Internet_Connection(getActivity()) == false) {
                        //Showing AlertDialog if Internet Connection is off
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                        alertBuilder.setTitle("Warning");
                        alertBuilder.setCancelable(false);
                        alertBuilder.setIcon(R.drawable.error_icon);
                        alertBuilder.setMessage("Internet connection is turned off. Please turn on your internet connection to continue.");
                        alertBuilder.setPositiveButton("Turn on", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));

                            }
                        });
                        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog dialog = alertBuilder.create();
                        dialog.show();
                    }

                }

            }
        });
        return rootView;
    }

    /**
     * This method returns String xml from Goodreads url
     *
     * @param url
     * @return string
     */
    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return xml;
    }

    /**
     * Getting XML DOM element
     *
     * @param xml
     * @return Document
     */
    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /**
     * This method returns Language from Language Code
     *
     * @param code
     * @return String
     */
    public String LanguageCode(String code) {
        String LanCode = "";
        switch (code) {
            case "eng":
                LanCode = "English";
                break;
            case "ben":
                LanCode = "Bengali";

        }
        return LanCode;
    }

    public String MonthName(String num){
        String monthName="";
        switch (num){
            case "1":
                monthName="January";
                break;
            case "2":
                monthName="February";
                break;
            case "3":
                monthName="March";
                break;
            case "4":
                monthName="April";
                break;
            case "5":
                monthName="May";
                break;
            case "6":
                monthName="June";
                break;
            case "7":
                monthName="July";
                break;
            case "8":
                monthName="August";
                break;
            case "9":
                monthName="September";
                break;
            case "10":
                monthName="October";
                break;
            case "11":
                monthName="November";
                break;
            case "12":
                monthName="December";
        }

        return monthName;
    }

    public class UrlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String itemISBN = "", itemDescription = "", itemTitle = "", itemAuthor = "", itemRating = "", itemReview = "", itemLanguage = "";
            String itemError = "", errorCheck = "",itemRatingsCount="",itemReviewCount="",itemFormat="",itemPages="",itemYear="",itemMonth="",itemDay="",itemPublisher="";
            book_title = title.getText().toString();
            book_title = book_title.replaceAll(" ", "+");
            url[0] = "https://www.goodreads.com/book/title.xml?key=" + developer_key + "&title=" + book_title;
            xml = getXmlFromUrl(url[0]);
            document = getDomElement(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();
            try {
                itemError = xPath.evaluate("//error/text()", document).trim();
                if (itemError.equalsIgnoreCase("book not found")) {
                    errorCheck = "not OK";
                    title.setText("");
                    InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } else {
                    errorCheck = "OK";
                    itemISBN = xPath.evaluate("//GoodreadsResponse/book/isbn/text()", document).trim();
                    itemDescription = xPath.evaluate("//GoodreadsResponse/book/description/text()", document).trim();
                    itemTitle = xPath.evaluate("//GoodreadsResponse/book/work/original_title/text()", document).trim();
                    itemAuthor = xPath.evaluate("//GoodreadsResponse/book/authors/author/name/text()", document).trim();
                    itemRating = xPath.evaluate("//GoodreadsResponse/book/average_rating/text()", document).trim();
                    itemLanguage = xPath.evaluate("//GoodreadsResponse/book/language_code/text()", document).trim();
                    itemReview = xPath.evaluate("//GoodreadsResponse/book/reviews_widget/text()", document).trim();
                    itemReview="<root>" + itemReview + "</root>";
                    Document doc=getDomElement(itemReview);
                    itemReview= xPath.evaluate("/root/div/iframe[@id=\"the_iframe\"]/@src",doc).trim();
                    Log.i("PialCheck",itemReview);

                    itemLanguage = LanguageCode(itemLanguage);
                    itemRatingsCount= xPath.evaluate("//GoodreadsResponse/book/work/ratings_count/text()", document).trim();
                    itemReviewCount=xPath.evaluate("//GoodreadsResponse/book/work/reviews_count/text()", document).trim();
                    itemFormat=xPath.evaluate("//GoodreadsResponse/book/format/text()", document).trim();
                    itemPages=xPath.evaluate("//GoodreadsResponse/book/num_pages/text()", document).trim();
                    itemYear=xPath.evaluate("//GoodreadsResponse/book/publication_year/text()", document).trim();
                    itemMonth=xPath.evaluate("//GoodreadsResponse/book/publication_month/text()", document).trim();
                    itemMonth= MonthName(itemMonth);
                    itemDay=xPath.evaluate("//GoodreadsResponse/book/publication_day/text()", document).trim();
                    itemPublisher=xPath.evaluate("//GoodreadsResponse/book/publisher/text()", document).trim();

                    book = new ArrayList<String>();
                    book.add(itemISBN);
                    book.add(itemTitle);
                    book.add(itemAuthor);
                    book.add(itemDescription);
                    book.add(itemRating);
                    book.add(itemLanguage);
                    book.add(itemReview);
                    book.add(itemRatingsCount);
                    book.add(itemReviewCount);
                    book.add(itemFormat);
                    book.add(itemPages);
                    book.add(itemYear);
                    book.add(itemMonth);
                    book.add(itemDay);
                    book.add(itemPublisher);
                }

            } catch (XPathExpressionException e1) {
                e1.printStackTrace();
            }
            return errorCheck;
        }

        @Override
        protected void onPostExecute(String s) {
            /*Fragment fragment = new BookFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("bookMap", book);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
            if (s.equalsIgnoreCase("not OK")) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Book can't be found from Goodreads.Please check the spelling of book title.");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Search Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            } else if (s.equalsIgnoreCase("OK")) {
                hud.dismiss();
                title.setText("");
                Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
                intent.putExtra("bookMap", book);
                startActivity(intent);
            }

        }
    }

}

