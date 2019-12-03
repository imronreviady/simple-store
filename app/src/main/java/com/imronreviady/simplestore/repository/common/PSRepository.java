package com.imronreviady.simplestore.repository.common;

import androidx.lifecycle.LiveData;
import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.utils.Connectivity;
import com.imronreviady.simplestore.utils.RateLimiter;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.common.Resource;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Parent Class of All Repository Class in this project
 * Created by Panacea-Soft on 12/5/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

public abstract class PSRepository {


    //region Variables

    protected final ApiService apiService;
    protected final AppExecutors appExecutors;
    protected final PSCoreDb db;
    @Inject
    protected Connectivity connectivity;
    protected RateLimiter<String> rateLimiter = new RateLimiter<>( Config.API_SERVICE_CACHE_LIMIT, TimeUnit.MINUTES);

    //endregion


    //region Constructor

    /**
     * Constructor of PSRepository
     * @param apiService Panacea-Soft API Service Instance
     * @param appExecutors Executors Instance
     * @param db Panacea-Soft DB
     */
    protected PSRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db) {
        Utils.psLog("Inside NewsRepository");
        this.apiService = apiService;
        this.appExecutors = appExecutors;
        this.db = db;
    }

    //endregion


    //region public Methods

    public LiveData<Resource<Boolean>> save(Object obj) {

        if(obj == null) {
            return AbsentLiveData.create();
        }

        SaveTask saveTask = new SaveTask(apiService, db, obj);
        appExecutors.diskIO().execute(saveTask);
        return saveTask.getStatusLiveData();
    }


    public LiveData<Resource<Boolean>> delete(Object obj) {

        if(obj == null) {
            return AbsentLiveData.create();
        }

        DeleteTask deleteTask = new DeleteTask(apiService, db, obj);
        appExecutors.diskIO().execute(deleteTask);
        return deleteTask.getStatusLiveData();
    }
    //endregion

}
