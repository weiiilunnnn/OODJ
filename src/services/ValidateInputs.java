/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.awt.Component;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JOptionPane;
/**
 *
 * @author lunwe
 */
public class ValidateInputs {

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isAlphabetic(String value) {
        return value.matches("[a-zA-Z]+");
    }

    public static boolean containsAlphabet(String value) {
        return value.matches(".*[a-zA-Z]+.*");
    }

    public static boolean isNumeric(String value) {
        return value.matches("\\d+");
    }

    public static boolean isDecimal(String value) {
        return value.matches("\\d+(\\.\\d+)?");
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return email.matches(emailRegex);
    }

    public static boolean isLengthAtLeast(String value, int minLength) {
        return value != null && value.trim().length() >= minLength;
    }
    
    public static boolean validateIntegerField(String valueStr, int minValue, String fieldName, Component parentComponent) {
        try {
            int value = Integer.parseInt(valueStr.trim());
            if (value < minValue) {
                JOptionPane.showMessageDialog(parentComponent, fieldName + " must be " + minValue + " or more.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parentComponent, fieldName + " must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public static boolean validateDecimalField(String valueStr, double minValue, String fieldName, Component parentComponent) {
        try {
            double value = Double.parseDouble(valueStr.trim());
            if (value < minValue) {
                JOptionPane.showMessageDialog(parentComponent, fieldName + " must be " + minValue + " or more.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parentComponent, fieldName + " must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    // === Supplier Entry Validation ===

    public static boolean validateSupplierFields(String name, String contact, String address, String distance) {
        if (isEmpty(name) || isEmpty(contact) || isEmpty(address) || isEmpty(distance)) {
            JOptionPane.showMessageDialog(null, "Insufficient Credentials. Supplier Entry Declined.");
            return false;
        }

        if (containsAlphabet(contact)) {
            JOptionPane.showMessageDialog(null, "Invalid Contact Number. Letters are not allowed.");
            return false;
        }

        if (containsAlphabet(distance)) {
            JOptionPane.showMessageDialog(null, "Invalid Distance. Letters are not allowed.");
            return false;
        }

        return true;
    }

    public static boolean validateItemFields(String name, String qtyStr, String priceStr, String rolStr, Component parentComponent) {
        name = name.trim();

        if (isEmpty(name)) {
            JOptionPane.showMessageDialog(parentComponent, "Item name cannot be empty.");
            return false;
        }

        if (!containsAlphabet(name)) {
            JOptionPane.showMessageDialog(parentComponent, "Item name must contain at least one letter.");
            return false;
        }

        if (!isLengthAtLeast(name, 3)) {
            JOptionPane.showMessageDialog(parentComponent, "Item name must be at least 3 characters long.");
            return false;
        }

        if (!validateIntegerField(qtyStr, 0, "Quantity", parentComponent)) return false;
        if (!validateDecimalField(priceStr, 0, "Price", parentComponent)) return false;
        if (!validateIntegerField(rolStr, 0, "Reorder Level", parentComponent)) return false;

        return true;
    }
    
    public static boolean validateTodayOrFutureDate(Date selectedDate, Component parentComponent, String fieldName) {
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(parentComponent, "Please select a " + fieldName + ".", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Strip time and compare only date
        LocalDate today = LocalDate.now();
        LocalDate selected = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (selected.isBefore(today)) {
            JOptionPane.showMessageDialog(parentComponent, fieldName + " cannot be in the past.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public static boolean validatePRFields(String restockQtyStr, Date requiredDate, Component parentComponent) {
        if (!validateIntegerField(restockQtyStr, 1, "Restock Quantity", parentComponent)) return false;
        return validateTodayOrFutureDate(requiredDate, parentComponent, "Required Date");
    }

    public static boolean validatePOFields(String supplierId, String restockQtyStr, Date purchasedDate, Component parentComponent) {
        if (isEmpty(supplierId)) {
            JOptionPane.showMessageDialog(parentComponent, "Please select a supplier.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!validateIntegerField(restockQtyStr, 1, "Restock quantity", parentComponent)) return false;
        return validateTodayOrFutureDate(purchasedDate, parentComponent, "Required Date");
    }
    
    
    public static boolean isQuantityChanged(int originalQty, String inputQtyStr) {
        try {
            int inputQty = Integer.parseInt(inputQtyStr);
            return inputQty != originalQty;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
