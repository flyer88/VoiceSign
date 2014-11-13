package me.shyboy.mengma.listener;
import android.support.v4.view.ViewPager;
import android.view.View;


/**
 * Created by foul on 14/11/13.
 */
public class PagerTabOnClickListener implements View.OnClickListener {

    private int index = 0;
    private ViewPager pager;
    public PagerTabOnClickListener(int i,ViewPager pager)
    {
        index = i;
        this.pager = pager;
    }
    @Override
    public void onClick(View v) {
        pager.setCurrentItem(index);
    }
}