package com.example.wolf_z.bookingroom.Menu.Profile_Setting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wolf_z.bookingroom.R;

import java.util.Objects;


public class Profile_Change_Displayname_Fragment extends Fragment {

    private Button btnSave_Displayname;
    private EditText edittext_new_displayname;
    private EditText edittext_password_confirm;

    public Profile_Change_Displayname_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile__change__displayname_, container, false);

        edittext_new_displayname = (EditText) view.findViewById(R.id.edittext_new_displayname);
        edittext_password_confirm = (EditText) view.findViewById(R.id.edittext_password_confirm);
        btnSave_Displayname = (Button) view.findViewById(R.id.btnSave_Displayname);
        btnSave_Displayname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(edittext_new_displayname.getText().toString(), "") || Objects.equals(edittext_new_displayname.getText().toString(), null)) {
                    Snackbar.make(view, "Not displayname", Snackbar.LENGTH_SHORT).show();
                } else if (Objects.equals(edittext_password_confirm.getText().toString(), "") || Objects.equals(edittext_password_confirm.getText().toString(), null)) {
                    Snackbar.make(view, "Not password confirm", Snackbar.LENGTH_SHORT).show();
                } else {
                    //task
                    //if task success, go to main
                }
            }
        });


        return view;
    }


}
