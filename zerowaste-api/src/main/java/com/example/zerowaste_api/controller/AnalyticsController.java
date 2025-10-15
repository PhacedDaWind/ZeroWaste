package com.example.zerowaste_api.controller;

import com.example.zerowaste_api.common.BaseController;
import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController extends BaseController {

  private final AnalyticsService analyticsService;

  public AnalyticsController(AnalyticsService analyticsService) {
    this.analyticsService = analyticsService;
  }

  @GetMapping("/basic")
  public ResponseDTO<Map<String,Object>> basic(@RequestParam Long userId) {
    return createResponse(HttpStatus.OK, analyticsService.basicFoodAnalytics(userId));
  }

  @GetMapping("/aggregate")
  public ResponseDTO<Map<String,Object>> aggregate(
      @RequestParam Long userId,
      @RequestParam(required = false, defaultValue = "weekly") String period,
      @RequestParam(required = false, defaultValue = "false") boolean byCategory,
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to
  ) {
    LocalDate fromD = from==null?null:LocalDate.parse(from);
    LocalDate toD = to==null?null:LocalDate.parse(to);
    return createResponse(HttpStatus.OK, analyticsService.aggregate(userId, period, byCategory, fromD, toD));
  }

  
  @GetMapping("/food-analysis")
  public ResponseDTO<Map<String,Object>> foodAnalysis(
      @RequestParam Long userId,
      @RequestParam(required = false, defaultValue = "weekly") String period,
      @RequestParam(required = false, defaultValue = "false") boolean byCategory,
      @RequestParam(required = false) String from,
      @RequestParam(required = false) String to
  ) {
    LocalDate fromD = from==null?null:LocalDate.parse(from);
    LocalDate toD = to==null?null:LocalDate.parse(to);
    return createResponse(HttpStatus.OK, analyticsService.foodAnalysis(userId, period, byCategory, fromD, toD));
  }
}
