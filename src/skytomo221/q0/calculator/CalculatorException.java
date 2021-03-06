package skytomo221.q0.calculator;

public class CalculatorException extends IllegalArgumentException {
    protected Calculator calculator;

    public Calculator getCalculator() {
        return calculator;
    }

    CalculatorException(Calculator calculator, String message) {
        super(message);
        this.calculator = calculator;
    }

    public String getCalculatorErrorMessage() {
        StringBuilder s = new StringBuilder();
        s.append(getLocalizedMessage());
        s.append(calculator.getLog());
        s.append(calculator.getLogNumber().replaceAll(".", " "));
        s.append(" → throw new Exception(...);\n");
        return s.toString();
    }
}
