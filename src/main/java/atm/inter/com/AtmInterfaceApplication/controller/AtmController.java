package atm.inter.com.AtmInterfaceApplication.controller;

import atm.inter.com.AtmInterfaceApplication.model.Transaction;
import atm.inter.com.AtmInterfaceApplication.model.User;
import atm.inter.com.AtmInterfaceApplication.service.AtmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm")
@RequiredArgsConstructor
public class AtmController {
    private final AtmService atmService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestParam String userId, @Valid @RequestParam String userPin, @RequestParam double initialBalance, @RequestParam String phoneNumber) {
        atmService.registerUser(userId, userPin, initialBalance, phoneNumber);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestParam String userId, @RequestParam String userPin) {
        return atmService.login(userId, userPin) != null ? "Login successful" : "Invalid user ID or PIN";
    }
    @GetMapping("/transactionHistory")
    public List<Transaction> transactionHistory(@RequestParam String userId, @RequestParam String userPin) {
        return atmService.transactionHistory(userId, userPin);
    }
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String userId, @RequestParam String userPin, @RequestParam double amount) {
        return atmService.withdraw(userId, userPin, amount);
    }
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestParam String userId, @RequestParam String userPin, @RequestParam double amount) {
        atmService.deposit(userId, userPin, amount);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromUserId, @RequestParam String fromUserPin, @RequestParam String toUserId, @RequestParam double amount) {
        return atmService.transfer(fromUserId, fromUserPin, toUserId, amount);
    }
    @PostMapping("/quit")
    public ResponseEntity<String> quit() {
        return new ResponseEntity<>("Thank you for using the ATM. Goodbye!", HttpStatus.OK);
    }
}
