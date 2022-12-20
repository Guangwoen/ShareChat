package sharechat.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.UserInfo;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String> {
    @Query(value="select password from userinfo where Id = :Id",nativeQuery = true)
    String findPasswordById(@Param("Id") String Id);

    @Transactional
    @Modifying
    @Query(value = "update userinfo set name = :name, workplace = :workplace, region = :region, age = :age, gender = :gender, signature = :signature, headPicture = :headPicture where Id = :Id",nativeQuery = true)
    int updateInfo(@Param("Id") String Id,
                   @Param("name") String name,
                   @Param("workplace") String workplace,
                   @Param("region") String region,
                   @Param("age") int age,
                   @Param("gender") String gender,
                   @Param("signature") String signature,
                   @Param("headPicture") String headPicture);

    @Transactional
    @Modifying
    @Query(value = "update userinfo set password = :password where Id = :Id",nativeQuery = true)
    int updatePassword(@Param("Id") String Id,@Param("password") String password);

    @Transactional
    @Modifying
    @Query(value = "update userinfo set online = true where Id = :Id",nativeQuery = true)
    int goOnline(@Param("Id") String Id);

    @Transactional
    @Modifying
    @Query(value="update userinfo set online = false where Id = :Id",nativeQuery = true)
    int noOnline(@Param("Id") String Id);
}
