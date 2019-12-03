package com.imronreviady.simplestore.viewmodel.comment;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.repository.comment.CommentRepository;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.common.PSViewModel;
import com.imronreviady.simplestore.viewobject.Comment;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class CommentListViewModel extends PSViewModel {

    //for recent comment list

    public final String PRODUCT_ID_KEY = "product_id";
    public String productId = "";

    private final LiveData<Resource<List<Comment>>> commentListData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageCommentLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> sendCommentHeaderPostData;
    private MutableLiveData<com.imronreviady.simplestore.viewmodel.comment.CommentListViewModel.TmpDataHolder> sendCommentHeaderPostDataObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> commentCountLoadingData;
    private MutableLiveData<CommentListViewModel.TmpDataHolder> commentCountLoadingStateObj = new MutableLiveData<>();
    //region Constructor

    @Inject
    public CommentListViewModel(CommentRepository commentRepository) {
        // Latest comment List
        commentListData = Transformations.switchMap(commentListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentList(Config.API_KEY, obj.productId, obj.limit, obj.offset);
        });

        nextPageCommentLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getNextPageCommentList(obj.productId, obj.limit, obj.offset);
        });

        sendCommentHeaderPostData = Transformations.switchMap(sendCommentHeaderPostDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return commentRepository.uploadCommentHeaderToServer(
                    obj.productId,
                    obj.userId,
                    obj.headerComment);
        });

        commentCountLoadingData = Transformations.switchMap(commentCountLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment List.");
            return commentRepository.getCommentDetailReplyCount(obj.comment_id);
        });
    }

    //endregion
    public void setSendCommentHeaderPostDataObj(String product_id,
                                                String userId,
                                                String headerComment
    ) {
        if (!isLoading) {
            com.imronreviady.simplestore.viewmodel.comment.CommentListViewModel.TmpDataHolder tmpDataHolder = new com.imronreviady.simplestore.viewmodel.comment.CommentListViewModel.TmpDataHolder();
            tmpDataHolder.productId = product_id;
            tmpDataHolder.userId = userId;
            tmpDataHolder.headerComment = headerComment;
            sendCommentHeaderPostDataObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getsendCommentHeaderPostData() {
        return sendCommentHeaderPostData;
    }


    //region Getter And Setter for Comment List

    public void setCommentListObj(String limit, String offset, String productId) {
        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.offset = offset;
            tmpDataHolder.productId = productId;
            commentListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<Comment>>> getCommentListData() {
        return commentListData;
    }

    //Get Comment Next Page
    public void setNextPageCommentLoadingObj(String product_id, String limit, String offset) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.limit = limit;
            tmpDataHolder.productId = product_id;
            tmpDataHolder.offset = offset;
            nextPageLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public void setCommentCountLoadingObj(String comment_id) {

        if (!isLoading) {
            CommentListViewModel.TmpDataHolder tmpDataHolder = new CommentListViewModel.TmpDataHolder();
            tmpDataHolder.comment_id = comment_id;
            commentCountLoadingStateObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getCommentCountLoadingStateData() {
        return commentCountLoadingData;
    }


    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageCommentLoadingData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        public String limit = "";
        public String offset = "";
        public String productId = "";
        public String userId = "";
        public String headerComment = "";
        public String comment_id = "";
        public Boolean isConnected = false;
        public String shopId;
    }
    //endregion
}
