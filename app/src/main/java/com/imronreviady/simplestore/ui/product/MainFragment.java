package com.imronreviady.simplestore.ui.product;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdRequest;
import com.like.LikeButton;
import com.imronreviady.simplestore.Config;
import com.imronreviady.simplestore.R;
import com.imronreviady.simplestore.binding.FragmentDataBindingComponent;
import com.imronreviady.simplestore.databinding.FragmentMainBinding;
import com.imronreviady.simplestore.ui.category.adapter.CategoryIconListAdapter;
import com.imronreviady.simplestore.ui.category.adapter.TrendingCategoryAdapter;
import com.imronreviady.simplestore.ui.common.DataBoundListAdapter;
import com.imronreviady.simplestore.ui.common.PSFragment;
import com.imronreviady.simplestore.ui.product.adapter.DiscountListAdapter;
import com.imronreviady.simplestore.ui.product.adapter.ProductCollectionRowAdapter;
import com.imronreviady.simplestore.ui.product.adapter.ProductHorizontalListAdapter;
import com.imronreviady.simplestore.ui.product.adapter.ViewPagerAdapter;
import com.imronreviady.simplestore.utils.AutoClearedValue;
import com.imronreviady.simplestore.utils.Constants;
import com.imronreviady.simplestore.utils.PSDialogMsg;
import com.imronreviady.simplestore.utils.Utils;
import com.imronreviady.simplestore.viewmodel.category.CategoryViewModel;
import com.imronreviady.simplestore.viewmodel.clearalldata.ClearAllDataViewModel;
import com.imronreviady.simplestore.viewmodel.collection.ProductCollectionViewModel;
import com.imronreviady.simplestore.viewmodel.homelist.HomeFeaturedProductViewModel;
import com.imronreviady.simplestore.viewmodel.homelist.HomeLatestProductViewModel;
import com.imronreviady.simplestore.viewmodel.homelist.HomeSearchProductViewModel;
import com.imronreviady.simplestore.viewmodel.homelist.HomeTrendingCategoryListViewModel;
import com.imronreviady.simplestore.viewmodel.homelist.HomeTrendingProductViewModel;
import com.imronreviady.simplestore.viewmodel.product.BasketViewModel;
import com.imronreviady.simplestore.viewmodel.product.ProductFavouriteViewModel;
import com.imronreviady.simplestore.viewmodel.product.TouchCountViewModel;
import com.imronreviady.simplestore.viewmodel.apploading.AppLoadingViewModel;
import com.imronreviady.simplestore.viewmodel.shop.ShopViewModel;
import com.imronreviady.simplestore.viewobject.Category;
import com.imronreviady.simplestore.viewobject.Product;
import com.imronreviady.simplestore.viewobject.ProductCollectionHeader;
import com.imronreviady.simplestore.viewobject.common.Resource;
import com.imronreviady.simplestore.viewobject.common.Status;
import com.imronreviady.simplestore.viewobject.holder.ProductParameterHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class MainFragment extends PSFragment implements DataBoundListAdapter.DiffUtilDispatchedInterface {

    private final androidx.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    private HomeLatestProductViewModel homeLatestProductViewModel;
    private BasketViewModel basketViewModel;
    private HomeSearchProductViewModel homeSearchProductViewModel;
    private HomeTrendingProductViewModel homeTrendingProductViewModel;
    private HomeTrendingCategoryListViewModel homeTrendingCategoryListViewModel;
    private ProductCollectionViewModel productCollectionViewModel;
    private HomeFeaturedProductViewModel homeFeaturedProductViewModel;
    private TouchCountViewModel touchCountViewModel;
    private CategoryViewModel categoryViewModel;
    private ProductFavouriteViewModel favouriteViewModel;
    private ShopViewModel shopViewModel;
    private AppLoadingViewModel psAppInfoViewModel;
    private ClearAllDataViewModel clearAllDataViewModel;
    private int dotsCount = 0;
    private ImageView[] dots;
    private boolean layoutDone = false;
    private int loadingCount = 0;
    private GridLayoutManager gridLayoutManager, categoryGridLayoutManager;
    private MenuItem basketMenuItem;
    private PSDialogMsg psDialogMsg;
    private String startDate = Constants.ZERO;
    private String endDate = Constants.ZERO;

    @Inject
    protected SharedPreferences pref;

    @VisibleForTesting
    private AutoClearedValue<FragmentMainBinding> binding;
    private AutoClearedValue<ViewPagerAdapter> viewPagerAdapter;
    private AutoClearedValue<CategoryIconListAdapter> categoryIconListAdapter;
    private AutoClearedValue<DiscountListAdapter> discountListAdapter;
    private AutoClearedValue<ProductHorizontalListAdapter> trendingAdapter, latestAdapter;
    private AutoClearedValue<TrendingCategoryAdapter> categoryAdapter;
    private AutoClearedValue<ProductCollectionRowAdapter> verticalRowAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        FragmentMainBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false, dataBindingComponent);

        binding = new AutoClearedValue<>(this, dataBinding);

        binding.get().setLoadingMore(connectivity.isConnected());
        setHasOptionsMenu(true);

        return binding.get().getRoot();
    }

    private void setBasketMenuItemVisible(Boolean isVisible) {
        if (basketMenuItem != null) {
            basketMenuItem.setVisible(isVisible);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.basket_menu, menu);
        inflater.inflate(R.menu.blog_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        basketMenuItem = menu.findItem(R.id.action_basket);
        MenuItem blogMenuItem = menu.findItem(R.id.action_blog);
        blogMenuItem.setVisible(true);

        if (basketViewModel != null) {
            if (basketViewModel.basketCount > 0) {
                basketMenuItem.setVisible(true);
            } else {
                basketMenuItem.setVisible(false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_basket) {
            navigationController.navigateToBasketList(getActivity());
        }
        if (item.getItemId() == R.id.action_blog) {
            navigationController.navigateToBlogList(getActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void initUIAndActions() {

        if (Config.SHOW_ADMOB && connectivity.isConnected()) {
            AdRequest adRequest = new AdRequest.Builder()
                    .build();
            binding.get().adView.loadAd(adRequest);

            AdRequest adRequest2 = new AdRequest.Builder()
                    .build();
            binding.get().adView2.loadAd(adRequest2);

        } else {
            binding.get().adView.setVisibility(View.GONE);
            binding.get().adView2.setVisibility(View.GONE);
        }

        psDialogMsg = new PSDialogMsg(getActivity(), false);

        binding.get().viewAllSliderTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(MainFragment.this.getActivity(), homeFeaturedProductViewModel.productParameterHolder, getString(R.string.menu__featured_product)));

        binding.get().viewAllDiscountTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(MainFragment.this.getActivity(), homeSearchProductViewModel.holder.getDiscountParameterHolder(), getString(R.string.menu__discount)));

        binding.get().viewAllTrendingTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(MainFragment.this.getActivity(), homeTrendingProductViewModel.productParameterHolder, getString(R.string.menu__trending_products)));

        binding.get().viewAllTrendingCategoriesTextView.setOnClickListener(view -> navigationController.navigateToCategoryActivity(MainFragment.this.getActivity(), Constants.CATEGORY_TRENDING));

        binding.get().viewALlLatestTextView.setOnClickListener(view -> navigationController.navigateToHomeFilteringActivity(MainFragment.this.getActivity(), homeLatestProductViewModel.productParameterHolder, getString(R.string.menu__latest_product)));

        binding.get().categoryViewAllTextView.setOnClickListener(v -> navigationController.navigateToCategoryActivity(MainFragment.this.getActivity(), Constants.CATEGORY));

        binding.get().viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setupSliderPagination();
                if (dotsCount != 0) {

                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
                    }

                    dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initViewModels() {

        homeLatestProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeLatestProductViewModel.class);
        homeSearchProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeSearchProductViewModel.class);
        homeTrendingProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingProductViewModel.class);
        homeTrendingCategoryListViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeTrendingCategoryListViewModel.class);
        categoryViewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel.class);
        productCollectionViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductCollectionViewModel.class);
        homeFeaturedProductViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeFeaturedProductViewModel.class);
        touchCountViewModel = ViewModelProviders.of(this, viewModelFactory).get(TouchCountViewModel.class);
        favouriteViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductFavouriteViewModel.class);
        basketViewModel = ViewModelProviders.of(this, viewModelFactory).get(BasketViewModel.class);
        shopViewModel = ViewModelProviders.of(this, viewModelFactory).get(ShopViewModel.class);
        psAppInfoViewModel = ViewModelProviders.of(this, viewModelFactory).get(AppLoadingViewModel.class);
        clearAllDataViewModel = ViewModelProviders.of(this, viewModelFactory).get(ClearAllDataViewModel.class);

    }

    @Override
    protected void initAdapters() {

        /*LatestList*/

        ProductHorizontalListAdapter latestAdapter1 = new ProductHorizontalListAdapter(dataBindingComponent, new ProductHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(MainFragment.this.getActivity(), product);
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        }, this);


        this.latestAdapter = new AutoClearedValue<>(this, latestAdapter1);
        binding.get().productList.setAdapter(latestAdapter1);

        /*LatestList*/

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*discountList*/

        DiscountListAdapter discountListAdapter1 = new DiscountListAdapter(dataBindingComponent, new DiscountListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(MainFragment.this.getActivity(), product);
            }

            @Override
            public void onViewAllClick() {
                navigationController.navigateToHomeFilteringActivity(getActivity(), new ProductParameterHolder().getDiscountParameterHolder(), getString(R.string.menu__discount));
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        });

        this.discountListAdapter = new AutoClearedValue<>(this, discountListAdapter1);
        binding.get().discountList.setAdapter(discountListAdapter1);

        /*discountList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*featuredList*/

        ViewPagerAdapter viewPagerAdapter1 = new ViewPagerAdapter(dataBindingComponent, product -> navigationController.navigateToDetailActivity(MainFragment.this.getActivity(), product));

        this.viewPagerAdapter = new AutoClearedValue<>(this, viewPagerAdapter1);
        binding.get().viewPager.setAdapter(viewPagerAdapter1);
        binding.get().viewPagerCountDots.setVisibility(View.VISIBLE);


        /*featuredList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*CategoryIconList*/

        CategoryIconListAdapter categoryIconListAdapter1 = new CategoryIconListAdapter(dataBindingComponent, category -> {
            categoryViewModel.productParameterHolder.catId = category.id;
            navigationController.navigateToHomeFilteringActivity(MainFragment.this.getActivity(), categoryViewModel.productParameterHolder, category.name);
        }, this);

