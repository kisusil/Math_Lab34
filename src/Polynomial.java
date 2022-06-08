import java.io.FileNotFoundException;

/*
Класс Polynomial представляет собой полином вида Pn(x). Он хранит слагаемые полинома в виде односвязного списка
Node. Каждая Node-а хранит в себе коэффициент (factor), степень (power), а также ссылку на следующую Node-у.
Так же класс Polynomial предоставляет 3 метода для операций над полиномом: add(Polynomial), multiply(Polynomial) и
multiply(double). Первый - прибавить к данному полиному другой полином. Второй - умножить данных полином на
другой полином и сохранить результат в данном полиноме. Третий - умножить данный полином на вещественно число.

Кроме методов-операций, Polynomial предоставляет метод print() для форматированного вывода полинома на экран и
calculate(double) - вычисление значения полинома в точке.

При выполнении операций Polynmial поддерживает два инварианта:
1. Список нод упорядочен по убыванию степеней слагаемых
2. Все степени нод различны, причем, если есть нода с коэффициентом 0, то она корневая и единственная.
 */
public class Polynomial {
    private static final double EPSILON = 1e-16;
    // rootNode - корневая нода, начало списка, нода с наибольшей степенью
    private Node rootNode;


    // конструктор создает полином, состоящий из единственного слагаемого вида initalFactor * x ^ (initialPower)
    public Polynomial(double initialFactor, int initialPower) {
        rootNode = new Node(
                initialFactor,
                initialPower,
                null
        );
    }

    // isEqualToZero - проверяет, равен ли x 0 в пределах точности EPSILON
    private boolean isEqualToZero(double x) {
        return Math.abs(x) < EPSILON;
    }

    // метод print() осуществляет форматированный вывод полинома на экран
    public void print() {
        // итерируемся в цикле while по всем нодам списка, начиная с корневой
        Node node = rootNode;
        while (node != null) {
            // если коэффициент положителен или 0, то выводим лидирующий знак +, если отрицателен - знак -
            if (node.factor >= 0) {
                System.out.printf("+ %12.6E*x^%d ", node.factor, node.power);
            } else {
                System.out.printf("- %12.6E*x^%d ", Math.abs(node.factor), node.power);
            }
            node = node.nextNode;
        }
    }

    // add() - сложение двух полиномов в один проход одновременно по спискам обоих полиномов. В результате
    // будет обновлено состояние только данного полинома, другой останется нетронутым.
    public void add(Polynomial polynomial) {
        // thisNode - указывает на текущую ноду данного полинома
        Node thisNode = rootNode;
        // otherNode - указывает на текущую ноду другого полинома
        Node otherNode = polynomial.rootNode;
        // newRoot - новая корневая нода - она будет в конце алгоритма присвоена rootNode данного полинома
        Node newRoot;
        // newEnd - новая концевая нода - она нужна, чтобы подвешивать новые ноды к концу списка
        Node newEnd;

        // перед итерацией по спискам нод полиномов, нужно выбрать изначальные newRoot и newEnd таким образом,
        // чтобы не нарушить инварианты результирующего полинома. Поэтому первой нодой нового полинома станет:
        // 1. Первая нода данного полинома, если ее степень больше степени первой ноды другого
        // 2. Первая нода данного полинома с коэффициентом, равным сумме коэффициентов первых нод обоих полиномов,
        // если степени первых нод равны
        // 3. Первая нода другого полинома, если ее степень больше степени первой ноды данного
        if (thisNode.power > otherNode.power) {
            newRoot = thisNode;
            newEnd = thisNode;
            thisNode = thisNode.nextNode;
        } else if (thisNode.power == otherNode.power) {
            thisNode.factor += otherNode.factor;
            newRoot = thisNode;
            newEnd = thisNode;
            thisNode = thisNode.nextNode;
            otherNode = otherNode.nextNode;
        } else {
            newRoot = otherNode;
            newEnd = otherNode;
            otherNode = otherNode.nextNode;
        }

        // Основной цикл алгоритма. В нем мы одновременно проходим по обоим спискам нод полиномов и на каждой
        // итерации включаем в результат одну из нод, руководствуясь теми же принципами, как и при выборе
        // новой корневой ноды.
        while (thisNode != null && otherNode != null) {
            if (thisNode.power > otherNode.power) {
                newEnd.nextNode = thisNode;
                newEnd = thisNode;
                thisNode = thisNode.nextNode;
            } else if (thisNode.power == otherNode.power) {
                newEnd.nextNode = thisNode;
                thisNode.factor += otherNode.factor;

                // если получили нулевую ноду - пропускаем ее
                if (!isEqualToZero(thisNode.factor)) {
                    newEnd = thisNode;
                }

                thisNode = thisNode.nextNode;
                otherNode = otherNode.nextNode;
            } else {
                newEnd.nextNode = otherNode;
                newEnd = otherNode;
                otherNode = otherNode.nextNode;
            }
        }

        // Здесь и далее либо thisNode == null, либо otherNode == 0, либо и то и другое одноверменно. Так как это
        // условие выхода из цикла

        // Если thisNode != null, то значит otherNode == null, то есть мы имеем хвост из списка нод данного полинома.
        // Очевидно, что для него инварианты сохраняются, а значит мы просто довесим его к последнему элементу
        // сформированного списка нод результата
        if (thisNode != null) {
            newEnd.nextNode = thisNode;
        }

        // То же самое, но для списка нод другого полинома. Причем, "сработает" только один из этих if-ов в силу
        // условия выхода из вышестоящего цикла
        if (otherNode != null) {
            newEnd.nextNode = otherNode;
        }

        // Обновляем корневую ноду данного полинома
        // так как в основном цикле мы пропускали нулевые ноды, но перед ним не проверяли
        // получившуюся корневую ноду на равенство нулю, нужно проверить, не оказалась ли корневая нода
        // нулевой
        if (isEqualToZero(newRoot.factor)) {
            // Если корневая нода единственная, то нужно создать новую нулевую ноду
            if (newRoot.nextNode == null) {
                rootNode = new Node(0.0, 0, null);
            } else {
                rootNode = newRoot.nextNode;
            }
        } else {
            rootNode = newRoot;
        }
    }

