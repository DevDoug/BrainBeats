package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.brainbeats.R;

import utils.Constants;

/**
 * Created by douglas on 5/17/2016.
 */
public class ImageAdapter extends BaseAdapter {

    Context mAdapterContext;
    DialogImageSelectedListener mListener;

    public interface DialogImageSelectedListener{
        void dialogImageSelected(int position);
    }

    public ImageAdapter(Context context, DialogImageSelectedListener listenr) {
        this.mAdapterContext = context;
        this.mListener = listenr;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mAdapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.dialog_image_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.dialog_image_list_item);
        switch (position) {
            case 0:
                imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_alpha));
                break;
            case 1:
                imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_beta));
                break;
            case 2:
                imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_google));
                break;
            case 3:
                imageView.setImageDrawable(mAdapterContext.getDrawable(R.drawable.ic_theta));
                break;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.dialogImageSelected(position);
            }
        });
        return convertView;
    }
}
