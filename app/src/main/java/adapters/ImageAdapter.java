package adapters;

import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;

import utils.Constants;

/**
 * Created by douglas on 5/17/2016.
 */
public class ImageAdapter extends BaseAdapter{

    Context mAdapterContext;

    public ImageAdapter(Context context) {
        this.mAdapterContext = context;
    }

    @Override
    public int getCount() {
        return Constants.BEAT_ITEM_DRAWABLES.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_image_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.dialog_image_list_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (position) {
                case 0:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_music_note_black));
                    break;
                case 1:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_mic_none));
                    break;
                case 2:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_album));
                    break;
                case 3:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_album));
                    break;
                case 4:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_album));
                    break;
                case 5:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_album));
                    break;
                case 6:
                    imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_album));
                    break;
            }
        }
        return convertView;
    }
}
