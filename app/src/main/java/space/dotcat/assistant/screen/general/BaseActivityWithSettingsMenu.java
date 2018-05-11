package space.dotcat.assistant.screen.general;


import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import space.dotcat.assistant.R;
import space.dotcat.assistant.screen.settings.SettingsActivity;

public abstract class BaseActivityWithSettingsMenu extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.settings_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item_settings) {
            SettingsActivity.start(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
