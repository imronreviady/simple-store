package com.imronreviady.simplestore.ui.transaction.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentTransactionListBinding;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.ui.transaction.list.adapter.TransactionListAdapter;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.transaction.TransactionListViewModel;
import com.imronreviady.simplestore.viewobject.TransactionObject;
import com.imronreviady.simplestore.viewobject.common.Resource;
import com.imronreviady.simplestore.viewobject.common.Status;

import java.util.List;

public class TransactionListFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private TransactionListViewModel transactionListViewModel;
    public PSDialogMsg psDialogMsg;

    @VisibleForTesting
    private AutoClearedValue<FragmentTransactionListBinding> binding;
    private AutoClearedValue<TransactionListAdapter> adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTransactionListBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_transaction_list, container, false, dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());

        return binding.get().getRoot();

    }

    @Override
    public void onDispatched() {
        if (transactionListViewModel.loadingDirection == Utils.LoadingDirection.top) {

            if (binding.get().transactionListRecyclerView != null) {

                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        binding.get().transactionListRecyclerView.getLayoutManager();

                if (layoutManager != null) {
                    layoutManager.scrollToPosition(0);
                }
            }
        }
    }

    @Override
    protected void initUIAndActions() {

        psDialogMsg = new PSDialogMsg(getActivity(), false);


        binding.get().transactionListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                if (layoutManager != null) {

                    int lastPosition = layoutManager
                            .findLastVisibleItemPosition();
                    if (lastPosition == adapter.get().getItemCount() - 1) {

                        if (!binding.get().getLoadingMore() && !transactionListViewModel.forceEndLoading) {

                            if (connectivity.isConnected()) {

                                transactionListViewModel.loadingDirection = Utils.LoadingDirection.bottom;

                                int limit = Config.TRANSACTION_COUNT;
                                transactionListViewModel.offset = transactionListViewModel.offset + limit;

                                transactionListViewModel.setNextPageLoadingStateObj(String.valueOf(transactionListViewModel.offset), loginUserId);
                            }
                        }
                    }
                }
            }
        });

        binding.get().swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.view__primary_line));
        binding.get().swipeRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.global__primary));
        binding.get().swipeRefresh.setOnRefreshListener(() -> {

            transactionListViewModel.loadingDirection = Utils.LoadingDirection.top;

            // reset productViewModel.offset
            transactionListViewModel.offset = 0;

            // reset productViewModel.forceEndLoading
            transactionListViewModel.forceEndLoading = false;

            // update live data
            transactionListViewModel.setTransactionListObj("",String.valueOf(transactionListViewModel.offset), loginUserId);

        });

    }

    @Override
    protected void initViewModels() {
        transactionListViewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionListViewModel.class);
    }

    @Override
    protected void initAdapters() {

        TransactionListAdapter nvAdapter = new TransactionListAdapter(dataBindingComponent, new TransactionListAdapter.TransactionClickCallback() {
            @Override
            public void onClick(TransactionObject transaction) {
                navigationController.navigateToTransactionDetail(getActivity(), transaction);
            }

            @Override
            public void onCopyClick() {

                Toast.makeText(getActivity(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();

            }
        }, this);

        this.adapter = new AutoClearedValue<>(this, nvAdapter);
        binding.get().transactionListRecyclerView.setAdapter(nvAdapter);
    }

    @Override
    protected void initData() {
        LoadData();
    }

    private void LoadData() {
        // Load Latest Product
        transactionListViewModel.setTransactionListObj("",String.valueOf(transactionListViewModel.offset), loginUserId);

        LiveData<Resource<List<TransactionObject>>> news = transactionListViewModel.getTransactionListData();

        if (news != null) {
            news.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().getRoot());

                                // Update the data
                                replaceData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data
                                replaceData(listResource.data);
                            }

                            transactionListViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            transactionListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (transactionListViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        transactionListViewModel.forceEndLoading = true;
                    }

                }

            });
        }
        transactionListViewModel.getNextPageLoadingStateData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    transactionListViewModel.setLoadingState(false);//hide
                    transactionListViewModel.forceEndLoading = true;//stop
                }
            }
        });

        transactionListViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(transactionListViewModel.isLoading);

            if (loadingState != null && !loadingState) {
                binding.get().swipeRefresh.setRefreshing(false);
            }

        });
    }

    private void replaceData(List<TransactionObject> transactionList) {
        adapter.get().replace(transactionList);
        binding.get().executePendingBindings();
    }
}