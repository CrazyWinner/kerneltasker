package com.dhome.crazywinner.appdeneme;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mertcan on 4/19/2015.

    /**
     * A page adapter which works with a large data set by reusing views.
     */
    class myPagerAdapter extends PagerAdapter {

    private List<View> views;
    private List<String> adlari;
    myPagerAdapter(){
        views=new ArrayList<>();
        adlari=new ArrayList<>();
    }

    void addView(View v,String ad){
        views.add(v);
        adlari.add(ad);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = views.get(position);
        if (view != null) {

            container.removeView(view);

        }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return adlari.get(position);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View vi=views.get(position);
        container.addView(vi);
        return vi;
    }
    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

