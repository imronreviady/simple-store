package com.imronreviady.simplestore.ui.checkout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.CheckoutFragment3Binding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.paypal.PaypalViewModel;
import com.imronreviady.simplestore.viewmodel.product.BasketViewModel;
import com.imronreviady.simplestore.viewmodel.shop.ShopViewModel;
import com.imronreviady.simplestore.viewmodel.transaction.TransactionListViewModel;
import com.imronreviady.simplestore.viewobject.Basket;
import com.imronreviady.simplestore.viewobject.BasketProductListToServerContainer;
import com.imronreviady.simplestore.viewobject.BasketProductToServer;
import com.imronreviady.simplestore.viewobject.TransactionHeaderUpload;
import com.imronreviady.simplestore.viewobject.User;
import com.imronreviady.simplestore.viewobject.common.Status;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class CheckoutFragment3 extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private PaypalViewModel paypalViewModel;

    AutoClearedValue<CheckoutFragment3Binding> binding;

    private BasketViewModel basketViewModel;
    private TransactionListViewModel transactionListViewModel;
    private ShopViewModel shopViewModel;
    private User user;
    private List<Basket> basketList;
    private BasketProductListToServerContainer basketProductListToServerContainer = new BasketProductListToServerContainer();
    private String clientTokenString;
    String paymentMethod = Constants.PAYMENT_CASH_ON_DELIVERY;
    private String payment_method_nonce;
    private CardView oldCardView;
    private TextView oldTextView;
    private ImageView oldImageView;
    private ProgressDialog progressDialog;
    boolean clicked = false;

    private PSDialogMsg psDialogMsg;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CheckoutFragment3Binding dataBinding = DataBindingUtil.inflate(inflater, R.layout.checkout_fragment_3, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }


    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        binding.get().cashImageView.setColorFilter(getResources().getColor(R.color.md_grey_500));

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage(getString(R.string.com_facebook_loading));
        progressDialog.setCancelable(false);

        binding.get().cashCardView.setOnClickListener(v -> {

            if (!clicked) {
                clicked = true;

                oldCardView = binding.get().cashCardView;
                oldImageView = binding.get().cashImageView;
                oldTextView = binding.get().cashTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);
            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().cashCardView, binding.get().cashTextView, binding.get().cashImageView);

                    oldCardView = binding.get().cashCardView;
                    oldImageView = binding.get().cashImageView;
                    oldTextView = binding.get().cashTextView;
                }
            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__COD);

            paymentMethod = Constants.PAYMENT_CASH_ON_DELIVERY;

        });

        binding.get().cardCardView.setOnClickListener(v -> {

            if (!clicked) {
                clicked = true;

                oldCardView = binding.get().cardCardView;
                oldImageView = binding.get().cardImageView;
                oldTextView = binding.get().cardTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);
            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().cardCardView, binding.get().cardTextView, binding.get().cardImageView);

                    oldCardView = binding.get().cardCardView;
                    oldImageView = binding.get().cardImageView;
                    oldTextView = binding.get().cardTextView;
                }
            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__STRIPE);
            paymentMethod = Constants.PAYMENT_STRIPE;

        });

        psDialogMsg = new PSDialogMsg(this.getActivity(), false);

        binding.get().paypalCardView.setOnClickListener(v -> {

            if (!clicked) {
                clicked = true;

                oldCardView = binding.get().paypalCardView;
                oldImageView = binding.get().paypalImageView;
                oldTextView = binding.get().paypalTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);
            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().paypalCardView, binding.get().paypalTextView, binding.get().paypalImageView);

                    oldCardView = binding.get().paypalCardView;
                    oldImageView = binding.get().paypalImageView;
                    oldTextView = binding.get().paypalTextView;
                }
            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__PAYPAL);
            paymentMethod = Constants.PAYMENT_PAYPAL;

        });

        binding.get().bankCardView.setOnClickListener(v -> {
            if (!clicked) {

                clicked = true;

                oldCardView = binding.get().bankCardView;
                oldImageView = binding.get().bankImageView;
                oldTextView = binding.get().bankTextView;

                changeToOrange(oldCardView, oldTextView, oldImageView);

            } else {

                if (oldCardView != null && oldImageView != null && oldTextView != null) {
                    changeToWhite(oldCardView, oldTextView, oldImageView);
                    changeToOrange(binding.get().bankCardView, binding.get().bankTextView, binding.get().bankImageView);

                    oldCardView = binding.get().bankCardView;
                    oldImageView = binding.get().bankImageView;
                    oldTextView = binding.get().bankTextView;
                }

            }

            binding.get().warningTitleTextView.setText(R.string.checkout__information__PAYPAL);
            paymentMethod = Constants.PAYMENT_BANK;

        });

        if (cod.equals(Constants.ONE)) {
            binding.get().cashCardView.setVisibility(View.VISIBLE);

            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }

        if (paypal.equals(Constants.ONE)) {
            binding.get().paypalCardView.setVisibility(View.VISIBLE);
            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }

        if (stripe.equals(Constants.ONE)) {
            binding.get().cardCardView.setVisibility(View.VISIBLE);
            if (binding.get().noPaymentTextView.getVisibility() == View.VISIBLE) {
                binding.get().noPaymentTextView.setVisibility(View.GONE);
            }
        }

    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel.class);
        paypalViewModel = ViewModelProviders.of(this, viewModelFactory).get(PaypalViewModel.class);
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);
    }

    @Override
    protected void initAdapters() {

    }

    void sendData() {

        if (getActivity() != null) {
            user = ((CheckoutActivity) CheckoutFragment3.this.getActivity()).getCurrentUser();
        }

        if (basketList != null) {

            if (basketList.size() > 0) {

                if (getActivity() != null) {
                    switch (paymentMethod) {

                        case Constants.PAYMENT_PAYPAL:

                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ONE,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            basketProductListToServerContainer.productList
                                    )
                            );
                            break;

                        case Constants.PAYMENT_STRIPE:

                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ONE,
                                            Constants.RATING_ZERO,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            basketProductListToServerContainer.productList
                                    )
                            );
                            break;

                        case Constants.PAYMENT_CASH_ON_DELIVERY:

                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ONE,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            basketProductListToServerContainer.productList
                                    )
                            );
                            break;

                        case Constants.PAYMENT_BANK:

                            transactionListViewModel.setSendTransactionDetailDataObj(new TransactionHeaderUpload(
                                            user.userId,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.sub_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.coupon_discount),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_tax),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.final_total),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total),
                                            user.userName,
                                            user.userPhone,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ZERO,
                                            Constants.RATING_ONE,
                                            payment_method_nonce,
                                            Constants.RATING_ONE,
                                            basketList.get(0).product.currencySymbol,
                                            basketList.get(0).product.currencyShortForm,
                                            user.billingFirstName,
                                            user.billingLastName,
                                            user.billingCompany,
                                            user.billingAddress1,
                                            user.billingAddress2,
                                            user.billingCountry,
                                            user.billingState,
                                            user.billingCity,
                                            user.billingPostalCode,
                                            user.billingEmail,
                                            user.billingPhone,
                                            user.shippingFirstName,
                                            user.shippingLastName,
                                            user.shippingCompany,
                                            user.shippingAddress1,
                                            user.shippingAddress2,
                                            user.shippingCountry,
                                            user.shippingState,
                                            user.shippingCity,
                                            user.shippingPostalCode,
                                            user.shippingEmail,
                                            user.shippingPhone,
                                            shippingTax,
                                            overAllTax,
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shipping_cost),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.shippingMethodName),
                                            binding.get().memoEditText.getText().toString(),
                                            String.valueOf(((CheckoutActivity) getActivity()).transactionValueHolder.total_item_count),
                                            basketProductListToServerContainer.productList
                                    )
                            );
                            break;
                    }
                }

            } else {
                psDialogMsg.showErrorDialog(getString(R.string.basket__no_item_desc), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE__PAYPAL) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);

                if (result.getPaymentMethodNonce() != null) {
                    onPaymentMethodNonceCreated(result.getPaymentMethodNonce());
                }
            }
