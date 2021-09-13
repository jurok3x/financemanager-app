package com.financemanager.demo.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financemanager.demo.site.entity.Item;
import com.financemanager.demo.site.entity.projects.DatePartAndCost;
import com.financemanager.demo.site.entity.projects.ProjectNameAndCountAndCost;

public interface ItemRepository extends JpaRepository<Item, Integer> {
	
	List<Item> findByUserId(int userId);

	List<Item> findByUserIdAndCategoryId(int userId, int categoryId);

	@Query(value = "SELECT * FROM item_table\n"
			+ "WHERE user_id = :userId\n"
			+ "AND CAST (date as text) LIKE :dateString\n"
			+ "ORDER BY item_id OFFSET :offset ROWS\n"
			+ "FETCH FIRST :limit ROW ONLY", nativeQuery = true)
	List<Item> findByUserIdAndDate(
			@Param("userId") int userId,
			@Param("dateString") String dateString,
			@Param("limit") int limit,
			@Param("offset") int offset);
	
	@Query(value = "SELECT * FROM item_table\n"
			+ "WHERE user_id = :userId\n"
			+ "AND CAST (date as text) LIKE :dateString\n"
			+ "ORDER BY item_id", nativeQuery = true)
	List<Item> findByUserIdAndDateAll(
			@Param("userId") int userId,
			@Param("dateString") String dateString);
	
	@Query(value = "SELECT * FROM item_table\n"
			+ "WHERE user_id = :userId and category_id = :categoryId\n"
			+ "AND CAST (date as text) LIKE :dateString\n"
			+ "ORDER BY item_id OFFSET :offset ROWS\n"
			+ "FETCH FIRST :limit ROW ONLY", nativeQuery = true)
	List<Item> findByUserIdAndCategoryIdAndDate(
			@Param("userId") int userId,
			@Param("categoryId") int categoryId,
			@Param("dateString") String dateString,
			@Param("limit") int limit,
			@Param("offset") int offset);
	
	@Query(value = "SELECT  COUNT(item_name) as value\n"
			+ "FROM item_table WHERE user_id = :userId AND category_id = :categoryId\n"
			+ "AND CAST (date as text) LIKE :dateString",
			nativeQuery = true)
	Integer countByUserIdAndCategoryIdAndDate(
			@Param("userId") int userId,
			@Param("categoryId") int categoryId,
			@Param("dateString") String dateString);

	@Query(value = "SELECT item_name, COUNT(item_name) as value, SUM (price) AS total\n"
			+ "FROM item_table WHERE user_id = :userId\n"
			+ "AND CAST (date as text) LIKE :dateString\n"
			+ "GROUP BY item_name ORDER BY value DESC OFFSET :offset ROWS\n"
			+ "FETCH FIRST :limit ROW ONLY", nativeQuery = true)
	List<ProjectNameAndCountAndCost> getMostFrequentItemsByDate(
			@Param("userId") int userId,
			@Param("dateString") String dateString,
			@Param("limit") int limit,
			@Param("offset") int offset);

	@Query(value = "SELECT item_name, COUNT(item_name) as value, SUM (price) AS total\n"
			+ "FROM item_table WHERE user_id = :userId AND category_id = :categoryId\n"
			+ "AND CAST (date as text) LIKE :dateString\n"
			+ "GROUP BY item_name ORDER BY value DESC OFFSET :offset ROWS\n"
			+ "FETCH FIRST :limit ROW ONLY", nativeQuery = true)
	List<ProjectNameAndCountAndCost> getMostFrequentItemsByCategoryAndDate(
			@Param("userId") int userId,
			@Param("categoryId") int categoryId,
			@Param("dateString") String dateString,
			@Param("limit") int limit,
			@Param("offset") int offset);
	
	@Query(value = "SELECT DISTINCT(EXTRACT (YEAR FROM date))\n"
			+ "FROM item_table WHERE user_id = :userId \n"
			+ "ORDER BY date_part", nativeQuery = true)
	List<Integer> getAllYears(@Param("userId") int userId);
	
	@Query(value = "WITH months AS (SELECT * FROM generate_series(1, 12) AS t(n))\n"
			+ "SELECT months.n as date_part, COALESCE(SUM(price), 0) AS total\n"
			+ "FROM item_table RIGHT JOIN months ON EXTRACT(MONTH from date) = months.n\n"
			+ "AND user_id = :userId AND CAST (date as text) LIKE :dateString\n"
			+ "GROUP BY months.n ORDER BY months.n", nativeQuery = true)
	List<DatePartAndCost> getMonthStatistics(
			@Param("userId") int userId,
			@Param("dateString") String dateString);
	
	@Query(value = "WITH months AS (SELECT * FROM generate_series(1, 12) AS t(n))\n"
			+ "SELECT months.n as date_part, COALESCE(SUM(price), 0) AS total\n"
			+ "FROM item_table RIGHT JOIN months ON EXTRACT(MONTH from date) = months.n\n"
			+ "AND user_id = :userId AND category_id = :categoryId\n"
			+ "AND CAST (date as text) LIKE :dateString\n"
			+ "GROUP BY months.n ORDER BY months.n", nativeQuery = true)
	List<DatePartAndCost> getMonthStatisticsByCategory(
			@Param("userId") int userId,
			@Param("dateString") String dateString,
			@Param("categoryId") int categoryId);
}