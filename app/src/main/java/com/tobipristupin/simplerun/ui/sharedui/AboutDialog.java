package com.tobipristupin.simplerun.ui.sharedui;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.tobipristupin.simplerun.BuildConfig;
import com.tobipristupin.simplerun.R;

public class AboutDialog {

    /**
     * Shows about dialog. This method does not follow the convention in the other Dialog classes to
     * return the dialog instead of showing it because it needs to add hyperlinks to the text and that has
     * to be done after calling show().
     *
     * @param context
     */
    public static void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.aboutdialog_title));

        String message = String.format(context.getString(R.string.about_dialog), BuildConfig.VERSION_NAME);
        final SpannableString spannableString = new SpannableString(message);
        Linkify.addLinks(spannableString, Linkify.ALL);

        builder.setMessage(spannableString);

        AlertDialog dialog = builder.show();

        ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
    }
}
