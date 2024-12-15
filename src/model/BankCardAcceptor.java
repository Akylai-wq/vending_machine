package model;

public class BankCardAcceptor {
    private int bankCardNumber;
    private int passwordCard;
    private int amount;

    public BankCardAcceptor(int amount) {
        this.amount = amount;
    }

    public int getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(int bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public int getPasswordCard() {
        return passwordCard;
    }

    public void setPasswordCard(int passwordCard) {
        this.passwordCard = passwordCard;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}