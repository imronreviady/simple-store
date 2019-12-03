package com.imronreviady.simplestore.ui.product.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivitySearchByCategoryBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

public class SearchByCategoryActivity extends PSAppCompactActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySearchByCategoryBinding databinding = DataBindingUtil.setContentView(this, R.layout.activity_search_by_category);

        initUI(databinding);

    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));
    }

    protected void initUI(ActivitySearchByCategoryBinding binding) {
        Intent intent = getIntent();

        String fragName = intent.getStringExtra(Constants.CATEGORY_FLAG);

        if (fragName.equals(Constants.CATEGORY)) {
            setupFragment(new SearchCategoryFragment());

            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_cat_title));
        } else if (fragName.equals(Constants.SUBCATEGORY)) {
            setupFragment(new SearchSubCategoryFragment());

            initToolbar(binding.toolbar, getResources().getString(R.string.Feature_UI__search_alert_sub_cat_title));
        }


    }
}
