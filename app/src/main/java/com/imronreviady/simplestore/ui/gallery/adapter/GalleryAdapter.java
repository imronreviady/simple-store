package com.imronreviady.simplestore.ui.gallery.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ItemGalleryAdapterBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.Image;

import androidx.databinding.DataBindingUtil;

/**
 * Created by Panacea-Soft on 12/6/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


public class GalleryAdapter extends DataBoundListAdapter<Image, ItemGalleryAdapterBinding> {

    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private final ImageClickCallback callback;

    public GalleryAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                          ImageClickCallback callback,
                          DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemGalleryAdapterBinding createBinding(ViewGroup parent) {
        ItemGalleryAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_gallery_adapter, parent, false,
                        dataBindingComponent);

        binding.getRoot().setOnClickListener(v -> {
            Image image = binding.getImage();
            if (image != null && callback != null) {
                Utils.psLog("Clicked Image" + image.imgDesc);

                callback.onClick(image);
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
    protected void bind(ItemGalleryAdapterBinding binding, Image item) {
        binding.setImage(item);
    }

    @Override
    protected boolean areItemsTheSame(Image oldItem, Image newItem) {
        return Objects.equals(oldItem.imgId, newItem.imgId);
    }

    @Override
    protected boolean areContentsTheSame(Image oldItem, Image newItem) {
        return Objects.equals(oldItem.imgId, newItem.imgId);
    }

    public interface ImageClickCallback {
        void onClick(Image item);
    }
}