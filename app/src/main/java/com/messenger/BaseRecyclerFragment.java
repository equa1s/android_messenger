package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.messenger.animation.FadeInAnimator;


/**
 * @author equals on 17.11.16.
 */
public abstract class BaseRecyclerFragment extends BaseFragment {

    public abstract RecyclerView getRecyclerView();

    public abstract RecyclerView.Adapter setRecyclerViewAdapter();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
    }

    private void setRecyclerView() {
        getRecyclerView().setHasFixedSize(true);
        getRecyclerView().setItemAnimator(new FadeInAnimator());
        getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        getRecyclerView().setAdapter(setRecyclerViewAdapter());
    }
}
