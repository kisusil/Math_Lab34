/*
NewtonPolynomial конструирует полином Ньютона на основе исходных точек x, массива значений y и n.
Использует внутри себя Polynomial
 */
public class NewtonPolynomial {
    // полином Ньютона
    private Polynomial polynomial;

    public NewtonPolynomial(double[] x, int n, double[][] dividedDifferences) {
        // инициализируем полином
        polynomial = new Polynomial(0.0, 0);

        // вычисляем полином Ньютона
        for (int i = 0; i < n; i++) {
            // в tempPolynomial храним произведение вида (x - x0)*(x-x1)...(x-xn-1)
            Polynomial tempPolynomial = new Polynomial(1.0, 0);

            for (int j = 0; j < i; j++) {
                // конструируем очередную "скобку"
                Polynomial xPolynomial = new Polynomial(1.0, 1);
                Polynomial xjPolynomial = new Polynomial(-x[j], 0);
                xPolynomial.add(xjPolynomial);

                // умножаем tempPolynomial на очередную скобку
                tempPolynomial.multiply(xPolynomial);
            }

            // домножаем tempPolynomial на соответствующую разделенную разность
            tempPolynomial.multiply(dividedDifferences[0][i + 1]);
            // прибавляем очередное слагаемое многочлена Ньютона к polynomial
            polynomial.add(tempPolynomial);
        }
    }

    public void print() {
        polynomial.print();
    }

    // findDividedDifferences метод находит частичные разности на основе исходных точек
    public static double[][] findDividedDifferences(double[] x, double[] y, int n) {
        // в dividedDifferences храним таблицу "уголком" с разделенными разностями как Вы показывали на лекции
        double[][] dividedDifferences = new double[n][];
        for (int i = 0; i < n; i++) {
            dividedDifferences[i] = new double[(n + 1) - i];
            // заполняем первые два столбца исходными точками и значениями функции в этих точках
            dividedDifferences[i][0] = x[i];
            dividedDifferences[i][1] = y[i];
        }

        // находим непосредственно значения разделенных разностей
        for (int j = 2; j <= n + 1; j++) {
            for (int i = 0; i < n - j + 1; i++) {
                dividedDifferences[i][j] = (dividedDifferences[i][j - 1] - dividedDifferences[i + 1][j - 1]) / (x[i] - x[i + (j - 1)]);
            }
        }

        return dividedDifferences;
    }

    public double calculate(double x) {
        return polynomial.calculate(x);
    }
}
//переборчик