//            else {
//                // handle errors here, an exception may be available in
////                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
//            }
        } else if (requestCode == Constants.REQUEST_CODE__STRIPE_ACTIVITY && resultCode == Constants.RESULT_CODE__STRIPE_ACTIVITY) {
            if (this.getActivity() != null) {

                payment_method_nonce = data.getStringExtra(Constants.PAYMENT_TOKEN);

                progressDialog.show();

                sendData();

            }
        }
    }


    @Override
    protected void initData() {

        shopViewModel.setShopObj(Config.API_KEY);

        shopViewModel.getShopData().observe(this, result -> {

            if(result != null)
            {
                switch (result.status)
                {
                    case SUCCESS:

                        if(result.data != null)
                        {
                            if(result.data.paypalEnabled.equals(Constants.ONE))
                            {
                                binding.get().paypalCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().paypalCardView.setVisibility(View.GONE);
                            }

                            if(result.data.codEnabled.equals(Constants.ONE))
                            {
                                binding.get().cashCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().cashCardView.setVisibility(View.GONE);
                            }

                            if(result.data.stripeEnabled.equals(Constants.ONE))
                            {
                                binding.get().cardCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().cardCardView.setVisibility(View.GONE);
                            }

                            if(result.data.banktransferEnabled.equals(Constants.ONE))
                            {
                                binding.get().bankCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().bankCardView.setVisibility(View.GONE);
                            }
                        }

                        break;

                    case LOADING:

                        if(result.data != null)
                        {
                            if(result.data.paypalEnabled.equals(Constants.ONE))
                            {
                                binding.get().paypalCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().paypalCardView.setVisibility(View.GONE);
                            }

                            if(result.data.codEnabled.equals(Constants.ONE))
                            {
                                binding.get().cashCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().cashCardView.setVisibility(View.GONE);
                            }

                            if(result.data.stripeEnabled.equals(Constants.ONE))
                            {
                                binding.get().cardCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().cardCardView.setVisibility(View.GONE);
                            }

                            if(result.data.banktransferEnabled.equals(Constants.ONE))
                            {
                                binding.get().bankCardView.setVisibility(View.VISIBLE);
                            }else
                            {
                                binding.get().bankCardView.setVisibility(View.GONE);
                            }
                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        paypalViewModel.getPaypalTokenData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case SUCCESS:

                        clientTokenString = result.message;

                        onBraintreeSubmit();

                        progressDialog.cancel();

                        break;

                    case ERROR:

                        Toast.makeText(getContext(), result.message, Toast.LENGTH_SHORT).show();

                        progressDialog.cancel();

                        break;
                }
            }
        });

        basketViewModel.setBasketListWithProductObj();
        basketViewModel.getAllBasketWithProductList().observe(this, baskets -> {
            if (baskets != null && baskets.size() > 0) {
                basketList = baskets;

//                String currency = "";
                for (Basket basket : baskets) {
                    //                    currency = basket.product.currencyShortForm;
                    HashMap<String, String> map = new Gson().fromJson(basket.selectedAttributes, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    Iterator<String> keyIterator = map.keySet().iterator();
                    StringBuilder keyStr = new StringBuilder();
                    StringBuilder nameStr = new StringBuilder();
                    while (keyIterator.hasNext()) {
                        String key = keyIterator.next();
                        if (!key.equals("-1")) {
                            if (map.containsKey(key)) {

                                if (!keyStr.toString().equals("")) {
                                    keyStr.append(Config.ATTRIBUT_SEPERATOR);
                                    nameStr.append(Config.ATTRIBUT_SEPERATOR);
                                }
                                keyStr.append(key);
                                nameStr.append(map.get(key));

                            }
                        }

                    }

                    HashMap<String, String> priceMap = new Gson().fromJson(basket.selectedAttributesPrice, new TypeToken<HashMap<String, String>>() {
                    }.getType());
                    Iterator<String> priceKeyIterator = priceMap.keySet().iterator();
                    StringBuilder priceStr = new StringBuilder();
                    while (priceKeyIterator.hasNext()) {
                        String key = priceKeyIterator.next();

                        if (!key.equals("-1")) {
                            if (priceMap.containsKey(key)) {

                                if (!priceStr.toString().equals("")) {
                                    priceStr.append(Config.ATTRIBUT_SEPERATOR);
                                }
                                priceStr.append(priceMap.get(key));
                            }
                        }
                    }

                    Utils.psLog("Data for map" + basket.selectedAttributes);

                    BasketProductToServer basketProductToServer = new BasketProductToServer(
                            "",
                            basket.productId,
                            basket.product.name,
                            keyStr.toString(),
                            nameStr.toString(),
                            priceStr.toString(),
                            basket.selectedColorId,
                            basket.selectedColorValue,
                            String.valueOf(basket.product.unitPrice),
                            String.valueOf(basket.basketOriginalPrice),
                            String.valueOf(basket.basketOriginalPrice - basket.product.unitPrice),
                            String.valueOf(basket.product.discountAmount),
                            String.valueOf(basket.count),
                            String.valueOf(basket.product.discountValue),
                            String.valueOf(basket.product.discountPercent),
                            basket.product.currencyShortForm,
                            basket.product.currencySymbol);

                    basketProductListToServerContainer.productList.add(basketProductToServer);
                }
            }

        });

        transactionListViewModel.getSendTransactionDetailData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (CheckoutFragment3.this.getActivity() != null) {

                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).number = 4;

                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).transactionObject = result.data;

                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).goToFragment4();

                        basketViewModel.setWholeBasketDeleteStateObj();

                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }

