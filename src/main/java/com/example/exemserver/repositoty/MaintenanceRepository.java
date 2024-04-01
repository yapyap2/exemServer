package com.example.exemserver.repositoty;


import com.example.exemserver.dto.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
