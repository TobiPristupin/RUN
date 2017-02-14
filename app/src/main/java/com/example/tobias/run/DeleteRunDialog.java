package com.example.tobias.run;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DeleteRunDialog {

    private Context context;
    //Id of tracked run object to delete from database
    private int id;

    public DeleteRunDialog(Context context, int id){
        this.context = context;
        this.id = id;
    }


    public AlertDialog make() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Are you sure you want to delete?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //new DatabaseHandler(context).deleteItem(id);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
