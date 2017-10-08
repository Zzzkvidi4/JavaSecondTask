package com.zzzkvidi4.casters;

/**
 * Created by Роман on 07.10.2017.
 */
public class DoubleCaster extends Caster<Double> {
    @Override
    public Double cast(String value) throws NumberFormatException {
        return Double.valueOf(value);
    }
}
