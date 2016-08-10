package fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.MainActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;

import java.util.ArrayList;

import adapters.LibraryMixAdapter;
import adapters.LibraryPlaylistAdapter;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import entity.Track;
import model.Mix;
import utils.Constants;
import web.OfflineSyncManager;

public class LibraryTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ArrayList<Track> trackList;
    private ListView mMixListView;
    private LibraryMixAdapter mLibraryMixAdapter;
    private LibraryPlaylistAdapter mLibraryPlaylistAdapter;
    private int mDataType;
    public String mFilter = "";
    private TextView mEmptyDataPlaceholder;

    public LibraryTabFragment() {
        // Required empty public constructor
    }

    public static LibraryTabFragment newInstance(Constants.LibraryDataType dataType) {
        LibraryTabFragment tabFragment = new LibraryTabFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_LIBRARY_DATA_TYPE, dataType.getCode());
        //args.putString(Constants.KEY_EXTRA_LIBRARY_FILTER_TEXT,tabFilter);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mDataType = args.getInt(Constants.KEY_LIBRARY_DATA_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library_tab, container, false);
        mMixListView = (ListView) v.findViewById(R.id.library_content_list);
        mEmptyDataPlaceholder = (TextView) v.findViewById(R.id.no_data_placeholder);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        trackList = new ArrayList<>();
        setRetainInstance(true);
        getLoaderManager().initLoader(mDataType, null, this);

        mMixListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mLibraryMixAdapter.getItem(i);
                cursor.moveToPosition(i);
                Mix selectedMix = Constants.buildMixFromCursor(getContext(),cursor,i); // get the selected mix item

                //update local db with change
                Bundle settingsBundle = new Bundle();
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_TYPE, Constants.SyncDataType.Mixes.getCode());
                settingsBundle.putInt(Constants.KEY_EXTRA_SYNC_ACTION, Constants.SyncDataAction.UpdateAddToMixer.getCode());
                settingsBundle.putParcelable(Constants.KEY_EXTRA_SELECTED_MIX, selectedMix);
                OfflineSyncManager.getInstance(getContext()).performSyncOnLocalDb(((LibraryActivity) getActivity()).mCoordinatorLayout, settingsBundle, getActivity().getContentResolver());

                //start intent to send user to their new mix for them to add/sub mix items.
                Intent mixerIntent = new Intent(getContext(), MixerActivity.class);
                startActivity(mixerIntent);
            }
        });
    }

    public void updateFilterParams(String params) {
        mFilter = params;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case 0:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY +
                                BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL +
                                BrainBeatsDbHelper.AND_CLAUSE +
                                BrainBeatsContract.MixEntry.COLUMN_NAME_MIX_TITLE +
                                BrainBeatsDbHelper.WHERE_CLAUSE_LIKE, // where the mix is in the lib
                        new String[]{BrainBeatsDbHelper.DB_TRUE_VALUE, mFilter + "%"},                  // No selection arguments
                        null                   // Default sort order
                );
            case 1:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixPlaylistEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        null,
                        null,
                        null                   // Default sort order
                );
            case 2:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_FAVORITE + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{BrainBeatsDbHelper.DB_TRUE_VALUE},
                        null                   // Default sort order
                );
            default:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{BrainBeatsDbHelper.DB_TRUE_VALUE},                  // No selection arguments
                        null                   // Default sort order
                );
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {
        if (data == null) { //no mix data found
            mMixListView.setVisibility(View.GONE);
            mEmptyDataPlaceholder.setVisibility(View.VISIBLE);
        } else {
            mEmptyDataPlaceholder.setVisibility(View.INVISIBLE);
            if (mDataType == 0 || mDataType == 2) {
                mLibraryMixAdapter = new LibraryMixAdapter(getContext(), data, 0);
                mMixListView.setAdapter(mLibraryMixAdapter);
            } else {
                mLibraryPlaylistAdapter = new LibraryPlaylistAdapter(getContext(), data, 0);
                mMixListView.setAdapter(mLibraryPlaylistAdapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
    }
}
