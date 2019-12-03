package com.imronreviady.simplestore.repository.subcategory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.api.ApiResponse;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.db.SubCategoryDao;
import com.imronreviady.simplestore.repository.common.NetworkBoundResource;
import com.imronreviady.simplestore.repository.common.PSRepository;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.SubCategory;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SubCategoryRepository extends PSRepository {

    private final SubCategoryDao subCategoryDao;

    @Inject
    SubCategoryRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db, SubCategoryDao subCategoryDao) {
        super(apiService, appExecutors, db);

        Utils.psLog("Inside SubCategoryRepository");

        this.subCategoryDao = subCategoryDao;
    }

    public LiveData<Resource<List<SubCategory>>> getAllSubCategoryList(String apiKey) {
        return new NetworkBoundResource<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of getAllSubCategoryList.");

                try {
                    db.beginTransaction();

                    subCategoryDao.deleteAllSubCategory();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId, item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getAllSubCategoryList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getAllSubCategory();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return apiService.getAllSubCategoryList(apiKey);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }


    public LiveData<Resource<List<SubCategory>>> getSubCategoriesWithCatId(String loginUserId, String offset, String catId) {
        return new NetworkBoundResource<List<SubCategory>, List<SubCategory>>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull List<SubCategory> itemList) {

                Utils.psLog("SaveCallResult of Sub Category.");

                try {
                    db.beginTransaction();

                    subCategoryDao.insertAll(itemList);

                    for (SubCategory item : itemList) {
                        subCategoryDao.insert(new SubCategory(item.id, item.catId,item.name, item.status, item.ordering, item.addedDate, item.addedUserId, item.updatedDate, item.updatedUserId, item.updatedFlag, item.addedDateStr, item.defaultPhoto, item.defaultIcon));
                    }

                    db.setTransactionSuccessful();
                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of recent product list.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SubCategory> data) {
                return connectivity.isConnected();
            }

            @NonNull
            @Override
            protected LiveData<List<SubCategory>> loadFromDb() {
                return subCategoryDao.getSubCategoryList(catId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<SubCategory>>> createCall() {
                return apiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, "", offset);
            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed of " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageSubCategoriesWithCatId(String loginUserId, String limit, String offset, String catId) {
        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<SubCategory>>> apiResponse = apiService.getSubCategoryListWithCatId(Config.API_KEY, Utils.checkUserId(loginUserId), catId, limit, offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            for (SubCategory news : response.body) {
                                db.subCategoryDao().insert(new SubCategory(news.id, news.catId, news.name, news.status, news.ordering, news.addedDate, news.addedUserId, news.updatedDate, news.updatedUserId, news.updatedFlag, news.addedDateStr, news.defaultPhoto, news.defaultIcon));
                            }

                            db.subCategoryDao().insertAll(response.body);
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

            } else {
                statusLiveData.postValue(Resource.error(response.errorMessage, null));
            }

        });

        return statusLiveData;
    }

}
