import enums.ActionLetter;
import enums.PaymentMethod;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;

    private final BankCardAcceptor bankCardAcceptor;

    private PaymentMethod currentPaymentMethod;

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
        bankCardAcceptor = new BankCardAcceptor(150);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        if (currentPaymentMethod == null) {
            choosePaymentMethod();
        }
        while (!isExit) {
            print("В автомате доступны:");
            showProducts(products);

            UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
            allowProducts.addAll(getAllowedProducts().toArray());
            if (allowProducts.size() == 0) {
                print("На вашем балансе недостаточно средств.\nВы можете сменить способ оплаты: (да/нет)");
                String answer = fromConsole().toLowerCase();
                if (answer.equals("да")) {
                    choosePaymentMethod();
                } else {
                    print("Пополните текущий баланс.");
                }
            } else {
                chooseAction(allowProducts);
            }
        }
    }

    private void choosePaymentMethod() {
        print("Доступна оплата через банковскую карту и монетами.\nВыберите способ оплаты:\n* Карта\n* Монеты");
        String choice = fromConsole().toLowerCase();

        switch (choice){
            case "карта":
                currentPaymentMethod = PaymentMethod.BANKCARD;
                printBankCard();
                break;
            case "монеты":
                currentPaymentMethod = PaymentMethod.COINS;
                print("Монет на сумму: " + coinAcceptor.getAmount());
                break;
            default:
                print("Недопустимая команда. Попрбуйте еще раз.");
                break;
        }
    }

    private void printBankCard() {
        print("Введите номер карты:");
        long bankCardNumber = fromConsoleLong();
        bankCardAcceptor.setBankCardNumber(bankCardNumber);
        print("Введите четырехзначный пароль от карты:");
        int passwordCard = fromConsoleInt();
        bankCardAcceptor.setPasswordCard(passwordCard);
        print("Баланс карты: " + bankCardAcceptor.getAmount());
    }


    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (currentPaymentMethod == PaymentMethod.COINS){
                if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                    allowProducts.add(products.get(i));
                }
            } else if (currentPaymentMethod == PaymentMethod.BANKCARD) {
                if (bankCardAcceptor.getAmount() >= products.get(i).getPrice()) {
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
            if (currentPaymentMethod == PaymentMethod.COINS) {
                coinAcceptor.setAmount(coinAcceptor.getAmount() + 10);
                print("Вы пополнили баланс на 10\nМонет на сумму:" + coinAcceptor.getAmount());
            } else if (currentPaymentMethod == PaymentMethod.BANKCARD) {
                bankCardAcceptor.setAmount(bankCardAcceptor.getAmount() + 20);
                print("Вы пополнили баланс на 20\nБаланс карты:" + bankCardAcceptor.getAmount());
            }
        } else {
            try {
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                        if (currentPaymentMethod == PaymentMethod.COINS) {
                            coinAcceptor.setAmount(coinAcceptor.getAmount() - products.get(i).getPrice());
                            print("Вы купили " + products.get(i).getName() + "\nМонет на сумму: " + coinAcceptor.getAmount());
                        } else if (currentPaymentMethod == PaymentMethod.BANKCARD) {
                            bankCardAcceptor.setAmount(bankCardAcceptor.getAmount() - products.get(i).getPrice());
                            print("Вы купили " + products.get(i).getName() + "\nБаланс карты: " + bankCardAcceptor.getAmount());
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

    private long fromConsoleLong() {
        return new Scanner(System.in).nextLong();
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
