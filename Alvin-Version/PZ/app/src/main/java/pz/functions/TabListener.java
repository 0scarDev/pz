package pz.functions;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pz.cse145.pz.ProfileTab;
import com.pz.cse145.pz.R;
import com.pz.cse145.pz.ScheduleTab;
import com.pz.cse145.pz.SettingTab;

public class TabListener extends FragmentStatePagerAdapter {

    Context context;

    public TabListener(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position){
            case 0:
                frag = new ProfileTab();
                break;
            case 1:
                frag = new ScheduleTab();
                break;
            case 2:
                frag = new SettingTab();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = context.getString(R.string.design_000);
                break;
            case 1:
                title = context.getString(R.string.design_001);
                break;
            case 2:
                title = context.getString(R.string.design_002);
                break;
        }
        return title;
    }

}
