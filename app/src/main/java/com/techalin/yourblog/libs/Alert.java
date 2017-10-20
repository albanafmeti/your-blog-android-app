package com.techalin.yourblog.libs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Alert {

    private AlertDialog alertDialog;
    private AlertDialog.Builder alertBuilder;
    private Context context;

    private boolean warningResponse = false;

    public Alert(Context context) {
        this.context = context;
        alertBuilder = new AlertDialog.Builder(context);
        alertDialog = alertBuilder.create();
    }

    public void showAlert(String title, String message) {

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.tick);

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public boolean showWarningAlert(String title, String message) {

        this.warningResponse = false;

        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);

        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                warningResponse = true;
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                warningResponse = false;
                dialog.dismiss();
            }
        });

        alertBuilder.create().show();
        return this.warningResponse;
    }
}
