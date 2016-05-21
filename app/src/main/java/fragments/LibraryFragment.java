package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brainbeats.LibraryActivity;
import com.brainbeats.R;

import java.util.ArrayList;
import java.util.List;

import adapters.BeatAdapter;
import adapters.ViewPagerAdapter;
import fragments.LibraryTabFragment;
import model.Beat;
import utils.Constants;

public class LibraryFragment extends Fragment {

    ArrayList<Beat> beatList = new ArrayList<>();
    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;

    public LibraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBeatData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_library, container, false);
        mTabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) v.findViewById(R.id.base_viewpager);
        setupViewPager();
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(LibraryTabFragment.newInstance(beatList), getResources().getStringArray(R.array.library_tab_titles)[0]);
        adapter.addFragment(LibraryTabFragment.newInstance(beatList), getResources().getStringArray(R.array.library_tab_titles)[1]);
        adapter.addFragment(LibraryTabFragment.newInstance(beatList), getResources().getStringArray(R.array.library_tab_titles)[2]);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getBeatData(){
        beatList.add(new Beat());
        beatList.get(0).setBeatTitle("Focus");
        beatList.add(new Beat());
        beatList.get(1).setBeatTitle("Meditation");
        beatList.add(new Beat());
        beatList.get(2).setBeatTitle("Relaxation");
        beatList.add(new Beat());
        beatList.get(3).setBeatTitle("Yoga");
    }
}
