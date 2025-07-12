package com.task.mathsequences.repository;

import com.task.mathsequences.entity.Factorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactorialRepository extends JpaRepository<Factorial, Long> {

    List<Factorial> findByUserUsernameAndEnabledTrueOrderByRequestTimeDesc(String username);

}
