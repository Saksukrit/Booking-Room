package com.example.wolf_z.bookingroom.Createbooking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class SubjectFragment extends Fragment {

    protected ProgressDialog prgDialog;
    private Spinner starttimeHr;
    private Spinner starttimeMin;
    private Spinner endtimeHr;
    private Spinner endtimeMin;
    private EditText ETsubject;
    private EditText ETdetail;
    private RadioGroup meeting_type_rediogroup;
    protected RadioButton meeting_redioButton;
    private Spinner room_spinner;
    private TextView date_show;
    private String date_send;
    private TextView projector_txt;
    private Spinner projector2_spinner;
    private Button select;
    private Button cancel;


    protected String settime;
    protected String settotime;
    private final ArrayList<String> hr = new ArrayList<>();
    private final ArrayList<String> min = new ArrayList<>();
    private DatePickerDialog fromDatePickerDialog;
    protected SimpleDateFormat dateFormatSend;
    protected Createbooking createbooking;
    private View view;

    private int oldposition_starthr;
    private int oldposition_startmin;
    private int oldposition_endhr;
    private int oldposition_endmin;
    private String old_projector_txt = "click select projector";

    public SubjectFragment(Createbooking createbooking) {
        this.createbooking = createbooking;
    }

    public SubjectFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_createbooking_subject, container, false);

        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        ETsubject = (EditText) view.findViewById(R.id.ETsubject);
        ETdetail = (EditText) view.findViewById(R.id.ETdetail);


        /** meeting_type */
        meeting_type_rediogroup = (RadioGroup) view.findViewById(R.id.meeting_type);


        /** Date picker*/
        date_show = (TextView) view.findViewById(R.id.date);
        getDatePicker();
        date_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
                projector_txt.setText("click select projector");
                createbooking.setProjector_accept(false);
            }
        });

        /** Time */
        starttimeHr = (Spinner) view.findViewById(R.id.timeHr);
        starttimeMin = (Spinner) view.findViewById(R.id.timeMin);
        endtimeHr = (Spinner) view.findViewById(R.id.totimeHr);
        endtimeMin = (Spinner) view.findViewById(R.id.totimeMin);
        getSpinnerTimeData();
        setSpinnerTimeDateEvent();


        /** select room_spinner */
        room_spinner = (Spinner) view.findViewById(R.id.room);
        getSpinnerRoom();

        /**projector_txt_spinner*/
        projector_txt = (TextView) view.findViewById(R.id.txtprojector);
        getDialogSpinnerProjector();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * State
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * get Event each view
     *********************************************/

    private void getDatePicker() {
        Calendar caledar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                /****format*/
                date_show.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + (year + 543));

            }
        }, caledar.get(Calendar.YEAR), caledar.get(Calendar.MONTH), caledar.get(Calendar.DAY_OF_MONTH));
    }

    private void getSpinnerTimeData() {
        /** Time */
        hr.add("8");
        hr.add("9");
        hr.add("10");
        hr.add("11");
        hr.add("12");
        hr.add("13");
        hr.add("14");
        hr.add("15");
        hr.add("16");
        hr.add("17");
        hr.add("18");
        hr.add("19");
        hr.add("20");
        hr.add("21");
        hr.add("22");

        min.add("00");
        min.add("15");
        min.add("30");
        min.add("45");

        ArrayAdapter<String> adapterhr = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, hr);
        adapterhr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> adaptermin = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, min);
        adaptermin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        starttimeHr.setAdapter(adapterhr);
        starttimeMin.setAdapter(adaptermin);

        endtimeHr.setAdapter(adapterhr);
        endtimeHr.setSelection(adapterhr.getPosition(hr.get(4)));  //set default show
        endtimeMin.setAdapter(adaptermin);
    }

    public void setSpinnerTimeDateEvent() {
        starttimeHr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (oldposition_starthr != position) {
                    projector_txt.setText("click select projector");
                    createbooking.setProjector_accept(false);
                } else {
                    projector_txt.setText(old_projector_txt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        starttimeMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (oldposition_startmin != position) {
                    projector_txt.setText("click select projector");
                    createbooking.setProjector_accept(false);
                } else {
                    projector_txt.setText(old_projector_txt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        endtimeHr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (oldposition_endhr != position) {
                    projector_txt.setText("click select projector");
                    createbooking.setProjector_accept(false);
                } else {
                    projector_txt.setText(old_projector_txt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        endtimeMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (oldposition_endmin != position) {
                    projector_txt.setText("click select projector");
                    createbooking.setProjector_accept(false);
                } else {
                    projector_txt.setText(old_projector_txt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getSpinnerRoom() {
        ArrayAdapter<String> adapterroom = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, createbooking.getRoomshow_spinner_arraylist());
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_spinner.setAdapter(adapterroom);
    }

    // setTime
    public String getStartTime() {
        String timehr;
        if (starttimeHr.getSelectedItem() == "8") {
            timehr = "08";
        } else if (starttimeHr.getSelectedItem() == "9") {
            timehr = "09";
        } else {
            timehr = starttimeHr.getSelectedItem().toString();
        }
        settime = timehr + ":" + starttimeMin.getSelectedItem() + ":00";
        return settime;
    }

    public String getEndTime() {
        String totimehr;
        if (endtimeHr.getSelectedItem() == "8") {
            totimehr = "08";
        } else if (endtimeHr.getSelectedItem() == "9") {
            totimehr = "09";
        } else {
            totimehr = endtimeHr.getSelectedItem().toString();
        }
        settotime = totimehr + ":" + endtimeMin.getSelectedItem() + ":00";
        return settotime;
    }

    //projector dialog
    private void getDialogSpinnerProjector() {
        projector_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(date_show.getText().toString(), "click to get date")) {
                    Snackbar.make(v, "unselect date", Snackbar.LENGTH_SHORT).show();
                } else {
                    createbooking.getProjector_spinner_arraylist().clear();
                    final Dialog room_dialog = new Dialog(getActivity());
                    room_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    room_dialog.setContentView(R.layout.customdialog_projector);
                    room_dialog.setCancelable(true);
                    createbooking.getProjector_spinner_arraylist().add("unselected");
                    createbooking.getSetProjector_Spinner();//task      // size 1 is "unselected" ??
                    projector2_spinner = (Spinner) room_dialog.findViewById(R.id.projector_spinner);
                    ArrayAdapter<String> adapterprojector = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, createbooking.getProjector_spinner_arraylist());
                    adapterprojector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    projector2_spinner.setAdapter(adapterprojector);

                    //check set select projector in spinner
//                    if (!Objects.equals(old_projector_txt, "unselected") || !Objects.equals(old_projector_txt, "click select projector")) {
//                        for (int i = 1; i < createbooking.getProjector_spinner_arraylist().size(); i++) {
//                            if (Objects.equals(old_projector_txt, createbooking.getProjector_spinner_arraylist().get(i))) {
//                                projector2_spinner.setSelection(i);
//                            }
//                        }
//                    }
                    select = (Button) room_dialog.findViewById(R.id.select);
                    select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            projector_txt.setText(projector2_spinner.getSelectedItem().toString());
                            room_dialog.cancel();
                        }
                    });
                    cancel = (Button) room_dialog.findViewById(R.id.cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            room_dialog.cancel();
                        }
                    });

                    room_dialog.show();

                }
            }
        });
    }

    /**
     * get data from view
     */
    public EditText getETsubject() {
        return ETsubject;
    }

    public EditText getETdetail() {
        return ETdetail;
    }

    public Spinner getRoom_spinner() {
        return room_spinner;
    }

    public TextView getDate_show() {
        return date_show;
    }

    public String getDate_send() {
        SimpleDateFormat dateFormatShow = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM", Locale.ENGLISH);
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("dd", Locale.ENGLISH);

        try {
            String date_show_des_year = dateFormatYear.format(dateFormatShow.parse(date_show.getText().toString()));
            date_show_des_year = String.valueOf(Integer.valueOf(date_show_des_year) - 543);
            String date_show_des_month = dateFormatMonth.format(dateFormatShow.parse(date_show.getText().toString()));
            String date_show_des_day = dateFormatDay.format(dateFormatShow.parse(date_show.getText().toString()));
            String date_pre_send = date_show_des_day + "/" + date_show_des_month + "/" + date_show_des_year;
            date_send = dateFormatSend.format(dateFormatShow.parse(date_pre_send));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_send;
    }

    public Spinner getStarttimeHr() {
        return starttimeHr;
    }

    public Spinner getStarttimeMin() {
        return starttimeMin;
    }

    public Spinner getEndtimeHr() {
        return endtimeHr;
    }

    public Spinner getEndtimeMin() {
        return endtimeMin;
    }

    public ArrayList<String> getHr() {
        return hr;
    }

    public ArrayList<String> getMin() {
        return min;
    }

    public TextView getProjector_txt() {
        return projector_txt;
    }

    public RadioButton getMeeting_redioButton() {
        int selectedId = meeting_type_rediogroup.getCheckedRadioButtonId();
        meeting_redioButton = (RadioButton) view.findViewById(selectedId);
        return meeting_redioButton;
    }

    //set by type
    public RadioButton getMeeting_rediobutton_Brainstorming() {
        meeting_redioButton = (RadioButton) view.findViewById(R.id.brainstorming);
        return meeting_redioButton;
    }

    public RadioButton getMeeting_rediobutton_Problemsolving() {
        meeting_redioButton = (RadioButton) view.findViewById(R.id.problem);
        return meeting_redioButton;
    }

    public RadioButton getMeeting_rediobutton_Training() {
        meeting_redioButton = (RadioButton) view.findViewById(R.id.training);
        return meeting_redioButton;
    }

    // set position time to config projector spinner
    public void setOldposition_starthr(int oldposition_starthr) {
        this.oldposition_starthr = oldposition_starthr;
    }

    public void setOldposition_startmin(int oldposition_startmin) {
        this.oldposition_startmin = oldposition_startmin;
    }

    public void setOldposition_endhr(int oldposition_endhr) {
        this.oldposition_endhr = oldposition_endhr;
    }

    public void setOldposition_endmin(int oldposition_endmin) {
        this.oldposition_endmin = oldposition_endmin;
    }

    public void setOld_projector_txt(String old_projector_txt) {
        this.old_projector_txt = old_projector_txt;
    }
}
