import enums.ActionLetter;
import enums.PaymentMethod;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private final BanknoteAcceptor banknoteAcceptor;

    private PaymentMethod paymentMethod;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        banknoteAcceptor = new BanknoteAcceptor(140);

    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        if (paymentMethod == null) {
            print("В автомате доступны:");
            showProducts(products);
            choosePaymentMethod();
        } else {
            balance();
        }
    }

    private UniversalArray<Product> balance() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());

        if (allowProducts.size() == 0) {
            print("На вашем балансе недостаточно средств.\nВы можете сменить способ оплаты: напишите (да/нет)");
            String answer = fromConsole().toLowerCase();
            if (answer.equals("да")) {
                choosePaymentMethod();
            } else if (answer.equals("нет")) {
                print("Пополните текущий баланс.");
                chooseAction(allowProducts);
            } else {
                print("Неверная команда!");
            }
        } else {
            chooseAction(allowProducts);
        }
        return allowProducts;
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (paymentMethod == PaymentMethod.COINS){
                if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            } else if (paymentMethod == PaymentMethod.BANKNOTE) {
                if (banknoteAcceptor.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            if (paymentMethod == PaymentMethod.COINS) {
                coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
                print("Вы пополнили баланс на 10\nМонет на сумму: " + coinAcceptor.getAmount());
                chooseAction(getAllowedProducts());
            } else if (paymentMethod == PaymentMethod.BANKNOTE) {
                banknoteAcceptor.setAmount(banknoteAcceptor.getAmount() + 20);
                print("Вы пополнили баланс на 20\nКупюр на сумму: " + banknoteAcceptor.getAmount());
                chooseAction(getAllowedProducts());
            }
        } else {
            try {
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                        if (paymentMethod == PaymentMethod.COINS) {
                            coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                            print("Вы купили " + products.get(i).getName() + "\nМонет на сумму: " + coinAcceptor.getAmount());
                            chooseAction(getAllowedProducts());
                        } else if (paymentMethod == PaymentMethod.BANKNOTE) {
                            banknoteAcceptor.setAmount(banknoteAcceptor.getAmount() - products.get(i).getPrice());
                            print("Вы купили " + products.get(i).getName() + "\nКупюр на сумму: " + banknoteAcceptor.getAmount());
                            chooseAction(getAllowedProducts());
                        }
                        break;
                    }
                }
            } catch (IllegalArgumentException e) {
                if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                } else {
                    print("Недопустимая буква. Попрбуйте еще раз.");
                    chooseAction(products);
                }
            }
        }
    }

    private void choosePaymentMethod() {
        while (paymentMethod == null) {
            try {
                print("Выберите способ оплаты: \n1. Монеты\n2. Купюра");
                int choice = fromConsoleInt();

                switch (choice) {
                    case 1:
                        paymentMethod = PaymentMethod.COINS;
                        print("Монет на сумму: " + coinAcceptor.getAmount());
                        balance();
                        break;
                    case 2:
                        paymentMethod = PaymentMethod.BANKNOTE;
                        print("Купюр на сумму: " + banknoteAcceptor.getAmount());
                        balance();
                        break;
                    default:
                        print("Недопустимая команда. Попрбуйте еще раз.");
                        print("f");
                        break;
                }
            } catch (InputMismatchException e) {
                print("Введите числовое значние " + e.getMessage());
            } catch (Exception e) {
                print("Введите числовое значние ");
            }
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private int fromConsoleInt() {
        return new Scanner(System.in).nextInt();
    }
    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
