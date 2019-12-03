package com.imronreviady.simplestore.di;


import com.imronreviady.simplestore.MainActivity;
import com.imronreviady.simplestore.ui.apploading.AppLoadingActivity;
import com.imronreviady.simplestore.ui.apploading.AppLoadingFragment;
import com.imronreviady.simplestore.ui.basket.BasketListActivity;
import com.imronreviady.simplestore.ui.basket.BasketListFragment;
import com.imronreviady.simplestore.ui.blog.detail.BlogDetailActivity;
import com.imronreviady.simplestore.ui.blog.detail.BlogDetailFragment;
import com.imronreviady.simplestore.ui.blog.list.BlogListActivity;
import com.imronreviady.simplestore.ui.blog.list.BlogListFragment;
import com.imronreviady.simplestore.ui.category.CategoryListActivity;
import com.imronreviady.simplestore.ui.category.CategoryListFragment;
import com.imronreviady.simplestore.ui.category.TrendingCategoryFragment;
import com.imronreviady.simplestore.ui.checkout.CheckoutActivity;
import com.imronreviady.simplestore.ui.checkout.CheckoutFragment1;
import com.imronreviady.simplestore.ui.checkout.CheckoutFragment2;
import com.imronreviady.simplestore.ui.checkout.CheckoutFragment3;
import com.imronreviady.simplestore.ui.checkout.CheckoutStatusFragment;
import com.imronreviady.simplestore.ui.collection.CollectionActivity;
import com.imronreviady.simplestore.ui.collection.CollectionFragment;
import com.imronreviady.simplestore.ui.collection.productCollectionHeader.ProductCollectionHeaderListFragment;
import com.imronreviady.simplestore.ui.comment.detail.CommentDetailActivity;
import com.imronreviady.simplestore.ui.comment.detail.CommentDetailFragment;
import com.imronreviady.simplestore.ui.comment.list.CommentListActivity;
import com.imronreviady.simplestore.ui.comment.list.CommentListFragment;
import com.imronreviady.simplestore.ui.contactus.ContactUsFragment;
import com.imronreviady.simplestore.ui.forceupdate.ForceUpdateActivity;
import com.imronreviady.simplestore.ui.forceupdate.ForceUpdateFragment;
import com.imronreviady.simplestore.ui.gallery.GalleryActivity;
import com.imronreviady.simplestore.ui.gallery.GalleryFragment;
import com.imronreviady.simplestore.ui.gallery.detail.GalleryDetailActivity;
import com.imronreviady.simplestore.ui.gallery.detail.GalleryDetailFragment;
import com.imronreviady.simplestore.ui.language.LanguageFragment;
import com.imronreviady.simplestore.ui.product.MainFragment;
import com.imronreviady.simplestore.ui.notification.detail.NotificationActivity;
import com.imronreviady.simplestore.ui.notification.detail.NotificationFragment;
import com.imronreviady.simplestore.ui.notification.list.NotificationListActivity;
import com.imronreviady.simplestore.ui.notification.list.NotificationListFragment;
import com.imronreviady.simplestore.ui.notification.setting.NotificationSettingFragment;
import com.imronreviady.simplestore.ui.product.detail.ProductActivity;
import com.imronreviady.simplestore.ui.product.detail.ProductDetailFragment;
import com.imronreviady.simplestore.ui.product.favourite.FavouriteListActivity;
import com.imronreviady.simplestore.ui.product.favourite.FavouriteListFragment;
import com.imronreviady.simplestore.ui.product.filtering.CategoryFilterFragment;
import com.imronreviady.simplestore.ui.product.filtering.FilterFragment;
import com.imronreviady.simplestore.ui.product.filtering.FilteringActivity;
import com.imronreviady.simplestore.ui.product.history.HistoryFragment;
import com.imronreviady.simplestore.ui.product.history.UserHistoryListActivity;
import com.imronreviady.simplestore.ui.product.list.ProductListActivity;
import com.imronreviady.simplestore.ui.product.list.ProductListFragment;
import com.imronreviady.simplestore.ui.product.productbycatId.ProductListByCatIdActivity;
import com.imronreviady.simplestore.ui.product.productbycatId.ProductListByCatIdFragment;
import com.imronreviady.simplestore.ui.product.search.SearchByCategoryActivity;
import com.imronreviady.simplestore.ui.product.search.SearchCategoryFragment;
import com.imronreviady.simplestore.ui.product.search.SearchFragment;
import com.imronreviady.simplestore.ui.product.search.SearchSubCategoryFragment;
import com.imronreviady.simplestore.ui.rating.RatingListActivity;
import com.imronreviady.simplestore.ui.rating.RatingListFragment;
import com.imronreviady.simplestore.ui.setting.AppInfoActivity;
import com.imronreviady.simplestore.ui.setting.AppInfoFragment;
import com.imronreviady.simplestore.ui.setting.NotificationSettingActivity;
import com.imronreviady.simplestore.ui.setting.SettingActivity;
import com.imronreviady.simplestore.ui.setting.SettingFragment;
import com.imronreviady.simplestore.ui.shop.ShopProfileFragment;
import com.imronreviady.simplestore.ui.stripe.StripeActivity;
import com.imronreviady.simplestore.ui.stripe.StripeFragment;
import com.imronreviady.simplestore.ui.terms.TermsAndConditionsActivity;
import com.imronreviady.simplestore.ui.terms.TermsAndConditionsFragment;
import com.imronreviady.simplestore.ui.transaction.detail.TransactionActivity;
import com.imronreviady.simplestore.ui.transaction.detail.TransactionFragment;
import com.imronreviady.simplestore.ui.transaction.list.TransactionListActivity;
import com.imronreviady.simplestore.ui.transaction.list.TransactionListFragment;
import com.imronreviady.simplestore.ui.user.PasswordChangeActivity;
import com.imronreviady.simplestore.ui.user.PasswordChangeFragment;
import com.imronreviady.simplestore.ui.user.ProfileEditActivity;
import com.imronreviady.simplestore.ui.user.ProfileEditFragment;
import com.imronreviady.simplestore.ui.user.ProfileFragment;
import com.imronreviady.simplestore.ui.user.UserFBRegisterActivity;
import com.imronreviady.simplestore.ui.user.UserFBRegisterFragment;
import com.imronreviady.simplestore.ui.user.UserForgotPasswordActivity;
import com.imronreviady.simplestore.ui.user.UserForgotPasswordFragment;
import com.imronreviady.simplestore.ui.user.UserLoginActivity;
import com.imronreviady.simplestore.ui.user.UserLoginFragment;
import com.imronreviady.simplestore.ui.user.UserRegisterActivity;
import com.imronreviady.simplestore.ui.user.UserRegisterFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Panacea-Soft on 11/15/17.
 * Contact Email : teamps.is.cool@gmail.com
 */


