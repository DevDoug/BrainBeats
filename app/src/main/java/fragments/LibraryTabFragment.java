package fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.stream.Stream;
import adapters.LibraryAdapter;
import adapters.MixerAdapter;
import architecture.AccountManager;
import data.MixContract;
import entity.Track;
import entity.UserPlaylistsResponse;
import entity.UserTrackResponse;
import utils.Constants;
import web.WebApiManager;

public class LibraryTabFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int VOLLEY_LOADER = 0;

    ArrayList<Track> trackList;
    private ListView mMixListView;
    private LibraryAdapter mLibraryAdapter;
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
            default:
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        MixContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        null,                  // No selection clause
                        null,                  // No selection arguments
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
            mLibraryAdapter = new LibraryAdapter(getContext(), data,0);
            mMixListView.setAdapter(mLibraryAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
