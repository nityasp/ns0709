import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RentalAgreement {
    private String toolCode;
    private String toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BigDecimal dailyCharge;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    // Constructor
    public RentalAgreement(String toolCode, String toolType, String toolBrand, int rentalDays,
                           LocalDate checkoutDate, BigDecimal dailyCharge, int discountPercent) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.rentalDays = rentalDays;
        this.checkoutDate = checkoutDate;
        this.dailyCharge = dailyCharge;
        this.discountPercent = discountPercent;
        calculate();
    }

    // Calculate all fields
    private void calculate() {
        // Calculate due date
        this.dueDate = checkoutDate.plusDays(rentalDays);

        // Calculate charge days and pre-discount charge
        LocalDate currentDate = checkoutDate;
        int totalChargeDays = 0;
        BigDecimal totalPreDiscountCharge = BigDecimal.ZERO;

        while (!currentDate.isAfter(dueDate)) {
            if (isChargeable(currentDate)) {
                totalChargeDays++;
                totalPreDiscountCharge = totalPreDiscountCharge.add(dailyCharge);
            }
            currentDate = currentDate.plusDays(1);
        }
        this.chargeDays = totalChargeDays;

        // Calculate pre-discount charge (rounded half up to cents)
        this.preDiscountCharge = totalPreDiscountCharge.setScale(2, RoundingMode.HALF_UP);

        // Calculate discount amount (rounded half up to cents)
        this.discountAmount = preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent / 100.0))
                                               .setScale(2, RoundingMode.HALF_UP);

        // Calculate final charge
        this.finalCharge = preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    // Check whether the day is chargeble
    private boolean isChargeable(LocalDate date) {
        return new Tool(toolCode, toolType, toolBrand, dailyCharge, true, true, true).isChargeable(date);
    }

    // Print rental agreement details
    public void printAgreement() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        System.out.println("Tool code: " + toolCode);
        System.out.println("Tool type: " + toolType);
        System.out.println("Tool brand: " + toolBrand);
        System.out.println("Rental days: " + rentalDays);
        System.out.println("Checkout date: " + checkoutDate.format(formatter));
        System.out.println("Due date: " + dueDate.format(formatter));
        System.out.println("Daily rental charge: $" + dailyCharge.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Charge days: " + chargeDays);
        System.out.println("Pre-discount charge: $" + preDiscountCharge.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Discount percent: " + discountPercent + "%");
        System.out.println("Discount amount: $" + discountAmount.setScale(2, RoundingMode.HALF_UP));
        System.out.println("Final charge: $" + finalCharge.setScale(2, RoundingMode.HALF_UP));
    }
}
