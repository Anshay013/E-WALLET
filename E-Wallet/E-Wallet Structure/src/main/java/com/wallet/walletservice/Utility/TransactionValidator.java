package com.wallet.walletservice.Utility;

import com.wallet.walletservice.Model.Transaction;
import com.wallet.walletservice.Repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionValidator {

    @Autowired
    TransactionRepository transactionRepository;

    public boolean validateRequest (Transaction request){
        int id = request.getId(),sid = request.getSid(),rid = request.getRid();
        String s = Integer.toString(id);

        for(int i= 0;i<s.length(); i++){
            if(Character.isDigit(s.charAt(i)) == false)
                 return false;
        }
        s = Integer.toString(sid);

        for(int i= 0;i<s.length(); i++){
            if(Character.isDigit(s.charAt(i)) == false)
                return false;
        }
        s = Integer.toString(rid);
        for(int i= 0;i<s.length(); i++){
            if(Character.isDigit(s.charAt(i)) == false)
                return false;
        }

        return true;
    }
}
