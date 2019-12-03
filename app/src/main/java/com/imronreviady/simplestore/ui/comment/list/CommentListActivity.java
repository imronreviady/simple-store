package com.imronreviady.simplestore.ui.comment.list;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivityCommentListBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.MyContextWrapper;
import com.imronreviady.simplestore.utils.Utils;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class CommentListActivity extends PSAppCompactActivity {


    //region Override Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCommentListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_list);

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
    private void initUI(ActivityCommentListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.comment__title));

        // setup Fragment
        setupFragment(new CommentListFragment());
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.psLog("Inside Result MainActivity");
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    //endregion


}
