package com.example.demo.sorter;

import com.example.demo.sorter.Model.Chute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuteMasterRepository extends JpaRepository<Chute.Master, Integer> {
}
