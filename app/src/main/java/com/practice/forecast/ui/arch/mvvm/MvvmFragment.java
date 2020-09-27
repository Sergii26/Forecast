package com.practice.forecast.ui.arch.mvvm;

import android.content.Context;

import com.practice.forecast.ui.arch.Contract;

import java.lang.reflect.ParameterizedType;

import androidx.fragment.app.Fragment;

public class MvvmFragment<Host extends Contract.Host> extends Fragment {
    /**
     * the fragment callBack
     */
    private Host callBack;

    //@Override
    public final boolean hasCallBack() {
        return callBack != null;
    }

    public final boolean noHost() {
        return callBack == null;
    }

    /**
     * get the current fragment call back
     *
     * @return the current fragment call back
     */
    public final Host getCallBack() {
        return callBack;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // keep the call back
        try {
            this.callBack = (Host) context;
        } catch (Throwable e) {
            final String hostClassName = ((Class) ((ParameterizedType) getClass().
                    getGenericSuperclass())
                    .getActualTypeArguments()[1]).getCanonicalName();
            throw new RuntimeException("Activity must implement " + hostClassName
                    + " to attach " + this.getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // release the call back
        this.callBack = null;
    }
}