//                new CategoryIconListAdapter().CategoryClickCallback() {
//            @Override
//            public void onClick(Category category) {
//
//                categoryViewModel.productParameterHolder.catId = category.id;
//                navigationController.navigateToHomeFilteringActivity(SelectedShopFragment.this.getActivity(), categoryViewModel.productParameterHolder, category.name);
//            }
//
//        });

        this.categoryIconListAdapter = new AutoClearedValue<>(this, categoryIconListAdapter1);
        binding.get().categoryIconList.setAdapter(categoryIconListAdapter1);
//        binding.get().categoryIconList.setNestedScrollingEnabled(false);

        /*CategoryIconList*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* Latest Product*/

        ProductHorizontalListAdapter trendingAdapter1 = new ProductHorizontalListAdapter(dataBindingComponent, new ProductHorizontalListAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(MainFragment.this.getActivity(), product);
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        }, this);

        this.trendingAdapter = new AutoClearedValue<>(this, trendingAdapter1);
        binding.get().trendingList.setAdapter(trendingAdapter1);

        /* Latest Product*/
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        TrendingCategoryAdapter trendingCategoryAdapter = new TrendingCategoryAdapter(dataBindingComponent,
                category -> {
                    categoryViewModel.productParameterHolder.catId = category.id;
                    navigationController.navigateToHomeFilteringActivity(this.getActivity(), categoryViewModel.productParameterHolder, category.name);
                }, this);

        this.categoryAdapter = new AutoClearedValue<>(this, trendingCategoryAdapter);
        binding.get().trendingCategoryList.setAdapter(trendingCategoryAdapter);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        ProductCollectionRowAdapter verticalRowAdapter1 = new ProductCollectionRowAdapter(dataBindingComponent, new ProductCollectionRowAdapter.NewsClickCallback() {
            @Override
            public void onClick(Product product) {
                navigationController.navigateToDetailActivity(MainFragment.this.getActivity(), product);
            }

            @Override
            public void onViewAllClick(ProductCollectionHeader productCollectionHeader) {
                navigationController.navigateToCollectionProductList(MainFragment.this.getActivity(), productCollectionHeader.id, productCollectionHeader.name, productCollectionHeader.defaultPhoto.imgPath);
            }

            @Override
            public void onFavLikeClick(Product product, LikeButton likeButton) {
                favFunction(product, likeButton);
            }

            @Override
            public void onFavUnlikeClick(Product product, LikeButton likeButton) {
                unFavFunction(product, likeButton);
            }
        });

        this.verticalRowAdapter = new AutoClearedValue<>(this, verticalRowAdapter1);
        binding.get().collections.setAdapter(verticalRowAdapter1);
        binding.get().collections.setNestedScrollingEnabled(false);
    }

    private void replaceLatestData(List<Product> productList) {
        latestAdapter.get().replace(productList);
        binding.get().executePendingBindings();
    }

    private void replaceCategoryIconList(List<Category> categoryList) {
        categoryIconListAdapter.get().replace(categoryList);
        binding.get().executePendingBindings();
    }

    private void replaceFeaturedData(List<Product> featuredProductList) {
        viewPagerAdapter.get().replaceFeaturedList(featuredProductList);
        setupSliderPagination();

        binding.get().executePendingBindings();
    }

    private void replaceDiscountList(List<Product> productList) {

        discountListAdapter.get().replaceDiscount(productList);
        discountListAdapter.get().notifyDataSetChanged();
        binding.get().executePendingBindings();
    }

    private void replaceTrendingData(List<Product> productList) {
        trendingAdapter.get().replace(productList);
        binding.get().executePendingBindings();
    }

    private void replaceTrendingCategoryData(List<Category> categoryList) {
        categoryAdapter.get().replace(categoryList);
        binding.get().executePendingBindings();
    }

    private void replaceCollection(List<ProductCollectionHeader> productCollectionHeaders) {
        verticalRowAdapter.get().replaceCollectionHeader(productCollectionHeaders);
        binding.get().executePendingBindings();
    }


    @Override
    protected void initData() {

        setShopTouchCount();

        basketData();

        loadProducts();

        deleteHistoryData();
    }

    private void deleteHistoryData() {
        if (connectivity.isConnected()) {
            if (startDate.equals(Constants.ZERO)) {

                startDate = getDateTime();
                Utils.setDatesToShared(startDate, endDate, pref);
            }

        } else {

            if (!Config.APP_VERSION.equals(versionNo) && !force_update) {
                psDialogMsg.showInfoDialog(getString(R.string.force_update_true), getString(R.string.app__ok));
                psDialogMsg.show();
            }
        }

        clearAllDataViewModel.getDeleteAllDataData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {

                    case ERROR:
                        break;

                    case SUCCESS:
                        break;
                }
            }
        });
    }

    private void basketData() {
        //set and get basket list
        basketViewModel.setBasketListObj();
        basketViewModel.getAllBasketList().observe(this, resourse -> {
            if (resourse != null) {
                basketViewModel.basketCount = resourse.size();
                if (resourse.size() > 0) {
                    setBasketMenuItemVisible(true);
                } else {
                    setBasketMenuItemVisible(false);
                }
            }
        });
    }

    private void loadProducts() {

        //get favourite post method
        favouriteViewModel.getFavouritePostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                        favouriteViewModel.setLoadingState(false);
                    }
                }
            }
        });

        shopViewModel.setShopObj(Config.API_KEY);
        shopViewModel.getShopData().observe(this, result -> {

            if (result != null) {
                switch (result.status) {
                    case SUCCESS:

                        if (result.data != null) {
                            pref.edit().putString(Constants.SHIPPING_ID, result.data.shippingId).apply();
                            pref.edit().putString(Constants.PAYMENT_SHIPPING_TAX, String.valueOf(result.data.shippingTaxValue)).apply();
                            pref.edit().putString(Constants.PAYMENT_OVER_ALL_TAX, String.valueOf(result.data.overallTaxValue)).apply();
                            pref.edit().putString(Constants.PAYMENT_CASH_ON_DELIVERY, String.valueOf(result.data.codEnabled)).apply();
                            pref.edit().putString(Constants.PAYMENT_PAYPAL, String.valueOf(result.data.paypalEnabled)).apply();
                            pref.edit().putString(Constants.PAYMENT_STRIPE, String.valueOf(result.data.stripeEnabled)).apply();
                            pref.edit().putString(Constants.MESSENGER, String.valueOf(result.data.messenger)).apply();
                            pref.edit().putString(Constants.WHATSAPP, String.valueOf(result.data.whapsappNo)).apply();

                        }

                        break;

                    case ERROR:
                        break;
                }
            }
        });

        // Load Latest Product List

        homeLatestProductViewModel.setGetProductListByKeyObj(homeLatestProductViewModel.productParameterHolder, loginUserId, String.valueOf(Config.LOAD_FROM_DB), String.valueOf(homeLatestProductViewModel.offset));

        LiveData<Resource<List<Product>>> latest = homeLatestProductViewModel.getGetProductListByKeyData();

        if (latest != null) {

            latest.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {

                        case LOADING:
                            // Loading State
                            // Data are from Local DB
                            if (listResource.data != null) {

                                binding.get().latestTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewALlLatestTextView.setVisibility(View.VISIBLE);

                                // Update the data
                                replaceLatestData(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {

                                binding.get().latestTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewALlLatestTextView.setVisibility(View.VISIBLE);

                                // Update the data
                                replaceLatestData(listResource.data);
                            }

                            homeLatestProductViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            homeLatestProductViewModel.setLoadingState(false);
                            homeLatestProductViewModel.forceEndLoading = true;

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (homeLatestProductViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        homeLatestProductViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        homeLatestProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeLatestProductViewModel.setLoadingState(false);
                    homeLatestProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeLatestProductViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(homeLatestProductViewModel.isLoading);
            Utils.psLog("getLoadingState : start");
            if (loadingState != null && !loadingState) {
                Utils.psLog("getLoadingState end");
            }

        });


        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        homeFeaturedProductViewModel.setGetProductListByKeyObj(homeFeaturedProductViewModel.productParameterHolder, loginUserId, String.valueOf(Config.LOAD_FROM_DB), String.valueOf(homeFeaturedProductViewModel.offset));

        LiveData<Resource<List<Product>>> featured = homeFeaturedProductViewModel.getGetProductListByKeyData();

        if (featured != null) {

            featured.observe(this, listResource -> {

                if (listResource != null) {
                    switch (listResource.status) {
                        case LOADING:

                            if (listResource.data != null) {
                                MainFragment.this.fadeIn(binding.get().viewPager);
                                binding.get().sliderHeaderTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllSliderTextView.setVisibility(View.VISIBLE);

                                MainFragment.this.replaceFeaturedData(listResource.data);
                            }
                            break;

                        case SUCCESS:
                            if (listResource.data != null) {
                                MainFragment.this.fadeIn(binding.get().viewPager);
                                binding.get().sliderHeaderTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllSliderTextView.setVisibility(View.VISIBLE);

                                MainFragment.this.replaceFeaturedData(listResource.data);

                                homeFeaturedProductViewModel.setLoadingState(false);
                            }
                            break;

                        case ERROR:

                            homeFeaturedProductViewModel.setLoadingState(false);
                            homeFeaturedProductViewModel.forceEndLoading = true;

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        homeFeaturedProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeFeaturedProductViewModel.setLoadingState(false);
                    homeFeaturedProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeFeaturedProductViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(homeFeaturedProductViewModel.isLoading));

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /* CategoryRecyclerView*/

        categoryViewModel.setCategoryListObj(loginUserId, categoryViewModel.categoryParameterHolder, String.valueOf(Config.LOAD_FROM_DB), String.valueOf(homeSearchProductViewModel.offset));

        LiveData<Resource<List<Category>>> categories = categoryViewModel.getCategoryListData();

        if (categories != null) {
            categories.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().categoryIconList);

//                                // Update the data

                                if (listResource.data.size() > 0) {
                                    if (listResource.data.size() < 9) {
                                        categoryGridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        categoryGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().categoryIconList.setLayoutManager(categoryGridLayoutManager);
                                    } else {
                                        categoryGridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        categoryGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().categoryIconList.setLayoutManager(categoryGridLayoutManager);
                                    }

                                    replaceCategoryIconList(listResource.data);
                                    binding.get().categoryTextView.setVisibility(View.VISIBLE);
                                    binding.get().categoryViewAllTextView.setVisibility(View.VISIBLE);
                                }
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                //fadeIn Animation
                                fadeIn(binding.get().categoryIconList);

//                                // Update the data

                                if (listResource.data.size() > 0) {
                                    if (listResource.data.size() < 9) {
                                        categoryGridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        categoryGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().categoryIconList.setLayoutManager(categoryGridLayoutManager);
                                    } else {
                                        categoryGridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        categoryGridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().categoryIconList.setLayoutManager(categoryGridLayoutManager);
                                    }

                                    replaceCategoryIconList(listResource.data);
                                    binding.get().categoryTextView.setVisibility(View.VISIBLE);
                                    binding.get().categoryViewAllTextView.setVisibility(View.VISIBLE);
                                }
                            }

                            break;

                        case ERROR:
                            // Error State

                            categoryViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (categoryViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        categoryViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        categoryViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(categoryViewModel.isLoading));


        /* CategoryRecyclerView*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*DiscountList*/


        homeSearchProductViewModel.setGetProductListByKeyObj(homeSearchProductViewModel.holder, loginUserId, String.valueOf(Config.LOAD_FROM_DB), String.valueOf(homeSearchProductViewModel.offset));
        LiveData<Resource<List<Product>>> discount = homeSearchProductViewModel.getGetProductListByKeyData();

        if (discount != null) {

            discount.observe(this, listResource -> {

                if (listResource != null) {
                    switch (listResource.status) {

                        case LOADING:

                            if (listResource.data != null) {
                                binding.get().discountTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllDiscountTextView.setVisibility(View.VISIBLE);
                                MainFragment.this.replaceDiscountList(listResource.data);
                            }

                            break;

                        case SUCCESS:

                            if (listResource.data != null) {
                                binding.get().discountTitleTextView.setVisibility(View.VISIBLE);
                                binding.get().viewAllDiscountTextView.setVisibility(View.VISIBLE);
                                MainFragment.this.replaceDiscountList(listResource.data);

                                homeSearchProductViewModel.setLoadingState(false);
                            }

                            break;

                        case ERROR:

                            homeSearchProductViewModel.setLoadingState(false);
                            homeSearchProductViewModel.forceEndLoading = true;

                            break;

                        default:
                            break;
                    }

                }
            });
        }

        homeSearchProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeSearchProductViewModel.setLoadingState(false);
                    homeSearchProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeSearchProductViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(homeSearchProductViewModel.isLoading));

        /*DiscountList*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*trendingList*/

        homeTrendingProductViewModel.setGetProductListByKeyObj(homeTrendingProductViewModel.productParameterHolder, loginUserId, String.valueOf(Config.LOAD_FROM_DB), String.valueOf(homeSearchProductViewModel.offset));

        LiveData<Resource<List<Product>>> trending = homeTrendingProductViewModel.getGetProductListByKeyData();

        if (trending != null) {

            trending.observe(this, listResource -> {

                if (listResource != null) {
                    switch (listResource.status) {

                        case LOADING:

                            binding.get().trendingTitleTextView.setVisibility(View.VISIBLE);
                            binding.get().viewAllTrendingTextView.setVisibility(View.VISIBLE);

                            replaceTrendingData(listResource.data);

                            break;

                        case SUCCESS:

                            binding.get().trendingTitleTextView.setVisibility(View.VISIBLE);
                            binding.get().viewAllTrendingTextView.setVisibility(View.VISIBLE);

                            replaceTrendingData(listResource.data);

                            homeTrendingProductViewModel.setLoadingState(false);

                            break;

                        case ERROR:

                            homeTrendingProductViewModel.setLoadingState(false);
                            homeTrendingProductViewModel.forceEndLoading = true;

                            break;

                        default:
                            break;
                    }
                }
            });
        }

        homeTrendingProductViewModel.getGetNextPageProductListByKeyData().observe(this, state -> {
            if (state != null) {
                if (state.status == Status.ERROR) {
                    Utils.psLog("Next Page State : " + state.data);

                    homeTrendingProductViewModel.setLoadingState(false);
                    homeTrendingProductViewModel.forceEndLoading = true;
                }
            }
        });

        homeTrendingProductViewModel.getLoadingState().observe(this, loadingState -> {

            binding.get().setLoadingMore(homeTrendingProductViewModel.isLoading);

//                if (loadingState != null && !loadingState) {
////                    binding.get().swipeRefresh.setRefreshing(false);
//                }

        });

        /*trendingList*/
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*trendingCategoryList*/

        homeTrendingCategoryListViewModel.setHomeTrendingCatrgoryListDataObj(homeTrendingCategoryListViewModel.categoryParameterHolder, String.valueOf(Config.LOAD_FROM_DB), String.valueOf(homeTrendingCategoryListViewModel.offset));

        LiveData<Resource<List<Category>>> trendingCategories = homeTrendingCategoryListViewModel.getHomeTrendingCategoryListData();

        if (trendingCategories != null) {
            trendingCategories.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                if (listResource.data.size() > 0) {

                                    if (listResource.data.size() < 5) {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    } else {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    }

                                    binding.get().trendingCategoriesTitleTextView.setVisibility(View.VISIBLE);
                                    binding.get().viewAllTrendingCategoriesTextView.setVisibility(View.VISIBLE);
                                    replaceTrendingCategoryData(listResource.data);
                                }
                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data

                                if (listResource.data.size() > 0) {
                                    if (listResource.data.size() < 5) {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 1);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    } else {
                                        gridLayoutManager = new GridLayoutManager(getContext(), 2);
                                        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                                        binding.get().trendingCategoryList.setLayoutManager(gridLayoutManager);
                                    }

                                    binding.get().trendingCategoriesTitleTextView.setVisibility(View.VISIBLE);
                                    binding.get().viewAllTrendingCategoriesTextView.setVisibility(View.VISIBLE);
                                    replaceTrendingCategoryData(listResource.data);
                                }
                            }

                            categoryViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            homeTrendingCategoryListViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (homeTrendingCategoryListViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        homeTrendingCategoryListViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        homeTrendingCategoryListViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(homeTrendingCategoryListViewModel.isLoading));
        /*trendingCategoryList*/

        /*Collection List*/

        productCollectionViewModel.setProductCollectionHeaderListForHomeObj( String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT), String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT),
                String.valueOf(Config.COLLECTION_PRODUCT_LIST_LIMIT), String.valueOf(homeTrendingCategoryListViewModel.offset));

        LiveData<Resource<List<ProductCollectionHeader>>> productCollection = productCollectionViewModel.getProductCollectionHeaderListDataForHome();

        if (productCollection != null) {
            productCollection.observe(this, listResource -> {
                if (listResource != null) {

                    switch (listResource.status) {
                        case LOADING:
                            // Loading State
                            // Data are from Local DB

                            if (listResource.data != null) {
                                //fadeIn Animation
                                //fadeIn(binding.get().getRoot());

                                // Update the data

                                replaceCollection(listResource.data);

                            }

                            break;

                        case SUCCESS:
                            // Success State
                            // Data are from Server

                            if (listResource.data != null) {
                                // Update the data

                                replaceCollection(listResource.data);
                            }

                            productCollectionViewModel.setLoadingState(false);

                            break;

                        case ERROR:
                            // Error State

                            productCollectionViewModel.setLoadingState(false);

                            break;
                        default:
                            // Default

                            break;
                    }

                } else {

                    // Init Object or Empty Data
                    Utils.psLog("Empty Data");

                    if (productCollectionViewModel.offset > 1) {
                        // No more data for this list
                        // So, Block all future loading
                        productCollectionViewModel.forceEndLoading = true;
                    }

                }

            });
        }

        productCollectionViewModel.getLoadingState().observe(this, loadingState -> binding.get().setLoadingMore(productCollectionViewModel.isLoading));

        binding.get().categoryIconList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (binding.get() != null) {
                    if (binding.get().categoryIconList != null) {
                        if (binding.get().categoryIconList.getChildCount() > 0) {
                            layoutDone = true;
                            loadingCount++;
                            hideLoading();
                            binding.get().categoryIconList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            }
        });

        binding.get().viewPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {


                if (binding.get() != null && binding.get().viewPager != null) {
                    if (binding.get().viewPager.getChildCount() > 0) {
                        layoutDone = true;
                        loadingCount++;
                        hideLoading();
                        binding.get().viewPager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });

        binding.get().discountList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (binding.get() != null && binding.get().discountList != null &&
                        binding.get().discountList.getChildCount() > 0) {
                    layoutDone = true;
                    loadingCount++;
                    hideLoading();
                    binding.get().discountList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

    }

    @Override
    public void onDispatched() {

        if (homeLatestProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            LinearLayoutManager layoutManager = (LinearLayoutManager)
                    binding.get().productList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }

        }

        if (homeSearchProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            GridLayoutManager layoutManager = (GridLayoutManager)
                    binding.get().discountList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }

        }

        if (homeTrendingProductViewModel.loadingDirection == Utils.LoadingDirection.top) {

            GridLayoutManager layoutManager = (GridLayoutManager)
                    binding.get().trendingList.getLayoutManager();

            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }

        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void setupSliderPagination() {

        dotsCount = viewPagerAdapter.get().getCount();

        if (dotsCount > 0 && dots == null) {

            dots = new ImageView[dotsCount];

            if (binding.get() != null) {
                if (binding.get().viewPagerCountDots.getChildCount() > 0) {
                    binding.get().viewPagerCountDots.removeAllViewsInLayout();
                }
            }

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(4, 0, 4, 0);

                binding.get().viewPagerCountDots.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

        }

    }

    private void hideLoading() {

        if (loadingCount == 3 && layoutDone) {

            binding.get().loadingView.setVisibility(View.GONE);
            binding.get().loadHolder.setVisibility(View.GONE);
        }
    }

    private void setShopTouchCount() {
        touchCountViewModel.setTouchCountPostDataObj(loginUserId, "", Constants.SHOP);

        touchCountViewModel.getTouchCountPostData().observe(this, result -> {
            if (result != null) {
                if (result.status == Status.SUCCESS) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }

                } else if (result.status == Status.ERROR) {
                    if (this.getActivity() != null) {
                        Utils.psLog(result.status.toString());
                    }
                }
            }
        });
    }

    private void unFavFunction(Product product, LikeButton likeButton) {
        if (loginUserId.equals("")) {
            navigationController.navigateToUserLoginActivity(getActivity());
            likeButton.setLiked(false);
        } else {
            if(!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_off, null));
            }
        }
    }

    private void favFunction(Product product, LikeButton likeButton) {
        if (loginUserId.equals("")) {
            likeButton.setLiked(false);
            navigationController.navigateToUserLoginActivity(getActivity());
        } else {
            if(!favouriteViewModel.isLoading) {
                favouriteViewModel.setFavouritePostDataObj(product.id, loginUserId);
                likeButton.setLikeDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.heart_on, null));
            }
        }
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
        Date date = new Date();
        return dateFormat.format(date);
    }


    @Override
    public void onResume() {
        loadLoginUserId();
        super.onResume();
    }
}
