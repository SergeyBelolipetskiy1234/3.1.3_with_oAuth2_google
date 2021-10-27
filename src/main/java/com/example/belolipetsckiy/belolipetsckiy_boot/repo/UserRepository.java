package com.example.belolipetsckiy.belolipetsckiy_boot.repo;

import com.example.belolipetsckiy.belolipetsckiy_boot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
