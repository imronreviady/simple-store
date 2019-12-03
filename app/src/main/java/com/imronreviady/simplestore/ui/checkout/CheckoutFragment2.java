package com.imronreviady.simplestore.ui.checkout;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.CheckoutFragment2Binding;
import com.imronreviady.simplestore.ui.checkout.adapter.ShippingMethodsAdapter;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.coupondiscount.CouponDiscountViewModel;
import com.imronreviady.simplestore.viewmodel.product.BasketViewModel;
import com.imronreviady.simplestore.viewmodel.shippingmethod.ShippingMethodViewModel;
import com.imronreviady.simplestore.viewobject.Basket;
import com.imronreviady.simplestore.viewobject.ShippingMethod;
import com.imronreviady.simplestore.viewobject.common.Status;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class CheckoutFragment2 extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private BasketViewModel basketViewModel;
    private ShippingMethodViewModel shippingMethodViewModel;
    private CouponDiscountViewModel couponDiscountViewModel;
    private ProgressDialog progressDialog;

    @VisibleForTesting
    private AutoClearedValue<CheckoutFragment2Binding> binding;
    private AutoClearedValue<ShippingMethodsAdapter> adapter;

    private PSDialogMsg psDialogMsg;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CheckoutFragment2Binding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_2, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setCancelable(false);

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);


        binding.get().couponDiscountButton.setOnClickListener(v -> {
            if(( CheckoutFragment2.this.getActivity()) != null) {
                ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.couponDiscountText = binding.get().couponDiscountValueEditText.getText().toString();
            }
            couponDiscountViewModel.setCouponDiscountObj( binding.get().couponDiscountValueEditText.getText().toString());
            progressDialog.setMessage(getString(R.string.check_coupon));
            progressDialog.show();
        });

        if (!overAllTax.isEmpty()) {
            binding.get().overAllTaxTextView.setText(getString(R.string.tax, String.valueOf(Math.round(Float.parseFloat(overAllTax) * 100))));
        }

        if (!shippingTax.isEmpty()) {
            binding.get().shippingTaxTextView.setText(getString(R.string.shipping_tax, String.valueOf(Math.round(Float.parseFloat(shippingTax) * 100))));
        }

    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        shippingMethodViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShippingMethodViewModel.class);
        couponDiscountViewModel = ViewModelProviders.of(this, viewModelFactory).get(CouponDiscountViewModel.class);
    }

    @Override
    protected void initAdapters() {

        if(getActivity() != null) {

            ShippingMethodsAdapter nvAdapter = new ShippingMethodsAdapter(dataBindingComponent, shippingMethod -> {

                if (CheckoutFragment2.this.getActivity() != null) {

                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);
                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shippingMethodName = shippingMethod.name;
                    ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId = shippingMethod.id;

                    CheckoutFragment2.this.calculateTheBalance();
                }
            },shippingId,((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId);

            adapter = new AutoClearedValue<>(this, nvAdapter);
            binding.get().shippingMethodsRecyclerView.setAdapter(adapter.get());
        }

    }

    @Override
    protected void initData() {

        shippingMethodViewModel.setShippingMethodsObj();

        shippingMethodViewModel.getShippingMethodsData().observe(this, result -> {

            if (result.data != null) {
                switch (result.status) {

                    case SUCCESS:
                        CheckoutFragment2.this.replaceShippingMethods(result.data);

                        for (ShippingMethod shippingMethod : result.data) {

                            if(( CheckoutFragment2.this.getActivity()) != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id.equals(shippingId) && ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId.isEmpty()) {
                                        if (CheckoutFragment2.this.getActivity() != null) {
                                            ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);

                                            CheckoutFragment2.this.calculateTheBalance();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;

                    case LOADING:
                        CheckoutFragment2.this.replaceShippingMethods(result.data);

                        for (ShippingMethod shippingMethod : result.data) {

                            if(( CheckoutFragment2.this.getActivity()) != null) {
                                if (!shippingId.isEmpty()) {
                                    if (shippingMethod.id.equals(shippingId) && ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.selectedShippingId.isEmpty()) {
                                        if (CheckoutFragment2.this.getActivity() != null) {
                                            ((CheckoutActivity) CheckoutFragment2.this.getActivity()).transactionValueHolder.shipping_cost = Utils.round(shippingMethod.price, 2);

                                            CheckoutFragment2.this.calculateTheBalance();
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                }
            }
        });

        couponDiscountViewModel.getCouponDiscountData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case ERROR:

                        progressDialog.cancel();

                        psDialogMsg.showErrorDialog(getString(R.string.error_coupon), getString(R.string.app__ok));
                        psDialogMsg.show();

                        break;

                    case SUCCESS:

                        if (result.data != null) {

                            progressDialog.cancel();

                            psDialogMsg.showSuccessDialog(getString(R.string.checkout_detail__claimed_coupon), getString(R.string.app__ok));
                            psDialogMsg.show();

                            if(getActivity() != null)
                            {
                                ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount = Utils.round(Float.parseFloat(result.data.couponAmount), 2);
                                Utils.psLog("coupon5" +  ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount + "");
                            }

                            calculateTheBalance();
                        }

                        break;
                }
            }
        });

        basketViewModel.setBasketListWithProductObj();
        basketViewModel.getAllBasketWithProductList().observe(this, baskets -> {
            if (baskets != null && baskets.size() > 0) {

                if (getActivity() != null) {
                    ((CheckoutActivity) getActivity()).transactionValueHolder.resetValues();

                    for (Basket basket : baskets) {

                        ((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count += basket.count;
                        ((CheckoutActivity) getActivity()).transactionValueHolder.total += Utils.round((basket.basketOriginalPrice) * basket.count, 2);

                        ((CheckoutActivity) getActivity()).transactionValueHolder.discount += Utils.round(basket.product.discountAmount * basket.count, 2);
                        ((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol = basket.product.currencySymbol;

                    }
                }
                calculateTheBalance();
            }
        });

        basketViewModel.getWholeBasketDeleteData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    Utils.psLog("Success");
                } else if (result.status == Status.ERROR) {
                    Utils.psLog("Fail");
                }
            }
        });
    }

    private void replaceShippingMethods(List<ShippingMethod> shippingMethods) {
        this.adapter.get().replace(shippingMethods);
        this.binding.get().executePendingBindings();
    }

    private void calculateTheBalance() {

        if (getActivity() != null) {

            ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.total - (((CheckoutActivity) getActivity()).transactionValueHolder.discount),2);

            if (((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount > 0) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.sub_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total - ((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount, 2);
            }

            if (!overAllTax.isEmpty()) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.tax = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total * Float.parseFloat(overAllTax), 2);
            }

            if (!shippingTax.isEmpty() && ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost >= 0) {
                ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost * Float.parseFloat(shippingTax),2);
            }

            ((CheckoutActivity) getActivity()).transactionValueHolder.final_total = Utils.round(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total + ((CheckoutActivity) getActivity()).transactionValueHolder.tax +
                    ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax + ((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost,2);
            updateUI();
        }

    }

    private void updateUI() {

        if (getActivity() != null) {

            if (((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count > 0) {
                binding.get().totalItemCountValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.total > 0) {

                binding.get().totalValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.total)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount > 0) {
                binding.get().couponDiscountValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.discount > 0) {
                binding.get().discountValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.discount)));
            }

            if (!((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText.isEmpty()) {
                binding.get().couponDiscountValueEditText.setText(((CheckoutActivity) getActivity()).transactionValueHolder.couponDiscountText);
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.sub_total > 0) {
                binding.get().subtotalValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.tax > 0) {
                binding.get().taxValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.tax)));
            }

            if (!shippingTax.equals("0.0") && !shippingTax.equals(Constants.RATING_ZERO)) {
                binding.get().shippingTaxValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(Utils.round((((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax), 2))));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.final_total > 0.0) {
                binding.get().finalTotalValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.final_total)));
            }

            if (((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost >= 0) {
                binding.get().shippingCostValueTextView.setText(String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.currencySymbol + " " + Utils.format(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost)));
            }

        }

    }

}
