package com.imronreviady.simplestore.ui.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivitySettingBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

public class SettingActivity extends PSAppCompactActivity {

    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySettingBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        // Init all UI
        initUI(binding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));

    }
    //endregion


    //region Private Methods

    private void initUI(ActivitySettingBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.edit_setting__title));
        // setup Fragment
        setupFragment(new SettingFragment());

    }

    //endregion


}
