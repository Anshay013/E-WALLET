package com.wallet.walletservice.Repository;

import com.wallet.walletservice.Model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("SELECT t from Transaction t WHERE t.sid = ?1 OR t.rid = ?1")
    List<Transaction> findBySid(int sid);
    // getting all the list of transaction by passing either sid or rid.

    @Query("SELECT t from Transaction t WHERE t.id = ?1 ")
    Transaction findTransactionById(int id);
    // finding a specific transaction by id.



}

