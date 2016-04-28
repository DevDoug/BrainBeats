package utils;

import com.brainbeats.R;

/**
 * Created by Douglas on 4/20/2016.
 */
public class Constants {

    public static final int GRID_SPAN_COUNT = 3;

    // Nav drawer items
    public static final int NAVDRAWER_ITEM_DASHBOARD = 0;
    public static final int NAVDRAWER_ITEM_LIBRARY = 1;
    public static final int NAVDRAWER_ITEM_MIXER = 2;
    public static final int NAVDRAWER_ITEM_SOCIAL = 3;

    // Titles for navdrawer items (indices must correspond to the above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
            R.string.nav_item_label_dashboard,
            R.string.nav_item_label_library,
            R.string.nav_item_label_mixer,
            R.string.nav_item_label_social,
    };

    // Icons for navdrawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[]{
            R.drawable.ic_dashboard,
            R.drawable.ic_library_music,
            R.drawable.ic_album,
            R.drawable.ic_navview_social,
    };

}
