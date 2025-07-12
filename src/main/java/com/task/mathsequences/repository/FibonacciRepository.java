package com.task.mathsequences.repository;

import com.task.mathsequences.entity.Fibonacci;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FibonacciRepository extends JpaRepository<Fibonacci, Long> {

    List<Fibonacci> findByUserUsernameAndEnabledTrueOrderByRequestTimeDesc(String username);

}
