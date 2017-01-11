package com.afra55.baseclient.common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.afra55.baseclient.view.CustomDialog;


public class AlertUtils {

	public interface OnDialogClickListener{
		void onConfirm(DialogInterface dialog, int which);

		void onCancel(DialogInterface dialog, int which);
	}

	private static CustomDialog dialog = null;
	/**
	 * 弹出框
	 * @param context context
	 * @param title title
	 * @param content content
	 * @param ok ok show text
	 * @param cancel cancel show text
	 * @param clickListener  OnDialogClickListener
	 * @return
	 */
	public static CustomDialog.Builder showAlert(final Context context,
                                                 final String title, String content, final String ok,
                                                 final String cancel, final OnDialogClickListener clickListener) {
		if (context instanceof Activity && ((Activity) context).isFinishing() || (dialog!=null && dialog.isShowing())) {
			Toast.makeText(context, "null"+ (dialog!=null && dialog.isShowing()), Toast.LENGTH_SHORT).show();
			return null;
		}
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNegativeButton(cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if(clickListener!=null){

					clickListener.onCancel(dialog, which);
				}
			}
		});
		builder.setPositiveButton(ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (clickListener != null) {
					clickListener.onConfirm(dialog, which);
				}
			}
		});
		dialog = builder.create();
		dialog.show();
		return builder;
	}

	public static CustomDialog.Builder showConfirmAlert(final Context context,
                                                        final String title, String content) {
		if (context instanceof Activity && ((Activity) context).isFinishing() || (dialog!=null && dialog.isShowing())) {
			return null;
		}
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
		return builder;
	}

	public static CustomDialog.Builder showConfirmAlert(final Context context,
                                                        final String title, final String confirm, String content) {
		if (context instanceof Activity && ((Activity) context).isFinishing()  || (dialog!=null && dialog.isShowing())) {
			return null;
		}
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton(confirm, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog = builder.create();
		dialog.show();
		return builder;
	}

	public static CustomDialog.Builder showConfirmAlert(final Context context,
                                                        final String title, String content, final OnClickListener lOk) {
		if (context instanceof Activity && ((Activity) context).isFinishing()  || (dialog!=null && dialog.isShowing())) {
			return null;
		}
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				lOk.onClick(dialog, which);
			}
		});
		dialog = builder.create();
		dialog.show();
		return builder;
	}
}
