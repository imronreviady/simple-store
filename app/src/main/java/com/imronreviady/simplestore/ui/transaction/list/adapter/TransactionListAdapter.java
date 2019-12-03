package com.imronreviady.simplestore.ui.transaction.list.adapter;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.ItemTransactionListAdapterBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.NavigationController;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.TransactionObject;

import androidx.databinding.DataBindingUtil;

public class TransactionListAdapter extends DataBoundListAdapter<TransactionObject, ItemTransactionListAdapterBinding> {

    protected NavigationController navigationController;

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private TransactionClickCallback callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface;


    public TransactionListAdapter(androidx.databinding.DataBindingComponent dataBindingComponent,
                                  TransactionClickCallback callback,
                                  DiffUtilDispatchedInterface diffUtilDispatchedInterface) {
        this.dataBindingComponent = dataBindingComponent;
        this.callback = callback;
        this.diffUtilDispatchedInterface = diffUtilDispatchedInterface;
    }

    @Override
    protected ItemTransactionListAdapterBinding createBinding(ViewGroup parent) {
        ItemTransactionListAdapterBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_transaction_list_adapter, parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            TransactionObject transaction = binding.getTransaction();
            if (transaction != null && callback != null) {
                callback.onClick(transaction);
            }
        });

        binding.copyImageView.setOnClickListener(v -> {
            ClipboardManager cManager = (ClipboardManager) parent.getContext().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData cData = ClipData.newPlainText(Constants.TRANSACTON_TEXT, binding.transactionNoValueTextView.getText());
            if (cManager != null) {
                cManager.setPrimaryClip(cData);

                callback.onCopyClick();
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
    protected void bind(ItemTransactionListAdapterBinding binding, TransactionObject item) {

        if (item != null) {
            binding.setTransaction(item);

            String totalAmount = item.currencySymbol + " " + Utils.format(Double.parseDouble(item.subTotalAmount));
            binding.totalAmountValueTextView.setText(totalAmount);
        }

    }

    @Override
    protected boolean areItemsTheSame(TransactionObject oldItem, TransactionObject newItem) {
        return Objects.equals(oldItem.id, newItem.id) && Objects.equals(oldItem.transStatusId, newItem.transStatusId);
    }

    @Override
    protected boolean areContentsTheSame(TransactionObject oldItem, TransactionObject newItem) {
        return Objects.equals(oldItem.id, newItem.id) && Objects.equals(oldItem.transStatusId, newItem.transStatusId);
    }


    public interface TransactionClickCallback {
        void onClick(TransactionObject transaction);

        void onCopyClick();
    }
}
