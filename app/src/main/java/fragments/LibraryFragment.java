package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brainbeats.R;

import java.util.ArrayList;

import adapters.ViewPagerAdapter;
import model.Mix;

public class LibraryFragment extends Fragment {

    ArrayList<Mix> mixList = new ArrayList<>();
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
        adapter.addFragment(LibraryTabFragment.newInstance(mixList), getResources().getStringArray(R.array.library_tab_titles)[0]);
        adapter.addFragment(LibraryTabFragment.newInstance(mixList), getResources().getStringArray(R.array.library_tab_titles)[1]);
        adapter.addFragment(LibraryTabFragment.newInstance(mixList), getResources().getStringArray(R.array.library_tab_titles)[2]);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //TODO: Replace dummy data with real data from sound cloud
    public void getBeatData(){
        mixList.add(new Mix());
        mixList.get(0).setMixTitle("Focus");
        mixList.add(new Mix());
        mixList.get(1).setMixTitle("Meditation");
        mixList.add(new Mix());
        mixList.get(2).setMixTitle("Relaxation");
        mixList.add(new Mix());
        mixList.get(3).setMixTitle("Yoga");
    }
}
