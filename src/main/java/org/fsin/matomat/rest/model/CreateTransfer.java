package org.fsin.matomat.rest.model;

public class CreateTransfer {
    private int sender;
    private int receiver;
    private int amount;

    //IMPORTNAT: if sender is null it may not be an error it just means
    // that the current user is sending the money
    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public int getReceiver() {
        return receiver;
    }

    public void setReceiver(int receiver) {
        this.receiver = receiver;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
