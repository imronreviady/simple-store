package com.imronreviady.simplestore.viewmodel.shippingmethod;

import com.imronreviady.simplestore.repository.shippingmethod.ShippingMethodRepository;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.viewmodel.common.PSViewModel;
import com.imronreviady.simplestore.viewobject.ShippingMethod;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class ShippingMethodViewModel extends PSViewModel {

    private final LiveData<Resource<List<ShippingMethod>>> shippingMethodsData;
    private MutableLiveData<Boolean> shippingMethodsObj = new MutableLiveData<>();

    public String shippingSelectedId;
    public List<ShippingMethod> proceededShippingListData = new ArrayList<>();

    @Inject
    ShippingMethodViewModel(ShippingMethodRepository repository) {

        shippingMethodsData = Transformations.switchMap(shippingMethodsObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }

            return repository.getAllShippingMethods();

        });

    }

    public void setShippingMethodsObj() {

        this.shippingMethodsObj.setValue(true);
    }

    public LiveData<Resource<List<ShippingMethod>>> getShippingMethodsData() {
        return shippingMethodsData;
    }

}

