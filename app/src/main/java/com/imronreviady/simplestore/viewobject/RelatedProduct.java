package com.imronreviady.simplestore.viewobject;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class RelatedProduct {
    @NonNull
    @PrimaryKey
    @SerializedName("id")
    public final String id;

    public RelatedProduct(@NonNull String id) {
        this.id = id;
    }
}
