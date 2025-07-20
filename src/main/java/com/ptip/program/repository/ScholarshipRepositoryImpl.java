package com.ptip.program.repository;

import com.ptip.program.entity.Scholarship;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ptip.program.entity.QScholarship.scholarship;

public class ScholarshipRepositoryImpl implements ScholarshipRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ScholarshipRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Scholarship> getScholarships(String keyword, String amount, String status, int limit, Pageable pageable) {
        JPAQuery<Scholarship> scholarshipQuery = jpaQueryFactory
                .selectFrom(scholarship)
                .where(containsKeyword(keyword), amountCondition(amount), statusCondition(status, limit))
                .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset()) // 페이지 시작 위치
                .limit(pageable.getPageSize()); // 페이지 크기

        List<Scholarship> scholarships = scholarshipQuery.fetch();

        JPAQuery<Long> scholarshipCountQuery = jpaQueryFactory
                .select(scholarship.count())
                .from(scholarship)
                .where(containsKeyword(keyword), amountCondition(amount), statusCondition(status, limit));

        return PageableExecutionUtils.getPage(scholarships, pageable, scholarshipCountQuery::fetchOne);
    }

    private BooleanExpression containsKeyword(String query) {
        return StringUtils.hasText(query) ? scholarship.title.contains(query).or(scholarship.description.contains(query)) : null;
    }

    private BooleanExpression amountCondition(String amount) {
        if (amount == null) return null;

        return switch (amount) {
            case "5미만" -> scholarship.maxAmount.lt(50_000);
            case "5~10" -> scholarship.maxAmount.between(50_000, 100_000);
            case "10이상" -> scholarship.maxAmount.gt(100_000);
            default -> throw new IllegalArgumentException("지원하지 않는 조건 필드입니다. " + amount);
        };
    }

    private BooleanExpression statusCondition(String status, int limit) {
        LocalDate now = LocalDate.now();

        if (status == null) return null;
        return switch (status) {
            case "진행중" -> scholarship.deadline.goe(now);
            case "마감임박" -> scholarship.deadline.between(now, now.plusDays(limit != 0 ? limit : 4));
            case "마감" -> scholarship.deadline.lt(now);
            default -> throw new IllegalArgumentException("지원하지 않는 조건 필드입니다. " + status);
        };
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            switch (order.getProperty()) {
                case "금액순" -> orders.add(scholarship.maxAmount.asc());
                case "인기순" -> orders.add(scholarship.popularity.desc());
                case "마감일순" -> {
                    LocalDate now = LocalDate.now();
                    orders.add(new CaseBuilder()
                            .when(scholarship.deadline.goe(now)).then(0)
                            .when(scholarship.deadline.isNull()).then(1)
                            .otherwise(2)
                            .asc());
                    orders.add(scholarship.deadline.asc());
                }
                default -> throw new IllegalArgumentException("지원하지 않는 정렬 필드입니다. " + order.getProperty());
            }
        }
        return orders;
    }
}