    // multiply(Polynomial) реализует умножение двух полиномов. Как и в случае с add в результет будет изменено только
    // состояние данного полинома. Другой полином останется нетронутым. Алгоритм умножения - каждую ноду
    // данного полинома умножаем на каждую ноду другого полинома и складываем полученные результаты. Таким образом,
    // т.к. add поддерживает инварианты класса Polynomial, то и multiply(Polynomial) тоже их поддерживает
    public void multiply(Polynomial polynomial) {
        // отдельно рассмотрим случаи, когда степень корневой ноды одного из полиномов 0. Эти случаи можно свести к
        // выполнению умножения как умножения на вещественное число
        if (polynomial.rootNode.power == 0) {
            multiply(polynomial.rootNode.factor);
            return;
        }

        // отдельно рассмотрим случаи, когда степень корневой ноды одного из полиномов 0. Эти случаи можно свести к
        // выполнению умножения как умножения на вещественное число
        if (rootNode.power == 0) {
            // т.к. результат должен быть сохранен в состоянии данного полинома, нам нужно заменить rootNode данного
            // полинома на rootNode другого. После чего выполняем умножение полинома на число
            Node tmp = rootNode;
            rootNode = polynomial.rootNode;
            multiply(tmp.factor);
            return;
        }

        // newPolynomial - результирующий полином
        Polynomial newPolynomial = new Polynomial(0.0, 0);

        // thisNode - текущая нода данного полинома
        Node thisNode = rootNode;

        // итерируемся в цикле по всем нодам данного полинома
        while (thisNode != null) {
            // otherNode - текущая нода другого полинома
            Node otherNode = polynomial.rootNode;

            // итерируемся в цикле по всем нодам другого полинома
            while (otherNode != null) {
                // для каждой пары (thisNode, otherNode) находим ноду-результат умножения через умножение коэффициентов
                // и сложение степеней. После чего прибавляем получившуюся ноду к newPolynomial, обернув ее в новый
                // Polynomial
                newPolynomial.add(
                        new Polynomial(
                                thisNode.factor * otherNode.factor,
                                thisNode.power + otherNode.power
                        ) // ето не эффективно!
                );
                otherNode = otherNode.nextNode;
            }

            thisNode = thisNode.nextNode;
        }

        // Обновляем rootNode
        rootNode = newPolynomial.rootNode;
    }

    // multiply(double) умножает каждую ноду данного полинома на число number. При этом порядок нод не нарушится.
    // однако, если number ноль, то тогда весь полином обрашается в тождественный ноль.
    public void multiply(double number) {
        // если number 0, то удаляем все ноды и создаем нулевую
        if (isEqualToZero(number)) {
            rootNode = new Node(0.0, 0, null);
            return;
        }

        // проходим по всему списку нод и коэффициент каждой ноды умножаем на number
        Node node = rootNode;
        while (node != null) {
            node.factor *= number;
            node = node.nextNode;
        }
    }

    // calculate(double) вычисляет значения полинома в точке.
    public double calculate(double x) {
        return gornerSchema(x);
    }

    private double gornerSchema(double x) {
        // отдельно обрабатываем случай, когда в списке лежит одна нода, то есть p(x) = a*x^n
        // это необходимо, так как иначе результат будет равен только лишь a, то есть теряется x^n
        if (rootNode.nextNode == null) {
            return rootNode.factor * pow(x, rootNode.power);
        }

        Node node = rootNode;
        // в result всегда будет храниться значение "предшествующей скобки", изначально result равен коэффициенту
        // при члене многочлена с наивысшей степенью
        double result = rootNode.factor;

        // итерируемся по списку и считаем значение в точке x по схеме Горнера
        while (node.nextNode != null) {
            // pow(..) учитывает, что степени некоторых узлов могут идти не по порядку
            result = result * pow(x, node.power - node.nextNode.power) + node.nextNode.factor;
            node = node.nextNode;
        }

        return result;
    }

    private double pow(double x, int n) {
        double result = 1.0;

        for (int i = 0; i < n; i++) {
            result *= x;
        }

        return result;
    }

    // removeZeroElements() - проходит по списку всех нод и удаляет нулевые ноды (коэффициент равен 0). При этом
    // если все или единственная ноды равны нулевые, то сохраняется одна нулевая нода в качесвте корневой для
    // обозначения полинома, тождественно равного 0 при любых вещественных x
//    private void removeZeroElements() {
//        Node old = rootNode;
//        Node node = rootNode.nextNode;
//
//        while (node != null) {
//            if (Math.abs(node.factor) < EPSILON) {
//                old.nextNode = node.nextNode;
//            } else {
//                old = node;
//            }
//            node = node.nextNode;
//        }
//
//         не удаляем единственную нулевую ноду
//        if (Math.abs(rootNode.factor) < EPSILON && rootNode.nextNode != null) {
//            rootNode = rootNode.nextNode;
//        }
//    }
    // класс Node хранит коэффициент при слагаемом (factor), степень х (power) и ссылку на следующую ноду (nextNode)
    private static class Node {
        private double factor;
        private int power;
        private Node nextNode;

        public Node(double factor, int power, Node nextNode) {
            this.factor = factor;
            this.power = power;
            this.nextNode = nextNode;
        }
    }
}
