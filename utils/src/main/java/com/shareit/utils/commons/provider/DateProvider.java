package com.shareit.utils.commons.provider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DateProvider {

    public LocalDateTime getNowDateTime(){
        return LocalDateTime.now();
    }

    public LocalDate getNowDate(){
        return LocalDate.now();
    }
}
