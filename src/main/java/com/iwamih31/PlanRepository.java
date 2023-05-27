package com.iwamih31;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {

	/**	ユーザープラン取得（対象月 start_time 昇順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.user_id = :user_id"
			+ " and plan.start_date = :localDate"
			+ " order by plan.start_time asc")
	public List<Plan> plan_List(
			@Param("user_id") int user_id,
			@Param("localDate") LocalDate localDate
			);

	/**	ユーザープラン取得（day_of_week 指定 start_time 昇順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.user_id = :user_id"
			+ " and plan.start_date = :localDate"
			+ " and plan.day_of_week Like %:day_of_week%"
			+ " order by plan.start_time asc")
	public List<Plan> get_Plan_List(
			@Param("user_id") int user_id,
			@Param("localDate") LocalDate localDate,
			@Param("day_of_week") String day_of_week
			);

	/**	start_date が localDate の個人プラン取得（start_time 昇順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.user_id = :user_id"
			+ " and plan.start_date = :localDate"
			+ " order by plan.start_time asc")
	public List<Plan> get_plan_List(
			@Param("user_id") int user_id,
			@Param("localDate") LocalDate localDate
			);

	/**	ユーザープラン取得（start_date 降順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.user_id = :user_id"
			+ " order by plan.start_date desc")
	public List<Plan> last_Plan_List(
			@Param("user_id") int user_id
			);

	/**	start_date が localDate 以前のユーザープラン取得（start_date 降順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.user_id = :user_id"
			+ " and plan.start_date < :localDate"
			+ " order by plan.start_date desc")
	public List<Plan> before_Plan_List(
			@Param("user_id") int user_id,
			@Param("localDate") LocalDate localDate
			);

	/**	start_date が localDate 以降のユーザープラン取得（start_date 昇順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.user_id = :user_id"
			+ " and plan.start_date > :localDate"
			+ " order by plan.start_date asc")
	public List<Plan> after_Plan_List(
			@Param("user_id") int user_id,
			@Param("localDate") LocalDate localDate
			);

	/**	全ユーザープラン取得（id 降順） */
	@Query("select plan"
			+ " from Plan plan"
			+ " order by plan.id desc")
	public List<Plan> desc_id_All();

	/**	id が id のユーザープラン取得 */
	@Query("select plan"
			+ " from Plan plan"
			+ " where plan.id = :id")
	public List<Plan> get_Plan(
			@Param("id") int id
			);

}
