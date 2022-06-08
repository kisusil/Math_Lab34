/*
LagrangePolynomial конструирует полином Лагранжа на основе исходных точек x, массива значений y и n.
Использует внутри себя Polynomial
 */
public class LagrangePolynomial {
    // полином Лагранжа
    private Polynomial polynomial;

    public LagrangePolynomial(double[] x, double[] y, int n) {
        // инициализируем полином
        polynomial = new Polynomial(0.0, 0);

        // вычисляем полином Лагранжа
        for (int i = 0; i <= n; i++) {
            // multiplication хранит в себе полином-произведение
            Polynomial multiplication = new Polynomial(1.0, 0);

            // находим полином-произведение
            for (int j = 0; j <= n; j++) {
                // пропускаем итерацию, если i == j
                if (i == j) {
                    continue;
                } // плоховато, это надо разбить на два цикла

                // partialOperand - (x - xj)
                Polynomial partialOperand = new Polynomial(1.0, 1);
                partialOperand.add(new Polynomial(-x[j], 0));

                // умножаем полином-произведение на (x - xj) и на 1 / (xi - xj)
                multiplication.multiply(partialOperand);
                multiplication.multiply(1.0 / (x[i] - x[j]));
            }

            // домножаем полином-произведение на yi
            multiplication.multiply(y[i]);
            // прибавляем найденный полином-произведение к полиному Лагранжа
            polynomial.add(multiplication);
        }
    }

    public void print() {
        polynomial.print();
    }

    public double calculate(double x) {
        return polynomial.calculate(x);
    }
}
