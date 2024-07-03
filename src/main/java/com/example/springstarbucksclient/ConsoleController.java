package com.example.springstarbucksclient;

import com.example.springstarbucksclient.model.Card;
import com.example.springstarbucksclient.model.Order;
import com.example.springstarbucksclient.model.Ping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/*
    RestTemplate JavaDoc:
        * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html
        * https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpEntity.html

    Tutorial Resources:
        * https://reflectoring.io/spring-resttemplate
        * https://www.baeldung.com/rest-template
        * https://springframework.guru/enable-pretty-print-of-json-with-jackson
        * https://attacomsian.com/blog/spring-boot-resttemplate-get-request-parameters-headers

 */

@Controller
@RequestMapping("/")
public class ConsoleController {
    String apikey = "2742a237475c4703841a2bf906531eb0";
    @GetMapping
    public String getAction(@ModelAttribute("command") ConsoleCommand command,
                            Model model) {
        return "console";
    }

    @PostMapping
    public String postAction(@ModelAttribute("command") ConsoleCommand command,
                             @RequestParam(value = "action", required = true) String action,
                             Errors errors, Model model, HttpServletRequest request) throws JsonProcessingException {

        System.out.println( command );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String resourceUrl = "" ;
        String message = "";

        // Set API Key Header
        headers.set( "apikey", "2742a237475c4703841a2bf906531eb0" ) ;
        if (action.equals("PING")) {
            message = "PING";
            resourceUrl = "http://35.232.250.104:80/api/ping?apikey={apikey}";
            // get response as string
            try {
            ResponseEntity<String> stringResponse = restTemplate.getForEntity(resourceUrl, String.class, apikey );
            message = stringResponse.getBody();
            // get response as POJO
            ResponseEntity<Ping> pingResponse = restTemplate.getForEntity(resourceUrl, Ping.class, apikey);
            Ping pingMsg = pingResponse.getBody();
            System.out.println( pingMsg );
            ObjectMapper objectMapper = new ObjectMapper() ;
            Object object = objectMapper.readValue(message, Object.class);
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(jsonString) ;
            message = "\n" + jsonString.substring(2, jsonString.length() - 2);
            }
            catch ( Exception e ) {
                message = "Faile to Ping, Please Try Again!";
            }
        }
        if (action.equals("NEW CARD")) {
            message = "";
            resourceUrl = "http://35.232.250.104:80/api/cards";
            try {
            // get response as POJO
            String emptyRequest = "" ;
            HttpEntity<String> newCardRequest = new HttpEntity<String>(emptyRequest, headers) ;
            ResponseEntity<Card> newCardResponse = restTemplate.postForEntity(resourceUrl, newCardRequest, Card.class);
            Card newCard = newCardResponse.getBody();
            System.out.println( newCard );
            ObjectMapper objectMapper = new ObjectMapper() ;
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newCard);
            System.out.println( jsonString) ;
            message = "\n" + jsonString.substring(2, jsonString.length() - 2);
            }
            catch ( Exception e ) {
                message = "Faile to Create a New Card, Please Try Again!";
            }
        }
        if (action.equals("NEW ORDER")) {
            message = "";
            resourceUrl = "http://35.232.250.104:80/api/order/register/5012349";
            try {
            // get response as POJO
            Order orderRequest = new Order() ;
            orderRequest.setDrink("Caffe Latte") ;
            orderRequest.setMilk("Whole") ;
            orderRequest.setSize("Grande");
            HttpEntity<Order> newOrderRequest = new HttpEntity<Order>(orderRequest,headers) ;
            ResponseEntity<Order> newOrderResponse = restTemplate.postForEntity(resourceUrl, newOrderRequest, Order.class);
            Order newOrder = newOrderResponse.getBody();
            System.out.println( newOrder );
            ObjectMapper objectMapper = new ObjectMapper() ;
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newOrder);
            System.out.println( jsonString) ;
            message = "New Order！！";
            message += "\n" + jsonString.substring(2, jsonString.length() - 2);
            }
            catch ( Exception e ) {
                message = "Alreadly Have a Order！！";
                try {
                    resourceUrl = "http://35.232.250.104:80/api/order/register/5012349?apikey={apikey}";
                    ResponseEntity<Order> stringResponse = restTemplate.getForEntity(resourceUrl, Order.class, apikey);
                    Order newOrder = stringResponse.getBody();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newOrder);
                    message += "\n" + jsonString.substring(2, jsonString.length() - 2);
                }
                catch ( Exception e2 ) {}
            }
        }
        if (action.equals("ACTIVATE CARD")) {
            message = "";
            resourceUrl = "http://35.232.250.104:80/api/card/activate/"+command.getCardnum()+"/"+command.getCardcode();
            try {
            // get response as POJO
            String emptyRequest = "" ;
            HttpEntity<String> newCardRequest = new HttpEntity<String>(emptyRequest,headers) ;
            ResponseEntity<Card> newCardResponse = restTemplate.postForEntity(resourceUrl, newCardRequest, Card.class);
            Card newCard = newCardResponse.getBody();
            System.out.println( newCard );
            ObjectMapper objectMapper = new ObjectMapper() ;
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newCard);
            System.out.println( jsonString) ;
            message = "\n" + jsonString.substring(2, jsonString.length() - 2);
            }
            catch ( Exception e ) {
                message = "Sorry, you cannot active the card you entered. Please check it is available or legal.";
            }
        }
        if (action.equals("PAY")) {
            message = "";
            resourceUrl = "http://35.232.250.104:80/api/order/register/5012349/pay/"+command.getCardnum() ;
            System.out.println(resourceUrl) ;
            // get response as POJO
            try {
            String emptyRequest = "" ;
            HttpEntity<String> paymentRequest = new HttpEntity<String>(emptyRequest,headers) ;
            ResponseEntity<Card> payForOrderResponse = restTemplate.postForEntity(resourceUrl, paymentRequest, Card.class);
            Card orderPaid = payForOrderResponse.getBody();
            System.out.println( orderPaid );
            ObjectMapper objectMapper = new ObjectMapper() ;
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderPaid);
            System.out.println( jsonString) ;
            message = "\n" + jsonString.substring(2, jsonString.length() - 2);
            }
            catch ( Exception e ) {
                message = "Sorry, you cannot complete your order, please check if the card is legal or have enough balance.";
            }
        }
        model.addAttribute("message", message);
        return "console";
    }

}