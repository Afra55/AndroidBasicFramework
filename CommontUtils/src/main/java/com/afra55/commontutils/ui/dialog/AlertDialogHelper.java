package com.afra55.commontutils.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


/**
 * Created by Victor Yang on 2017/1/9 0009.
 * AlertDialogHelper.
 */

public class AlertDialogHelper {
    private AlertDialogHelper() {

    }

    public static AlertDialog show(
            @NonNull Context context
            , String title
            , String msg
            , String positiveButtonText
            , DialogInterface.OnClickListener positiveButtonListener
            , String negativeButtonText
            , DialogInterface.OnClickListener negativeButtonListener

        ) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(msg)
                        .setPositiveButton(positiveButtonText, positiveButtonListener)
                        .setNegativeButton(negativeButtonText, negativeButtonListener)
                        .create();


        alertDialog.show();

//        Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        if (positiveBtn != null) {
//            positiveBtn
//                    .setTextColor(context.getResources().getColor(R.color.orange_f6a500));
//        }
//
//        Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        if (negativeBtn != null) {
//            negativeBtn
//                    .setTextColor(context.getResources().getColor(R.color.yellow_959595));
//        }

        return alertDialog;
    }

    public static AlertDialog show(
            @NonNull Context context
            , String title
            , String msg
            , String positiveButtonText
            , DialogInterface.OnClickListener positiveButtonListener) {

        return show(context, title, msg, positiveButtonText, positiveButtonListener, null, null);
    }
}
