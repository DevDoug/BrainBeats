package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import com.brainbeats.R;
import java.util.ArrayList;
import adapters.ImageAdapter;
import architecture.AccountManager;
import data.MixContract;
import model.Mix;
import model.MixItem;
import web.WebApiManager;

/**
 * Created by Douglas on 4/20/2016.
 */
public class Constants {

    //= Keys for bundles and extras =============================================
    public static final String KEY_EXTRA_BEAT_LIST = "BeatInfo";
    public static final String KEY_EXTRA_SELECTED_TRACK = "SelectedTrack";
    public static final String KEY_EXTRA_SELECTED_MIX = "SelectedMix";
    public static final String KEY_EXTRA_SEARCH_KEYWORD = "SearchKeyword";

    //Sound Cloud
    public static final String SOUND_CLOUD_CLIENT_ID       = "6af4e9b999eaa63f5d797d466cdc4ccb";
    public static final String SOUND_CLOUD_CLIENT_SECRET   = "09e8c5b6f91e2ab440b9137008d2d32c";

    public static final int GRID_SPAN_COUNT = 3;
    public static final int BEAT_LEVEL_INCREASE_DIFFERENCE = 10;
    public static final int MIX_ITEM_DEFAULT_LEVEL = 50;
    public static final int BEAT_ITEM_DRAWABLES[] = new int[]{R.drawable.ic_alpha, R.drawable.ic_beta,
                                                              R.drawable.ic_google, R.drawable.ic_theta,};

    public enum AudioServiceRepeatType {
        RepeatOff(0),
        RepeatOne(1),
        RepeatAll(2);

        private int mCode;

        private AudioServiceRepeatType(int code) {
            mCode = code;
        }

        public int getCode() {
            return mCode;
        }
    }

    public static Mix buildMixFromCursor(Context context,Cursor cursor, int position) {
        cursor.moveToPosition(position);
        Mix mix = new Mix();
        mix.setMixId(cursor.getLong(cursor.getColumnIndex(MixContract.MixEntry._ID)));
        mix.setMixTitle(cursor.getString(cursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
        String whereClause = MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + "= ?";
        String[] whereArgs = new String[] {
                " " + cursor.getLong(cursor.getColumnIndex(MixContract.MixEntry._ID)),
        };
        Cursor mixItemsCursor = context.getContentResolver().query(MixContract.MixItemsEntry.CONTENT_URI, null,whereClause,whereArgs,null); // get the mix items associated with this mix
        mixItemsCursor.moveToFirst();
        ArrayList<MixItem> mixItems = new ArrayList<>();
        for (int i = 0; i < mixItemsCursor.getCount(); i++) {
            MixItem mixItem = new MixItem();
            mixItem.setMixItemId(mixItemsCursor.getLong(mixItemsCursor.getColumnIndex(MixContract.MixItemsEntry._ID)));
            mixItem.setMixItemTitle(mixItemsCursor.getString(mixItemsCursor.getColumnIndex(MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE)));
            mixItem.setMixItemLevel(mixItemsCursor.getInt(mixItemsCursor.getColumnIndex(MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL)));
            mixItems.add(mixItem);
            mixItemsCursor.moveToNext();
        }
        mix.setMixItems(mixItems);
        return mix;
    }

    public static ContentValues buildMixRecord(Mix mix) {
        ContentValues values = new ContentValues();
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_TITLE, mix.getBeatTitle());
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_ALBUM_ART_URL, mix.getBeatAlbumCoverArt());
        return values;
    }

    public static ContentValues buildMixItemsRecord(long mixId,MixItem mixitem){
        ContentValues values = new ContentValues();
        values.put(MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_TITLE,mixitem.getMixItemTitle());
        values.put(MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEM_LEVEL,mixitem.getMixItemLevel());
        values.put(MixContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY,mixId);
        return values;
    }

    public static ContentValues[] buildMixItemsBulkRecord(long mixId,ArrayList<MixItem> mixitems) {
        ContentValues[] contentValues = new ContentValues[mixitems.size()];
        for (int i = 0; i < mixitems.size(); i++) {
            contentValues[i] = buildMixItemsRecord(mixId,mixitems.get(i));
        }
        return contentValues;
    }

    public static Mix buildNewDefaultMixRecord(Context context) {
        Mix defaultMix = new Mix();
        defaultMix.setMixTitle(context.getString(R.string.default_mix_title));
        defaultMix.setBeatAlbumCoverArt(context.getString(R.string.default_mix_album_art_url));
        ArrayList<MixItem> defaultMixItems = new ArrayList<>();
        for(int i = 0; i < context.getResources().getStringArray(R.array.default_mix_items).length; i++){
            MixItem item = new MixItem();
            item.setMixItemTitle(context.getResources().getStringArray(R.array.default_mix_items)[i]);
            item.setMixItemLevel(MIX_ITEM_DEFAULT_LEVEL);
            defaultMixItems.add(item);
        }
        defaultMix.setMixItems(defaultMixItems);

        return defaultMix;
    }

    public static void buildWebDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        View dialogView = (View) inflater.inflate(R.layout.custom_web_view_dialog_layout, null);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        WebView webView = (WebView) dialogView.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.requestFocus();
        webView.requestFocusFromTouch();
        webView.requestFocus(View.FOCUS_DOWN);
        webView.loadUrl(WebApiManager.API_CONNECT_URL + "?client_id=" + Constants.SOUND_CLOUD_CLIENT_ID + "&redirect_uri=" + "http://localhost" + "&response_type=token");
        webView.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
                progressDialog.setMessage("Connecting SoundCloud");
                progressDialog.show();
            }
            String authCode="";
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
                Log.i("URLLLL", url);
                if (url.contains("access_token=") && authComplete != true) {
                    for(int t=32; t <url.length();t++){
                        if(!(url.charAt(t) == '&'))
                            authCode = authCode+url.charAt(t);
                        else
                            break;
                    }
                    Log.i("CODE true", "CODE : " + authCode);
                    authComplete = true;
                    AccountManager.getInstance(context).setAccessToken(authCode);
                }else if(url.contains("error=access_denied")){
                    Log.i("CODE false", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    progressDialog.dismiss();
                }
            }
        });
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static AlertDialog buildImageListDialogue(Context context, String title, final ImageAdapter.DialogImageSelectedListener selectionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.custom_image_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ((GridView) dialogView.findViewById(R.id.options_list)).setAdapter(new ImageAdapter(context,selectionListener));
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog buildListDialogue(Context context, String title, int optionsId, AdapterView.OnItemClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.custom_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.dialog_list_item, R.id.dialog_item, context.getResources().getStringArray(optionsId));
        ((ListView) dialogView.findViewById(R.id.options_list)).setAdapter(adapter);
        ((ListView) dialogView.findViewById(R.id.options_list)).setOnItemClickListener(listener);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }
}
