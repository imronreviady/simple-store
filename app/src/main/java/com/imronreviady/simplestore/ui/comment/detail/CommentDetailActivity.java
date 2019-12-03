package com.imronreviady.simplestore.ui.comment.detail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivityCommentDetailBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.MyContextWrapper;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class CommentDetailActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCommentDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_comment_detail);

        // Init all UI
        initUI(binding);
    }

    @Override
    protected void attachBaseContext(Context newBase) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String LANG_CURRENT = preferences.getString(Constants.LANGUAGE, Config.DEFAULT_LANGUAGE);

        super.attachBaseContext(MyContextWrapper.wrap(newBase, LANG_CURRENT, true));
    }


    //region Private Methods

    private void initUI(ActivityCommentDetailBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.comment__detail_title));

        // setup Fragment
        CommentDetailFragment commentDetailFragment = new CommentDetailFragment();
        setupFragment(commentDetailFragment);
        // Or you can call like this
        //setupFragment(new NewsListFragment(), R.id.content_frame);

    }

    //endregion

}
