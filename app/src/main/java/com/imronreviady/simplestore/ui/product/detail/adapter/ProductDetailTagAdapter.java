package com.imronreviady.simplestore.ui.product.detail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ItemProductDetailTagAdapterBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.viewobject.holder.TabObject;

import androidx.databinding.DataBindingUtil;

public class ProductDetailTagAdapter extends DataBoundListAdapter<TabObject, ItemProductDetailTagAdapterBinding> {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private SimilarProductClickCallBack callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;


    public ProductDetailTagAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, SimilarProductClickCallBack similarProductClickCallBack) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = similarProductClickCallBack;
    }

    @Override
    protected ItemProductDetailTagAdapterBinding createBinding(ViewGroup parent) {
        ItemProductDetailTagAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_product_detail_tag_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener((View v) -> {
            TabObject tabObject = binding.getProductTab();
            if (tabObject != null && callback != null) {

                callback.onClick(tabObject, tabObject.tag_id);
            }
        });
        return binding;
    }

    @Override
    protected void dispatched() {
        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }
    }

    @Override
    protected void bind(ItemProductDetailTagAdapterBinding binding, TabObject item) {
        binding.setProductTab(item);
    }

    @Override
    protected boolean areItemsTheSame(TabObject oldItem, TabObject newItem) {
        return Objects.equals(oldItem.tag_id, newItem.tag_id)
                && oldItem.tag_name.equals(newItem.tag_name);
    }

    @Override
    protected boolean areContentsTheSame(TabObject oldItem, TabObject newItem) {
        return Objects.equals(oldItem.tag_id, newItem.tag_id)
                && oldItem.tag_name.equals(newItem.tag_name);
    }

    public interface SimilarProductClickCallBack {
        void onClick(TabObject tabObject, String selectedTabId);
    }


}
