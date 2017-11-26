package com.example.textdecorator.textdecorator.textcolordialog;

import android.content.Context;

/**
 * Created by libo on 2017/11/24.
 */

public class CommonUtil {
    public static int dip2px(Context context, int dp){
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5);
    }
}
