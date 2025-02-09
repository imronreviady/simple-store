package com.imronreviady.simplestore.viewmodel.comment;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.repository.comment.CommentDetailRepository;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.common.PSViewModel;
import com.imronreviady.simplestore.viewobject.CommentDetail;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public class CommentDetailListViewModel extends PSViewModel {
    //for comment detail list

    public String commentId = "";

    private final LiveData<Resource<List<CommentDetail>>> commentDetailListData;
    private MutableLiveData<CommentDetailListViewModel.TmpDataHolder> commentDetailListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageCommentDetailLoadingData;
    private MutableLiveData<CommentDetailListViewModel.TmpDataHolder> nextPageLoadingCommentDetailObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> sendCommentDetailPostData;
    private MutableLiveData<com.imronreviady.simplestore.viewmodel.comment.CommentDetailListViewModel.TmpDataHolder> sendCommentDetailPostDataObj = new MutableLiveData<>();
    //region Constructor

    @Inject
    CommentDetailListViewModel(CommentDetailRepository commentDetailRepository) {
        //  comment detail List
        commentDetailListData = Transformations.switchMap(commentDetailListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment detail List.");
            return commentDetailRepository.getCommentDetailList(Config.API_KEY, obj.offset, obj.commentId);
        });

        nextPageCommentDetailLoadingData = Transformations.switchMap(nextPageLoadingCommentDetailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("Comment detail List.");
            return commentDetailRepository.getNextPageCommentDetailList(obj.offset, obj.commentId);
        });

        sendCommentDetailPostData = Transformations.switchMap(sendCommentDetailPostDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return commentDetailRepository.uploadCommentDetailToServer(
                    obj.headerId,
                    obj.userId,
                    obj.detailComment);
        });
    }

    //endregion
    public void setSendCommentDetailPostDataObj(String headerId,
                                                String userId,
                                                String detailComment
    ) {
        if (!isLoading) {
            com.imronreviady.simplestore.viewmodel.comment.CommentDetailListViewModel.TmpDataHolder tmpDataHolder = new com.imronreviady.simplestore.viewmodel.comment.CommentDetailListViewModel.TmpDataHolder();
            tmpDataHolder.headerId = headerId;
            tmpDataHolder.userId = userId;
            tmpDataHolder.detailComment = detailComment;
            sendCommentDetailPostDataObj.setValue(tmpDataHolder);
        }
    }

    public LiveData<Resource<Boolean>> getsendCommentDetailPostData() {
        return sendCommentDetailPostData;
    }
    //region Getter And Setter for Comment detail List

    public void setCommentDetailListObj(String offset, String commentId) {
        if (!isLoading) {
            CommentDetailListViewModel.TmpDataHolder tmpDataHolder = new CommentDetailListViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.commentId = commentId;
            commentDetailListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<CommentDetail>>> getCommentDetailListData() {
        return commentDetailListData;
    }

    //Get Comment detail Next Page
    public void setNextPageLoadingCommentDetailObj(String offset, String commentId) {

        if (!isLoading) {
            CommentDetailListViewModel.TmpDataHolder tmpDataHolder = new CommentDetailListViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.commentId = commentId;
            nextPageLoadingCommentDetailObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<Boolean>> getNextPageCommentDetailLoadingData() {
        return nextPageCommentDetailLoadingData;
    }

    //endregion

    //region Holder
    class TmpDataHolder {
        String offset = "";
        String headerId = "";
        String userId = "";
        String commentId = "";
        String detailComment = "";
        String shopId = "";
    }
    //endregion
}
