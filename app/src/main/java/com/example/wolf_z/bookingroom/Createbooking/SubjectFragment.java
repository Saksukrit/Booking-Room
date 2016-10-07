package com.example.wolf_z.bookingroom.Createbooking;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wolf_z.bookingroom.Bean.BookBean;
import com.example.wolf_z.bookingroom.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class SubjectFragment extends Fragment {

    private static final String TIME_PATTERN = "HH:mm";
    private ProgressDialog prgDialog;
    private Spinner starttimeHr;
    private Spinner starttimeMin;
    private Spinner endtimeHr;
    private Spinner endtimeMin;
    protected EditText ETsubject;
    protected EditText ETdetail;
    protected RadioGroup meeting_type_rediogroup;
    protected RadioButton meeting_redioButton;
    protected Spinner room_spinner;
    protected Spinner projector_spinner;
    private TextView date;
    protected ArrayList<String> Aroom = new ArrayList<>();

    private String setdate;
    protected String settime;
    protected String settotime;
    private DatePickerDialog fromDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    protected SimpleDateFormat dateFormatSend;
    protected SimpleDateFormat dateFormatter1;
    private Button next;
    protected Createbooking createbooking;

    public SubjectFragment(Createbooking createbooking) {
        this.createbooking = createbooking;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_createbooking_subject, container, false);
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
        date = (TextView) view.findViewById(R.id.date);
        getDatePicker();
        date.setOnClickListener(new View.OnClickListener() {
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


        /** Save data Subject*/
        next = (Button) view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = meeting_type_rediogroup.getCheckedRadioButtonId();
                meeting_redioButton = (RadioButton) view.findViewById(selectedId);
                BookBean bookBean = new BookBean();
                bookBean.setSubject(ETsubject.getText().toString());
                bookBean.setMeeting_type(meeting_redioButton.getText().toString());
                bookBean.setDetail(ETdetail.getText().toString());
                bookBean.setDate(setdate);
                bookBean.setStarttime(setStartTime());
                bookBean.setEndtime(setEndTime());
                bookBean.setRoomid(Integer.parseInt(room_spinner.getSelectedItem().toString()));
                bookBean.setProjid(Integer.parseInt(projector_spinner.getSelectedItem().toString()));

                Createbooking createbooking = (Createbooking) getActivity();
                createbooking.setBookBeen_save(bookBean);
            }
        });


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

                date.setText(dateFormatter.format(newDate.getTime()));  /****format*/
                setdate = dateFormatter.format(newDate.getTime());
            }
        }, caledar.get(Calendar.YEAR), caledar.get(Calendar.MONTH), caledar.get(Calendar.DAY_OF_MONTH));
    }

    private void getSpinnerTimeData() {
        final ArrayList<String> hr = new ArrayList<String>();
        final ArrayList<String> min = new ArrayList<String>();
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
        Aroom.clear();
        Aroom.add("unselect");

        ArrayList<Integer> listprojectorEmpty = new ArrayList<>();
        listprojectorEmpty.add(1);
        listprojectorEmpty.add(2);

        ArrayAdapter<Integer> adapterprojector = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listprojectorEmpty);
        adapterprojector.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projector_spinner.setAdapter(adapterprojector);
    }

    // setTime
    private String setStartTime() {
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

    private String setEndTime() {
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
}
