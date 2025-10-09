package com.example.zerowaste_api.dto;

import com.example.zerowaste_api.common.BaseViewOption;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
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

    private List<@Pattern(
            regexp =  "^-{0,1}(id|usersId|convertToDonation|category|expiryDate|storageLocation)$",
            message = "Unsupported sort column(s). Supported column(s) for - [id|usersId|convertToDonation|category|expiryDate|storageLocation]"
    )
            String> sort;

    BrowseFoodItemReqDTO() {
        convertMap.put("id", "(id)");
        convertMap.put("-id", "-(id)");

        convertMap.put("usersId", "(usersId)");
        convertMap.put("-usersId", "-(usersId)");

        convertMap.put("convertToDonation", "(convertToDonation)");
        convertMap.put("-convertToDonation", "-(convertToDonation)");

        convertMap.put("category", "(category)");
        convertMap.put("-category", "-(category)");

        convertMap.put("expiryDate", "(expiryDate)");
        convertMap.put("-expiryDate", "-(expiryDate)");

        convertMap.put("storageLocation", "(storageLocation)");
        convertMap.put("-storageLocation", "-(storageLocation)");
    }
}
