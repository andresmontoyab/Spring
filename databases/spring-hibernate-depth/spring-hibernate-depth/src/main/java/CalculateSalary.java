import java.util.Arrays;
import java.util.List;

public class CalculateSalary {

    public static void main(String[] args) {
        calculateSalaryWithCompany();
        Double salary = 7_000_000.00;
        String category = "independent";
        calculateFinalAmountPerYear(salary, category);
    }

    private static void calculateSalaryWithCompany() {
        Double salary = 6_496_683.61;
        String category = "company";
        calculateFinalAmountPerYear(salary, category);
    }

    private static void calculateFinalAmountPerYear(Double salary, String category) {
        Double finalAmount = 0.0;
        System.out.println("Starting the calculate per year");
        if("company".equals(category)) {
            System.out.println("Salary per year in a company");
            Double prestationalSalary = salary * 0.65;
            System.out.println("Prestational Salary is " + prestationalSalary);
            Double salaryWithDeducctions = salary - discountHealth(prestationalSalary) - discountPension(prestationalSalary) - discountSolidaridad(prestationalSalary) - discountRF(prestationalSalary);
            System.out.println("Salary in one month is "+ salaryWithDeducctions);
            finalAmount = (salaryWithDeducctions * 12.0) + salary + salary;
            System.out.println("Salary per year " +finalAmount.longValue());
        } else if ("independent".equals(category)) {
            System.out.println("Starting the calculation per year");
            Double baseSalaryAmount = salary * 0.4;
            System.out.println("The base salary is " + baseSalaryAmount);
            Double discountHealthIndp = baseSalaryAmount * 0.125;
            Double discountPensionIndp = baseSalaryAmount * 0.16;
         //   System.out.println("Discounts, Health " + discountHealthIndp + " and Pension " + dis);
        }
    }

    private static Double discountRF(Double salary) {
        return salary * 0.077;
    }

    private static Double discountSolidaridad(Double salary) {
        return salary * 0.01;
    }

    private static Double discountPension(Double salary) {
        return salary * 0.04 ;
    }

    private static Double discountHealth(Double salary) {
        return salary * 0.04;
    }
}
