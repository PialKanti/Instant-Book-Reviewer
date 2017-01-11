package com.example.pial_pc.instantbookreview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
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
 * Created by Pial-PC on 1/16/2016.
 */
public class OCR_Image extends AsyncTask<Object, Void, String> {

    Bitmap image;
    Context context;
    KProgressHUD hud;
    String book_title, xml, url;
    Document document;
    String developer_key = "2LbbmGUeNFCEVdeZaux6hw";  //Developer Key for Goodreads API
    ArrayList<String> book;

    public OCR_Image(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                .setLabel("Getting book title...")
                .setMaxProgress(100)
                .setDimAmount(0.5f)
                .show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object... objects) {
        image = (Bitmap) objects[0];

        TessBaseAPI baseApi = new TessBaseAPI();
// DATA_PATH = Path to the storage
// lang = for which the language data exists, usually "eng"
        baseApi.init(context.getExternalFilesDir(null) + "/", "eng");
// Eg. baseApi.init("/mnt/sdcard/tesseract/tessdata/eng.traineddata", "eng");
        image = image.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(image);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();
        return recognizedText;
    }

    @Override
    protected void onPostExecute(String s) {
        hud.dismiss();
        UrlTask urlTask = new UrlTask();
        urlTask.execute(url, s);
        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Reconized Text");
        builder.setMessage(s + "");
        AlertDialog dialog = builder.create();
        dialog.show();*/

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

    public String MonthName(String num) {
        String monthName = "";
        switch (num) {
            case "1":
                monthName = "January";
                break;
            case "2":
                monthName = "February";
                break;
            case "3":
                monthName = "March";
                break;
            case "4":
                monthName = "April";
                break;
            case "5":
                monthName = "May";
                break;
            case "6":
                monthName = "June";
                break;
            case "7":
                monthName = "July";
                break;
            case "8":
                monthName = "August";
                break;
            case "9":
                monthName = "September";
                break;
            case "10":
                monthName = "October";
                break;
            case "11":
                monthName = "November";
                break;
            case "12":
                monthName = "December";
        }

        return monthName;
    }

    public class UrlTask extends AsyncTask<String, Void, String> {
        String alertTitle = "";

        @Override
        protected void onPreExecute() {
            hud = KProgressHUD.create(context)
                    .setStyle(KProgressHUD.Style.BAR_DETERMINATE)
                    .setLabel("Searching Book from Goodreads.com ...")
                    .setMaxProgress(100)
                    .setDimAmount(0.5f)
                    .show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... url) {
            String itemISBN = "", itemDescription = "", itemTitle = "", itemAuthor = "", itemRating = "", itemReview = "", itemLanguage = "";
            String itemError = "", errorCheck = "", itemRatingsCount = "", itemReviewCount = "", itemFormat = "", itemPages = "", itemYear = "", itemMonth = "", itemDay = "", itemPublisher = "";
            book_title = url[1];
            alertTitle=book_title;
            book_title = book_title.replaceAll(",", "");
            book_title = book_title.replaceAll("\n", "+");
            book_title = book_title.replaceAll(" ", "+");
            url[0] = "https://www.goodreads.com/book/title.xml?key=" + developer_key + "&title=" + book_title;
            xml = getXmlFromUrl(url[0]);
            document = getDomElement(xml);
            XPath xPath = XPathFactory.newInstance().newXPath();
            try {
                itemError = xPath.evaluate("//error/text()", document).trim();
                if (itemError.equalsIgnoreCase("book not found")) {
                    errorCheck = "not OK";
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
                    itemRatingsCount = xPath.evaluate("//GoodreadsResponse/book/work/ratings_count/text()", document).trim();
                    itemReviewCount = xPath.evaluate("//GoodreadsResponse/book/work/reviews_count/text()", document).trim();
                    itemFormat = xPath.evaluate("//GoodreadsResponse/book/format/text()", document).trim();
                    itemPages = xPath.evaluate("//GoodreadsResponse/book/num_pages/text()", document).trim();
                    itemYear = xPath.evaluate("//GoodreadsResponse/book/publication_year/text()", document).trim();
                    itemMonth = xPath.evaluate("//GoodreadsResponse/book/publication_month/text()", document).trim();
                    itemMonth = MonthName(itemMonth);
                    itemDay = xPath.evaluate("//GoodreadsResponse/book/publication_day/text()", document).trim();
                    itemPublisher = xPath.evaluate("//GoodreadsResponse/book/publisher/text()", document).trim();

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
                hud.dismiss();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Error");
                alertDialog.setMessage("Detected Text: " + alertTitle + "\nBook can't be found from Goodreads.Please check the spelling of book title.");
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Search Again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Intent refresh = new Intent(context, ImageCapture.class);
                        context.startActivity(refresh);
                        ((Activity) context).finish();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            } else if (s.equalsIgnoreCase("OK")) {
                hud.dismiss();
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("bookMap", book);
                context.startActivity(intent);
                ((Activity)context).finish();
            }

        }
    }
}
