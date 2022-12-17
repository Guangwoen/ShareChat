package sharechat.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    @Query(value="select password from userinfo where Id = :Id",nativeQuery = true)
    String findPasswordById(@Param("Id") String Id);

}
