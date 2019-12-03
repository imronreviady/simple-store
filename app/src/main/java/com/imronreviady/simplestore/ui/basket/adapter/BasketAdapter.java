package com.imronreviady.simplestore.ui.basket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ItemBasketAdapterBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.Basket;

import androidx.databinding.DataBindingUtil;

public class BasketAdapter extends DataBoundListAdapter<Basket, ItemBasketAdapterBinding> {
    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private BasketClickCallBack callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;
    public Context context;

    public BasketAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, BasketClickCallBack basketClickCallBack, DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = basketClickCallBack;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemBasketAdapterBinding createBinding(ViewGroup parent) {
        ItemBasketAdapterBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_basket_adapter, parent, false,
                        dataBindingComponent);
        context = parent.getContext();
        binding.getRoot().setOnClickListener(v -> {
            Basket basket = binding.getBasket();
            if (basket != null && callback != null) {
                callback.onClick(basket);
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
    protected void bind(ItemBasketAdapterBinding binding, Basket item) {
        binding.setBasket(item);

        binding.basketNameTextView.setText(item.product.name);

        if (item.count == 0) {
            item.count = 1;
        }

        binding.minusImageView.setOnClickListener(view -> {

            if (item.count > 1) {
                item.count -= 1;
                binding.qtyEditText.setText(String.valueOf(item.count));
                TotalPriceUpdate(binding, item);

                callback.onMinusClick(item);
            }
        });
        binding.plusImageView.setOnClickListener(view -> {
            item.count += 1;
            binding.qtyEditText.setText(String.valueOf(item.count));
            TotalPriceUpdate(binding, item);

            callback.onAddClick(item);
        });

        callback.onAddClick(item);
        TotalPriceUpdate(binding, item);

        binding.deleteIconImageView.setOnClickListener(view -> callback.onDeleteConfirm(item));

        binding.deleteBgImageView.setOnClickListener(view -> callback.onDeleteConfirm(item));

    }

    private void TotalPriceUpdate(ItemBasketAdapterBinding binding, Basket item) {


        String priceString = item.product.currencySymbol+ Constants.SPACE_STRING +Utils.format(item.basketPrice);
        binding.priceTextView.setText(priceString);
        float subTotalPrice = Utils.round(item.count * item.basketPrice, 2);
        String subTotalString =item.product.currencySymbol+ Constants.SPACE_STRING  + Utils.format(subTotalPrice)  ;

        binding.subTotalTextView.setText(subTotalString);
    }

    @Override
    protected boolean areItemsTheSame(Basket oldItem, Basket newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId)
                && oldItem.count == newItem.count
                && oldItem.selectedAttributes.equals(newItem.selectedAttributes)
                && oldItem.selectedColorId.equals(newItem.selectedColorId);
    }

    @Override
    protected boolean areContentsTheSame(Basket oldItem, Basket newItem) {
        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId)
                && oldItem.count == newItem.count
                && oldItem.selectedAttributes.equals(newItem.selectedAttributes)
                && oldItem.selectedColorId.equals(newItem.selectedColorId);
    }

    public interface BasketClickCallBack {
        void onMinusClick(Basket basket);

        void onAddClick(Basket basket);

        void onDeleteConfirm(Basket basket);

        void onClick(Basket basket);
    }

}
