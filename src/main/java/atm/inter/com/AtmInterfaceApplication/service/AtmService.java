package atm.inter.com.AtmInterfaceApplication.service;

import atm.inter.com.AtmInterfaceApplication.model.Transaction;
import atm.inter.com.AtmInterfaceApplication.model.User;
import atm.inter.com.AtmInterfaceApplication.repository.TransactionRepository;
import atm.inter.com.AtmInterfaceApplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AtmService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public String registerUser(String userId, String userPin, double initialBalance, String phoneNumber) {
        if (userRepository.findByUserId(userId) != null) {
            return "User ID already exists.";
        }

        User user1 = new User();
        user1.setUserId(userId);
        user1.setUserPin(userPin);
        user1.setBalance(initialBalance);
        user1.setPhoneNumber(phoneNumber);
        userRepository.save(user1);
        return "User registered successfully.";
    }

    public User login(String userId, String userPin) {
        return userRepository.findByUserIdAndUserPin(userId, userPin);
    }

    public List<Transaction> transactionHistory(String userId, String userPin) {
        User user = login(userId, userPin);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID or PIN.");
        }
        return transactionRepository.findByUserId(user.getId());
    }

    public String withdraw(String userId, String userPin, double amount) {
        User user = login(userId, userPin);
        if (user == null) {
            return "Invalid user ID or PIN.";
        }
        if (user.getBalance() >= amount) {
            user.setBalance(user.getBalance() - amount);
            saveTransaction(user, "Withdraw", amount);
            userRepository.save(user);

            return "Withdraw successful. New balance: " + user.getBalance();
        }
        return "Insufficient balance.";
    }

    public String deposit(String userId, String userPin, double amount) {
        User user = login(userId, userPin);
        if (user == null) {
            return "Invalid user ID or PIN.";
        }
        user.setBalance(user.getBalance() + amount);
        saveTransaction(user, "Deposit", amount);
        userRepository.save(user);

        return "Deposit successful. New balance: " + user.getBalance();
    }

    public String transfer(String fromUserId, String fromUserPin, String toUserId, double amount) {
        User fromUser = login(fromUserId, fromUserPin);
        if (fromUser == null) {
            return "Invalid sender user ID or PIN.";
        }
        User toUser = userRepository.findByUserId(toUserId);
        if (toUser == null) {
            return "Recipient user not found.";
        }
        if (fromUser.getBalance() >= amount) {
            fromUser.setBalance(fromUser.getBalance() - amount);
            toUser.setBalance(toUser.getBalance() + amount);
            saveTransaction(fromUser, "Transfer to " + toUser.getUserId(), amount);
            saveTransaction(toUser, "Transfer from " + fromUser.getUserId(), amount);
            userRepository.save(fromUser);
            userRepository.save(toUser);

            return "Transfer successful. New balance: " + fromUser.getBalance() + ", " + toUser.getBalance();
        }
        return "Insufficient balance.";
    }

    private void saveTransaction(User user, String type, double amount) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }
}
