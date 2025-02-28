package com.imronreviady.simplestore.repository.comment;

import com.imronreviady.simplestore.AppExecutors;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.api.ApiResponse;
import com.imronreviady.simplestore.api.ApiService;
import com.imronreviady.simplestore.db.CommentDetailDao;
import com.imronreviady.simplestore.db.PSCoreDb;
import com.imronreviady.simplestore.repository.common.NetworkBoundResource;
import com.imronreviady.simplestore.repository.common.PSRepository;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewobject.CommentDetail;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Response;

@Singleton
public class CommentDetailRepository extends PSRepository {

    //region Variables

    private final CommentDetailDao commentDetailDao;

    //endregion

    //region Constructor
    @Inject
    CommentDetailRepository(ApiService apiService, AppExecutors appExecutors, PSCoreDb db, CommentDetailDao commentDetailDao) {
        super(apiService, appExecutors, db);
        this.commentDetailDao = commentDetailDao;
    }
    //endregion

    //Get comment detail list
    public LiveData<Resource<List<CommentDetail>>> getCommentDetailList(String apiKey, String offset, String commentid) {

        return new NetworkBoundResource<List<CommentDetail>, List<CommentDetail>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<CommentDetail> itemList) {
                Utils.psLog("SaveCallResult of getCommentDetailList.");

                try {

                    db.beginTransaction();

                    commentDetailDao.deleteCommentDetailListByHeaderId(commentid);

                    commentDetailDao.insertAllCommentDetailList(itemList);

                    db.setTransactionSuccessful();

                } catch (Exception e) {
                    Utils.psErrorLog("Error in doing transaction of getCommentDetailList.", e);
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<CommentDetail> data) {

                // Recent news always load from server
                return connectivity.isConnected();

            }

            @NonNull
            @Override
            protected LiveData<List<CommentDetail>> loadFromDb() {
                Utils.psLog("Load getCommentDetailList Comment From Db");
                return commentDetailDao.getAllCommentDetailList(commentid);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<CommentDetail>>> createCall() {
                return apiService.getCommentDetailList(apiKey, commentid, String.valueOf(Config.COMMENT_COUNT), offset);

            }

            @Override
            protected void onFetchFailed(String message) {
                Utils.psLog("Fetch Failed (getCommentDetailList) : " + message);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPageCommentDetailList(String offset, String commentid) {

        final MediatorLiveData<Resource<Boolean>> statusLiveData = new MediatorLiveData<>();
        LiveData<ApiResponse<List<CommentDetail>>> apiResponse = apiService.getCommentDetailList(Config.API_KEY, commentid, String.valueOf(Config.COMMENT_COUNT), offset);

        statusLiveData.addSource(apiResponse, response -> {

            statusLiveData.removeSource(apiResponse);

            //noinspection ConstantConditions
            if (response.isSuccessful()) {

                appExecutors.diskIO().execute(() -> {


                    try {
                        db.beginTransaction();

                        if (response.body != null) {
                            db.commentDetailDao().insertAllCommentDetailList(response.body);
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
                statusLiveData.postValue(Resource.error(response.errorMessage, false));
            }
        });

        return statusLiveData;

    }


    public LiveData<Resource<Boolean>> uploadCommentDetailToServer(String headerId,
                                                                   String userId,
                                                                   String detailComment
    ) {
        final MutableLiveData<Resource<Boolean>> statusLiveData = new MutableLiveData<>();

        appExecutors.networkIO().execute(() -> {

            Response<List<CommentDetail>> response;

            try {
                response = apiService.rawCommentDetailPost(
                        Config.API_KEY,
                        headerId,
                        userId,
                        detailComment).execute();

                ApiResponse<List<CommentDetail>> apiResponse = new ApiResponse<>(response);

                if (response.isSuccessful()) {

                    try {
                        db.beginTransaction();

                        if (apiResponse.body != null) {
                            db.commentDetailDao().insertAllCommentDetailList(apiResponse.body);
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
                } else {
                    statusLiveData.postValue(Resource.error(apiResponse.errorMessage, false));
                }

            } catch (IOException e) {
                statusLiveData.postValue(Resource.error(e.getMessage(), false));
            }

        });

        return statusLiveData;
    }

}
