package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.brainbeats.MainActivity;
import com.brainbeats.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapters.ImageAdapter;
import adapters.SearchMusicAdapter;
import adapters.TrackAdapter;
import architecture.AccountManager;
import entity.Playlists;
import entity.Track;
import entity.User;
import entity.UserTrackResponse;
import utils.BeatLearner;
import utils.Constants;
import web.WebApiManager;

public class DashboardFragment extends Fragment {

    public static final String TAG = "DashboardFragment";

    private RecyclerView mTrackGrid;
    private SearchMusicAdapter mTrackAdapter;
    private GridLayoutManager mBeatGridLayoutManager;
    List<Track> trackList = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).getToolBar();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mTrackGrid = (RecyclerView) v.findViewById(R.id.category_grid);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_dashboard, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get item selected and deal with it
        switch (item.getItemId()) {
            case R.id.action_login:
                doLogin();
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList<Track> trackCategory = new ArrayList<>();
        for(int i = 0; i < getResources().getStringArray(R.array.beat_categories).length;i++){
            Track  defaultTrack = new Track();
            defaultTrack.setTitle(getResources().getStringArray(R.array.beat_categories)[i]);
            trackCategory.add(defaultTrack);
        }
        trackList = trackCategory;
        mTrackAdapter = new SearchMusicAdapter(getContext(), trackList);
        mBeatGridLayoutManager = new GridLayoutManager(getContext(), Constants.GRID_SPAN_COUNT);
        mTrackGrid.setLayoutManager(mBeatGridLayoutManager);
        mTrackGrid.setAdapter(mTrackAdapter);
        mTrackAdapter.notifyDataSetChanged();

        WebApiManager.getSoundCloudUser(getContext(), AccountManager.getInstance(getContext()).getAccessToken(), new WebApiManager.OnObjectResponseListener() {
            @Override
            public void onObjectResponse(JSONObject object) {
                object.toString();
                Log.i(getClass().getSimpleName(), "Response = " + object.toString());
                Gson gson = new Gson();
                Type token = new TypeToken<User>(){}.getType();
                try {
                    User user = gson.fromJson(object.toString(), token);
                    AccountManager.getInstance(getContext()).setUserId(String.valueOf(user.getId()));
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

    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public void doLogin(){
        mTrackGrid.setVisibility(View.GONE);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        final WebView webView = (WebView) getActivity().findViewById(R.id.webView);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.requestFocus();
        webView.requestFocusFromTouch();
        webView.requestFocus(View.FOCUS_DOWN);

        String urlToken = WebApiManager.API_CONNECT_URL + "?client_id=" + Constants.SOUND_CLOUD_CLIENT_ID + "&redirect_uri=" + "http://localhost" + "&response_type=token";
        String urlAccessToken = WebApiManager.API_OAUTH + "?client_id=" + Constants.SOUND_CLOUD_CLIENT_ID + "&client_secret=" + Constants.SOUND_CLOUD_CLIENT_SECRET +
                "&redirect_uri=" + "http://localhost" + "&grant_type=authorization_code" + "code" + AccountManager.getInstance(getContext()).getAccessToken();

        webView.loadUrl(urlToken);
        webView.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);
                progressDialog.setMessage("Connecting SoundCloud");
                progressDialog.show();
            }
            String authCode="";
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
                Log.i("URLLLL", url);
                if (url.contains("access_token=") && authComplete != true) {
                    for(int t=32; t <url.length();t++){
                        if(!(url.charAt(t) == '&'))
                            authCode = authCode+url.charAt(t);
                        else
                            break;
                    }
                    Log.i("CODE true", "CODE : " + authCode);
                    authComplete = true;
                    AccountManager.getInstance(getContext()).setAccessToken(authCode);
                }else if(url.contains("error=access_denied")){
                    Log.i("CODE false", "ACCESS_DENIED_HERE");
                    authComplete = true;
                    progressDialog.dismiss();
                }
            }
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        String s = AccountManager.getInstance(getContext()).getAccessToken();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
