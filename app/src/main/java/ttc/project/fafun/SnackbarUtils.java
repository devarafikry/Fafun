package ttc.project.fafun;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Fikry-PC on 10/14/2017.
 */

public class SnackbarUtils {
    public static void showSnackbar(View view, Snackbar s, String message, int duration){
        if(s != null){
            s.dismiss();
        }
        s = Snackbar.make(view, message, duration);
        s.show();
    }
}
