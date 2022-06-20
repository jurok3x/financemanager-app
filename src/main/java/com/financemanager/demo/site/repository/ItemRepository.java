package com.financemanager.demo.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.financemanager.demo.site.entity.Item;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	
	public static final String OFFSET_PARAM = "offset";
    public static final String LIMIT_PARAM = "limit";
    public static final String YEAR_PARAM = "year";
    public static final String MONTH_PARAM = "month";
    public static final String CATEGORY_ID_PARAM = "categoryId";
    public static final String USER_ID_PARAM = "userId";
    public final String GET_MOST_POPULAR_ITEMS = "SELECT item_name, COUNT(item_name) as value, SUM (price) AS total\n"
			+ "FROM item_table WHERE user_id = :userId AND (category_id = :categoryId OR :categoryId IS NULL)\n"
			+ "AND (EXTRACT(month from date) = :month OR :month IS NULL)\n"
            + "AND (EXTRACT(year from date) = :year OR :year IS NULL)\n"
			+ "GROUP BY item_name ORDER BY value DESC LIMIT :limit\n"
			+ "OFFSET :offset";
    public static final String GET_ALL_ACTIVE_YEARS = "SELECT DISTINCT(EXTRACT (YEAR FROM date))\n"
			+ "FROM item_table WHERE user_id = :userId \n"
			+ "ORDER BY date_part";
    public static final String COUNT_BY_CATEGORY_ID = "SELECT  COUNT(item_name) as value\n"
			+ "FROM item_table WHERE user_id = :userId AND category_id = :categoryId\n"
			+ "AND (EXTRACT(month from date) = :month OR :month IS NULL)\n"
            + "AND (EXTRACT(year from date) = :year OR :year IS NULL)\n";
    public static final String FIND_BY_USER_ID = "SELECT item FROM Item item\n"
			+ "WHERE item.user.id = :userId AND (item.category.id = :categoryId OR :categoryId IS NULL OR :limit=:offset)\n"
			+ "AND (EXTRACT(month from item.date) = :month OR :month IS NULL)\n"
            + "AND (EXTRACT(year from item.date) = :year OR :year IS NULL)\n"
			+ "ORDER BY item.date\n";
    public static final String MONTH_STATISTICS = "WITH months AS (SELECT * FROM generate_series(1, 12) AS t(n))\n"
			+ "SELECT months.n as date_part, COALESCE(SUM(price), 0) AS total\n"
			+ "FROM item_table RIGHT JOIN months ON EXTRACT(MONTH from date) = months.n\n"
			+ "AND user_id = :userId AND (category_id = :categoryId OR :categoryId IS NULL)\n"
			+ "AND (EXTRACT(year from date) = :year OR :year IS NULL)\n"
			+ "GROUP BY months.n ORDER BY months.n";
	
	@Query(value = FIND_BY_USER_ID, nativeQuery = false)
	List<Item> findAllByUserId(
			@Param(USER_ID_PARAM) Integer userId,
			@Nullable @Param(CATEGORY_ID_PARAM) Integer categoryId,
			@Nullable @Param(MONTH_PARAM) Integer month,
			@Nullable @Param(YEAR_PARAM) Integer year,
			@Nullable @Param(LIMIT_PARAM) Integer limit,
			@Nullable @Param(OFFSET_PARAM) Integer offset);
	
	@Query(value = COUNT_BY_CATEGORY_ID,
			nativeQuery = true)
	Integer countByCategoryId(
			@Param(USER_ID_PARAM) Integer userId,
			@Param(CATEGORY_ID_PARAM) Integer categoryId,
			@Param(MONTH_PARAM) Integer month,
            @Param(YEAR_PARAM) Integer year);

	@Query(value = GET_MOST_POPULAR_ITEMS, nativeQuery = true)
	List<ProjectNameAndCountAndCost> getMostPopularItems(
			@Param(USER_ID_PARAM) Integer userId,
			@Param(CATEGORY_ID_PARAM) Integer categoryId,
			@Param(MONTH_PARAM) Integer month,
            @Param(YEAR_PARAM) Integer year,
			@Param(LIMIT_PARAM) Integer limit,
			@Param(OFFSET_PARAM) Integer offset);
	
	@Query(value = GET_ALL_ACTIVE_YEARS, nativeQuery = true)
	List<Integer> getAllActiveYears(@Param(USER_ID_PARAM) Integer userId);

	@Query(value = MONTH_STATISTICS, nativeQuery = true)
	List<DatePartAndCost> getMonthStatistics(
			@Param(USER_ID_PARAM) Integer userId,
			@Param(YEAR_PARAM) Integer year,
			@Param(CATEGORY_ID_PARAM) Integer categoryId);
}