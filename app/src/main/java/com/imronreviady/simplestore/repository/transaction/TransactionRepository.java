package com.imronreviady.simplestore.repository.transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.api.ApiResponse;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.db.TransactionDao;
import com.imronreviady.simplestore.repository.common.NetworkBoundResource;
import com.imronreviady.simplestore.repository.common.PSRepository;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.TransactionHeaderUpload;
import com.imronreviady.simplestore.viewobject.TransactionObject;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;

@Singleton
public class TransactionRepository extends PSRepository {

    //region variable
    private final TransactionDao transactionDao;
    //end region

    //region constructor
    @Inject
    TransactionRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db, TransactionDao transactionDao) {
        super(apiService, appExecutors, db);
        this.transactionDao = transactionDao;
    }

    //Get transaction list
    public LiveData<Resource<List<TransactionObject>>> getTransactionList(String apiKey, String userId,String loadFromDB, String offset) {

        return new NetworkBoundResource<List<TransactionObject>, List<TransactionObject>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<TransactionObject> itemList) {
                Utils.psLog("SaveCallResult of recent transaction.");

                db.beginTransaction();

                try {

                    transactionDao.deleteAllTransactionList();

                    transactionDao.insertAllTransactionList(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of recent transaction list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<TransactionObject> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<TransactionObject>> loadFromDb() {
                Utils.psLog("Load Recent transaction From Db");
                if(loadFromDB.equals("")){
                    return transactionDao.getAllTransactionList();
                }else {
                    return transactionDao.getAllTransactionListByLimit(loadFromDB);
                }
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<TransactionObject>>> createCall() {
                return apiService.getTransList(apiKey, Utils.checkUserId(userId), String.valueOf(Config.TRANSACTION_COUNT), offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getRecentTransactionList) : " + message);

            }
        }.asLiveData();

    }

    // Get next page transaction list
    public LiveData<Resource<Boolean>> getNextPageTransactionList(String userId, String offset) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<TransactionObject>>> apiResponse = apiService.getTransList(Config.API_KEY, Utils.checkUserId(userId), String.valueOf(Config.TRANSACTION_COUNT), offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            db.transactionDao().insertAllTransactionList(response.body);
                        }

                        db.setTransactionSuccessful();
                    } catch (NullPointerException ne) {
                        Utils.psErrorLog("Null Pointer Exception : ", ne);
                    } catch (Exception e) {
                        Utils.psErrorLog("Exception : ", e);
                    } finally {
                        db.endTransaction();
                    }

                    statusLiveData.postValue(Resource.success(true));

                });
            }else {
                    statusLiveData.postValue(Resource.error(response.errorMessage, false));
                }

        });

        return statusLiveData;

    }

    public LiveData<Resource<TransactionObject>> uploadTransDetailToServer(TransactionHeaderUpload transactionHeaderUpload) {

        final MutableLiveData<Resource<TransactionObject>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<TransactionObject> response;

            try {
                response = apiService.uploadTransactionHeader(transactionHeaderUpload, Config.API_KEY).execute();

                ApiResponse<TransactionObject> apiResponse = new ApiResponse<>(response);

                if (apiResponse.isSuccessful()) {
                    statusLiveData.postValue(Resource.success(apiResponse.body));
                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, null));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), null));
            }

        });

        return statusLiveData;

    }


    //Get transaction detail
    public LiveData<Resource<TransactionObject>> getTransactionDetail(String apiKey, String userId, String id) {

        return new NetworkBoundResource<TransactionObject, TransactionObject>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull TransactionObject itemList) {
                Utils.psLog("SaveCallResult of recent transaction.");

                db.beginTransaction();

                try {

                    transactionDao.deleteTransactionById(id);

                    transactionDao.insert(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of discount list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable TransactionObject data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<TransactionObject> loadFromDb() {
                Utils.psLog("Load discount From Db");

                return transactionDao.getTransactionById(id);

            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<TransactionObject>> createCall() {
                Utils.psLog("Call API Service to get discount.");

                return apiService.getTransactionDetail(apiKey, userId, id);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getDiscount) : " + message);
            }

        }.asLiveData();
    }
}
