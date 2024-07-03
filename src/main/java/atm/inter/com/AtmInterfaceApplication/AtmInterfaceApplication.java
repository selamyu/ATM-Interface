package atm.inter.com.AtmInterfaceApplication;

import atm.inter.com.AtmInterfaceApplication.model.User;
import atm.inter.com.AtmInterfaceApplication.repository.UserRepository;
import atm.inter.com.AtmInterfaceApplication.service.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class AtmInterfaceApplication implements CommandLineRunner {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AtmService atmService;

	public static void main(String[] args) {
		SpringApplication.run(AtmInterfaceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("Enter User ID: ");
			String userId = scanner.nextLine();
			System.out.print("Enter User PIN: ");
			String userPin = scanner.nextLine();

			User user = atmService.login(userId, userPin);
			if (user == null) {
				System.out.println("Invalid User ID or PIN. Try again.");
				continue;
			}

			while (true) {
				System.out.println("ATM Menu:");
				System.out.println("1. Transaction History");
				System.out.println("2. Withdraw");
				System.out.println("3. Deposit");
				System.out.println("4. Transfer");
				System.out.println("5. Quit");
				System.out.print("Choose an option: ");
				int option = Integer.parseInt(scanner.nextLine());

				switch (option) {
					case 1:
						System.out.println("Transaction History:");
						atmService.transactionHistory(userId, userPin).forEach(System.out::println);
						break;
					case 2:
						System.out.print("Enter amount to withdraw: ");
						double withdrawAmount = Double.parseDouble(scanner.nextLine());
						System.out.println(atmService.withdraw(userId, userPin, withdrawAmount));
						break;
					case 3:
						System.out.print("Enter amount to deposit: ");
						double depositAmount = Double.parseDouble(scanner.nextLine());
						System.out.println(atmService.deposit(userId, userPin, depositAmount));
						break;
					case 4:
						System.out.print("Enter recipient User ID: ");
						String toUserId = scanner.nextLine();
						System.out.print("Enter amount to transfer: ");
						double transferAmount = Double.parseDouble(scanner.nextLine());
						System.out.println(atmService.transfer(userId, userPin, toUserId, transferAmount));
						break;
					case 5:
						System.out.println("Thank you for using the ATM. Goodbye!");
						System.exit(0);
					default:
						System.out.println("Invalid option. Please try again.");
				}
			}
		}
	}

}
