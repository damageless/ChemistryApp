package edu.weber.chemistryapp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import edu.weber.chemistryapp.R;
import edu.weber.chemistryapp.models.Section;
import edu.weber.chemistryapp.persistence.Preferences;

/**
 * Created by adamgessel on 7/21/15.
 */
public class ManageSectionsFragment extends Fragment {
    private Preferences mPreferences;
    private List<Section> mSections = new ArrayList<>();
    private SectionListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_manage_sections, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);
        mAdapter = new SectionListAdapter();
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                new MaterialDialog.Builder(getActivity())
                        .title(R.string.delete_confirm)
                        .content(R.string.are_you_sure)
                        .positiveText(android.R.string.ok)
                        .negativeText(android.R.string.cancel)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                mSections.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        }).show();

            }
        });

        setHasOptionsMenu(true);

        return layout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.action_create_section:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.create_section)
                        .content(R.string.create_section_input)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(R.string.create_section_hint, R.string.create_section_hint, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                mSections.add(new Section(input.toString()));
                                mAdapter.notifyDataSetChanged();
                                EventBus.getDefault().post(new OnSectionAddedEvent(input.toString()));
                            }
                        }).show();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPreferences = Preferences.createInstance(getActivity());
        mSections = mPreferences.getSections();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPreferences.setSections(mSections);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_sections, menu);
    }

    private class SectionListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mSections.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_section, null);
                viewHolder = new ViewHolder();
                viewHolder.sectionName = (TextView) convertView.findViewById(R.id.section_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.sectionName.setText(mSections.get(position).name);

            return convertView;
        }

        private class ViewHolder {
            TextView sectionName;
        }
    }

    public class OnSectionAddedEvent {
        public String name;

        public OnSectionAddedEvent(String name) {
            this.name = name;
        }
    }
}
