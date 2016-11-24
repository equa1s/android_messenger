package com.messenger;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messenger.database.MessengerDatabaseHelper;

import butterknife.ButterKnife;

/**
 * @author equals on 24.11.16.
 */
public abstract class ButterKnifeFragment extends Fragment {

    public abstract @LayoutRes int setLayout();

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayout(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public MessengerDatabaseHelper getMessengerDatabaseHelper() {
        return ((ApplicationContext)getActivity().getApplication()).getMessengerDatabaseHelper();
    }

}
