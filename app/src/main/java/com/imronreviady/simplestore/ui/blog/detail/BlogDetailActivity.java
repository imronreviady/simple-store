package com.imronreviady.simplestore.ui.blog.detail;

import android.os.Bundle;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivityBlogDetailBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;

import androidx.databinding.DataBindingUtil;

public class BlogDetailActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBlogDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_blog_detail);

        initUI(binding);

    }

    private void initUI(ActivityBlogDetailBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.blog_detail__title));

        // setup Fragment
        setupFragment(new BlogDetailFragment());

    }
}
