package com.zzzkvidi4.predicates;

import com.zzzkvidi4.User;

import java.util.function.Predicate;

public class IdGreaterPredicate implements Predicate<User> {
    private int id;

    public IdGreaterPredicate(int id) {
        this.id = id;
    }

    @Override
    public boolean test(User user) {
        return user.getId() >= id;
    }
}
