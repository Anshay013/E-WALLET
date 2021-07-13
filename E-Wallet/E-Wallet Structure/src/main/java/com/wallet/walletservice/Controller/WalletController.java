package com.wallet.walletservice.Controller;

import com.wallet.walletservice.Model.*;
import com.wallet.walletservice.Repository.TransactionRepository;
import com.wallet.walletservice.Repository.WalletRepository;
import com.wallet.walletservice.Util.WalletValidator;
import com.wallet.walletservice.exception.WalletBadRequest;
import com.wallet.walletservice.exception.WalletNotFoundException;
import com.wallet.walletservice.Model.Wallet;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class WalletController {
    @Autowired
    private TransactionRepository TranRepository;
    @Autowired
    private WalletRepository walletRepository;




    private static final Logger logger = LoggerFactory.getLogger(WalletResource.class);
    WalletValidator walletValidator = new WalletValidator();
     /* final means the variable cannot be reassigned it isn the final value that it has be assigned.
     /* LOGGER --> logger is an API which provides the ability to trace out the error of applications( When app. generates logging call,
     the LOGGER  records the event in LogRecord. After this it sends the corresponding handler/appender to handle it. (logging can be found
       in almost every language) */
    /* LoggerFactory --> It helps us to add logging support to our RESTFUL Web Service application with Spring Boot by providing loggers
     for various logging API's. */
    // getLogger --> returns a logger named according to the name parameter.


    @GetMapping("/findAllWallet")
    @ApiOperation(value = "WHOLE WALLET ")
    List<Wallet> findAllWallet() {
        return walletRepository.findAll();
    }
    // @RequestParam and @PathVariable are both used to extract value --> @RP extracts values from query string while @PV extracts it from
    // URI path. They give us the same response with just different URI (Uniform Resource Identifier), it is those things which with http
    // or mailto, it is same as URL (URI has URL within it(encompasses it)).

    @GetMapping("/wallet/{id}") // finding a wallet from ID. --> localhost:9011/wallet/id
    @ApiOperation(value = "SEARCH WALLET BY ID ")
    Wallet walletById(@ApiParam(value = "SEARCH BY ID", required = true)@PathVariable int id) {
        // @ApiParam is used used to provide documentation for swagger and the annotation content generated in document.
        logger.info("/wallet/{id} API by ID = "+ id); // used to display information.
        // Optional<User> user = repository.findById(id);
        return walletRepository.findById(id)  // i.e finding by primary key.
                .orElseThrow(() -> new WalletNotFoundException(id));
    }







    @DeleteMapping("/wallet/{id}") // deleting by id(user_id)
    @ApiOperation(value = "DELETE WALLET BY ID")
    public String deleteById(@PathVariable int id) throws Exception{
        logger.info("/wallet/{id} API by ID deleted = " + id);
       List<Wallet> wallets = walletRepository.findAll();
       for(Wallet w : wallets){
           if(w.getUser_id() == id){
               walletRepository.delete(w);
               return "Wallet was deleted";
           }
       }

       return "No wallet with such id found";
    }






    @PostMapping("/createNewWallet")
    @ResponseStatus(HttpStatus.CREATED)   //return 201 crated instead of 200 ok.
    @ApiOperation(value = "NEW WALLET ADDED ")
    Wallet CreateNewWallet(@RequestBody Wallet newWallet) throws Exception {
        if(!walletValidator.validateWalletRequest(newWallet)){
            logger.info("CreateNewWallet request not valid");
            throw  new WalletBadRequest();
        }

        int id = newWallet.getUser_id();
        Wallet w = walletRepository.findWalletByUserId(id);
        if(w == null){

            Wallet wallet = walletRepository.save(newWallet);
            return wallet;
        }
        throw new Exception("Id has already been registered");
    }






    @PutMapping("/updateWallet") 
    @ApiOperation(value = "UPDATING WALLET ")
    Wallet updateWallet(@RequestBody Wallet newWallet) throws Exception { // if newUser is not present simply adds it
        // mapping if present
        if(!walletValidator.validateWalletRequest(newWallet)){
            logger.info("CreateNewWallet request not valid");
            throw  new WalletBadRequest();
        }
        Wallet wallet = walletRepository.save(newWallet); 
        return wallet;
    }
}

