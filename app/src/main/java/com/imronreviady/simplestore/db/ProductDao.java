package com.imronreviady.simplestore.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.imronreviady.simplestore.viewobject.FavouriteProduct;
import com.imronreviady.simplestore.viewobject.Product;
import com.imronreviady.simplestore.viewobject.ProductListByCatId;
import com.imronreviady.simplestore.viewobject.RelatedProduct;

import java.util.List;

/**
 * Created by Panacea-Soft on 9/18/18.
 * Contact Email : teamps.is.cool@gmail.com
 */

@Dao
public abstract class ProductDao {

    //region product list

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<Product> productList);

    @Query("DELETE FROM PRODUCT")
    public abstract void deleteAll();

    @Query("SELECT p.* FROM Product p, ProductMap pm WHERE p.id = pm.productId AND pm.mapKey = :value ORDER BY pm.sorting asc")
    public abstract LiveData<List<Product>> getProductsByKey (String value);

    @Query("SELECT p.* FROM Product p, ProductMap pm WHERE p.id = pm.productId AND pm.mapKey = :value ORDER BY pm.sorting asc limit:loadFromDb")
    public abstract LiveData<List<Product>> getProductsByKeyByLimit (String value,String loadFromDb);



    //endregion

    //region product detail

    @Query("SELECT * FROM Product WHERE id =:productId ORDER BY addedDate DESC")
    public abstract LiveData<Product> getProductById(String productId);

    //endregion

    //region Product List by Category Id

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ProductListByCatId productListByCatId);

    @Query("DELETE FROM ProductListByCatId")
    public abstract void deleteAllProductListByCatIdVOs();

    @Query("SELECT * FROM product WHERE id IN (SELECT productId FROM ProductListByCatId WHERE catId=:catId) ORDER BY addedDate DESC")
    public abstract LiveData<List<Product>> getAllProductListByCatId(String catId);

    //endregion

    //region related

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(RelatedProduct relatedProducts);

    @Query("DELETE FROM RelatedProduct")
    public abstract void deleteAllRelatedProducts();

    @Query("SELECT * FROM Product WHERE id IN (SELECT id FROM RelatedProduct) ORDER BY addedDate DESC")
    public abstract LiveData<List<Product>> getAllRelatedProducts();

    @Query("DELETE FROM Product WHERE id in (SELECT id FROM RelatedProduct)")
    public abstract void deleteAllBasedOnRelated();

    //endregion

    //region favourite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertFavourite(FavouriteProduct favouriteProduct);

    @Query("DELETE FROM favouriteproduct")
    public abstract void deleteAllFavouriteProducts();

    @Query("DELETE FROM favouriteproduct where productId = :productId")
    public abstract void deleteFavouriteProductByProductId(String productId);

    @Query("SELECT prd.* FROM Product prd, favouriteproduct fp WHERE prd.id = fp.productId order by fp.sorting ")
    public abstract LiveData<List<Product>> getAllFavouriteProducts();

    @Query("SELECT max(sorting) from favouriteproduct")
    public abstract int getMaxSortingFavourite();

    @Query("UPDATE Product SET isFavourited =:is_favourited WHERE id =:productId")
    public abstract void updateProductForFavById(String productId,String is_favourited);

    @Query("SELECT isFavourited FROM Product WHERE id =:productId")
    public abstract String selectFavouriteById(String productId);

    @Query("DELETE FROM Product WHERE id =:productId")
    public abstract void deleteProductById(String productId);

    //endregion
}
