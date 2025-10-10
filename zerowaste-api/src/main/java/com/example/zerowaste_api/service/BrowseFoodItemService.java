package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.PageWrapperVO;
import com.example.zerowaste_api.common.PaginateService;
import com.example.zerowaste_api.common.error.BrowseErrorConstant;
import com.example.zerowaste_api.converter.BrowseFoodItemConverter;
import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.BrowseFoodItemDAO;
import com.example.zerowaste_api.dao.FoodItemDAO;
import com.example.zerowaste_api.dto.*;

import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.FoodItemActionType;
import org.hibernate.service.spi.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BrowseFoodItemService extends PaginateService {
    private final BrowseFoodItemDAO browseFoodItemDAO;
    private final FoodItemDAO foodItemDAO;
    private final BrowseFoodItemConverter browseFoodItemConverter;

    public BrowseFoodItemService(BrowseFoodItemDAO browseFoodItemDAO, BrowseFoodItemConverter browseFoodItemConverter, FoodItemDAO foodItemDAO) {
        this.browseFoodItemDAO = browseFoodItemDAO;
        this.browseFoodItemConverter = browseFoodItemConverter;
        this.foodItemDAO = foodItemDAO;
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
        String category = getLikeSearchOrNull(reqDTO.getCategory());
        LocalDate expiryDate = reqDTO.getExpiryDate();
        String storageLocation = getLikeSearchOrNull(reqDTO.getStorageLocation());
        return browseFoodItemDAO.getPage(
                pageable,
                usersId,
                convertToDonation,
                category,
                expiryDate,
                storageLocation);
    }


    public PageWrapperVO<BrowseFoodItemResDTO> getBrowseList(BrowseFoodItemReqDTO browseFoodItemReqDTO){
        Page<BrowseFoodItemTuple> tuples= toBrowseFoodItemList(browseFoodItemReqDTO);
        List<BrowseFoodItemResDTO> content=browseFoodItemConverter.toBrowseFoodItemResDTO(tuples.getContent());
        return new PageWrapperVO<>(tuples,content);
    }

    public BrowseFoodItemATResDTO chooseActionType(Long Id, FoodItemActionType foodItemActionType){
        FoodItem foodItem=browseFoodItemDAO.getFoodItem(Id);
        foodItem.setActionType(foodItemActionType);
        browseFoodItemDAO.save(foodItem);
        return browseFoodItemConverter.browseFoodItemATResDTO(foodItem);

    }

}
