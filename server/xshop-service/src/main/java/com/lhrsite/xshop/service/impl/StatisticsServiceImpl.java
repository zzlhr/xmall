package com.lhrsite.xshop.service.impl;


import com.lhrsite.xshop.service.StatisticsService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class StatisticsServiceImpl extends BaseServiceImpl implements StatisticsService {

    private JPAQueryFactory queryFactory;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatisticsServiceImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        super(entityManager);
        this.jdbcTemplate = jdbcTemplate;
        queryFactory = getQueryFactory();
    }

    @Override
    public List orderNumberByYear(int year) {

        String sql = "select ";

        return null;
    }

    @Override
    public List orderNumberByMonth(int month) {
        return null;
    }

    @Override
    public List turnoverByYear(int year) {
        return null;
    }

    @Override
    public List turnoverByMonth(int month) {
        return null;
    }

    @Override
    public List goodsSalesVolumeByYear(int year) {
        return null;
    }

    @Override
    public List goodsSalesVolumeByMonth(int month) {
        return null;
    }
}
