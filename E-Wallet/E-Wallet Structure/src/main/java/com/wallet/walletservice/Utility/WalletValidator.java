package com.wallet.walletservice.Utility;

import com.wallet.walletservice.Model.Wallet;
import com.wallet.walletservice.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

public class WalletValidator {
    @Autowired
    WalletRepository walletRepository;

    public boolean validateWalletRequest(Wallet wallet) throws Exception {
        if (wallet.getUser_id() < 0) return false;
        int num = wallet.getUser_id();
        String s;
        s = Integer.toString(num); 
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i)) == false) 
                return false;
        }
        if (wallet.getBalance() < 0) return false;
        num = wallet.getBalance();
        String k = Integer.toString(num); 
        for (int i = 0; i < k.length(); i++) {
            if (Character.isDigit(k.charAt(i)) == false) 
                return false;
        }
        return true;
    }
}
