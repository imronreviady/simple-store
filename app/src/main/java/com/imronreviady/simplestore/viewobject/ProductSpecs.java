package com.imronreviady.simplestore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = "id")
public class ProductSpecs {

    @SerializedName("id")
    @NonNull
    public final String id;

    @SerializedName("product_id")
    public final String productId;

    @SerializedName("name")
    public final String name;

    @SerializedName("description")
    public final String description;

    @SerializedName("added_date")
    public final String addedDate;

    @SerializedName("added_user_id")
    public final String addedUserId;

    @SerializedName("updated_date")
    public final String updatedDate;

    @SerializedName("updated_user_id")
    public final String updatedUserId;

    @SerializedName("updated_flag")
    public final String updatedFlag;

    @SerializedName("is_empty_object")
    public final String isEmptyObject;

    public ProductSpecs(@NonNull String id, String productId, String name, String description, String addedDate, String addedUserId, String updatedDate, String updatedUserId, String updatedFlag, String isEmptyObject) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.addedDate = addedDate;
        this.addedUserId = addedUserId;
        this.updatedDate = updatedDate;
        this.updatedUserId = updatedUserId;
        this.updatedFlag = updatedFlag;
        this.isEmptyObject = isEmptyObject;
    }
}
