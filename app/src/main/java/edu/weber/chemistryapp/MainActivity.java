package edu.weber.chemistryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.blunderer.materialdesignlibrary.activities.NavigationDrawerActivity;
import com.blunderer.materialdesignlibrary.handlers.ActionBarDefaultHandler;
import com.blunderer.materialdesignlibrary.handlers.ActionBarHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerAccountsMenuHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerBottomHandler;
import com.blunderer.materialdesignlibrary.handlers.NavigationDrawerTopHandler;
import com.blunderer.materialdesignlibrary.models.Account;

import de.greenrobot.event.EventBus;
import edu.weber.chemistryapp.fragments.CreateAnimationsFragment;
import edu.weber.chemistryapp.fragments.DisplayFragment;
import edu.weber.chemistryapp.fragments.ManageSectionsFragment;
import edu.weber.chemistryapp.models.Section;
import edu.weber.chemistryapp.persistence.Preferences;


public class MainActivity extends NavigationDrawerActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public NavigationDrawerAccountsHandler getNavigationDrawerAccountsHandler() {
        return null;
    }

    @Override
    public NavigationDrawerAccountsMenuHandler getNavigationDrawerAccountsMenuHandler() {
        return null;
    }

    @Override
    public void onNavigationDrawerAccountChange(Account account) {

    }

    @Override
    public NavigationDrawerTopHandler getNavigationDrawerTopHandler() {
        NavigationDrawerTopHandler navigationDrawerTopHandler = new NavigationDrawerTopHandler(this);

        Preferences preferences = Preferences.createInstance(this);

        navigationDrawerTopHandler.addSection(R.string.lessons);

        for (Section section : preferences.getSections()) {
            navigationDrawerTopHandler.addItem(section.name, DisplayFragment.createInstance(section.name));
        }

        if (BuildConfig.FLAVOR.equals("instructor")) {
            navigationDrawerTopHandler.addSection(R.string.instructor);
            navigationDrawerTopHandler.addItem(R.string.create_animations, new CreateAnimationsFragment());
            navigationDrawerTopHandler.addItem(R.string.manage_sections, new ManageSectionsFragment());
        } else {
            // TODO: Pull from server
        }

        return navigationDrawerTopHandler;
    }

    @Override
    public NavigationDrawerBottomHandler getNavigationDrawerBottomHandler() {
        return new NavigationDrawerBottomHandler(this)
                .addSettings(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                    }

                });
    }

    @Override
    public boolean overlayActionBar() {
        return true;
    }

    @Override
    public boolean replaceActionBarTitleByNavigationDrawerItemTitle() {
        return true;
    }

    @Override
    public int defaultNavigationDrawerItemSelectedPosition() {
        return 0;
    }

    @Override
    protected ActionBarHandler getActionBarHandler() {
        return new ActionBarDefaultHandler(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEvent(ManageSectionsFragment.OnSectionAddedEvent event) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
