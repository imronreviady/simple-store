package com.imronreviady.simplestore.ui.basket;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentBasketListBinding;
import com.imronreviady.simplestore.ui.basket.adapter.BasketAdapter;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.product.BasketViewModel;
import com.imronreviady.simplestore.viewobject.Basket;
import com.imronreviady.simplestore.viewobject.common.Status;

import java.util.List;

/**
 * Created by Panacea-Soft
 * Contact Email : teamps.is.cool@gmail.com
 * Website : http://www.panacea-soft.com
 */
public class BasketListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    //region Variables

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private PSDialogMsg psDialogMsg;
    private BasketViewModel basketViewModel;

    @VisibleForTesting
    private AutoClearedValue<FragmentBasketListBinding> binding;
    private AutoClearedValue<BasketAdapter> basketAdapter;

    //endregion

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBasketListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_basket_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        return binding.get().getRoot();
    }

    @Override
    public void onDispatched() {

    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);
        } else {
            binding.get().adView.setVisibility(View.GONE);
        }

        binding.get().checkoutButton.setText(binding.get().checkoutButton.getText().toString());

        binding.get().checkoutButton.setOnClickListener(view -> {

            if (loginUserId.equals("")) {

                psDialogMsg.showInfoDialog(getString(R.string.error_message__login_first), getString(R.string.app__ok));

                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(v -> {
                    navigationController.navigateToUserLoginActivity(getActivity());
                    psDialogMsg.cancel();
                });

            } else {
                navigationController.navigateToCheckoutActivity(getActivity());
            }
        });
    }

    @Override
    protected void initViewModels() {
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
    }

    @Override
    protected void initAdapters() {

        //basket
        BasketAdapter basketAdapter1 = new BasketAdapter(dataBindingComponent, new BasketAdapter.BasketClickCallBack() {
            @Override
            public void onMinusClick(Basket basket) {
                basketViewModel.setUpdateToBasketListObj(basket.id, basket.count);

            }

            @Override
            public void onAddClick(Basket basket) {
                basketViewModel.setUpdateToBasketListObj(basket.id, basket.count);
            }

            @Override
            public void onDeleteConfirm(Basket basket) {

                psDialogMsg.showConfirmDialog(getString(R.string.delete_item_from_basket), getString(R.string.app__ok), getString(R.string.app__cancel));

                psDialogMsg.show();

                psDialogMsg.okButton.setOnClickListener(view -> {
                    basketViewModel.setDeleteToBasketListObj(basket.id);
                    psDialogMsg.cancel();
                });
                psDialogMsg.cancelButton.setOnClickListener(view -> psDialogMsg.cancel());

            }

            @Override
            public void onClick(Basket basket) {
                navigationController.navigateToProductDetailActivity(getActivity(), basket);
            }

        }, this);
        basketAdapter = new AutoClearedValue<>(this, basketAdapter1);
        bindingBasketAdapter(basketAdapter.get());

    }

    private void bindingBasketAdapter(BasketAdapter nvbasketAdapter) {
        this.basketAdapter = new AutoClearedValue<>(this, nvbasketAdapter);
//        binding.get().basketRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.get().basketRecycler.setAdapter(basketAdapter.get());
    }

    @Override
    protected void initData() {

        if (getContext() != null) {
            binding.get().noItemTitleTextView.setText(getContext().getString(R.string.basket__no_item_title));
            binding.get().noItemDescTextView.setText(getContext().getString(R.string.basket__no_item_desc));
        }

        LoadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE__BASKET_FRAGMENT
                && resultCode == Constants.RESULT_CODE__REFRESH_BASKET_LIST) {

            basketViewModel.setBasketListWithProductObj();

            loadLoginUserId();
        }

    }

    @Override
    public void onResume() {

        super.onResume();

        loadLoginUserId();


        basketViewModel.setBasketListWithProductObj();

    }

    private void LoadData() {
        //load basket

        basketViewModel.setBasketListWithProductObj();
        LiveData<List<Basket>> basketData = basketViewModel.getAllBasketWithProductList();
        if (basketData != null) {
            basketData.observe(this, listResource -> {
                if (listResource != null) {
                    if (listResource.size() > 0) {

                        binding.get().noItemConstraintLayout.setVisibility(View.GONE);
                        binding.get().checkoutConstraintLayout.setVisibility(View.VISIBLE);

                    } else {

                        binding.get().checkoutConstraintLayout.setVisibility(View.GONE);
                        binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);

                        if (getActivity() instanceof BasketListActivity) {
                            getActivity().finish();
                        }

                    }

                    replaceProductSpecsData(listResource);

                } else {
                    if(basketViewModel.getAllBasketWithProductList() != null) {
                        if (basketViewModel.getAllBasketWithProductList().getValue() != null) {
                            if (basketViewModel.getAllBasketWithProductList().getValue().size() == 0) {
                                binding.get().checkoutConstraintLayout.setVisibility(View.GONE);
                                binding.get().noItemConstraintLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

            });
        }

        basketViewModel.getBasketUpdateData().observe(this, resourse -> {
            if (resourse != null) {
                if (resourse.status == Status.SUCCESS) {

                    basketViewModel.totalPrice = 0;
                    basketViewModel.basketCount = 0;

                    basketViewModel.setBasketListWithProductObj();

                }
            }
        });

        basketViewModel.getBasketDeleteData().observe(this, resource -> {
            if (resource != null) {
                if (resource.status == Status.SUCCESS) {

                    basketViewModel.totalPrice = 0;
                    basketViewModel.basketCount = 0;

                    basketViewModel.setBasketListWithProductObj();

                }
            }
        });


    }

    private void replaceProductSpecsData(List<Basket> basketList) {

        basketAdapter.get().replace(basketList);

        if (basketList != null) {
            basketAdapter.get().replace(basketList);

            if (basketList.size() > 0) {
                basketViewModel.totalPrice = 0;

                for (int i = 0; i < basketList.size(); i++) {
                    basketViewModel.totalPrice += basketList.get(i).basketPrice * basketList.get(i).count;
                }

                basketViewModel.basketCount = 0;

                for (int i = 0; i < basketList.size(); i++) {
                    basketViewModel.basketCount += basketList.get(i).count;
                }

                String totalPriceString = basketList.get(0).product.currencySymbol + Constants.SPACE_STRING + Utils.format(Utils.round(basketViewModel.totalPrice, 2));

                binding.get().totalPriceTextView.setText(totalPriceString);
                binding.get().countTextView.setText(String.valueOf(basketViewModel.basketCount));
            } else {
                binding.get().totalPriceTextView.setText(Constants.ZERO);
                binding.get().countTextView.setText(Constants.ZERO);

                basketViewModel.totalPrice = 0;
                basketViewModel.basketCount = 0;

                if (basketList.size() > 0) {
                    for (int i = 0; i < basketList.size(); i++) {
                        basketViewModel.totalPrice += basketList.get(i).basketPrice * basketList.get(i).count;
                    }
                    for (int i = 0; i < basketList.size(); i++) {
                        basketViewModel.basketCount += basketList.get(i).count;
                    }

                    String totalPriceString = basketList.get(0).product.currencySymbol + Constants.SPACE_STRING + Utils.format(basketViewModel.totalPrice);
                    binding.get().totalPriceTextView.setText(totalPriceString);
                    binding.get().countTextView.setText(String.valueOf(basketViewModel.basketCount));
                } else {
                    binding.get().totalPriceTextView.setText(Constants.ZERO);
                    binding.get().countTextView.setText(Constants.ZERO);
                }
                binding.get().executePendingBindings();

            }
        }
    }

}