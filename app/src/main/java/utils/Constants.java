package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.brainbeats.R;

import adapters.ImageAdapter;
import data.MixContract;
import model.Mix;

/**
 * Created by Douglas on 4/20/2016.
 */
public class Constants {

    //= Keys for bundles and extras =============================================
    public static final String KEY_EXTRA_BEAT_LIST = "BeatInfo";
    public static final String KEY_EXTRA_SELECTED_TRACK = "SelectedTrack";
    public static final String KEY_EXTRA_SELECTED_MIX = "SelectedMix";

    //Sound Cloud
    public static final String SOUND_CLOUD_CLIENT_ID = "6af4e9b999eaa63f5d797d466cdc4ccb";

    public static final int GRID_SPAN_COUNT = 3;
    public static final int BEAT_LEVEL_INCREASE_DIFFERENCE = 10;
    public static final int BEAT_ITEM_DRAWABLES[] = new int[]{R.drawable.ic_music_note_black, R.drawable.ic_music_note_black,
                                                              R.drawable.ic_music_note_black, R.drawable.ic_music_note_black,
                                                              R.drawable.ic_music_note_black, R.drawable.ic_music_note_black,
                                                              R.drawable.ic_music_note_black};

    public static Mix buildMixFromCursor(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        Mix mix = new Mix();
        mix.setMixTitle(cursor.getString(cursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_MIX_TITLE)));
        mix.setAlphaLevel(cursor.getInt(cursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_MIX_ALPHA_LEVEL)));
        mix.setBetaLevel(cursor.getInt(cursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_MIX_BETA_LEVEL)));
        mix.setGammaLevel(cursor.getInt(cursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_MIX_GAMMA_LEVEL)));
        mix.setThetaLevel(cursor.getInt(cursor.getColumnIndex(MixContract.MixEntry.COLUMN_NAME_MIX_THETA_LEVEL)));
        return mix;
    }

    public static ContentValues buildMixRecord(Mix mix) {
        ContentValues values = new ContentValues();
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_TITLE, mix.getBeatTitle());
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_ALPHA_LEVEL, mix.getAlphaLevel());
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_BETA_LEVEL, mix.getBetaLevel());
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_GAMMA_LEVEL, mix.getGammaLevel());
        values.put(MixContract.MixEntry.COLUMN_NAME_MIX_THETA_LEVEL, mix.getThetaLevel());
        return values;
    }

    public static void buildImageListDialogue(Context context, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.custom_image_list_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ((GridView) dialogView.findViewById(R.id.options_list)).setAdapter(new ImageAdapter(context));
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
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