@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = MainModule.class)
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = TransactionModule.class)
    abstract TransactionListActivity contributeTransactionActivity();

    @ContributesAndroidInjector(modules = FavouriteListModule.class)
    abstract FavouriteListActivity contributeFavouriteListActivity();

    @ContributesAndroidInjector(modules = UserHistoryModule.class)
    abstract UserHistoryListActivity contributeUserHistoryListActivity();

    @ContributesAndroidInjector(modules = UserRegisterModule.class)
    abstract UserRegisterActivity contributeUserRegisterActivity();

    @ContributesAndroidInjector(modules = UserFBRegisterModule.class)
    abstract UserFBRegisterActivity contributeUserFBRegisterActivity();

    @ContributesAndroidInjector(modules = UserForgotPasswordModule.class)
    abstract UserForgotPasswordActivity contributeUserForgotPasswordActivity();

    @ContributesAndroidInjector(modules = UserLoginModule.class)
    abstract UserLoginActivity contributeUserLoginActivity();

    @ContributesAndroidInjector(modules = PasswordChangeModule.class)
    abstract PasswordChangeActivity contributePasswordChangeActivity();

    @ContributesAndroidInjector(modules = ProductListByCatIdModule.class)
    abstract ProductListByCatIdActivity productListByCatIdActivity();

    @ContributesAndroidInjector(modules = FilteringModule.class)
    abstract FilteringActivity filteringActivity();

    @ContributesAndroidInjector(modules = CommentDetailModule.class)
    abstract CommentDetailActivity commentDetailActivity();

    @ContributesAndroidInjector(modules = DiscountDetailModule.class)
    abstract ProductActivity discountDetailActivity();

    @ContributesAndroidInjector(modules = NotificationModule.class)
    abstract NotificationListActivity notificationActivity();

    @ContributesAndroidInjector(modules = HomeFilteringActivityModule.class)
    abstract ProductListActivity contributehomeFilteringActivity();

    @ContributesAndroidInjector(modules = NotificationDetailModule.class)
    abstract NotificationActivity notificationDetailActivity();

    @ContributesAndroidInjector(modules = TransactionDetailModule.class)
    abstract TransactionActivity transactionDetailActivity();

    @ContributesAndroidInjector(modules = CheckoutActivityModule.class)
    abstract CheckoutActivity checkoutActivity();

    @ContributesAndroidInjector(modules = CommentListActivityModule.class)
    abstract CommentListActivity commentListActivity();

    @ContributesAndroidInjector(modules = BasketlistActivityModule.class)
    abstract BasketListActivity basketListActivity();

    @ContributesAndroidInjector(modules = GalleryDetailActivityModule.class)
    abstract GalleryDetailActivity galleryDetailActivity();

    @ContributesAndroidInjector(modules = GalleryActivityModule.class)
    abstract GalleryActivity galleryActivity();

    @ContributesAndroidInjector(modules = SearchByCategoryActivityModule.class)
    abstract SearchByCategoryActivity searchByCategoryActivity();

    @ContributesAndroidInjector(modules = TermsAndConditionsModule.class)
    abstract TermsAndConditionsActivity termsAndConditionsActivity();

    @ContributesAndroidInjector(modules = EditSettingModule.class)
    abstract SettingActivity editSettingActivity();

    @ContributesAndroidInjector(modules = LanguageChangeModule.class)
    abstract NotificationSettingActivity languageChangeActivity();

    @ContributesAndroidInjector(modules = ProfileEditModule.class)
    abstract ProfileEditActivity contributeProfileEditActivity();

    @ContributesAndroidInjector(modules = AppInfoModule.class)
    abstract AppInfoActivity AppInfoActivity();

    @ContributesAndroidInjector(modules = CategoryListActivityAppInfoModule.class)
    abstract CategoryListActivity categoryListActivity();

    @ContributesAndroidInjector(modules = RatingListActivityModule.class)
    abstract RatingListActivity ratingListActivity();

    @ContributesAndroidInjector(modules = CollectionModule.class)
    abstract CollectionActivity collectionActivity();

    @ContributesAndroidInjector(modules = StripeModule.class)
    abstract StripeActivity stripeActivity();

    @ContributesAndroidInjector(modules = BlogListActivityModule.class)
    abstract BlogListActivity BlogListActivity();

    @ContributesAndroidInjector(modules = BlogDetailModule.class)
    abstract BlogDetailActivity blogDetailActivity();

    @ContributesAndroidInjector(modules = forceUpdateModule.class)
    abstract ForceUpdateActivity forceUpdateActivity();

    @ContributesAndroidInjector(modules = appLoadingModule.class)
    abstract AppLoadingActivity appLoadingActivity();
}

