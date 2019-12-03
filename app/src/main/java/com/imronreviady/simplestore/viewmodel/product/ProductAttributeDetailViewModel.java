package com.imronreviady.simplestore.viewmodel.product;

import com.imronreviady.simplestore.repository.product.ProductRepository;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.common.PSViewModel;
import com.imronreviady.simplestore.viewobject.ProductAttributeDetail;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ProductAttributeDetailViewModel extends PSViewModel {

    //for product attribute detail list

    private final LiveData<List<ProductAttributeDetail>> ProductAttributeDetailListData;
    private MutableLiveData<ProductAttributeDetailViewModel.TmpDataHolder> ProductAttributeDetailObj = new MutableLiveData<>();

    //endregion

    //region Constructor

    @Inject
    public ProductAttributeDetailViewModel(ProductRepository productRepository) {
        //  product attribute detail List
        ProductAttributeDetailListData = Transformations.switchMap(ProductAttributeDetailObj, (ProductAttributeDetailViewModel.TmpDataHolder obj) -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("product attribute detail List.");
            return productRepository.getProductAttributeDetail(obj.productId, obj.headerId);
        });

    }

    //endregion
    //region Getter And Setter for product attribute detail List

    public LiveData<List<ProductAttributeDetail>> getProductAttributeDetailListData() {
        return ProductAttributeDetailListData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String offset = "";
        public String productId = "";
        public String headerId = "";
        public Boolean isConnected = false;
    }
    //endregion
}
