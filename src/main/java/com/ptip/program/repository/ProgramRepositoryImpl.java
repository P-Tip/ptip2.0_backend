package com.ptip.program.repository;

import com.ptip.program.entity.Program;
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

import static com.ptip.program.entity.QProgram.program;

public class ProgramRepositoryImpl implements ProgramRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public ProgramRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Program> getPrograms(String keyword, List<String> categories, List<String> modes, List<String> tags, Pageable pageable) {
        JPAQuery<Program> programQuery = jpaQueryFactory
                .selectFrom(program)
                .where(containsKeyword(keyword), categoryIn(categories), modeIn(modes), tagIn(tags))
                .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Program> programs = programQuery.fetch();

        JPAQuery<Long> programCountQuery = jpaQueryFactory
                .select(program.count())
                .from(program)
                .where(containsKeyword(keyword), categoryIn(categories), modeIn(modes), tagIn(tags));

        return PageableExecutionUtils.getPage(programs, pageable, programCountQuery::fetchOne);
    }

    private BooleanExpression containsKeyword(String keyword) {
        return StringUtils.hasText(keyword) ? program.title.contains(keyword).or(program.description.contains(keyword)) : null;
    }

    private BooleanExpression categoryIn(List<String> categories) {
        if (categories == null || categories.isEmpty()) return null;
        return program.category.in(categories);
    }

    private BooleanExpression modeIn(List<String> modes) {
        if (modes == null || modes.isEmpty()) return null;
        return program.mode.in(modes);
    }

    private BooleanExpression tagIn(List<String> tags) {
        if (tags == null || tags.isEmpty()) return null;

        BooleanExpression predicate = null;
        for (String tag : tags) {
            BooleanExpression expr = program.tags.contains(tag);
            predicate = (predicate == null) ? expr : predicate.or(expr);
        }
        return predicate;
    }

    private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            switch (order.getProperty()) {
                case "최신순" -> orders.add(program.applicationEnd.desc());
                case "인기순" -> orders.add(program.popularity.desc());
                case "마감임박순" -> {
                    LocalDate now = LocalDate.now();
                    orders.add(new CaseBuilder()
                            .when(program.applicationEnd.goe(now)).then(0)
                            .when(program.applicationEnd.isNull()).then(1)
                            .otherwise(2)
                            .asc());
                    orders.add(program.applicationEnd.asc());
                }
                default -> throw new IllegalArgumentException("지원하지 않는 정렬 필드입니다. " + order.getProperty());
            }
        }
        return orders;
    }
}
