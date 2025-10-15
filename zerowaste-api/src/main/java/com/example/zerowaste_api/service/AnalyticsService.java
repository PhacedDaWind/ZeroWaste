package com.example.zerowaste_api.service;

import com.example.zerowaste_api.entity.FoodItem;
import com.example.zerowaste_api.enums.FoodItemActionType;
import com.example.zerowaste_api.repository.FoodItemRepository;
import com.example.zerowaste_api.repository.WeeklyMealPlanRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Extended analytics to provide weekly/monthly aggregations and by-category breakdowns,
 * plus food analysis metrics (total saved from waste, number of donations, progress indicators).
 */
@Service
public class AnalyticsService {

  private final FoodItemRepository foodItemRepository;
  private final WeeklyMealPlanRepository weeklyMealPlanRepository;

  public AnalyticsService(FoodItemRepository foodItemRepository,
                          WeeklyMealPlanRepository weeklyMealPlanRepository) {
    this.foodItemRepository = foodItemRepository;
    this.weeklyMealPlanRepository = weeklyMealPlanRepository;
  }

  public Map<String, Object> basicFoodAnalytics(Long userId) {
    List<FoodItem> all = foodItemRepository.findAll();
    long totalItems = all.stream().filter(f -> f.getUser()!=null && f.getUser().getId().equals(userId)).count();
    long totalDonations = all.stream().filter(f -> f.getUser()!=null && f.getUser().getId().equals(userId) && Boolean.TRUE.equals(f.getConvertToDonation())).count();
    long nearExpiry = all.stream().filter(f -> f.getUser()!=null && f.getUser().getId().equals(userId) && f.getExpiryDate()!=null && f.getExpiryDate().isBefore(LocalDate.now().plusDays(3))).count();
    Map<String,Object> res = Map.of(
        "totalItems", totalItems,
        "totalDonations", totalDonations,
        "nearExpiry", nearExpiry
    );
    return res;
  }

  public Map<String,Object> aggregate(Long userId, String period, boolean byCategory, LocalDate from, LocalDate to) {
    List<FoodItem> all = foodItemRepository.findAll().stream()
        .filter(f -> f.getUser()!=null && f.getUser().getId().equals(userId))
        .collect(Collectors.toList());

    // filter by date range if provided (createdAt in BaseDomain is a java.util.Date)
    List<FoodItem> inRange = all.stream().filter(f -> {
      if (from==null && to==null) return true;
      Date created = f.getCreatedAt();
      if (created==null) return true;
      LocalDate createdDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      if (from!=null && createdDate.isBefore(from)) return false;
      if (to!=null && createdDate.isAfter(to)) return false;
      return true;
    }).collect(Collectors.toList());

    Map<String,Object> result = new LinkedHashMap<>();

    if (byCategory) {
      Map<String, Long> byCat = inRange.stream()
          .collect(Collectors.groupingBy(f -> f.getCategory()==null?"Unknown":f.getCategory(), Collectors.counting()));
      result.put("byCategory", byCat);
    }

    // time-based aggregation
    DateTimeFormatter weekFmt = DateTimeFormatter.ofPattern("YYYY-'W'ww"); // week-year-week format
    DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("yyyy-MM");

    Map<String, Long> timeAgg = new TreeMap<>();
    for (FoodItem f : inRange) {
      Date created = f.getCreatedAt();
      if (created==null) continue;
      LocalDate createdDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String key = period.equalsIgnoreCase("monthly") ? createdDate.format(monthFmt) : createdDate.format(weekFmt);
      timeAgg.put(key, timeAgg.getOrDefault(key, 0L) + 1);
    }
    result.put("timeAggregation", timeAgg);

    // donations over time
    Map<String, Long> donationsTime = new TreeMap<>();
    for (FoodItem f : inRange) {
      if (!Boolean.TRUE.equals(f.getConvertToDonation())) continue;
      Date created = f.getCreatedAt();
      if (created==null) continue;
      LocalDate createdDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String key = period.equalsIgnoreCase("monthly") ? createdDate.format(monthFmt) : createdDate.format(weekFmt);
      donationsTime.put(key, donationsTime.getOrDefault(key, 0L) + 1);
    }
    result.put("donationsTime", donationsTime);

    return result;
  }

