package com.clearteam.phuotnhom.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.clearteam.phuotnhom.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    public static ProgressDialog progressDialog;

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

    public static void showLoading(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog_loading);
        if (progressDialog.getWindow() != null) {
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public static void hideLoading() {
        progressDialog.dismiss();
    }

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

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, Const.WIDTH_MARKER, Const.HEIGHT_MARKER, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }

    public static BitmapDescriptor bitmapDescriptorFromVectorFriend(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, Const.WIDTH_MARKER_FRIEND, Const.HEIGHT_MARKER_FRIEND, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }


    public static BitmapDescriptor getBitmapFromURL(Bitmap bitmap) {

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }

    public static String formatKM(double data) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator('.');
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###,###", symbols);
            return decimalFormat.format(data);
        } catch (Exception ex) {
            Log.e("formatKM: ", ex.getMessage());
        }

        return (String.valueOf(data) + " đ");

    }

    public static String formatTimeDirection(String text) {
        String s = text;
        return text.replace("hours", "giờ").replace("mins", "phút").replace("hour", "giờ").replace("min", "phút");
    }

}
