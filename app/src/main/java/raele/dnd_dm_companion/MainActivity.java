package raele.dnd_dm_companion;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import raele.dnd_dm_companion.fragment.calc.AbilityCalculatorFragment;
import raele.dnd_dm_companion.fragment.phb.PlayersHandbookFragment;
import raele.util.android.log.Log;

/**
 * <h2>Adding items to the navigation drawer</h2>
 * To create a new navigation drawer item, add a enum item to {@link NavigationDrawerItem}. This
 * creates the new item already configured with a label and a destination fragment, but the item
 * will not be initially visible. You need also to add it to the array {@link #sNavItems} in the
 * position you want it to appear in the list.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    // Defines each possible item for this activity's navigation drawer
    public static enum NavigationDrawerItem {
        PlayersHandbook(R.string.navitem_players_handbook, PlayersHandbookFragment.class),
        AbilityCalculator(R.string.navitem_stat_calculator, AbilityCalculatorFragment.class),
        ;
        private final int mDescriptionId;
        private final Class<? extends Fragment> mFragmentClass;

        NavigationDrawerItem(int descriptionId, Class<? extends Fragment> fragmentClass) {
            mDescriptionId = descriptionId;
            mFragmentClass = fragmentClass;
        }

        public Class<? extends Fragment> getmFragmentClass() {
            return mFragmentClass;
        }

        public int getmDescriptionId() {
            return mDescriptionId;
        }
    }

    // Defines what items will appear in this activity's navigation drawer and the order they appear.
    public static final NavigationDrawerItem[] sNavItems = new NavigationDrawerItem[] {
            NavigationDrawerItem.PlayersHandbook,
            NavigationDrawerItem.AbilityCalculator,
    };

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment targetFragment = null;
        try {
            targetFragment = sNavItems[position].getmFragmentClass().newInstance();
        } catch (Exception e) {
            Log.error("Malformed navigation drawer item found.");
            Log.error("Tried to select item " + position + ", but the following error occurred:");
            Log.error(e.toString());
            throw new RuntimeException(e);
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, targetFragment)
                .commit();
    }

//    public void restoreActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        if (!mNavigationDrawerFragment.isDrawerOpen()) {
//            // Only show items in the action bar relevant to this screen
//            // if the drawer is not showing. Otherwise, let the drawer
//            // decide what to show in the action bar.
//            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
//            return true;
//        }
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
