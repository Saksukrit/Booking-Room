package com.example.wolf_z.bookingroom.Menu.Profile_Setting;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.R;

import java.util.Objects;


public class Profile_Change_Password_Fragment extends Fragment {

    private Button btnSave_Password;
    private EditText edit_current_password;
    private EditText edit_new_password;
    private EditText edit_new_again_password;

    public Profile_Change_Password_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_profile__change__password_, container, false);

        edit_current_password = (EditText) view.findViewById(R.id.edit_current_password);
        edit_new_password = (EditText) view.findViewById(R.id.edit_new_password);
        edit_new_again_password = (EditText) view.findViewById(R.id.edit_new_again_password);
        btnSave_Password = (Button) view.findViewById(R.id.btnSave_Password);
        btnSave_Password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(edit_current_password.getText().toString(), "") || Objects.equals(edit_current_password.getText().toString(), null)) {
                    Snackbar.make(view, "Not current password", Snackbar.LENGTH_SHORT).show();
                } else if (Objects.equals(edit_new_password.getText().toString(), "") || Objects.equals(edit_new_password.getText().toString(), null)) {
                    Snackbar.make(view, "Not new password", Snackbar.LENGTH_SHORT).show();
                } else if (Objects.equals(edit_new_again_password.getText().toString(), "") || Objects.equals(edit_new_again_password.getText().toString(), null)) {
                    Snackbar.make(view, "Not again password", Snackbar.LENGTH_SHORT).show();
                } else {
                    // task
                    // if task success,go to main

                }
            }
        });

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
