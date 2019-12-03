package com.imronreviady.simplestore.viewobject.holder;

import com.imronreviady.simplestore.utils.Constants;

public class CategoryParameterHolder {

    public String order_by;

    public CategoryParameterHolder() {

        this.order_by = Constants.FILTERING_ADDED_DATE;

    }

    public CategoryParameterHolder getTrendingCategories()
    {
        this.order_by = Constants.FILTERING_TRENDING;

        return this;
    }

    public String changeToMapValue() {
        String result = "";

        if (!order_by.isEmpty()) {
            result += order_by;
        }

        return result;
    }
}
