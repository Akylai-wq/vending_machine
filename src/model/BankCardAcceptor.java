package model;

public class BankCardAcceptor {
    private long bankCardNumber;
    private int passwordCard;
    private int amount;

    public BankCardAcceptor(int amount) {
        this.amount = amount;
    }

    public void setBankCardNumber(long bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
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