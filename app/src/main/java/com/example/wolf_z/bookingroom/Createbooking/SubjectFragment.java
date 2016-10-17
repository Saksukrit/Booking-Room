package com.example.wolf_z.bookingroom.Createbooking;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
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
import java.util.StringTokenizer;


public class SubjectFragment extends Fragment {

    private static final String TIME_PATTERN = "HH:mm";
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
    private Spinner projector_spinner;
    private TextView date_show;
    private TextView projector_txt;
    private Spinner projector2_spinner;
    private Button select;
    private Button cancel;

    private String date_send;
    protected String settime;
    protected String settotime;
    private final ArrayList<String> hr = new ArrayList<>();
    private final ArrayList<String> min = new ArrayList<>();
    private DatePickerDialog fromDatePickerDialog;
    protected SimpleDateFormat dateFormatter;
    protected SimpleDateFormat dateFormatSend;
    protected SimpleDateFormat dateFormatter1;
    protected Createbooking createbooking;
    private View view;

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
        Locale lc = new Locale("th", "TH");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", lc);
        dateFormatter1 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        dateFormatSend = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        ETsubject = (EditText) view.findViewById(R.id.ETsubject);
        ETdetail = (EditText) view.findViewById(R.id.ETdetail);


        /** meeting_type */
        meeting_type_rediogroup = (RadioGroup) view.findViewById(R.id.meeting_type);


        /** Date picker*/
        date_show = (TextView) view.findViewById(R.id.date);
//        date_show = (TextView) getActivity().findViewById(R.id.date);
        getDatePicker();
        date_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        /** Time */
        starttimeHr = (Spinner) view.findViewById(R.id.timeHr);
        starttimeMin = (Spinner) view.findViewById(R.id.timeMin);
        endtimeHr = (Spinner) view.findViewById(R.id.totimeHr);
        endtimeMin = (Spinner) view.findViewById(R.id.totimeMin);
        getSpinnerTimeData();

        /** select room_spinner */
        room_spinner = (Spinner) view.findViewById(R.id.room);
        getSpinnerRoom();

        /**projector_spinner*/
        projector_spinner = (Spinner) view.findViewById(R.id.projector);
        getSpinnerProjector();

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

//                try {
//                    String x = dateFormatter.format(dateFormatter1.parse(String.valueOf(newDate.getTime())));
//                    date_show.setText(dateFormatter.format(dateFormatter1.parse(String.valueOf(newDate.getTime()))));
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
                /****format*/
                date_show.setText(dateFormatter1.format(newDate.getTime()));
                date_send = dateFormatSend.format(newDate.getTime());
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

    private void getSpinnerRoom() {
        ArrayAdapter<String> adapterroom = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, createbooking.getRoomshow_spinner_arraylist());
        adapterroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room_spinner.setAdapter(adapterroom);
    }

    private void getSpinnerProjector() {
        ArrayList<Integer> listprojectorEmpty = new ArrayList<>();
        listprojectorEmpty.add(1);
        listprojectorEmpty.add(2);

        ArrayAdapter<Integer> adapterprojector = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listprojectorEmpty);
        adapterprojector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projector_spinner.setAdapter(adapterprojector);
    }

    // setTime
    public String setStartTime() {
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

    public String setEndTime() {
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

        final ArrayList<String> roomid = new ArrayList<>();

        projector_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog room_dialog = new Dialog(getActivity());
                room_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                room_dialog.setContentView(R.layout.customdialog);
                room_dialog.setCancelable(true);
                roomid.add("unselected");
                roomid.add("101");

                //task by condition

                projector2_spinner = (Spinner) room_dialog.findViewById(R.id.projector_spinner);
                ArrayAdapter<String> adapterroomid = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, roomid);
                adapterroomid.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                projector2_spinner.setAdapter(adapterroomid);

                select = (Button) room_dialog.findViewById(R.id.select);
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        projector_txt.setText(projector2_spinner.getSelectedItem().toString());
                        roomid.clear();
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

    public Spinner getProjector_spinner() {
        return projector_spinner;
    }

    public TextView getDate_show() {
        return date_show;
    }

    public String getDate_send() {
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

}
