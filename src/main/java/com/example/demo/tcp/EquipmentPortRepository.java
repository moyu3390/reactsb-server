package com.example.demo.tcp;

import com.example.demo.tcp.model.EquipmentPort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipmentPortRepository extends JpaRepository<EquipmentPort, String> {
}
