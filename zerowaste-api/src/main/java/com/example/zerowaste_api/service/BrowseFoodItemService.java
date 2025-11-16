package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.PageWrapperVO;
import com.example.zerowaste_api.common.PaginateService;
import com.example.zerowaste_api.common.error.BrowseErrorConstant;
import com.example.zerowaste_api.converter.BrowseFoodItemConverter;
import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.BrowseFoodItemDAO;
import com.example.zerowaste_api.dao.FoodItemDAO;
import com.example.zerowaste_api.dao.UsersDAO;
import com.example.zerowaste_api.dto.*;

import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.FoodItemActionType;
import com.example.zerowaste_api.enums.NotificationType;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BrowseFoodItemService extends PaginateService {
    private final BrowseFoodItemDAO browseFoodItemDAO;
    private final FoodItemDAO foodItemDAO;
    private final BrowseFoodItemConverter browseFoodItemConverter;
    private final NotificationService notificationService;
    private final UsersDAO usersDAO;


    public BrowseFoodItemService(BrowseFoodItemDAO browseFoodItemDAO, BrowseFoodItemConverter browseFoodItemConverter, FoodItemDAO foodItemDAO, NotificationService notificationService, UsersDAO usersDAO) {
        this.browseFoodItemDAO = browseFoodItemDAO;
        this.browseFoodItemConverter = browseFoodItemConverter;
        this.foodItemDAO = foodItemDAO;
        this.notificationService = notificationService;
        this.usersDAO = usersDAO;
    }

    @Override
    protected Sort getDefaultSort() {
        return Sort.by("id");
    }

    public Page<BrowseFoodItemTuple> toBrowseFoodItemList(BrowseFoodItemReqDTO reqDTO) {
        Sort sort= getSortQueryV2(reqDTO.getConvertedSort(reqDTO.getSort()));
        Pageable pageable=getPageableQuery(reqDTO.getPage(),reqDTO.getPageSize(),sort);
        Long usersId = reqDTO.getUsersId();
        Boolean convertToDonation=reqDTO.getConvertToDonation();
        String itemName = getLikeSearchOrNull(reqDTO.getItemName());
        String category = getLikeSearchOrNull(reqDTO.getCategory());
        LocalDate expiryDate = reqDTO.getExpiryDate();
        String storageLocation = getLikeSearchOrNull(reqDTO.getStorageLocation());
        FoodItemActionType actionType = reqDTO.getActionType();
        return browseFoodItemDAO.getPage(
                pageable,
                usersId,
                convertToDonation,
                itemName,
                category,
                expiryDate,
                storageLocation,
                actionType);
    }


    public PageWrapperVO<BrowseFoodItemResDTO> getBrowseList(BrowseFoodItemReqDTO browseFoodItemReqDTO){
        Page<BrowseFoodItemTuple> tuples= toBrowseFoodItemList(browseFoodItemReqDTO);
        List<BrowseFoodItemResDTO> content=browseFoodItemConverter.toBrowseFoodItemResDTO(tuples.getContent());
        return new PageWrapperVO<>(tuples,content);
    }

    public BrowseFoodItemATResDTO chooseActionType(Long Id, Boolean convertToDonation, FoodItemActionType foodItemActionType, Long userId) {
        FoodItem foodItem = browseFoodItemDAO.getFoodItem(Id);
        foodItem.setActionType(foodItemActionType);
        if (foodItem.getUser().getId() != userId) {
            foodItem.setConvertToDonation(false);
            foodItem.setUser(usersDAO.findById(userId));
        }
        browseFoodItemDAO.save(foodItem);
        notificationService.create(NotificationType.DONATION_CLAIMED,
                userId,
                null,
                null,
                null,
                null);

        return browseFoodItemConverter.browseFoodItemATResDTO(foodItem);
    }

}
