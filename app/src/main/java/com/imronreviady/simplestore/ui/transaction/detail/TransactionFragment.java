package com.imronreviady.simplestore.ui.transaction.detail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentTransactionBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.ui.transaction.detail.adapter.TransactionAdapter;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.transaction.TransactionListViewModel;
import com.imronreviady.simplestore.viewmodel.transaction.TransactionOrderViewModel;
import com.imronreviady.simplestore.viewobject.TransactionObject;
import com.imronreviady.simplestore.viewobject.TransactionDetail;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

public class TransactionFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private TransactionListViewModel transactionListViewModel;
    private TransactionOrderViewModel transactionOrderViewModel;
    private String transactionId;
    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentTransactionBinding> binding;
    private AutoClearedValue<TransactionAdapter> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTransactionBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);
        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);

        binding.get().copyImage.setOnClickListener(view -> {
            if (getContext() != null) {
                ClipboardManager cManger = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData cData = ClipData.newPlainText(Constants.TRANSACTON_TEXT, binding.get().transactionNoValueTextView.getText());
                if (cManger != null) {
                    cManger.setPrimaryClip(cData);

                    Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    protected void initViewModels() {
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel.class);
        transactionOrderViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionOrderViewModel.class);
    }

    @Override
    protected void initAdapters() {
        TransactionAdapter nvAdapter = new TransactionAdapter(dataBindingComponent, transaction -> {
            //binding.get().getRoot().setOnClickListener(view -> navigationController.navigateToCommentDetailActivity(getActivity()));
        }, this);
        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().transactionOrderList.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {

        try {
            if (getActivity() != null) {
                if (getActivity().getIntent().getExtras() != null) {
                    transactionId = getActivity().getIntent().getExtras().getString(Constants.TRANSACTION_ID);
                }
            }
        } catch (Exception e) {
            Utils.psErrorLog("", e);
        }
        transactionListViewModel.setTransactionDetailObj(loginUserId, transactionId);

        LiveData<Resource<TransactionObject>> transactionDetail = transactionListViewModel.getTransactionDetailData();

        if (transactionDetail != null) {
            transactionDetail.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceTransactionDetailData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceTransactionDetailData(listResource.data);

                            }

                            transactionListViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            transactionListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    transactionListViewModel.setLoadingState(false);
                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                }

            });

        }


        //Transaction Order
        transactionOrderViewModel.setTransactionOrderListObj(String.valueOf(transactionOrderViewModel.offset), transactionId);

        LiveData<Resource<List<TransactionDetail>>> news = transactionOrderViewModel.getTransactionListData();

        if (news != null) {
            news.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            transactionOrderViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            transactionOrderViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (transactionOrderViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        transactionOrderViewModel.forceEndLoading = true;
                    }

                }

            });
        }

    }

    //endregion
    private void replaceData(List<TransactionDetail> transactionDetailList) {
        adapter.get().replace(transactionDetailList);
        binding.get().executePendingBindings();


    }

    private void replaceTransactionDetailData(TransactionObject transactionObject) {

        binding.get().setTransactionDetail(transactionObject);

        binding.get().statusText.setText(getString(R.string.transaction_detail__status));
        binding.get().orderDateText.setText(getString(R.string.transaction_detail__order_date));
        binding.get().originalPriceTextView.setText(getString(R.string.transaction_detail__total_charges));
        binding.get().discountPriceTextView.setText(getString(R.string.transaction_detail__discount_price));
        binding.get().priceTextView.setText(getString(R.string.transaction_detail__total_charges));

        binding.get().transactionNoTextView.setText(getString(R.string.transaction_detail__transaction_number));

        binding.get().transactionNoValueTextView.setText(transactionObject.transCode);
        binding.get().countValueText.setText(transactionObject.transStatusTitle);
        binding.get().orderDateValueText.setText(transactionObject.addedDate);

        String originalPriceValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.subTotalAmount));
        binding.get().originalPriceValueTextView.setText(originalPriceValueString);
        String discountValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.discountAmount));
        binding.get().discountValueTextView.setText(discountValueString);
        String priceValueString = transactionObject.currencySymbol + Constants.SPACE_STRING + Utils.format(Double.parseDouble(transactionObject.balanceAmount));
        binding.get().priceValueTextView.setText(priceValueString);

        binding.get().shippingPhoneValueTextView.setText(transactionObject.shippingPhone);
        binding.get().shippingEmailValueTextView.setText(transactionObject.shippingEmail);
        binding.get().shippingAddressValueTextView.setText(transactionObject.shippingAddress1);

        binding.get().billingphoneValueTextView.setText(transactionObject.billingPhone);
        binding.get().billingEmailValueTextView.setText(transactionObject.billingEmail);
        binding.get().billingAddressValueTextView.setText(transactionObject.billingAddress1);

    }
}