@Module
abstract class CheckoutActivityModule {

    @ContributesAndroidInjector
    abstract CheckoutFragment1 checkoutFragment1();

    @ContributesAndroidInjector
    abstract LanguageFragment languageFragment();

    @ContributesAndroidInjector
    abstract CheckoutFragment2 checkoutFragment2();

    @ContributesAndroidInjector
    abstract CheckoutFragment3 checkoutFragment3();

    @ContributesAndroidInjector
    abstract CheckoutStatusFragment checkoutStatusFragment();
}

@Module
abstract class MainModule {

    @ContributesAndroidInjector
    abstract ProductListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract MainFragment contributeSelectedShopFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract ProductCollectionHeaderListFragment contributeProductCollectionHeaderListFragment();

    @ContributesAndroidInjector
    abstract ContactUsFragment contributeContactUsFragment();

    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();

    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();

    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();

    @ContributesAndroidInjector
    abstract UserFBRegisterFragment contributeUserFBRegisterFragment();

    @ContributesAndroidInjector
    abstract NotificationSettingFragment contributeNotificationSettingFragment();

    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract LanguageFragment contributeLanguageFragment();

    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteListFragment();

    @ContributesAndroidInjector
    abstract TransactionListFragment contributTransactionListFragment();

    @ContributesAndroidInjector
    abstract SettingFragment contributEditSettingFragment();

    @ContributesAndroidInjector
    abstract HistoryFragment historyFragment();

    @ContributesAndroidInjector
    abstract ShopProfileFragment contributeAboutUsFragmentFragment();

    @ContributesAndroidInjector
    abstract BasketListFragment basketFragment();

    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract NotificationListFragment contributeNotificationFragment();

    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();

}


@Module
abstract class ProfileEditModule {
    @ContributesAndroidInjector
    abstract ProfileEditFragment contributeProfileEditFragment();
}

@Module
abstract class TransactionModule {
    @ContributesAndroidInjector
    abstract TransactionListFragment contributeTransactionListFragment();
}

@Module
abstract class UserFBRegisterModule {
    @ContributesAndroidInjector
    abstract UserFBRegisterFragment contributeUserFBRegisterFragment();
}

@Module
abstract class FavouriteListModule {
    @ContributesAndroidInjector
    abstract FavouriteListFragment contributeFavouriteFragment();
}

@Module
abstract class UserRegisterModule {
    @ContributesAndroidInjector
    abstract UserRegisterFragment contributeUserRegisterFragment();
}

