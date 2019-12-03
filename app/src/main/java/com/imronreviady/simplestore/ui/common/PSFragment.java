package com.imronreviady.simplestore.ui.common;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.di.Injectable;
import com.imronreviady.simplestore.utils.Connectivity;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.Utils;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * Parent class for all fragment in this project.
 * Created by Panacea-Soft on 12/2/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public abstract class PSFragment extends Fragment implements Injectable {

    //region Variables

    @Inject
    protected ViewModelProvider.Factory viewModelFactory;

    @Inject
    protected NavigationController navigationController;

    @Inject
    protected Connectivity connectivity;

    @Inject
    protected SharedPreferences pref;

    protected String loginUserId;

    protected String shippingId;

    protected String shippingTax;

    protected String overAllTax;

    protected String versionNo;

    protected Boolean force_update = false;

    protected String force_update_msg;

    protected String force_update_title;

    private boolean isFadeIn = false;

    protected String cod, paypal, stripe, messenger,whatsappNo;

    protected String consent_status;
    //endregion


    //region Override Methods
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadLoginUserId();

        initViewModels();

        initUIAndActions();

        initAdapters();

        initData();
    }

    //endregion


    //region Methods

    protected void loadLoginUserId(){
        try {

            if(getActivity() != null && getActivity().getBaseContext() != null) {
                loginUserId = pref.getString(Constants.USER_ID, Constants.EMPTY_STRING);
                shippingId = pref.getString(Constants.SHIPPING_ID, "");
                shippingTax = pref.getString(Constants.PAYMENT_SHIPPING_TAX, "");
                versionNo = pref.getString(Constants.APPINFO_PREF_VERSION_NO, "");
                force_update = pref.getBoolean(Constants.APPINFO_PREF_FORCE_UPDATE, false);
                force_update_msg = pref.getString(Constants.APPINFO_FORCE_UPDATE_MSG, "");
                force_update_title = pref.getString(Constants.APPINFO_FORCE_UPDATE_TITLE, "");
                overAllTax = pref.getString(Constants.PAYMENT_OVER_ALL_TAX, "");
                cod = pref.getString(Constants.PAYMENT_CASH_ON_DELIVERY, Constants.ZERO);
                paypal = pref.getString(Constants.PAYMENT_PAYPAL, Constants.ZERO);
                stripe = pref.getString(Constants.PAYMENT_STRIPE, Constants.ZERO);
                messenger = pref.getString(Constants.MESSENGER, Constants.ZERO);
                whatsappNo = pref.getString(Constants.WHATSAPP, Constants.ZERO);
                consent_status = pref.getString(Config.CONSENTSTATUS_CURRENT_STATUS, Config.CONSENTSTATUS_CURRENT_STATUS);
            }

        }catch (NullPointerException ne){
            Utils.psErrorLog("Null Pointer Exception.", ne);
        }catch(Exception e){
            Utils.psErrorLog("Error in getting notification flag data.", e);
        }
    }

    protected abstract void initUIAndActions();

    protected abstract void initViewModels();

    protected abstract void initAdapters();

    protected abstract void initData();

    protected void fadeIn(View view) {

        if(!isFadeIn) {
            view.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));
            isFadeIn = true; // Fade in will do only one time.
        }
    }
    //endregion

}
