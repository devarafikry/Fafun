package ttc.project.fafun;

/**
 * Created by Fikry-PC on 10/19/2017.
 */

public class CategoryHelper {
    // 0 = home
    // 1 = academic
    // 2 = accomplishment
    // 3 = manner
    // 4 = behaviour
    public static int getCategoryColor(int color){
        switch (color){
            case 0:
                return R.color.colorHome;
            case 1:
                return R.color.colorAcademic;
            case 2:
                return R.color.colorAccomplishment;
            case 3:
                return R.color.colorManner;
            case 4:
                return R.color.colorBehaviour;
            default:
                return R.color.colorHome;
        }
    }
}
