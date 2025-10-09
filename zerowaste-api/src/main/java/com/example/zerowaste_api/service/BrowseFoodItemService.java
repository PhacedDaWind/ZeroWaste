package com.example.zerowaste_api.service;

import com.example.zerowaste_api.common.PageWrapperVO;
import com.example.zerowaste_api.common.PaginateService;
import com.example.zerowaste_api.converter.BrowseFoodItemConverter;
import com.example.zerowaste_api.converter.FoodItemConverter;
import com.example.zerowaste_api.dao.BrowseFoodItemDAO;
import com.example.zerowaste_api.dto.BrowseFoodItemReqDTO;
import com.example.zerowaste_api.dto.BrowseFoodItemResDTO;

import com.example.zerowaste_api.dto.BrowseFoodItemTuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public abstract class   BrowseFoodItemService extends PaginateService {
    private final BrowseFoodItemDAO browseFoodItemDAO;

    private final BrowseFoodItemConverter browseFoodItemConverter;

    public BrowseFoodItemService(BrowseFoodItemDAO browseFoodItemDAO, BrowseFoodItemConverter browseFoodItemConverter) {
        this.browseFoodItemDAO = browseFoodItemDAO;
        this.browseFoodItemConverter = browseFoodItemConverter;
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
}
