package com.afra55.commontutils.device;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by Victor Yang on 2016/4/13.
 *
 */
public class LanguageSettingUtil {

    /**
     * @param context
     * @param baseContext   getBaseContext()
     * @param language_flag
     */
    public static void changeLanguage(Context context, Context baseContext, Class<?> restartActivity, int language_flag, boolean needRestart) {
        Locale mWillBeLocation;
        Configuration configuration = baseContext.getResources().getConfiguration();
        switch (language_flag) {
            case 0:
                mWillBeLocation = Locale.ENGLISH;
                break;
            case 1:
                mWillBeLocation = new Locale("es", "ES");
                break;
            case 2:
                mWillBeLocation = Locale.CHINA;
                break;
            default:
                mWillBeLocation = Locale.getDefault();
                break;
        }
        if (configuration.locale.getLanguage().equals(mWillBeLocation.getLanguage())
                && configuration.locale.getCountry().equals(mWillBeLocation.getCountry())) {
            return;
        }
        configuration.locale = mWillBeLocation;
        baseContext.getResources().updateConfiguration(configuration, null);
        if (needRestart) {
            Intent intent = new Intent(context, restartActivity);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }
}
