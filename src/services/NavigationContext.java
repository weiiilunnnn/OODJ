/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author lunwe
 */
public class NavigationContext {
    private static String origin;

    public static String getOrigin() {
        return origin;
    }

    public static void setOrigin(String origin) {
        NavigationContext.origin = origin;
    }
}
