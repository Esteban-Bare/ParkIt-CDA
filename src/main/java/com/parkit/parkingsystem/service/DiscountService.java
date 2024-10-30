package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;

public class DiscountService {
    public double applyDiscount(double price, boolean isRecurrentVisitor) {
        if (isRecurrentVisitor) {
            return price * Fare.REDUCTION_RECURRENT_VISITOR;
        }
        return price;
    }
}
