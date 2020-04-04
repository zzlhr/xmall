package com.lhrsite.xshop.service;

import java.util.List;

/**
 * 统计服务
 */
public interface StatisticsService {

    List orderNumberByYear(int year);

    List orderNumberByMonth(int month);

    List turnoverByYear(int year);

    List turnoverByMonth(int month);

    List goodsSalesVolumeByYear(int year);

    List goodsSalesVolumeByMonth(int month);

}
