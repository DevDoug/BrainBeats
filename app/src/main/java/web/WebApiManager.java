package web;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.brainbeats.R;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import architecture.AccountManager;
import utils.Constants;

/**
 * Created by douglas on 5/24/2016.
 */
public class WebApiManager {

    public static final int MY_SOCKET_TIMEOUT_MS = 30000;

    public static final String API_CONNECT_URL_FORMATED      = "https://soundcloud.com/connect";

    //Sound cloud API links
    public static final String API_CONNECT_URL       = "https://soundcloud.com/connect";
    public static final String API_OAUTH             = "https://api.soundcloud.com/oauth2/token";
    public static final String API_ROOT_URL          = "https://api.soundcloud.com";
    public static final String API_TRACKS_URL        = "/tracks/";
    public static final String API_PLAYLIST_URL      = "/playlists/";
    public static final String API_USER_URL          = "/users/";
    public static final String API_USER_FOLLOWING_URL = "/followings";
    public static final String API_FAVORITES_URL     = "/favorites/";
    public static final String API_ME_URL            = "/me";

    //Sound cloud API links version 2
    public static final String API_VERSION_TWO_BASE_URL = "https://api-v2.soundcloud.com/tracks";
    public static final String API_VERSION_TWO_RELATED_TRACKS_URL = "/%s/related";

    //Sound cloud API keys
    public static final String SOUND_CLOUD_API_KEY_CLIENT_ID     = "client_id";
    public static final String SOUND_CLOUD_API_KEY_CLIENT_SECRET = "client_secret";
    public static final String SOUND_CLOUD_API_KEY_OAUTH_TOKEN   = "oauth_token";

    //Sound Cloud track search params
    public static final String SOUND_CLOUD_QUERY_FILTER_QUERY = "q";
    public static final String SOUND_CLOUD_QUERY_FILTER_TAGS = "tags";
    public static final String SOUND_CLOUD_QUERY_FILTER_LIMIT = "limit";
    public static final String SOUND_CLOUD_QUERY_FILTER_LINKED_PARTITIONING = "linked_partitioning";

    //Sound Cloud track filter params
    public static final String SOUND_CLOUD_QUERY_FILTER_INSTRUMENTAL            =  "instrumental";
    public static final String SOUND_CLOUD_QUERY_FILTER_PARAM_POPULAR           =  "popular";
    public static final String SOUND_CLOUD_QUERY_FILTER_PARAM_RECENT            =  "recent";
    public static final String SOUND_CLOUD_QUERY_FILTER_PARAM_A_TO_Z         =  "popular";
    public static final String SOUND_CLOUD_QUERY_FILTER_PARAM_LIMIT_ONE_HUNDRED = "100";
    public static final String SOUND_CLOUD_QUERY_FILTER_PARAM_LIMIT_TWO_HUNDRED = "200";
    public static final String SOUND_CLOUD_QUERY_FILTER_PARAM_LINKED_ENABLED = "1";


    public interface OnObjectResponseListener {
        void onObjectResponse(JSONObject object);
    }

    public interface OnArrayResponseListener {
        void onArrayResponse(JSONArray array);
    }

    public interface OnErrorListener {
        void onErrorResponse(VolleyError error);
    }

    public static void getAccessToken(Context context, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);
        mParams.put("client_secret", "09e8c5b6f91e2ab440b9137008d2d32c");
        mParams.put("redirect_uri", "BrainBeats://soundcloud/callback");

