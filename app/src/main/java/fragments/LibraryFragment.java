package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import adapters.ViewPagerAdapter;
import entity.Track;
import entity.UserPlaylistsResponse;
import entity.UserTrackResponse;
import web.WebApiManager;

public class LibraryFragment extends Fragment {

    ArrayList<Track> mTrackList = new ArrayList<>();
    ArrayList<Track> mTrackPlayList = new ArrayList<>();
    ArrayList<Track> mFavoriteTrackList = new ArrayList<>();
    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;

    public LibraryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserSongs();
        getUserPlaylist();
        getUserFavorites();
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
        adapter.addFragment(LibraryTabFragment.newInstance(mTrackList), getResources().getStringArray(R.array.library_tab_titles)[0]);
        adapter.addFragment(LibraryTabFragment.newInstance(mTrackPlayList), getResources().getStringArray(R.array.library_tab_titles)[1]);
        adapter.addFragment(LibraryTabFragment.newInstance(mFavoriteTrackList), getResources().getStringArray(R.array.library_tab_titles)[2]);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    //TODO: Replace dummy data with real data from sound cloud
    public ArrayList<Track> getUserSongs() {
        WebApiManager.getUserTracks(getContext(), "3207", new WebApiManager.OnArrayResponseListener() {
            @Override
            public void onArrayResponse(JSONArray array) {
                Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<ArrayList<UserTrackResponse>>() {
                }.getType();
                try {
                    ArrayList<UserTrackResponse> userTracks = gson.fromJson(array.toString(), token);
                    for(int i = 0; i < userTracks.size();i++){
                        Track tempTrack = new Track();
                        tempTrack.setTitle(userTracks.get(i).getTitle());
                        mTrackList.add(tempTrack);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.toString();
            }
        });
        return null;
    }

    public void getUserPlaylist() {
        WebApiManager.getUserPlaylists(getContext(), "3207", new WebApiManager.OnArrayResponseListener() {
            @Override
            public void onArrayResponse(JSONArray array) {
                Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<ArrayList<UserPlaylistsResponse>>() {
                }.getType();
                try {
                    ArrayList<UserPlaylistsResponse> userPlaylists = gson.fromJson(array.toString(), token);
                    mTrackPlayList = (ArrayList<Track>) userPlaylists.get(0).getTracks();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "Response = " + error.toString());
            }
        });
    }

    public void getUserFavorites() {
        WebApiManager.getUserFavorites(getContext(), "3207", new WebApiManager.OnArrayResponseListener() {
            @Override
            public void onArrayResponse(JSONArray array) {
                Log.i(getClass().getSimpleName(), "Response = " + array.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<ArrayList<UserTrackResponse>>() {
                }.getType();
                try {
                    ArrayList<UserTrackResponse> userTracks = gson.fromJson(array.toString(), token);
                    for(int i = 0; i < userTracks.size();i++){
                        Track tempTrack = new Track();
                        tempTrack.setTitle(userTracks.get(i).getTitle());
                        mFavoriteTrackList.add(tempTrack);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, new WebApiManager.OnErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "Response = " + error.toString());
            }
        });
    }
}
