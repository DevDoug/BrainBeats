package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.brainbeats.R;
import java.util.ArrayList;
import java.util.List;
import adapters.LibraryAdapter;
import model.Beat;
import utils.Constants;

public class LibraryTabFragment extends Fragment {

    List<Beat> beatList;
    private RecyclerView mBeatListView;
    private LibraryAdapter mLibraryAdapter;

    public LibraryTabFragment() {
        // Required empty public constructor
    }

    public static LibraryTabFragment newInstance(ArrayList<Beat> data) {
        LibraryTabFragment tabFragment = new LibraryTabFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.KEY_EXTRA_BEAT_LIST,data);
        tabFragment.setArguments(args);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            beatList = args.getParcelableArrayList(Constants.KEY_EXTRA_BEAT_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library_tab, container, false);
        mBeatListView = (RecyclerView) v.findViewById(R.id.library_content_list);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLibraryAdapter = new LibraryAdapter(getContext(),beatList);
        mBeatListView.setAdapter(mLibraryAdapter);
    }
}
