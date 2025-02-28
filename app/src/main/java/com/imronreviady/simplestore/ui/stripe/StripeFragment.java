package com.imronreviady.simplestore.ui.stripe;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentStripeBinding;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.shop.ShopViewModel;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

/**
 * A simple {@link Fragment} subclass.
 */
public class StripeFragment extends PSFragment {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private ShopViewModel shopViewModel;

    private Card card;
    private ProgressDialog progressDialog;

    @VisibleForTesting
    private AutoClearedValue<Stripe> stripe;
    private AutoClearedValue<FragmentStripeBinding> binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        FragmentStripeBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stripe, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();

    }

    @Override
    protected void initUIAndActions() {

//        psDialogMsg = new PSDialogMsg(getActivity(), false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage(getString(R.string.get_token));
        progressDialog.setCancelable(false);

        binding.get().button3.setOnClickListener(v -> {

            card = binding.get().cardInputWidget.getCard();

            if (card != null) {
                createTransaction();
            }
        });

    }

    @Override
    protected void initViewModels() {

        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);

    }

    @Override
    protected void initAdapters() {

    }

    @Override
    protected void initData() {

//        if (getActivity() != null) {
//            blogId = getActivity().getIntent().getStringExtra(Constants.BLOG_ID);
//        }

        shopViewModel.setShopObj(Config.API_KEY);
        shopViewModel.getShopData().observe(this, resource -> {

            if (resource != null) {

                Utils.psLog("Got Data" + resource.message + resource.toString());

                switch (resource.status) {
                    case LOADING:
                        // Loading State
                        // Data are from Local DB

                        if (resource.data != null) {

                            fadeIn(binding.get().getRoot());

                            binding.get().setShop(resource.data);
                        }
                        break;
                    case SUCCESS:
                        // Success State
                        // Data are from Server

                        if (resource.data != null) {
                            binding.get().setShop(resource.data);
                            shopViewModel.stripePublishableKey = resource.data.stripePublishableKey;

                            if(getContext() != null) {
                                stripe = new AutoClearedValue<>(this, new Stripe(getContext(), shopViewModel.stripePublishableKey));
                            }
                        }

                        break;
                    case ERROR:
                        // Error State

                        break;
                    default:
                        // Default

                        break;
                }

            } else {

                // Init Object or Empty Data
                Utils.psLog("Empty Data");

            }


            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            if (resource != null) {
                Utils.psLog("Got Data Of About Us.");

            } else {
                //noinspection ConstantConditions
                Utils.psLog("No Data of About Us.");
            }
        });

    }

    private void createTransaction() {
        progressDialog.show();

        if(getContext() != null) {

            stripe.get().createToken(
                    card,
                    new TokenCallback() {
                        public void onSuccess(Token token) {
                            // Send token to your server

                            Utils.psLog("PAYMENT_STRIPE Token Id" + token.getId());

                            progressDialog.cancel();

                            close(token.getId());
                        }

                        public void onError(Exception error) {
                            // Show localized error message

                            Utils.psLog("PAYMENT_STRIPE ERROR");

                            progressDialog.cancel();

                        }
                    }
            );
        }
    }

    public void close(String token) {
        navigationController.navigateBackToCheckoutFragment(getActivity(), token);

        if(getActivity() != null) {
            getActivity().finish();
        }
    }
}
