package com.imronreviady.simplestore.ui.transaction.detail.adapter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ItemTransactionAdapterBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.NavigationController;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.TransactionDetail;

import androidx.databinding.DataBindingUtil;

public class TransactionAdapter extends DataBoundListAdapter<TransactionDetail, ItemTransactionAdapterBinding> {

    protected NavigationController navigationController;

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private TransactionClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;


    public TransactionAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                              TransactionClickCallback callback,
                              DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemTransactionAdapterBinding createBinding(ViewGroup parent) {
        ItemTransactionAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_transaction_adapter, parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            TransactionDetail transactionDetail = binding.getTransactionDetail();
            if (transactionDetail != null && callback != null) {
                callback.onClick(transactionDetail);
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
    protected void bind(ItemTransactionAdapterBinding binding, TransactionDetail item) {
        binding.setTransactionDetail(item);
        setDataToBalanceAndSubTotalToTransactionDetailOrder(binding, item);

        if(item.productColorCode.equals("")){
            binding.color1BgImageView.setVisibility(View.GONE);
        }else {
            binding.color1BgImageView.setVisibility(View.VISIBLE);
            Bitmap b = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(b);
            canvas.drawColor(Color.parseColor(item.productColorCode));
            dataBindingComponent.getFragmentBindingAdapters().bindCircleBitmap(binding.color1BgImageView, b);

        }

        if(item.productAttributeName.equals("")){
            binding.attributesTextView.setVisibility(View.GONE);
        }else {
            binding.attributesTextView.setVisibility(View.VISIBLE);
            String replaceComaForAttribute = item.productAttributeName;
            String replaceString = replaceComaForAttribute.replace("#",",");
            binding.attributesTextView.setText(String.format("(%s)", replaceString));
        }
    }

    @Override
    protected boolean areItemsTheSame(TransactionDetail oldItem, TransactionDetail newItem) {
        return Objects.equals(oldItem.id, newItem.id);

    }

    @Override
    protected boolean areContentsTheSame(TransactionDetail oldItem, TransactionDetail newItem) {
        return Objects.equals(oldItem.id, newItem.id);
    }


    public interface TransactionClickCallback {
        void onClick(TransactionDetail transactionDetail);
    }

    private void setDataToBalanceAndSubTotalToTransactionDetailOrder(ItemTransactionAdapterBinding binding, TransactionDetail item) {
        float amount = item.price;
        int qty = Integer.parseInt(item.qty);
        float subTotal = amount * qty;

        if(item.originalPrice != 0 && item.discountAvailableAmount !=0) {
            int originalPrice =(int)item.originalPrice - (int)item.discountAvailableAmount;
            String balanceString =item.currencySymbol + " " + originalPrice;
            binding.balanceValueTextView.setText(balanceString);
        }
        else {
            String balanceString =item.currencySymbol + " " + Utils.format(item.originalPrice);
            binding.balanceValueTextView.setText(balanceString);
        }
        binding.subTotalValueTextView.setText(String.valueOf(subTotal));
        String subTotalValueString = item.currencyShortForm + Constants.SPACE_STRING + Utils.format(subTotal);
        binding.subTotalValueTextView.setText(subTotalValueString);

        String priceValue = item.currencySymbol + " " + Utils.format(item.originalPrice);
        binding.priceValueTextView.setText(priceValue);

        String discountAvailableAmount = item.currencySymbol + " " + Utils.format(item.discountAvailableAmount);
        binding.discountAvailableAmountValueTextView.setText(discountAvailableAmount);


    }
}
