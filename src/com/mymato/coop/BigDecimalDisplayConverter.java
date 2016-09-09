package com.mymato.coop;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

// This class is required to format BigDecimal inputs by dropping the trailing zero in decimal values e.g. 2.0 is displayed as 2

@FacesConverter("bigDecimalPlainDisplay")
public class BigDecimalDisplayConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return new BigDecimal(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        BigDecimal  bd = (BigDecimal)value;
        return bd.setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }
}