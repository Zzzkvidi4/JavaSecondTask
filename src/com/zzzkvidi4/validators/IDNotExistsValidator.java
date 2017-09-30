package com.zzzkvidi4.validators;

import com.zzzkvidi4.Product;
import com.zzzkvidi4.ProductList;

import java.util.Iterator;

/**
 * Created by Роман on 25.09.2017.
 */
public class IDNotExistsValidator extends BasicValidator<Integer> {
    private ProductList productList;

    public IDNotExistsValidator(String validationFailsMsg, Integer initialValue, ProductList productList) {
        super(validationFailsMsg, initialValue);
        this.productList = productList;
    }

    @Override
    public boolean validate(Integer value) {
        boolean flag = true;
        Iterator<Product> iterator = productList.iterator();
        while (flag && iterator.hasNext()) {
            flag = value != iterator.next().getId();
        }
        return flag;
    }

    @Override
    public Integer cast(String value) {
        return Integer.valueOf(value);
    }
}