@Module
abstract class UserForgotPasswordModule {
    @ContributesAndroidInjector
    abstract UserForgotPasswordFragment contributeUserForgotPasswordFragment();
}

@Module
abstract class UserLoginModule {
    @ContributesAndroidInjector
    abstract UserLoginFragment contributeUserLoginFragment();
}

@Module
abstract class PasswordChangeModule {
    @ContributesAndroidInjector
    abstract PasswordChangeFragment contributePasswordChangeFragment();
}

@Module
abstract class CommentDetailModule {
    @ContributesAndroidInjector
    abstract CommentDetailFragment commentDetailFragment();
}

@Module
abstract class DiscountDetailModule {
    @ContributesAndroidInjector
    abstract ProductDetailFragment discountDetailFragment();
}

@Module
abstract class NotificationModule {
    @ContributesAndroidInjector
    abstract NotificationListFragment notificationFragment();


}


@Module
abstract class NotificationDetailModule {
    @ContributesAndroidInjector
    abstract NotificationFragment notificationDetailFragment();
}

@Module
abstract class TransactionDetailModule {
    @ContributesAndroidInjector
    abstract TransactionFragment transactionDetailFragment();
}

@Module
abstract class UserHistoryModule {
    @ContributesAndroidInjector
    abstract HistoryFragment contributeHistoryFragment();
}

@Module
abstract class AppInfoModule {
    @ContributesAndroidInjector
    abstract AppInfoFragment contributeAppInfoFragment();
}

@Module
abstract class CategoryListActivityAppInfoModule {
    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract TrendingCategoryFragment contributeTrendingCategoryFragment();
}

@Module
abstract class RatingListActivityModule {
    @ContributesAndroidInjector
    abstract RatingListFragment contributeRatingListFragment();
}

@Module
abstract class TermsAndConditionsModule {
    @ContributesAndroidInjector
    abstract TermsAndConditionsFragment TermsAndConditionsFragment();
}

@Module
abstract class EditSettingModule {
    @ContributesAndroidInjector
    abstract SettingFragment EditSettingFragment();
}

@Module
abstract class LanguageChangeModule {
    @ContributesAndroidInjector
    abstract NotificationSettingFragment notificationSettingFragment();
}

@Module
abstract class EditProfileModule {
    @ContributesAndroidInjector
    abstract ProfileFragment ProfileFragment();
}

@Module
abstract class ProductListByCatIdModule {
    @ContributesAndroidInjector
    abstract ProductListByCatIdFragment contributeProductListByCatIdFragment();

}

@Module
abstract class FilteringModule {

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract FilterFragment contributeSpecialFilteringFragment();
}

@Module
abstract class HomeFilteringActivityModule {
    @ContributesAndroidInjector
    abstract ProductListFragment contributefeaturedProductFragment();

    @ContributesAndroidInjector
    abstract CategoryListFragment contributeCategoryFragment();

    @ContributesAndroidInjector
    abstract CategoryFilterFragment contributeTypeFilterFragment();

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();
}

@Module
abstract class CommentListActivityModule {
    @ContributesAndroidInjector
    abstract CommentListFragment contributeCommentListFragment();
}

@Module
abstract class BasketlistActivityModule {
    @ContributesAndroidInjector
    abstract BasketListFragment contributeBasketListFragment();
}

@Module
abstract class GalleryDetailActivityModule {
    @ContributesAndroidInjector
    abstract GalleryDetailFragment contributeGalleryDetailFragment();
}

@Module
abstract class GalleryActivityModule {
    @ContributesAndroidInjector
    abstract GalleryFragment contributeGalleryFragment();
}

@Module
abstract class SearchByCategoryActivityModule {

    @ContributesAndroidInjector
    abstract SearchCategoryFragment contributeSearchCategoryFragment();

    @ContributesAndroidInjector
    abstract SearchSubCategoryFragment contributeSearchSubCategoryFragment();
}

@Module
abstract class CollectionModule {

    @ContributesAndroidInjector
    abstract CollectionFragment contributeCollectionFragment();

}

@Module
abstract class StripeModule {

    @ContributesAndroidInjector
    abstract StripeFragment contributeStripeFragment();

}

@Module
abstract class BlogListActivityModule {

    @ContributesAndroidInjector
    abstract BlogListFragment contributeBlogListFragment();

}

@Module
abstract class BlogDetailModule {

    @ContributesAndroidInjector
    abstract BlogDetailFragment contributeBlogDetailFragment();
}

@Module
abstract class forceUpdateModule {

    @ContributesAndroidInjector
    abstract ForceUpdateFragment contributeForceUpdateFragment();
}

@Module
abstract class appLoadingModule {

    @ContributesAndroidInjector
    abstract AppLoadingFragment contributeAppLoadingFragment();
}