  /**
   * Food analysis endpoint: computes total saved from waste, number of donations,
   * time series and category breakdowns. Criteria derived from project doc:
   * - totalSaved: count of items marked as 'MARK_AS_USED' (or equivalent) before expiry
   * - totalDonations: items with convertToDonation == true
   * - progressIndicator: percentage saved out of total handled items (saved + donated + wasted)
   *
   * Returns a map containing summary metrics, time series and optional category breakdown.
   */
  public Map<String,Object> foodAnalysis(Long userId, String period, boolean byCategory, LocalDate from, LocalDate to) {
    List<FoodItem> all = foodItemRepository.findAll().stream()
        .filter(f -> f.getUser()!=null && f.getUser().getId().equals(userId))
        .collect(Collectors.toList());

    // apply date filtering based on createdAt if provided
    List<FoodItem> inRange = all.stream().filter(f -> {
      if (from==null && to==null) return true;
      Date created = f.getCreatedAt();
      if (created==null) return true;
      LocalDate createdDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      if (from!=null && createdDate.isBefore(from)) return false;
      if (to!=null && createdDate.isAfter(to)) return false;
      return true;
    }).collect(Collectors.toList());

    long totalDonations = inRange.stream().filter(f -> Boolean.TRUE.equals(f.getConvertToDonation())).count();

    // totalSaved: items marked as used before expiry (actionType == MARK_AS_USED)
    long totalSaved = inRange.stream().filter(f -> {
      try {
        return f.getActionType() == FoodItemActionType.MARK_AS_USED && (f.getExpiryDate()==null || !f.getExpiryDate().isBefore(LocalDate.now()));
      } catch (Exception e) { return false; }
    }).count();

    // totalWasted: items that expired and were not used nor donated
    long totalWasted = inRange.stream().filter(f -> {
      try {
        boolean expired = f.getExpiryDate()!=null && f.getExpiryDate().isBefore(LocalDate.now());
        boolean donated = Boolean.TRUE.equals(f.getConvertToDonation());
        boolean used = f.getActionType() == FoodItemActionType.MARK_AS_USED;
        return expired && !donated && !used;
      } catch (Exception e) { return false; }
    }).count();

    long totalHandled = totalSaved + totalDonations + totalWasted;
    double progress = totalHandled == 0 ? 0.0 : ((double) totalSaved / (double) totalHandled) * 100.0;

    // time series aggregation (saved & donations)
    DateTimeFormatter weekFmt = DateTimeFormatter.ofPattern("YYYY-'W'ww");
    DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("yyyy-MM");

    Map<String, Long> savedTime = new TreeMap<>();
    Map<String, Long> donationsTime = new TreeMap<>();

    for (FoodItem f : inRange) {
      Date created = f.getCreatedAt();
      if (created==null) continue;
      LocalDate createdDate = created.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      String key = period.equalsIgnoreCase("monthly") ? createdDate.format(monthFmt) : createdDate.format(weekFmt);
      if (f.getActionType() == FoodItemActionType.MARK_AS_USED) {
        savedTime.put(key, savedTime.getOrDefault(key, 0L) + 1);
      }
      if (Boolean.TRUE.equals(f.getConvertToDonation())) {
        donationsTime.put(key, donationsTime.getOrDefault(key, 0L) + 1);
      }
    }

    Map<String,Object> result = new LinkedHashMap<>();
    result.put("totalSaved", totalSaved);
    result.put("totalDonations", totalDonations);
    result.put("totalWasted", totalWasted);
    result.put("progressSavedPercentage", progress);
    result.put("savedTime", savedTime);
    result.put("donationsTime", donationsTime);

    if (byCategory) {
      Map<String, Long> savedByCat = inRange.stream()
          .filter(f -> f.getActionType() == FoodItemActionType.MARK_AS_USED)
          .collect(Collectors.groupingBy(f -> f.getCategory()==null?"Unknown":f.getCategory(), Collectors.counting()));
      Map<String, Long> donationsByCat = inRange.stream()
          .filter(f -> Boolean.TRUE.equals(f.getConvertToDonation()))
          .collect(Collectors.groupingBy(f -> f.getCategory()==null?"Unknown":f.getCategory(), Collectors.counting()));
      result.put("savedByCategory", savedByCat);
      result.put("donationsByCategory", donationsByCat);
    }

    return result;
  }
}
