package com.wallet.walletservice.service;

import com.wallet.walletservice.Model.User;
import com.wallet.walletservice.Model.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<User> getAllUsers(){

        ResponseEntity<UserResponse> E =
                restTemplate.getForEntity("http://127.0.0.1:9016/users", UserResponse.class);
        // ResponseEntity represents the whole HTTP response(status code, headers, body) --> e.g everything that postman displays
        logger.info(E.getHeaders().toString()); // displaying the headers of response in terminal window as information( to_string means we
          // convert the headers of response into string then display it.
        if(E.getStatusCode().equals(HttpStatus.NOT_FOUND)){ // checking whether status code is 404(NOT_FOUND) if yes then returning null.
            return null;
        }
        return E.getBody().getList(); // otherwise we return the list (getBody().getList() --> the list of responses is stored in body)
    }





    public User getAUser(String userId){
        final String uri = "http://127.0.0.1:9016/users/{userId}";

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);

        ResponseEntity<User> E =
                restTemplate.getForEntity(uri,  User.class,params);
        // here user class is not an Entity but lets say some other project contains this user class as a table now we don't have access
        // to DB directly from here(e.g calling findAll() and other fun. since User Class is not a table), Hence we use restTemplate to
        // access the info. stored in User table in different projects and store it in ResponseEntity.

        if(E.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return null;
        }
        return E.getBody(); // body in responseEntity is
        // Although ResponseEntity contains all status code, body, headers but here we just return the body i.e fields of User class.
    }

}

