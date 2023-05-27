package com.iwamih31;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/**	利用者情報取得(利用中のみ) */
	@Query("select user"
			+ " from User user"
			+ " where user.use = '利用中'"
			+ " order by user.room asc")
	public List<User> user_List();

	/**	利用者名リスト取得 */
	@Query("select user.name"
			+ " from User user"
			+ " order by user.use asc")
	public String[] names();

}
