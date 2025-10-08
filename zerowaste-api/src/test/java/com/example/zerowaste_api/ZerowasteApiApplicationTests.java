package com.example.zerowaste_api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.controller.FoodInventoryController;
import com.example.zerowaste_api.dto.FoodItemReqDTO;
import com.example.zerowaste_api.dto.FoodItemResDTO;
import com.example.zerowaste_api.service.FoodInventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(FoodInventoryController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {FoodInventoryController.class})
class FoodInventoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private FoodInventoryService foodInventoryService;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void testCreateFoodItem() throws Exception {
    FoodItemReqDTO reqDTO = new FoodItemReqDTO();
    reqDTO.setName("Apple");
    reqDTO.setQuantity(BigDecimal.valueOf(10));
    reqDTO.setExpiryDate(LocalDate.of(2025, 12, 31));

    FoodItemResDTO resDTO = new FoodItemResDTO();
    resDTO.setId(1L);
    resDTO.setName("Apple");
    resDTO.setQuantity(BigDecimal.valueOf(10));
    resDTO.setExpiryDate(LocalDate.of(2025, 12, 31));

    when(foodInventoryService.create(any(FoodItemReqDTO.class))).thenReturn(resDTO);

    // Act
    MvcResult result =
        mockMvc
            .perform(
                post("/api/food-inventory/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDTO)))
            .andReturn();

    // Deserialize response
    String responseJson = result.getResponse().getContentAsString();
    ResponseDTO<FoodItemResDTO> response =
        objectMapper.readValue(
            responseJson,
            objectMapper
                .getTypeFactory()
                .constructParametricType(ResponseDTO.class, FoodItemResDTO.class));

    // Assert with assertEquals
    assertEquals(200, result.getResponse().getStatus());
    assertEquals("Apple", response.getData().getName());
    assertEquals(BigDecimal.valueOf(10), response.getData().getQuantity());
    assertEquals(LocalDate.of(2025, 12, 31), response.getData().getExpiryDate());
    assertEquals(1L, response.getData().getId());
  }
}
