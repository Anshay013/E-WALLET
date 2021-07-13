package com.wallet.walletservice.Controller;

import com.wallet.walletservice.Model.*;
import com.wallet.walletservice.Repository.TransactionRepository;
import com.wallet.walletservice.Repository.WalletRepository;
import com.wallet.walletservice.Util.TransactionValidator;
import com.wallet.walletservice.Util.WalletValidator;
import com.wallet.walletservice.exception.TransactionBadRequest;
import com.wallet.walletservice.service.EmailService;
import com.wallet.walletservice.service.UserService;
import com.wallet.walletservice.Model.AddBalanceDetails;
import com.wallet.walletservice.Model.Transaction;
import com.wallet.walletservice.Model.User;
import com.wallet.walletservice.Model.Wallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

@RestController
public class TransactionController {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private TransactionRepository TranRepository;

    @Autowired
    private UserService userService;

    WalletValidator walletValidator = new WalletValidator();
    TransactionValidator validator = new TransactionValidator();
    private static final String TOPIC = "test";
    private static final Logger logger = LoggerFactory.getLogger(WalletResource.class);




    @PutMapping("/sendMoney")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    Transaction addBal(@RequestBody Transaction transaction) throws Exception {

        int id = transaction.getId();

        if(!validator.validateRequest(transaction)){
            throw new TransactionBadRequest();
        }
        Transaction tt  = TranRepository.findTransactionById(id);

        if(tt == null) {
            transaction.setDate(new Date(Calendar.getInstance().getTime().getTime()));
            Wallet senderWallet = walletRepository.findWalletByUserId(transaction.getSid());
            Wallet receiverWallet = walletRepository.findWalletByUserId(transaction.getRid());

            if (senderWallet == null || receiverWallet == null) {
                logger.info("No wallet for sender or receiver");
                throw new TransactionBadRequest();
            }

            int amt = transaction.getAmount();

            if (senderWallet.getBalance() < amt) {
                throw new Exception("Insufficient Balance");
            }
            senderWallet.setBalance(senderWallet.getBalance() - amt); // debiting the amt and updating the balance for sender

            receiverWallet.setBalance(receiverWallet.getBalance() + amt);// crediting the amt and updating receiver balance.

            transaction.setStatus("SENT");
            logger.info(String.format("$$ -> Producing Transaction --> %s", transaction));
            walletRepository.save(receiverWallet);
            walletRepository.save(senderWallet);

             TranRepository.save(transaction);
             return transaction;
        }
        else throw new Exception("TXN. Id already present");
    }




    @ApiOperation(value = "BALANCE BY ID")
    @GetMapping("/getBal/{id}")
    int getBal(@PathVariable int id) throws Exception {

            Wallet wallet = walletRepository.findWalletByUserId(id);

            if(wallet==null) throw new Exception("Wallet Not Found");
            else {
                return wallet.getBalance();
            }
    }



    @PutMapping("/addBalance") // since we update the balance we use @PutMapping
    @ApiOperation(value = "ADDING BALANCE TO WALLET")
    AddBalanceDetails addBalance(@RequestBody AddBalanceDetails request) throws Exception {

         int uid = request.getUid(), bal = request.getAmount();
        logger.info(request.toString());
        Wallet wallet = walletRepository.findWalletByUserId(request.getUid());
        if (wallet == null) throw new Exception("Cannot addBalance Uid doesn't exist");
        else {
            int initialBal = wallet.getBalance();
            int newBal = initialBal + bal;
            wallet.setBalance(newBal);
            walletRepository.save(wallet);
            return request;
        }
    }




    @GetMapping("/txnHistory/{id}")
    String getTransactionHistory(@PathVariable int id) {
        logger.info(String.format("$$ -> Producing Transaction --> %s", id));
        
       TranHistoryToEmail( Integer.toString(id));     
         // coverts id to String format.
        return "You will get the file on your email";
    }




    private void TranHistoryToEmail(String id) {
        int id1 = Integer.parseInt(id); 
        
        ArrayList<Transaction> list =
                (ArrayList<Transaction>) TranRepository.findBySid(id1); 

        User user1 = userService.getAUser(String.valueOf(id1)); // getting the body of user with its id (getting the details of user whom
        // we want to send his/her txn history.
        String filename ="test.csv";
        try {
            FileWriter fw = new FileWriter(filename); // it will store in string format

            for(int i=0;i<list.size();i++) {
                fw.append(list.get(i).getStatus());
                fw.append(',');
                int amt = list.get(i).getAmount();
                Integer obj = amt;
                fw.append(obj.toString()); 
                fw.append(',');
                fw.append(list.get(i).getDate().toString());
                fw.append(',');
                int id2 = list.get(i).getId();
                Integer obj2 = id2;
                fw.append(obj2.toString());
                fw.append(',');
                int rid = list.get(i).getRid();
                obj = rid;
                fw.append(obj.toString());
                fw.append(',');
                int sid = list.get(i).getSid(); // this is users own id
                obj = sid;
                fw.append(obj.toString());
                fw.append('\n');
            }
            fw.flush();
            fw.close();
            logger.info("CSV File is created successfully.");
            EmailService.sendEmailWithAttachments("","",user1.getEmail(),"","to@gmail.com","","",filename);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
