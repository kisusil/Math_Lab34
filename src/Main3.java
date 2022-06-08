import java.io.FileNotFoundException;

public class Main3 {
    public static void main(String[] args) throws FileNotFoundException {
        // создаем объект класса InterpolationData и заполняем его данными из файла
        InterpolationData interpolationData = new InterpolationData();
        interpolationData.init("data.txt");

        // вычисляем полином Лагранжа
        LagrangePolynomial lagrangePolynomial = new LagrangePolynomial(
                interpolationData.getX(),
                interpolationData.getY(),
                interpolationData.getK() - 1
        );

        System.out.println("Полином Лагранжа:");
        lagrangePolynomial.print();
        System.out.println();
        System.out.println();

        // выводим таблицу X Y f(x) Ln(x) на экран
        printTable(interpolationData, lagrangePolynomial);
    }

    private static void printTable(InterpolationData interpolationData, LagrangePolynomial lagrangePolynomial) {
        System.out.printf("%20s %20s %20s %20s", "X", "Y", "f(x)", "Ln(x)");
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
                        lagrangePolynomial.calculate(xi)
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
                        lagrangePolynomial.calculate(xi)
                );
                System.out.println();
            }
        }
    }
}
