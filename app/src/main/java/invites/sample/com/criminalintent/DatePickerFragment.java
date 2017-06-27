package invites.sample.com.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by mpudota on 6/4/17.
 */

public class DatePickerFragment extends DialogFragment {

    protected static final String EXTRA_DATE = "date";
    Date date;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        date = (Date)getArguments().getSerializable(EXTRA_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = getActivity().getLayoutInflater().inflate(R.layout.date_picker, null);

        DatePicker datePicker = (DatePicker)v.findViewById(R.id.date_picker);
        Log.d("changed not date is ", date.toString());
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                date = new GregorianCalendar(i,i1,i2).getTime();
                getArguments().putSerializable(EXTRA_DATE, date);
                Log.d("changed date is ", date.toString());

            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.date_picker_title)
                .setView(R.layout.date_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();
    }
    public DatePickerFragment newInstance (Date date) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void sendResult (int resultCode) {
        if (getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

}