//                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).progressDialog.cancel();
                    }

                } else if (result.status == Status.ERROR) {
                    if (CheckoutFragment3.this.getActivity() != null) {

                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }

//                        ((CheckoutActivity) CheckoutFragment3.this.getActivity()).progressDialog.cancel();

                        psDialogMsg.showErrorDialog(result.message, getString(R.string.app__ok));

                        psDialogMsg.show();
                    }
                }
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

    void getToken() {

        paypalViewModel.setPaypalTokenObj();
        progressDialog.show();

    }

    private void onBraintreeSubmit() {
        if (getActivity() != null) {
            DropInRequest dropInRequest = new DropInRequest()
                    .clientToken(clientTokenString);
            this.getActivity().startActivityForResult(dropInRequest.getIntent(this.getActivity()), Constants.REQUEST_CODE__PAYPAL);
        }
    }

    private void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
//        if (paymentMethodNonce instanceof PayPalAccountNonce) {
//            PayPalAccountNonce paypalAccountNonce = (PayPalAccountNonce) paymentMethodNonce;

//            PayPalCreditFinancing creditFinancing = paypalAccountNonce.getCreditFinancing();
//            if (creditFinancing != null) {
//                // PayPal Credit was accepted
//            }

        // Access additional information

        progressDialog.show();

        payment_method_nonce = paymentMethodNonce.getNonce();

        sendData();

//        }
        /*else {
            // Send nonce to server
            String nonce = paymentMethodNonce.getNonce();
        }*/
    }

    private void changeToOrange(CardView cardView, TextView textView, ImageView imageView) {

        cardView.setCardBackgroundColor(getResources().getColor(R.color.global__primary));
        imageView.setColorFilter(getResources().getColor(R.color.md_white_1000));
        textView.setTextColor(getResources().getColor(R.color.md_white_1000));
    }

    private void changeToWhite(CardView cardView, TextView textView, ImageView imageView) {

        cardView.setCardBackgroundColor(getResources().getColor(R.color.md_white_1000));
        imageView.setColorFilter(getResources().getColor(R.color.md_grey_500));
        textView.setTextColor(getResources().getColor(R.color.md_grey_700));
    }
}
