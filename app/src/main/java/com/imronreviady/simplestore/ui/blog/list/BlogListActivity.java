package com.imronreviady.simplestore.ui.blog.list;

import android.os.Bundle;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ActivityBlogListBinding;
import com.imronreviady.simplestore.ui.common.PSAppCompactActivity;

import androidx.databinding.DataBindingUtil;

public class BlogListActivity extends PSAppCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityBlogListBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_blog_list);

        initUI(binding);

    }

    private void initUI(ActivityBlogListBinding binding) {

        // Toolbar
        initToolbar(binding.toolbar, getResources().getString(R.string.blog_list__title));

        // setup Fragment

        setupFragment(new BlogListFragment());

    }
}
