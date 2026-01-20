package com.duybao.QUANLYCHITIEU.Repository;

import com.duybao.QUANLYCHITIEU.Model.Category;
import com.duybao.QUANLYCHITIEU.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String Username);
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String Email);
    List<User> findTop10ByOrderByCreatedAtDesc();
    // CategoryRepository
//    Optional<Category> findByNameIgnoreCase(String name);

    // UserRepository
    @Query("select case when count(u)>0 then true else false end from User u join u.categories c where u.id = :userId and c.id = :categoryId")
    boolean existsByIdAndCategoryId(@Param("userId") Long userId, @Param("categoryId") Long categoryId);
    @Query("select Count(u)from User u where u.updatedAt between :start and :end")
    Long countByDate(LocalDateTime start,LocalDateTime end);
}
