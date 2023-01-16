package com.practice.forecast.ui.arch.mvvm

import android.content.Context
import androidx.fragment.app.Fragment
import com.practice.forecast.ui.arch.Contract
import java.lang.reflect.ParameterizedType

open class MvvmFragment<Host : Contract.Host?> : Fragment() {
    val newOne = ""
    val newOne1 = ""

    /**
     * get the current fragment call back
     *
     * @return the current fragment call back
     */
    /**
     * the fragment callBack
     */
    var callBack: Host? = null
        private set

    //@Override
    fun hasCallBack(): Boolean {
        return callBack != null
    }

    fun noHost(): Boolean {
        return callBack == null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // keep the call back
        try {
            callBack = context as Host
        } catch (e: Throwable) {
            val hostClassName = ((javaClass.genericSuperclass as ParameterizedType?)
                    ?.getActualTypeArguments()?.get(1) as Class<*>).canonicalName
            throw RuntimeException("Activity must implement " + hostClassName
                    + " to attach " + this.javaClass.simpleName, e)
        }
    }

    override fun onDetach() {
        super.onDetach()
        // release the call back
        callBack = null
    }
}