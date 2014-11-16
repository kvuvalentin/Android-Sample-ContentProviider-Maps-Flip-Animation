package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment.AnimationFragment;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment.IrcFragment;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment.MapsFragment;

public class RootPagerAdapter extends FragmentStatePagerAdapter {
    private final int ADAPTER_COUNT = 3;
    private final Context context;
    private final FragmentManager fragmentManager;

    private enum Tag {
        IRC_FRAGMENT(IrcFragment.class),
        MAPS_FRAGMENT(MapsFragment.class),
        ANIMATION_FRAGMENT(AnimationFragment.class);
        private final Class fragmentsClass;

        Tag(Class fragmentsClass) {
            this.fragmentsClass = fragmentsClass;
        }
    }

    public RootPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);

        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        Tag tag = null;
        switch (i) {
            case 0:
                tag = Tag.IRC_FRAGMENT;
                break;
            case 1:
                tag = Tag.MAPS_FRAGMENT;
                break;
            case 2:
                tag = Tag.ANIMATION_FRAGMENT;
                break;
        }
        return tag != null ? (Fragment) instantiateItem(tag) : null;
    }

    public Object instantiateItem(Tag tag) {
        String tagStr = tag.name();
        Object result = fragmentManager.findFragmentByTag(tagStr);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (result == null) {
            result = Fragment.instantiate(context, tag.fragmentsClass.getName());
        } else {
            ft.remove((Fragment) result);
        }
        ft.add((Fragment) result, tagStr);
        return result;
    }

    @Override
    public int getCount() {
        return ADAPTER_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return IrcFragment.class.getSimpleName();
            case 1:
                return MapsFragment.class.getSimpleName();
            case 2:
                return AnimationFragment.class.getSimpleName();
            default:
                return "";
        }
    }
}
