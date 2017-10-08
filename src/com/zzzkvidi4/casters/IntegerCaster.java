package com.zzzkvidi4.casters;

/**
 * Created by Роман on 07.10.2017.
 */
public class IntegerCaster extends Caster<Integer> {
    @Override
    public Integer cast(String value) throws NumberFormatException {
        return Integer.valueOf(value);
    }
}
