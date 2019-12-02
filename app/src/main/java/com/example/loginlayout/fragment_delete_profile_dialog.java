package com.example.loginlayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class fragment_delete_profile_dialog extends AppCompatDialogFragment {
    private EditText editTextMail;
    private EditText editTextPasswd;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_userdel, null);

        builder.setView(view)
                .setTitle("Delete Account")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = editTextMail.getText().toString();
                        String passwd = editTextPasswd.getText().toString();
                        listener.DeleteAccount(email, passwd);
                    }
                });

        editTextMail = view.findViewById(R.id.user_mail);
        editTextPasswd = view.findViewById(R.id.user_passwd);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }

    }

    public interface DialogListener{
        void DeleteAccount(String email, String passwd);
    }
}
