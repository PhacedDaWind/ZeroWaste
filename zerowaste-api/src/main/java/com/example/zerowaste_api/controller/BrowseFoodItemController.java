package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.PageWrapperVO;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.dto.BrowseFoodItemReqDTO;
import com.example.zerowaste_api.dto.BrowseFoodItemResDTO;
import com.example.zerowaste_api.service.BrowseFoodItemService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("api/browse-food")
public class BrowseFoodItemController extends BaseController {

    private final BrowseFoodItemService browseFoodItemService;

    public BrowseFoodItemController(BrowseFoodItemService browseFoodItemService) {
        this.browseFoodItemService = browseFoodItemService;
    }

    @GetMapping("/list")
    public ResponseDTO<PageWrapperVO<BrowseFoodItemResDTO>> showList(@RequestBody(required = false) BrowseFoodItemReqDTO browseFoodItemReqDTO) {
        return createResponse(HttpStatus.OK, browseFoodItemService.getBrowseList(browseFoodItemReqDTO));
    }
}
