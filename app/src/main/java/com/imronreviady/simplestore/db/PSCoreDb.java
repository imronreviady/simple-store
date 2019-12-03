package com.imronreviady.simplestore.db;

import com.imronreviady.simplestore.db.common.Converters;
import com.imronreviady.simplestore.viewobject.AboutUs;
import com.imronreviady.simplestore.viewobject.Basket;
import com.imronreviady.simplestore.viewobject.Blog;
import com.imronreviady.simplestore.viewobject.Category;
import com.imronreviady.simplestore.viewobject.CategoryMap;
import com.imronreviady.simplestore.viewobject.Comment;
import com.imronreviady.simplestore.viewobject.CommentDetail;
import com.imronreviady.simplestore.viewobject.DeletedObject;
import com.imronreviady.simplestore.viewobject.DiscountProduct;
import com.imronreviady.simplestore.viewobject.FavouriteProduct;
import com.imronreviady.simplestore.viewobject.FeaturedProduct;
import com.imronreviady.simplestore.viewobject.HistoryProduct;
import com.imronreviady.simplestore.viewobject.Image;
import com.imronreviady.simplestore.viewobject.LatestProduct;
import com.imronreviady.simplestore.viewobject.LikedProduct;
import com.imronreviady.simplestore.viewobject.Noti;
import com.imronreviady.simplestore.viewobject.PSAppInfo;
import com.imronreviady.simplestore.viewobject.PSAppVersion;
import com.imronreviady.simplestore.viewobject.Product;
import com.imronreviady.simplestore.viewobject.ProductAttributeDetail;
import com.imronreviady.simplestore.viewobject.ProductAttributeHeader;
import com.imronreviady.simplestore.viewobject.ProductCollection;
import com.imronreviady.simplestore.viewobject.ProductCollectionHeader;
import com.imronreviady.simplestore.viewobject.ProductColor;
import com.imronreviady.simplestore.viewobject.ProductListByCatId;
import com.imronreviady.simplestore.viewobject.ProductMap;
import com.imronreviady.simplestore.viewobject.ProductSpecs;
import com.imronreviady.simplestore.viewobject.Rating;
import com.imronreviady.simplestore.viewobject.RelatedProduct;
import com.imronreviady.simplestore.viewobject.ShippingMethod;
import com.imronreviady.simplestore.viewobject.Shop;
import com.imronreviady.simplestore.viewobject.ShopByTagId;
import com.imronreviady.simplestore.viewobject.ShopMap;
import com.imronreviady.simplestore.viewobject.ShopTag;
import com.imronreviady.simplestore.viewobject.SubCategory;
import com.imronreviady.simplestore.viewobject.TransactionDetail;
import com.imronreviady.simplestore.viewobject.TransactionObject;
import com.imronreviady.simplestore.viewobject.User;
import com.imronreviady.simplestore.viewobject.UserLogin;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


/**
 * Created by Panacea-Soft on 11/20/17.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Database(entities = {
        Image.class,
        Category.class,
        User.class,
        UserLogin.class,
        AboutUs.class,
        Product.class,
        LatestProduct.class,
        DiscountProduct.class,
        FeaturedProduct.class,
        SubCategory.class,
        ProductListByCatId.class,
        Comment.class,
        CommentDetail.class,
        ProductColor.class,
        ProductSpecs.class,
        RelatedProduct.class,
        FavouriteProduct.class,
        LikedProduct.class,
        ProductAttributeHeader.class,
        ProductAttributeDetail.class,
        Noti.class,
        TransactionObject.class,
        ProductCollectionHeader.class,
        ProductCollection.class,
        TransactionDetail.class,
        Basket.class,
        HistoryProduct.class,
        Shop.class,
        ShopTag.class,
        Blog.class,
        Rating.class,
        ShippingMethod.class,
        ShopByTagId.class,
        ProductMap.class,
        ShopMap.class,
        CategoryMap.class,
        PSAppInfo.class,
        PSAppVersion.class,
        DeletedObject.class

}, version = 3, exportSchema = false)
//V1.3 = DBV 2
//V1.4 = DBV 3

@TypeConverters({Converters.class})

public abstract class PSCoreDb extends RoomDatabase {

    abstract public UserDao userDao();

    abstract public ProductColorDao productColorDao();

    abstract public ProductSpecsDao productSpecsDao();

    abstract public ProductAttributeHeaderDao productAttributeHeaderDao();

    abstract public ProductAttributeDetailDao productAttributeDetailDao();

    abstract public BasketDao basketDao();

    abstract public HistoryDao historyDao();

    abstract public AboutUsDao aboutUsDao();

    abstract public ImageDao imageDao();

    abstract public RatingDao ratingDao();

    abstract public CommentDao commentDao();

    abstract public CommentDetailDao commentDetailDao();

    abstract public ProductDao productDao();

    abstract public CategoryDao categoryDao();

    abstract public SubCategoryDao subCategoryDao();

    abstract public NotificationDao notificationDao();

    abstract public ProductCollectionDao productCollectionDao();

    abstract public TransactionDao transactionDao();

    abstract public TransactionOrderDao transactionOrderDao();

    abstract public ShopDao shopDao();

    abstract public BlogDao blogDao();

    abstract public ShippingMethodDao shippingMethodDao();

    abstract public ProductMapDao productMapDao();

    abstract public CategoryMapDao categoryMapDao();

    abstract public PSAppInfoDao psAppInfoDao();

    abstract public PSAppVersionDao psAppVersionDao();

    abstract public DeletedObjectDao deletedObjectDao();


//    /**
//     * Migrate from:
//     * version 1 - using Room
//     * to
//     * version 2 - using Room where the {@link } has an extra field: addedDateStr
//     */
//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE news "
//                    + " ADD COLUMN addedDateStr INTEGER NOT NULL DEFAULT 0");
//        }
//    };

    /* More migration write here */
}