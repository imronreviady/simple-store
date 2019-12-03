package com.imronreviady.simplestore.viewmodel.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.repository.transaction.TransactionRepository;
import com.imronreviady.simplestore.utils.AbsentLiveData;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.common.PSViewModel;
import com.imronreviady.simplestore.viewobject.BasketProductListToServerContainer;
import com.imronreviady.simplestore.viewobject.TransactionHeaderUpload;
import com.imronreviady.simplestore.viewobject.TransactionObject;
import com.imronreviady.simplestore.viewobject.common.Resource;

import java.util.List;

import javax.inject.Inject;

public class TransactionListViewModel extends PSViewModel {

    private final LiveData<Resource<List<TransactionObject>>> transactionListData;
    private MutableLiveData<TransactionListViewModel.TmpDataHolder> transactionListObj = new MutableLiveData<>();

    private final LiveData<Resource<Boolean>> nextPageTransactionLoadingData;
    private MutableLiveData<TransactionListViewModel.TmpDataHolder> nextPageLoadingStateObj = new MutableLiveData<>();

    private final LiveData<Resource<TransactionObject>> transactionDetailData;
    private MutableLiveData<TransactionListViewModel.TmpDataHolder> transactionDetailObj = new MutableLiveData<>();

    private final LiveData<Resource<TransactionObject>> sendTransactionDetailData;
    private MutableLiveData<TransactionListViewModel.TmpDataHolder> sendTransactionDetailDataObj = new MutableLiveData<>();

    @Inject
    TransactionListViewModel(TransactionRepository transactionRepository) {
        // Transaction List
        transactionListData = Transformations.switchMap(transactionListObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("transactionListData");
            return transactionRepository.getTransactionList(Config.API_KEY, obj.userId,obj.loadFromDB, obj.offset);
        });


        nextPageTransactionLoadingData = Transformations.switchMap(nextPageLoadingStateObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("nextPageTransactionLoadingData");
            return transactionRepository.getNextPageTransactionList(obj.userId, obj.offset);
        });

        transactionDetailData = Transformations.switchMap(transactionDetailObj, obj -> {
            if (obj == null) {
                return AbsentLiveData.create();
            }
            Utils.psLog("transactionDetailData");
            return transactionRepository.getTransactionDetail(Config.API_KEY, obj.userId, obj.transactionId);
        });

        sendTransactionDetailData = Transformations.switchMap(sendTransactionDetailDataObj, obj -> {

            if (obj == null) {
                return AbsentLiveData.create();
            }
            return transactionRepository.uploadTransDetailToServer(obj.transactionHeaderUpload);
        });

    }
    // region Getter And Setter for Transaction List

    public void setSendTransactionDetailDataObj(TransactionHeaderUpload transactionHeaderUpload) {

        TransactionListViewModel.TmpDataHolder tmpDataHolder = new TransactionListViewModel.TmpDataHolder();
        tmpDataHolder.transactionHeaderUpload = transactionHeaderUpload;
        sendTransactionDetailDataObj.setValue(tmpDataHolder);

    }

    public LiveData<Resource<TransactionObject>> getSendTransactionDetailData() {
        return sendTransactionDetailData;
    }


    public void setTransactionListObj(String loadFromDB,String offset, String loginUserId) {
        if (!isLoading) {
            TransactionListViewModel.TmpDataHolder tmpDataHolder = new TransactionListViewModel.TmpDataHolder();
            tmpDataHolder.loadFromDB =loadFromDB;
            tmpDataHolder.offset = offset;
            tmpDataHolder.userId = loginUserId;
            transactionListObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<List<TransactionObject>>> getTransactionListData() {
        return transactionListData;
    }

    public LiveData<Resource<Boolean>> getNextPageLoadingStateData() {
        return nextPageTransactionLoadingData;
    }

    public void setNextPageLoadingStateObj(String offset, String loginUserId) {

        if (!isLoading) {
            TransactionListViewModel.TmpDataHolder tmpDataHolder = new TransactionListViewModel.TmpDataHolder();
            tmpDataHolder.offset = offset;
            tmpDataHolder.userId = loginUserId;
            nextPageLoadingStateObj.setValue(tmpDataHolder);
            // start loading
            setLoadingState(true);
        }
    }

    public void setTransactionDetailObj(String userId, String transactionId) {
        if (!isLoading) {
            TmpDataHolder tmpDataHolder = new TmpDataHolder();
            tmpDataHolder.userId = userId;
            tmpDataHolder.transactionId = transactionId;
            transactionDetailObj.setValue(tmpDataHolder);

            // start loading
            setLoadingState(true);
        }
    }

    public LiveData<Resource<TransactionObject>> getTransactionDetailData() {
        return transactionDetailData;
    }

    public String getToken() {
        return null;
    }

    class TmpDataHolder {
        public String offset = "";
        public String userId = "";
        public String transactionId = "";
        public Boolean isConnected = false;
        public String loadFromDB = "";
        public int subTotalAmount;
        public int discount_amount;
        public int balanceAmount;
        public String contactName;
        public String contactPhone;
        public String delivery_address;
        public String billing_address;
        public String paymentMethod;
        public String transStatusId;
        public String currency_symbol;
        public String currency_short_form;
        public BasketProductListToServerContainer basketProductListToServerContainer;
        public String shopId;
        public TransactionHeaderUpload transactionHeaderUpload;
    }
}
