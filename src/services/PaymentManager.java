/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;


/**
 *
 * @author shihongwong
 */

import java.util.List;
import models.Payment;

public class PaymentManager extends MainManager<Payment> {

    public PaymentManager() {
        super("Payment.txt");
    }

    @Override
    protected Payment parseLine(String line) {
        String[] parts = line.split(",");
        if (parts.length < 3) return null; // invalid line

        String paymentID = parts[0];
        String status = parts[1];
        String purchaseOrderID = parts[2];

        if ("PAID".equalsIgnoreCase(status) && parts.length == 7) {
            String accountName = parts[3];
            String accountNumber = parts[4];
            String userID = parts[5];
            String timestamp = parts[6];
            return new Payment(paymentID, status, purchaseOrderID, accountName, accountNumber, userID, timestamp);
        } else {
            // Unpaid or malformed PAID record
            return new Payment(paymentID, status, purchaseOrderID);
        }
    }

    @Override
    protected String toLine(Payment payment) {
        if ("PAID".equalsIgnoreCase(payment.getStatus())) {
            return String.join(",",
                payment.getPaymentID(),
                payment.getStatus(),
                payment.getPurchaseOrderID(),
                payment.getAccountName(),
                payment.getAccountNumber(),
                payment.getUserID(),
                payment.getTimestamp());
        } else {
            return String.join(",",
                payment.getPaymentID(),
                payment.getStatus(),
                payment.getPurchaseOrderID());
        }
    }

    @Override
    public void delete(String id, List<Payment> list) {
        list.removeIf(p -> p.getPaymentID().equals(id));
        save(list);
    }

    @Override
    public void update(Payment updated, List<Payment> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getPaymentID().equals(updated.getPaymentID())) {
                list.set(i, updated);
                save(list);
                return;
            }
        }
    }
    
    public Payment findPaymentByPO(String poID) {
        List<Payment> payments = load();
        for (Payment payment : payments) {
            if (payment.getPurchaseOrderID().equals(poID)) {
                return payment;
            }
        }
        return null; // Not found
    }
}
