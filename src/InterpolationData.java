import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
InterpolationData хранит в себе узлы сетки, значения сеточной функции в этих узлах и функцию f(x)
 */
public class InterpolationData {
    // x - аргументы точек
    private double[] x;
    // y - значения точек
    private double[] y;
    // k - количество точек
    private int k;
    // functionNumber - определяет номер функции:
    // 1 - x^5 - 4.378 * x^4 - 2.177 * x^2 + 0.331
    // 2 - sin(x^2 / 2)
    // любой другой - sin(x)
    private int functionNumber;


    // init() считывает крайние точки a и b из файла filename, а также количество точек k
    public void init(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(filename));
        double a = scanner.nextDouble();
        double b = scanner.nextDouble();
        scanner.nextLine();
        k = scanner.nextInt();
        functionNumber = scanner.nextInt();
        scanner.close();

        x = new double[k];
        y = new double[k];

        // найти значения аргументов
        initX(a, b);
        // найти значения функции в f(x) в точках из массива x
        initY();
    }

    // initX() находит значения точек от a до b с шагом (b - a) / (k - 1) и записывает их в массив x
    private void initX(double a, double b) {
        double step = (b - a) / (k - 1);
        x[0] = a;
        x[k - 1] = b;
        for (int i = 1; i < k - 1; i++) {
            x[i] = x[i - 1] + step;
        }
    }

    // initY() находит значения функции f(x) в точках из массива x
    private void initY() {
        for (int i = 0; i < k; i++) {
            y[i] = function(x[i]);
        }
    }

    // function() исходная функция - косинус
    public double function(double x) {
        if (functionNumber == 1) {
            return first(x);
        }

        if (functionNumber == 2) {
            return second(x);
        }

        return Math.sin(x);
    }

    private double first(double x) {
        return -7.3782 * x * x * x * x * x - 4.3741 * x * x * x - 2.1712;
    }

    private double second(double x) {
        return Math.sin(x * x / 2);
    }

    public double[] getX() {
        return x;
    }

    public double[] getY() {
        return y;
    }

    public int getK() {
        return k;
    }
}