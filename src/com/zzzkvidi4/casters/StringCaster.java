package com.zzzkvidi4.casters;

/**
 * Created by Роман on 07.10.2017.
 */
public class StringCaster extends Caster<String> {

    @Override
    public String cast(String value) throws NumberFormatException {
        return value;
    }
}
