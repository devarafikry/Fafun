package ttc.project.fafun.helper;

import ttc.project.fafun.R;

/**
 * Created by Fikry-PC on 10/19/2017.
 */

public class PeriodHelper {
    // 0 = daily
    // 1 = weekly
    // 2 = monthly
    // 3 = once

    public static int getPeriodColor(int color){
        switch (color){
            case 0:
                return R.color.colorDaily;
            case 1:
                return R.color.colorWeekly;
            case 2:
                return R.color.colorMonthly;
            case 3:
                return R.color.colorOnce;
//            case 4:
//                return R.color.colorBehaviour;
            default:
                return R.color.colorDaily;
        }
    }
}
