package com.example.exemserver.repositoty;

import com.example.exemserver.dto.AlertRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<AlertRecord, Long> {
}
