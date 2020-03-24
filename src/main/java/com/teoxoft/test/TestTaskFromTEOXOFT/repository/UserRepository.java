package com.teoxoft.test.TestTaskFromTEOXOFT.repository;

import com.teoxoft.test.TestTaskFromTEOXOFT.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
