package com.imronreviady.simplestore.ui.product.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.databinding.CardviewHeaderBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.Objects;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.ProductAttributeDetail;
import com.imronreviady.simplestore.viewobject.ProductAttributeHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.databinding.DataBindingUtil;

public abstract class ProductAttributeHeaderAdapter extends DataBoundListAdapter<ProductAttributeHeader, CardviewHeaderBinding> implements AdapterView.OnItemSelectedListener {

    private final androidx.databinding.DataBindingComponent dataBindingComponent;
    private HeaderProductClickCallBack callback;
    private DataBoundListAdapter.DiffUtilDispatchedInterface diffUtilDispatchedInterface = null;
    public Map<String, String> basketItemHolderHashMap;
    public String currencySymbol;

    protected ProductAttributeHeaderAdapter(androidx.databinding.DataBindingComponent dataBindingComponent, Map<String, String> basketItemHolderHashMap, HeaderProductClickCallBack headerProductClickCallBack) {

        this.dataBindingComponent = dataBindingComponent;
        this.callback = headerProductClickCallBack;
        this.basketItemHolderHashMap = basketItemHolderHashMap;

    }

    @Override
    protected CardviewHeaderBinding createBinding(ViewGroup parent) {

        return DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.cardview_header
                        , parent,
                        false,
                        dataBindingComponent);

    }

    @Override
    protected void dispatched() {

        if (diffUtilDispatchedInterface != null) {
            diffUtilDispatchedInterface.onDispatched();
        }

    }

    @Override
    protected void bind(CardviewHeaderBinding binding, ProductAttributeHeader item) {

        if (item != null) {
            binding.setHeaderproduct(item);

            Context context = binding.getRoot().getContext();

            binding.spinner.setOnItemSelectedListener(this);

            List<ProductAttributeDetail> detail = new ArrayList<>();

            int selectedIndex = 0;

            detail.add(new ProductAttributeDetail("-1",
                    item.id,
                    item.id,
                    item.productId,
                    context.getString(R.string.product_detail__choose_zg, item.name),
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO,
                    Constants.ZERO));

            String selectedAttr = "";

            if (this.basketItemHolderHashMap.containsKey(item.id)) {

                selectedAttr = basketItemHolderHashMap.get(item.id);

            }

            for (int i = 0; i < item.attributesDetailList.size(); i++) {

                if (item.attributesDetailList.get(i).additionalPrice.equals(Constants.ZERO)) {

                    item.attributesDetailList.get(i).additionalPriceWithCurrency = "";

                } else {

                    item.attributesDetailList.get(i).additionalPriceWithCurrency = "( + " + currencySymbol + Constants.SPACE_STRING + item.attributesDetailList.get(i).additionalPrice + " )";

                }

                Utils.psLog(selectedAttr + "\n");
                if (item.attributesDetailList.get(i).name.equals(selectedAttr)) {

                    selectedIndex = i + 1;

                }

                detail.add(item.attributesDetailList.get(i));

            }

            //Creating the ArrayAdapter instance having the list
            ProductHeaderDetailAdapter adapter = new ProductHeaderDetailAdapter(binding.getRoot().getContext(),
                    R.layout.spinner_header_detail, detail, dataBindingComponent);

            //Setting the ArrayAdapter data on the Spinner
            binding.spinner.setAdapter(adapter);

            binding.spinner.setSelection(selectedIndex);
        }

    }

    @Override
    protected boolean areItemsTheSame(ProductAttributeHeader oldItem, ProductAttributeHeader newItem) {

        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId);

    }

    @Override
    protected boolean areContentsTheSame(ProductAttributeHeader oldItem, ProductAttributeHeader newItem) {

        return Objects.equals(oldItem.id, newItem.id)
                && oldItem.productId.equals(newItem.productId);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        ProductAttributeDetail productAttributeDetail = (ProductAttributeDetail) adapterView.getItemAtPosition(i);

        callback.onClick(productAttributeDetail);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    public interface HeaderProductClickCallBack {

        void onClick(ProductAttributeDetail productAttributeDetail);

    }

}
