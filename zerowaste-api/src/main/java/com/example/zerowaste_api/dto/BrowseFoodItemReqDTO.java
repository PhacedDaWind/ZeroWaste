package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.common.BaseViewOption;
import com.example.zerowaste_api.enums.FoodItemActionType;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BrowseFoodItemReqDTO extends BaseViewOption {
    private Long usersId;

    private Boolean convertToDonation;

    private String category;

    private LocalDate expiryDate;

    private String storageLocation;

    private FoodItemActionType actionType;

//    @ApiModelProperty("column(s)  - [id|usersId|convertToDonation|category|expiryDate|storageLocation]")
    private List<
            @Pattern(
            regexp =  "^-{0,1}(id|usersId|username|itemName|convertToDonation|category|expiryDate|storageLocation)$",
            message = "Unsupported sort column(s). Supported column(s) for - [id|usersId|username|itemName|convertToDonation|category|expiryDate|storageLocation]")
            String> sort;

    BrowseFoodItemReqDTO() {
        super.getConvertMap().put("id", "id");
        super.getConvertMap().put("-id", "-id");

        super.getConvertMap().put("usersId", "usersId");
        super.getConvertMap().put("-usersId", "-usersId");

        super.getConvertMap().put("username", "(username)");
        super.getConvertMap().put("-username", "-(username)");

        super.getConvertMap().put("itemName", "itemName");
        super.getConvertMap().put("-itemName", "-itemName");

        super.getConvertMap().put("convertToDonation", "(convertToDonation)");
        super.getConvertMap().put("-convertToDonation", "-(convertToDonation)");

        super.getConvertMap().put("category", "(category)");
        super.getConvertMap().put("-category", "-(category)");

        super.getConvertMap().put("expiryDate", "(expiryDate)");
        super.getConvertMap().put("-expiryDate", "-(expiryDate)");

        super.getConvertMap().put("storageLocation", "(storageLocation)");
        super.getConvertMap().put("-storageLocation", "-(storageLocation)");
    }
}
