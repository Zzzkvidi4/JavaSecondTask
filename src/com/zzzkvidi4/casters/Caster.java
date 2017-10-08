package com.zzzkvidi4.casters;

/**
 * Created by Роман on 07.10.2017.
 */
public abstract class Caster<T> {
    public abstract T cast(String value) throws NumberFormatException;
}
