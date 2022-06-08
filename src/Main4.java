import java.io.FileNotFoundException;

public class Main4 {
    public static void main(String[] args) throws FileNotFoundException {
        // создаем объект класса InterpolationData и заполняем его данными из файла
        InterpolationData interpolationData = new InterpolationData();
        interpolationData.init("data.txt");

        // найдем сперва разделенные разности
        double[][] dividedDifferences = NewtonPolynomial.findDividedDifferences(
                interpolationData.getX(),
                interpolationData.getY(),
                interpolationData.getK()
        );

        // выведем таблицу с разделенными разностями на экран
        printDividedDifferences(dividedDifferences);
        System.out.println();

        // вычисляем полином Ньютона
        NewtonPolynomial newtonPolynomial = new NewtonPolynomial(
                interpolationData.getX(),
                interpolationData.getK(),
                dividedDifferences
        );

        // выводим полином Ньютона на экран
        System.out.println("Полином Ньютона:");
        newtonPolynomial.print();
        System.out.println();
        System.out.println();

        // выводим таблицу X Y f(x) Nn(x) на экран
        printTable(interpolationData, newtonPolynomial);
    }

    // метод выводит таблицу с разделенными разностями на экран
    private static void printDividedDifferences(double[][] dividedDifferences) {
        System.out.println("Разделенные разности");
        for (int j = 0; j < dividedDifferences[0].length; j++) {
            if (j == 0) {
                System.out.printf("%12s", "x");
            } else if (j == 1) {
                System.out.printf("%12s", "y");
            } else {
                System.out.printf("%12s", (j - 1) + " порядок");
            }
        }
        System.out.println();
        for (int i = 0; i < dividedDifferences.length; i++) {
            for (int j = 0; j < dividedDifferences[i].length; j++) {
                System.out.printf("%12.3E", dividedDifferences[i][j]);
            }
            System.out.println();
        }
    }

    private static void printTable(InterpolationData interpolationData, NewtonPolynomial newtonPolynomial) {
        System.out.printf("%20s %20s %20s %20s", "X", "Y", "f(x)", "Nn(x)");
        System.out.println();
        // Итерируемся по числу точек 2k - 1
        for (int i = 0; i < interpolationData.getK() * 2 - 1; i++) {
            // если i четное, то xi и yi берем из interpolationData
            if (i % 2 == 0) {
                double xi = interpolationData.getX()[i / 2];
                double yi = interpolationData.getY()[i / 2];
                System.out.printf(
                        "%20.6E %20.6E %20.6E %20.6E",
                        xi,
                        yi,
                        interpolationData.function(xi),
                        newtonPolynomial.calculate(xi)
                );
                System.out.println();
            } else {
                // если же i нечетное, то находим xi как полусумму соседних x-ов. yi нет, не выводим его
                int j = i / 2 + 1;
                double xi = (interpolationData.getX()[j - 1] + interpolationData.getX()[j]) / 2.0;
                System.out.printf(
                        "%20.6E %20s %20.6E %20.6E",
                        xi,
                        "-",
                        interpolationData.function(xi),
                        newtonPolynomial.calculate(xi)
                );
                System.out.println();
            }
        }
    }
}