        String url = API_CONNECT_URL;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getTrack(Context context, String urlPart, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_ROOT_URL + API_TRACKS_URL + urlPart;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }

    }

    public static void getTracks(Context context, String queryFilter, String filterTags, final OnArrayResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);
        mParams.put(SOUND_CLOUD_QUERY_FILTER_TAGS, filterTags);
        mParams.put(SOUND_CLOUD_QUERY_FILTER_LIMIT, SOUND_CLOUD_QUERY_FILTER_PARAM_LIMIT_TWO_HUNDRED);
        //mParams.put(SOUND_CLOUD_QUERY_FILTER_LINKED_PARTITIONING,"1");

        if (queryFilter != null && !queryFilter.equalsIgnoreCase(""))
            mParams.put(SOUND_CLOUD_QUERY_FILTER_QUERY, queryFilter);

        String url = API_ROOT_URL + API_TRACKS_URL;
        try {
            JSONArray jsonRequest = new JSONArray();
            sendArrayRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getRelatedTracks(Context context, String urlPart, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_VERSION_TWO_BASE_URL + API_VERSION_TWO_RELATED_TRACKS_URL;
        if (urlPart != null) {
            url = String.format(url, urlPart);
        }

        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getUserTracks(Context context, String userId, final OnArrayResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_ROOT_URL + API_USER_URL + userId + API_TRACKS_URL;
        try {
            JSONArray jsonRequest = new JSONArray();
            sendArrayRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getUserPlaylists(Context context, String userId, final OnArrayResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_ROOT_URL + API_USER_URL + userId + API_PLAYLIST_URL;
        try {
            JSONArray jsonRequest = new JSONArray();
            sendArrayRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getUserFavorites(Context context, String userId, final OnArrayResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_ROOT_URL + API_USER_URL + userId + API_FAVORITES_URL;
        try {
            JSONArray jsonRequest = new JSONArray();
            sendArrayRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void putUserTrack(Context context, String trackId, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_ROOT_URL + API_TRACKS_URL + trackId;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.PUT, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void putUserFavorite(Context context, String userId, String trackID, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);
        mParams.put(SOUND_CLOUD_API_KEY_OAUTH_TOKEN, AccountManager.getInstance(context).getAccessToken());

        String url = API_ROOT_URL + API_USER_URL + userId + API_FAVORITES_URL + trackID;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.PUT, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getSoundCloudUser(Context context, String userId, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);

        String url = API_ROOT_URL + API_USER_URL + userId;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getSoundCloudSelf(Context context, String oauthToken, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_OAUTH_TOKEN, oauthToken);
        String url = API_ROOT_URL + API_ME_URL;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }
    }

    public static void getUserFollowing(Context context, String userId, final OnObjectResponseListener responseListener, final OnErrorListener errorListener) {
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put(SOUND_CLOUD_API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);
        String url = API_ROOT_URL + API_USER_URL + userId + API_USER_FOLLOWING_URL;
        try {
            JSONObject jsonRequest = new JSONObject();
            sendObjectRequest(context, Request.Method.GET, url, mParams, jsonRequest, responseListener, errorListener);
        } catch (Exception ex) {
            errorListener.onErrorResponse(new VolleyError(context.getString(R.string.unknown_volley_error)));
        }

    }

    public static void sendArrayRequest(Context context, int method, final String url, final HashMap<String, String> urlParams, final JSONArray requestParam, final OnArrayResponseListener onArrayResponseListener, final OnErrorListener onErrorListener) {
        JsonRequest request = new JsonArrayRequest(method, url, requestParam, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                onArrayResponseListener.onArrayResponse(response);
                response.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorListener.onErrorResponse(error);
            }
        }) {
            //Note: Need to override this method to put params in the URL, because JsonObjectRequest does not call getParams().
            @Override
            public String getUrl() {
                //NOTE: Code in here need to be replicated in the other case below (in case of JsonObjectRequest).
                StringBuilder stringBuilder = new StringBuilder(url);
                if (urlParams != null && urlParams.size() > 0) {
                    int i = 1;
                    for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                        if (i == 1) {
                            stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                        } else {
                            stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                        }
                        i++;
                    }
                }
                return stringBuilder.toString();
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                WebApiManager.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonVolley.getInstance(context).addToRequestQueue(request);
    }

    public static void sendObjectRequest(Context context, int method, final String url, final HashMap<String, String> urlParams, final JSONObject requestParam, final OnObjectResponseListener listener, final OnErrorListener onErrorListener) {
        JsonRequest request = new JsonObjectRequest(method, url, requestParam, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onObjectResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onErrorListener.onErrorResponse(error);
            }
        }) {
            //Note: Need to override this method to put params in the URL, because JsonObjectRequest does not call getParams().
            @Override
            public String getUrl() {
                //NOTE: Code in here need to be replicated in the other case below (in case of JsonObjectRequest).
                StringBuilder stringBuilder = new StringBuilder(url);
                if (urlParams != null && urlParams.size() > 0) {
                    int i = 1;
                    for (Map.Entry<String, String> entry : urlParams.entrySet()) {
                        if (i == 1) {
                            stringBuilder.append("?" + entry.getKey() + "=" + entry.getValue());
                        } else {
                            stringBuilder.append("&" + entry.getKey() + "=" + entry.getValue());
                        }
                        i++;
                    }
                }

                return stringBuilder.toString();
            }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                //headers.put(API_KEY_CLIENT_ID, Constants.SOUND_CLOUD_CLIENT_ID);
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                WebApiManager.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        SingletonVolley.getInstance(context).addToRequestQueue(request);
    }
}
