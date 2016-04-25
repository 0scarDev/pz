package pz.functions;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pz.cse145.pz.AutoTab;
import com.pz.cse145.pz.ManualTab;
import com.pz.cse145.pz.R;

public class TabListenerV2 extends FragmentStatePagerAdapter {

    Context context;

    public TabListenerV2(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position){
            case 0:
                frag = new AutoTab();
                break;
            case 1:
                frag = new ManualTab();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = context.getString(R.string.design_003);
                break;
            case 1:
                title = context.getString(R.string.design_004);
                break;
        }
        return title;
    }

}
