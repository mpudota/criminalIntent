package invites.sample.com.criminalintent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class CrimeListFragment extends ListFragment {

private ArrayList<Crime> crimes;
    private boolean mSubTitleVisible;

    Callbacks callbacks;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_new_crime) {
            Crime crime = new Crime();
            CrimeLab.get(getActivity()).addCrime(crime);
            ((CustomView)getListAdapter()).notifyDataSetChanged();
            callbacks.onCrimeSelected(crime);
            return true;
        }
        if (id == R.id.menu_item_show_subtitle) {
            if (((AppCompatActivity)getActivity()).getSupportActionBar().getSubtitle() == null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(R.string.subtitile);
                mSubTitleVisible = true;
                item.setTitle(R.string.hide_subtitle);
            }
            else {
                ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(null);
                item.setTitle(R.string.show_subtitle);
                mSubTitleVisible = false;
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_crime_list, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        mSubTitleVisible = false;
        setHasOptionsMenu(true);
        getActivity().setTitle("Crimes");
        crimes = CrimeLab.get(getActivity()).getCrimes();
        CustomView customView = new CustomView(crimes);
        setListAdapter(customView);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Crime c = ((CustomView)getListAdapter()).getItem(position);
        ((CustomView)getListAdapter()).notifyDataSetChanged();

        callbacks.onCrimeSelected(c);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public void onResume() {
        super.onResume();
        ((CustomView)getListAdapter()).notifyDataSetChanged();
    }

    private class CustomView extends ArrayAdapter<Crime> {

        public CustomView(ArrayList<Crime> arrayList) {
            super(getActivity(),0, arrayList);
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Crime c = getItem(i);

            if (view == null) {
                view = getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_1, null);
            }
            TextView crimeName = (TextView) view.findViewById(R.id.crimeName);
            crimeName.setText(c.getTitle());
            TextView crimeTime = (TextView) view.findViewById(R.id.crimeTime);
            crimeTime.setText(c.getDate().toString());

            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxinList);
            checkBox.setChecked(c.isSolved());

            return view;
        }
    }

    public View onCreateView (LayoutInflater layoutInflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = super.onCreateView(layoutInflater, parent, savedInstanceState);
        if (mSubTitleVisible) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitile);
        }

        ListView listView = (ListView) v.findViewById(android.R.id.list);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater menuInflater = actionMode.getMenuInflater();
                menuInflater.inflate(R.menu.crime_list_item_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case (R.id.menu_item_delete_crime) :
                        CustomView customView = (CustomView)getListAdapter();
                        CrimeLab crimeLab = CrimeLab.get(getActivity());
                        for (int i= customView.getCount() -1 ; i >= 0 ; i--) {
                            if (getListView().isItemChecked(i)) {
                                crimeLab.deleteCrime(customView.getItem(i));
                            }
                        }
                        actionMode.finish();
                        customView.notifyDataSetChanged();
                        return true;

                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }
        });
        return v;
    }
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        callbacks = (Callbacks) activity;

    }

    public interface Callbacks {
        void onCrimeSelected (Crime crime);
    }

    protected void updateUI() {
        ((CustomView)getListAdapter()).notifyDataSetChanged();
    }


}
