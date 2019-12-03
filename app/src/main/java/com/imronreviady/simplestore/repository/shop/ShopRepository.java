package com.imronreviady.simplestore.repository.shop;

import android.content.SharedPreferences;

import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.api.ApiResponse;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.db.ShopDao;
import com.imronreviady.simplestore.repository.common.NetworkBoundResource;
import com.imronreviady.simplestore.repository.common.PSRepository;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.Shop;
import com.imronreviady.simplestore.viewobject.common.Resource;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

public class ShopRepository extends PSRepository {

    private final ShopDao shopDao;
    //endregion


    //region Constructor

    @Inject
    protected SharedPreferences pref;

    @Inject
    ShopRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db, ShopDao shopDao) {
        super(apiService, appExecutors, db);

        Utils.psLog("Inside ShopRepository");

        this.shopDao = shopDao;
    }

    public LiveData<Resource<Shop>> getShop(String api_key) {
        return new NetworkBoundResource<Shop, Shop>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull Shop itemList) {
                Utils.psLog("SaveCallResult of recent products.");

                db.beginTransaction();

                try {

                    db.shopDao().insert(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable Shop data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<Shop> loadFromDb() {
                Utils.psLog("Load discount From Db");

                return shopDao.getShopById();

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Shop>> createCall() {
                Utils.psLog("Call API Service to get discount.");

                return apiService.getShopById(api_key);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getDiscount) : " + message);
            }

        }.asLiveData();
    }


}
