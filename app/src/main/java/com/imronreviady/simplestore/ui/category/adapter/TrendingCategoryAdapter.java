package com.imronreviady.simplestore.ui.category.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ItemTrendingCategoryAdapterBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.DataBoundViewHolder;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.viewobject.Category;

import androidx.databinding.DataBindingUtil;

public class TrendingCategoryAdapter extends DataBoundListAdapter<Category, ItemTrendingCategoryAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final CategoryClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface ;
    private int lastPosition = -1;

    public TrendingCategoryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                   CategoryClickCallback callback,
                                   DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemTrendingCategoryAdapterBinding createBinding(ViewGroup parent) {
        ItemTrendingCategoryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_trending_category_adapter, parent, false,
                        dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            Category category = binding.getCategory();
            if (category != null && callback != null) {
                callback.onClick(category);
            }
        });
        return binding;
    }

    // For general animation
    @Override
    public void bindView(DataBoundViewHolder<ItemTrendingCategoryAdapterBinding> holder, int position) {
        super.bindView(holder, position);

        //setAnimation(holder.itemView, position);
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemTrendingCategoryAdapterBinding binding, Category item) {

        binding.setCategory(item);

        binding.itemImageView.setOnClickListener(view -> callback.onClick(item));

    }

    @Override
    protected boolean areItemsTheSame(Category oldItem, Category newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(Category oldItem, Category newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.name.equals(newItem.name);
    }

    public interface CategoryClickCallback {
        void onClick(Category category);
    }

}
