package com.example.zerowaste_api.enums;

import com.example.zerowaste_api.common.BaseEnumInterface;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public enum NotificationType implements BaseEnumInterface {
    FOOD_INVENTORY_ALERT("$food is nearing expiry on $date!! Consider to use it soon or convert to donation?"),
    DONATION_POSTED("Confirmation: Your donation has been posted successfully! Item: $foods"),
    DONATION_CLAIMED("Update: Your donation has been claimed. Please check pickup detail."),
    MEAL_REMINDER("Reminder: You have a planned meal($meal) on $date.");

    private final String label;

    NotificationType(String label){
        this.label=label;
    }

    public String getLabel() {
        return label;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(){
        return (T) label;
    }

    //choose String depend on what type of notification
    public String format(Object... extra){
        String result= label;
        switch (this){
            case FOOD_INVENTORY_ALERT:
                if(extra.length>=2) {
                    List<String> food=(List<String>)extra[0];
                    String foodString = String.join(", ", food);
                    result = result.replace("$food", foodString);
                    LocalDate expiry=(LocalDate)extra[1];
                    result = result.replace("$date", formDate(expiry));
                }
                break;
            case DONATION_POSTED:
                if(extra.length>=2 && extra[0] != null && extra[1] != null) {
                    @SuppressWarnings("unchecked")
                         List<String> foods=(List<String>) extra[0];
                    @SuppressWarnings("unchecked")
                            List<Long> quantity=(List<Long>) extra[1];

                    if(foods.size()!=quantity.size()) {
                        throw new IllegalArgumentException("Number of foods and quantity are not equal");
                    }
                    String foodList="";
                    for(int i=0;i<foods.size();i++) {
                        foodList=foodList.concat(foods.get(i))
                                .concat("(")
                                .concat(String.valueOf(quantity.get(i)))
                                .concat(")");
                        if(i<foods.size()-1){
                            foodList=foodList.concat(",");
                        }
                    }
                    result = result.replace("$foods", foodList);
                }
                break;
            case MEAL_REMINDER:
                if(extra.length>=2) {
                    result=result.replace("$meal", String.valueOf(extra[0]));
                    LocalDate expiry=(LocalDate)extra[1];
                    result=result.replace("$date", formDate(expiry));
                }
                break;
            default:
                break;
        }
        return result;
    }

    //format date time
    public String formDate(LocalDate date){
        DateTimeFormatter sf=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return sf.format(date);
    }
}
