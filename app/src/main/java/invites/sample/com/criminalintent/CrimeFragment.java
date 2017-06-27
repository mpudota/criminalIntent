package invites.sample.com.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;
import java.util.UUID;


public class CrimeFragment extends Fragment {


    private EditText editText;
    Crime crime;
    ImageButton cameraButton;
    private Button button, chooseSuspect, reportACrime;
    private CheckBox checkBox;
    protected static final String EXTRA_CRIME_ID = "invites.sample.com.criminalintent.crime_id";
    private static final String DIALOG_DATE = "date";
    UUID crimeId;
    FragmentManager fragmentManager;
    protected static final int REQUEST_DATE = 0;
    protected static final int REQUEST_PHOTO = 1;
    protected static final int REQUEST_CONTACT = 2;
    Callbacks callbacks;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(getActivity(), CrimeListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        crime = new Crime();

        setHasOptionsMenu(true);
        crimeId = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);
        crime = CrimeLab.get(getActivity()).getCrime(crimeId);

    }
    public static CrimeFragment newInstance (UUID uuid) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CRIME_ID, uuid);
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        button = (Button) rootView.findViewById(R.id.crime_date);
        button.setText(crime.getDate().toString());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment = new DatePickerFragment().newInstance(crime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fragmentManager, DIALOG_DATE);
            }
        });

        cameraButton = (ImageButton) rootView.findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });


        checkBox = (CheckBox) rootView.findViewById(R.id.crime_solved);
        checkBox.setChecked(crime.isSolved());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                crime.setSolved(b);
                callbacks.onCrimeUpdated(crime);
            }
        });

        editText = (EditText) rootView.findViewById(R.id.crime_title);
        editText.setText(crime.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                crime.setTitle(charSequence.toString());
                callbacks.onCrimeUpdated(crime);
                getActivity().setTitle(crime.getTitle());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        chooseSuspect = (Button) rootView.findViewById(R.id.choose_suspect);
        chooseSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CONTACT);
            }
        });
        if (crime.getSuspect() != null) {
            chooseSuspect.setText(crime.getSuspect());
        }
        reportACrime = (Button) rootView.findViewById(R.id.report_a_crime);
        reportACrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });
        return rootView;


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callbacks = (Callbacks) activity;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.d("this mean not working", String.valueOf(resultCode));
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            crime.setDate(date);
            updateDate();
            Log.d("This mean working", date.toString());
        }
        else if (requestCode == REQUEST_CONTACT) {
            Uri contactURI = data.getData();
            String[] queryFields = new String[] {
              ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver().query(contactURI, queryFields, null, null, null);

            if (c.getCount() == 0) {
                c.close();
                return;
            }
            c.moveToFirst();
            String suspect = c.getString(0);
            crime.setSuspect(suspect);
            chooseSuspect.setText(suspect);
            c.close();
        }
    }

    private void updateDate () {
        button.setText(crime.getDate().toString());
    }

    @Override
    public void onPause () {
        super.onPause();
        CrimeLab.get(getActivity()).saveCrime();
    }

    private String getCrimeReport () {
        String solvedString = null;
        if (checkBox.isChecked()) {
            solvedString = getString(R.string.crime_report_solved);
        }
        else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, crime.getDate()).toString();
        String suspect = crime.getSuspect();
        String report = getString(R.string.crime_report, crime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

}
