package tech.vengine.v_browser.core.miscellaneous;


import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dalton on 7/22/2017.
 */

public class Content {

    public static List<webViewTabs> webViewTabList = new ArrayList<webViewTabs>();

    public static class webViewTabs {
        public Boolean ACTIVE;
        public String TITLE;
        public String ICON;
        public Bitmap THUMBNAIL;
        public String TAG;
        public String URL;

        public webViewTabs(
                Boolean ACTIVE,
                String TITLE,
                String ICON,
                Bitmap THUMBNAIL,
                String TAG,
                String URL) {
            this.ACTIVE = ACTIVE;
            this.TITLE = TITLE;
            this.ICON = ICON;
            this.THUMBNAIL = THUMBNAIL;
            this.TAG = TAG;
            this.URL = URL;
        }
    }

}
