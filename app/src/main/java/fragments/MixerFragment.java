package fragments;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brainbeats.LibraryActivity;
import com.brainbeats.MixerActivity;
import com.brainbeats.R;
import adapters.MixerAdapter;
import data.BrainBeatsContract;
import data.BrainBeatsDbHelper;
import model.Mix;
import model.MixItem;
import utils.Constants;


public class MixerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private RecyclerView mMixerItems;
    private TextView mEmptyText;
    private MixerAdapter mMixerAdapter;
    private OnFragmentInteractionListener mListener;
    public AlertDialog mAddOptionsDialog;

    public MixerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mixer, container, false);
        mMixerItems = (RecyclerView) v.findViewById(R.id.mixer_list);
        mEmptyText = (TextView) v.findViewById(R.id.empty_text);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(Constants.MIXES_LOADER, null, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMixerItems.setLayoutManager(layoutManager);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Mix defaultMix = Constants.buildNewDefaultMixRecord(getContext());
                defaultMix.setIsInMixer(1);
                defaultMix.setIsInLibrary(1);
                Uri returnRow = getActivity().getContentResolver().insert(BrainBeatsContract.MixEntry.CONTENT_URI, Constants.buildMixRecord(defaultMix));
                long returnRowId = ContentUris.parseId(returnRow);
                mAddOptionsDialog.dismiss();
                break;
            case 1:
                mAddOptionsDialog.dismiss();
                mAddOptionsDialog = Constants.buildListDialogue(getContext(), getString(R.string.create_beat_from_title), R.array.new_from_existing_beat_options, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                Toast.makeText(getContext(), "Creating from resource", Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                Toast.makeText(getContext(), "Creating from resource", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Intent libraryIntent = new Intent(getContext(), LibraryActivity.class);
                                startActivity(libraryIntent);
                                break;
                        }
                    }
                });
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case Constants.MIXES_LOADER:
                // Returns a new CursorLoader
                return new CursorLoader(
                        getActivity(),         // Parent activity context
                        BrainBeatsContract.MixEntry.CONTENT_URI,  // Table to query
                        null,                          // Projection to return
                        BrainBeatsContract.MixEntry.COLUMN_NAME_IS_IN_MIXER + BrainBeatsDbHelper.WHERE_CLAUSE_EQUAL, // where the mix is in the lib
                        new String[]{BrainBeatsDbHelper.DB_TRUE_VALUE},                  // No selection arguments
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
            mMixerItems.setVisibility(View.GONE);
            mEmptyText.setVisibility(View.VISIBLE);
        } else {
            mMixerAdapter = new MixerAdapter(getContext(), data);
            mMixerItems.setAdapter(mMixerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}