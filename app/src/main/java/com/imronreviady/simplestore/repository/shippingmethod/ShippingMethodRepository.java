package com.imronreviady.simplestore.repository.shippingmethod;

import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.api.ApiResponse;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.db.ShippingMethodDao;
import com.imronreviady.simplestore.repository.common.NetworkBoundResource;
import com.imronreviady.simplestore.repository.common.PSRepository;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.ShippingMethod;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class ShippingMethodRepository extends PSRepository {

    private ShippingMethodDao shippingMethodDao;

    /**
     * Constructor of PSRepository
     *
     * @param apiService Panacea-Soft API Service Instance
     * @param appExecutors Executors Instance
     * @param db           Panacea-Soft DB
     */
    @Inject
    ShippingMethodRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db, ShippingMethodDao shippingMethodDao) {
        super(apiService, appExecutors, db);

        Utils.psLog("Inside ProductRepository");

        this.shippingMethodDao = shippingMethodDao;
    }

    public LiveData<Resource<List<ShippingMethod>>> getAllShippingMethods() {
        return new NetworkBoundResource<List<ShippingMethod>, List<ShippingMethod>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<ShippingMethod> itemList) {
                Utils.psLog("SaveCallResult of getAllShippingMethods.");

                db.beginTransaction();

                try {

                    db.shippingMethodDao().insertAll(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllShippingMethods.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ShippingMethod> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<ShippingMethod>> loadFromDb() {
                Utils.psLog("Load getAllShippingMethods From Db");

                return shippingMethodDao.getShippingMethods();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<ShippingMethod>>> createCall() {
                Utils.psLog("Call API Service to getAllShippingMethods.");

                return apiService.getShipping(Config.API_KEY);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getAllShippingMethods) : " + message);
            }

        }.asLiveData();

    }
}
