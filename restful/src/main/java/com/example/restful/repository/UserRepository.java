package com.example.restful.repository;

import com.example.restful.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: shudj
 * @time: 2019/2/15 16:29
 * @description:
 */
public interface UserRepository extends JpaRepository<User, String> {
}
