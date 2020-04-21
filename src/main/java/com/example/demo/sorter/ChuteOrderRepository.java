package com.example.demo.sorter;

import com.example.demo.sorter.Model.Chute;
import com.example.demo.sorter.Model.Sorter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuteOrderRepository extends JpaRepository<Sorter.Order, Sorter.OrderId> {
    List<Sorter.Order> findByIdNo(int no);
}
