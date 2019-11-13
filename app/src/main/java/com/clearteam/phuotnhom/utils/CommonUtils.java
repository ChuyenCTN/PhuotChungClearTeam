package com.clearteam.phuotnhom.utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clearteam.phuotnhom.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

//    public static ProgressDialog showLoadingDialog(Context context) {
//        ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.show();
//        if (progressDialog.getWindow() != null) {
//            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
//        progressDialog.setContentView(R.layout.progress_dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(true);
//        progressDialog.setCanceledOnTouchOutside(false);
//        return progressDialog;
//    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showError(Context context, String message) {
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.txt_alert_warning))
                .content(message)
                .positiveText(R.string.txt_label_ok)
                .negativeText(R.string.txt_label_cancel)
                .onNegative((dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showDialog(Context context, String title, String message, MaterialDialog.SingleButtonCallback handleOk,
                                  MaterialDialog.SingleButtonCallback handleCancel) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title(title)
                .content(message);


        if (handleOk != null) {
            builder.positiveText(R.string.txt_label_agree)
                    .onPositive(handleOk);
        }

        if (handleCancel != null) {
            builder.negativeText(R.string.txt_label_cancel)
                    .onNegative(handleCancel);
        }

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public static void showDialogMessage(Context context, String message) {
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.txt_alert_warning))
                .content(message)
                .negativeText(R.string.txt_alert_close)
                .onPositive((dialog, which) -> dialog.dismiss())
                .show();
    }

}