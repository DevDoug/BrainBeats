package fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.brainbeats.R;

import java.util.ArrayList;

import adapters.LibraryMixAdapter;
import adapters.LibraryPlaylistAdapter;
import data.MixContract;
import data.MixDbHelper;
import entity.Track;
import utils.Constants;

public class LibraryTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    ArrayList<Track> trackList;
    private ListView mMixListView;
    private LibraryMixAdapter mLibraryMixAdapter;
    private LibraryPlaylistAdapter mLibraryPlaylistAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int mDataType;
    public String mFilter = "";
    private TextView mEmptyDataPlaceholder;

    public LibraryTabFragment() {
        // Required empty public constructor
    }

    public static LibraryTabFragment newInstance(Constants.LibraryDataType dataType) {
        LibraryTabFragment tabFragment = new LibraryTabFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY_LIBRARY_DATA_TYPE,dataType.getCode());
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
        getLoaderManager().initLoader(mDataType,null,this);
    }

    public void updateFilterParams(String params){
        mFilter = params;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case 0:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        MixContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        MixContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY + MixDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{MixDbHelper.DB_TRUE_VALUE},                  // No selection arguments
                        null                   // Default sort order
                );
            case 1:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        MixContract.MixPlaylistEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        null,
                        null,
                        null                   // Default sort order
                );
            case 2:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        MixContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        MixContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY + MixDbHelper.WHERE_CLAUSE_EQUAL + MixDbHelper.AND_CLAUSE + MixContract.MixEntry.COLUMN_NAME_IS_FAVORITE + MixDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{MixDbHelper.DB_TRUE_VALUE,MixDbHelper.DB_TRUE_VALUE},
                        null                   // Default sort order
                );
            default:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        MixContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        MixContract.MixEntry.COLUMN_NAME_IS_IN_LIBRARY + MixDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{MixDbHelper.DB_TRUE_VALUE},                  // No selection arguments
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
            if(mDataType == 0 || mDataType == 2){
                mLibraryMixAdapter = new LibraryMixAdapter(getContext(), data,0);
                mMixListView.setAdapter(mLibraryMixAdapter);
            }else {
                mLibraryPlaylistAdapter = new LibraryPlaylistAdapter(getContext(), data,0);
                mMixListView.setAdapter(mLibraryPlaylistAdapter);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
