package com.meditrack.meditrack_backend.repository;

import com.meditrack.meditrack_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    List<User> findByRole(User.Role role);

    List<User> findByDepartmentId(Integer departmentId);
}