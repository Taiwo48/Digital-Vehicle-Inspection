package com.example.vehicleinspection.repository;

import com.example.vehicleinspection.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);
    Optional<Admin> findByEmail(String email);
    List<Admin> findByDepartment(String department);
    List<Admin> findByRole(String role);
    List<Admin> findByCanManageClientsTrue();
    List<Admin> findByCanManageOfficersTrue();
    List<Admin> findByCanViewAnalyticsTrue();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}