package fragments;


import android.app.Dialog;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;

import adapters.ImageAdapter;
import adapters.MixItemAdapter;
import adapters.MixerAdapter;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import model.Mix;
import model.MixItem;
import utils.Constants;
import utils.MixManager;

public class MixerDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ImageAdapter.DialogImageSelectedListener {

    private List<MixItem> mixItemList = new ArrayList<>();
    private RecyclerView mMixerItemList;
    private MixItemAdapter mMixerItemAdapter;
    public Bundle mUserSelections;
    public EditText mMixTitle;
    public Mix mSelectedMix;
    private ImageView mPlayMixButton;
    private Handler handler = new Handler();
    public Dialog mDialog;
    //public FloatingActionButton mAddNewBeatButton;


    public MixerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer_detail, container, false);
        mMixerItemList = (RecyclerView) v.findViewById(R.id.beat_mix_item_list);
        mMixTitle = (EditText) v.findViewById(R.id.track_title);
        mPlayMixButton = (ImageView) v.findViewById(R.id.play_song_button);
        ((TextView) v.findViewById(R.id.separator_title)).setText(R.string.beat_levels);
        //mAddNewBeatButton = (FloatingActionButton) v.findViewById(R.id.mixer_fob);


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(getContext(), R.drawable.ic_up));
        DrawableCompat.setTint(up, getResources().getColor(R.color.theme_primary_text_color));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
/*                mSelectedMix.setMixTitle(mMixTitle.getText().toString());
                int returnId = getActivity().getContentResolver().update(
                        BrainBeatsContract.MixEntry.CONTENT_URI,
                        Constants.buildMixRecord(mSelectedMix),
                        BrainBeatsContract.MixEntry._ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                        new String[]{String.valueOf(mSelectedMix.getMixId())});

                if (returnId != -1) {
                    for (int i = 0; i < mSelectedMix.getMixItems().size(); i++) {
                        MixItem item = mSelectedMix.getMixItems().get(i);
                        int returnIdMixItem = getActivity().getContentResolver().update(
                                BrainBeatsContract.MixItemsEntry.CONTENT_URI,
                                Constants.buildMixItemsRecord(mSelectedMix.getMixId(), item),
                                BrainBeatsContract.MixEntry._ID + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL,
                                new String[]{String.valueOf(item.getMixItemId())});
                    }
                }*/
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus() != null) //
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                ((MixerActivity) getActivity()).navigateUpOrBack(getActivity(), fm);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMixerItemList.setLayoutManager(layoutManager);

        mPlayMixButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMix();
            }
        });
/*        mAddNewBeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = Constants.buildImageListDialogue(getContext(), getContext().getResources().getString(R.string.add_sound_item_to_current_beat), MixerDetailFragment.this);
                mDialog.show();
            }
        });*/

        mUserSelections = getArguments();
        if (mUserSelections != null) {
            mSelectedMix = (Mix) mUserSelections.get(Constants.KEY_EXTRA_SELECTED_MIX);
            mMixTitle.setText(mSelectedMix.getMixTitle());
        }

        getLoaderManager().initLoader(Constants.MIX_ITEMS_LOADER, null, this);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void dialogImageSelected(int position) {
        MixItem item = new MixItem();
        switch (position) {
            case 0:
                item.setMixItemTitle(getContext().getResources().getStringArray(R.array.default_mix_items)[0]);
                mDialog.dismiss();
                break;
            case 1:
                item.setMixItemTitle(getContext().getResources().getStringArray(R.array.default_mix_items)[1]);
                mDialog.dismiss();
                break;
            case 2:
                item.setMixItemTitle(getContext().getResources().getStringArray(R.array.default_mix_items)[2]);
                mDialog.dismiss();
                break;
            case 3:
                item.setMixItemTitle(getContext().getResources().getStringArray(R.array.default_mix_items)[3]);
                mDialog.dismiss();
                break;
            case 4:
                item.setMixItemTitle(getContext().getResources().getStringArray(R.array.default_mix_items)[4]);
                mDialog.dismiss();
                break;
        }
        item.setMixItemLevel(Constants.MIX_ITEM_DEFAULT_LEVEL);
        Uri returnRow = getActivity().getContentResolver().insert(BrainBeatsContract.MixItemsEntry.CONTENT_URI, Constants.buildMixItemsRecord(mSelectedMix.getMixId(), item));
    }

    public void playMix() {
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        MixManager.getInstance(getContext()).playMix((ArrayList<MixItem>) mixItemList);
                    }
                });
            }
        });
        thread.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case Constants.MIX_ITEMS_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixItemsEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        BrainBeatsContract.MixItemsEntry.COLUMN_NAME_MIX_ITEMS_FOREIGN_KEY + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{String.valueOf(mSelectedMix.getMixId())},                  // No selection arguments
                        null                   // Default sort order
                );
            default:
                // An invalid id was passed in
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) { //no mix data found
            mMixerItemList.setVisibility(View.GONE);
        } else {
            mMixerItemAdapter = new MixItemAdapter(getContext(), data);
            mMixerItemList.setAdapter(mMixerItemAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
