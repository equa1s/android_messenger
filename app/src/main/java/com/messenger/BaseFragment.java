package com.messenger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.messenger.database.model.DaoSession;

import butterknife.ButterKnife;

/**
 * @author equals on 17.11.16.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, daoSession());
    }

    public void onCreate(@Nullable Bundle savedInstanceState, DaoSession daoSession) {
        onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
    }

    private DaoSession daoSession() {
        return ((ApplicationContext)getActivity().getApplication()).daoSession();
    }

}
