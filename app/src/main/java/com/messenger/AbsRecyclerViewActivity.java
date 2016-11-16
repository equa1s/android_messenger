package com.messenger;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.messenger.animation.FadeInAnimator;

import butterknife.BindView;

/**
 * @author equals on 15.11.16.
 */
public abstract class AbsRecyclerViewActivity extends BaseToolbarActivity {

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    protected void onPostCreate() {
        super.onPostCreate();
        setRecyclerView();
    }

    @Override
    protected int setLayout() {
        return R.layout.abs_recycler_view_activity;
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new FadeInAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(setRecyclerViewAdapter());
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public abstract RecyclerView.Adapter setRecyclerViewAdapter();

}
