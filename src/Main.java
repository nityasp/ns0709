import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select a test case to run:");
            System.out.println("1. JAKR - Checkout on 09/03/15");
            System.out.println("2. LADW - Checkout on 07/02/20");
            System.out.println("3. CHNS - Checkout on 07/02/15");
            System.out.println("4. JAKD - Checkout on 09/03/15");
            System.out.println("5. JAKR - Checkout on 07/02/15");
            System.out.println("6. JAKR - Checkout on 07/02/20");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character after nextInt()

            switch (choice) {
                case 1:
                    executeTestCase("JAKR", 5, 101, LocalDate.of(2015, 9, 3));
                    break;
                case 2:
                    executeTestCase("LADW", 3, 10, LocalDate.of(2020, 7, 2));
                    break;
                case 3:
                    executeTestCase("CHNS", 5, 25, LocalDate.of(2015, 7, 2));
                    break;
                case 4:
                    executeTestCase("JAKD", 6, 0, LocalDate.of(2015, 9, 3));
                    break;
                case 5:
                    executeTestCase("JAKR", 9, 0, LocalDate.of(2015, 7, 2));
                    break;
                case 6:
                    executeTestCase("JAKR", 4, 50, LocalDate.of(2020, 7, 2));
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number between 0 and 6.");
                    break;
            }

            System.out.println("\nPress Enter to run another test case or 0 to exit.");
            scanner.nextLine(); // Wait for user to press Enter
        }
    }

    public static void executeTestCase(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        try {
            RentalAgreement agreement = checkout(toolCode, rentalDays, discountPercent, checkoutDate);
            agreement.printAgreement();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static RentalAgreement checkout(String toolCode, int rentalDays, int discountPercent, LocalDate checkoutDate) {
        Tool tool = getToolByCode(toolCode);
        if (tool == null) {
            throw new IllegalArgumentException("Invalid tool code: " + toolCode);
        }
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental days must be 1 or greater.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100.");
        }

        return new RentalAgreement(tool.getToolCode(), tool.getToolType(), tool.getBrand(), rentalDays,
                checkoutDate, tool.getDailyCharge(), discountPercent);
    }

    public static Tool getToolByCode(String toolCode) {
        Tool[] tools = {
                new Tool("CHNS", "Chainsaw", "Stihl", new BigDecimal("1.49"), true, false, true),
                new Tool("LADW", "Ladder", "Werner", new BigDecimal("1.99"), true, true, false),
                new Tool("JAKD", "Jackhammer", "DeWalt", new BigDecimal("2.99"), true, false, false),
                new Tool("JAKR", "Jackhammer", "Ridgid", new BigDecimal("2.99"), true, false, false)
        };

        for (Tool tool : tools) {
            if (tool.getToolCode().equals(toolCode)) {
                return tool;
            }
        }
        return null;
    }
}
