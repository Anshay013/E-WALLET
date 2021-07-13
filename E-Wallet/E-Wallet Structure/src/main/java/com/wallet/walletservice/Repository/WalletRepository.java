package com.wallet.walletservice.Repository;

import com.wallet.walletservice.Model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query("SELECT w FROM Wallet w WHERE w.user_id = ?1")
    Wallet findWalletByUserId(int userId); // since it returns a single wallet  it tells that user_id for a wallet is unique.

    @Query("SELECT w from Wallet w WHERE w.wallet_type = ?1")
    List<Wallet> findWalletByType(String wallet_type);
    // returns all the wallets with the passed wallet_type.
}
