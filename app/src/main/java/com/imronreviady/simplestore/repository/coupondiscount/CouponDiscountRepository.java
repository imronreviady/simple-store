package com.imronreviady.simplestore.repository.coupondiscount;

import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.api.ApiResponse;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.repository.common.PSRepository;
import com.imronreviady.simplestore.viewobject.CouponDiscount;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.io.IOException;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Response;

public class CouponDiscountRepository extends PSRepository {


    /**
     * Constructor of PSRepository
     *
     * @param apiService Panacea-Soft API Service Instance
     * @param appExecutors Executors Instance
     * @param db           Panacea-Soft DB
     */

    @Inject
    CouponDiscountRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db) {
        super(apiService, appExecutors, db);
    }

    public LiveData<Resource<CouponDiscount>> getCouponDiscount(String code)
    {
        final MutableLiveData<Resource<CouponDiscount>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            try {
                // Call the API Service
                Response<CouponDiscount> response;
                response = apiService.checkCouponDiscount(Config.API_KEY, code).execute();

                // Wrap with APIResponse Class
                ApiResponse<CouponDiscount> apiResponse = new ApiResponse<>(response);

                // If response is successful
                if (apiResponse.isSuccessful()) {

                    statusLiveData.postValue(Resource.success(apiResponse.body));

                } else {
                    statusLiveData.postValue(Resource.error(String.valueOf(apiResponse.errorMessage), null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }
        });

        return statusLiveData;
    }
}
