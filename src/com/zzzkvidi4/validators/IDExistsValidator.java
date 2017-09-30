package com.zzzkvidi4.validators;

import com.zzzkvidi4.Product;
import com.zzzkvidi4.ProductList;

import java.util.Iterator;

public class IDExistsValidator extends BasicValidator<Integer> {
    private ProductList productList;

    public IDExistsValidator(String validationFailsMsg, Integer initialValue, ProductList productList) {
        super(validationFailsMsg, initialValue);
        this.productList = productList;
    }

    @Override
    public boolean validate(Integer value) {
        boolean flag = true;
        Iterator<Product> iterator = productList.iterator();
        while ((flag) && (iterator.hasNext())){
            flag = iterator.next().getId() != value;
        }
        return !flag;
    }

    @Override
    public Integer cast(String value) {
        return Integer.valueOf(value);
    }
}
