package com.example.climbingworkout;

import android.content.res.Resources;
import android.util.TypedValue;

public class Utility {

    private Utility() {
    }

    public static int dpToPx(Float dp, Resources r){
        /*
        https://stackoverflow.com/questions/4605527/converting-pixels-to-dp
        05/04/2020
         */
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        ));
        return px;
    }

}
