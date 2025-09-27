package com.example.zerowaste_api.enums;

import com.example.zerowaste_api.common.BaseEnumInterface;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
                    result = result.replace("$food", String.valueOf(extra[0]));
                    result = result.replace("$date", String.valueOf(extra[1]));
                }
                break;
            case DONATION_CLAIMED:
                if(extra.length>=1) {
                    result=result.replace("$foods", String.valueOf(extra[0]));
                }
                break;
            case MEAL_REMINDER:
                if(extra.length>=2) {
                    result=result.replace("$meal", String.valueOf(extra[0]));
                    result=result.replace("$date", String.valueOf(extra[1]));
                }
                break;
            default:
                break;
        }
        return result;
    }

    //format date time
    private String formDate(LocalDateTime time){
            return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
