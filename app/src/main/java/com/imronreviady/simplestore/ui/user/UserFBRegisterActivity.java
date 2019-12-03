package com.imronreviady.simplestore.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivityUserFbRegisterBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.MyContextWrapper;
import com.imronreviady.simplestore.utils.Utils;

public class UserFBRegisterActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUserFbRegisterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_user_fb_register);

        // Init all UI
        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.psLog("Inside Result UserFBRegisterActivity");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if(fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    //endregion


    //region Private Methods

    private void initUI(ActivityUserFbRegisterBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.register__register));

        // setup Fragment
        setupFragment(new UserFBRegisterFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion


}