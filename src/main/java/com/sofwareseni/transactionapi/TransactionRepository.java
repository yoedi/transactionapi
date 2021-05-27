package com.sofwareseni.transactionapi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> getTransactionsByType(String type);

    @Query("select e from Transaction e where e.id =:id or e.parentId =:id")
    List<Transaction> getTransactionsByParentId(@Param("id") long id);

}
