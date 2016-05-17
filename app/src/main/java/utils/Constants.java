package utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brainbeats.R;

/**
 * Created by Douglas on 4/20/2016.
 */
public class Constants {

    public static final int GRID_SPAN_COUNT = 3;

    public static void buildListDialogue(Context context, String title, int optionsId, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogView = (View) inflater.inflate(R.layout.custom_dialog_layout, null);
        ((TextView) dialogView.findViewById(R.id.separator_title)).setText(title);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, android.R.id.text1,context.getResources().getStringArray(optionsId));
        ((ListView) dialogView.findViewById(R.id.options_list)).setAdapter(adapter);
        builder.setView(dialogView);
        AlertDialog alert = builder.create();
        alert.show();
    }
}
