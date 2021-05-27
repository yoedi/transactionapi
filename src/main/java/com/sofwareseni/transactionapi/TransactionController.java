package com.sofwareseni.transactionapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("transactionservice")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @PutMapping("/transaction/{transaction_id}")
    private ResponseEntity<Transaction> putTransaction(@RequestBody Transaction transaction,
                                                   @PathVariable long transaction_id) {
        ResponseEntity responseEntity = null;

        try {
            Transaction savedTransaction = transactionRepository.save(new Transaction(
                    transaction_id,
                    transaction.getAmount(),
                    transaction.getType(),
                    transaction.getParentId()
            ));

            responseEntity = new ResponseEntity<>(savedTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/transaction/{transaction_id}")
    private ResponseEntity<Transaction> getTransaction(@PathVariable long transaction_id) {
        ResponseEntity responseEntity = null;

        try {
            Optional<Transaction> savedTransaction = transactionRepository.findById(transaction_id);

            if(savedTransaction.isPresent()) {
                responseEntity = new ResponseEntity<>(savedTransaction.get(), HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/transaction/type/{type}")
    private ResponseEntity<Transaction> listTransactionIdByType(@PathVariable String type) {
        ResponseEntity responseEntity = null;

        try {
            List<Transaction> transactions = transactionRepository.getTransactionsByType(type);
            List<Long> transactionIds = transactions.stream()
                    .map(it -> it.getId())
                    .collect(Collectors.toList());;

            if(!transactionIds.isEmpty()) {
                responseEntity = new ResponseEntity<>(transactionIds, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }

    @GetMapping("/transaction/sum/{transaction_id}")
    private ResponseEntity<Transaction> sumTransactionByParentId(@PathVariable long transaction_id) {
        ResponseEntity responseEntity = null;

        try {
            List<Transaction> transactions = transactionRepository.getTransactionsByParentId(transaction_id);
            Double totalAmount = transactions.stream()
                    .mapToDouble(it -> it.getAmount())
                    .sum();

            if(!transactions.isEmpty()) {
                responseEntity = new ResponseEntity<>(totalAmount, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;
    }
}
