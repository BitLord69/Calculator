package com.JanInc;

/*
Obligatorisk uppgift 1 - miniräknare.

Programmerat av Jan-Erik "Janis" Karlsson 2019-09-17
Programmering i Java EMMJUH19, EC-Utbildning
CopyLeft 2019 - JanInc
*/

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.BinaryOperator;

public class Calculator {
    private final MenuChoice[] menu =
            {new MenuChoice("Addera", '1', this::add),
                    new MenuChoice("Subtrahera", '2', this::subtract),
                    new MenuChoice("Multiplicera", '3', this::multiply),
                    new MenuChoice("Dividera", '4', this::divide),
                    new MenuChoice("Kör senaste beräkning", '5', this::dummy),
                    new MenuChoice("Avsluta", '6', null)};
    private Scanner scan;
    private Object[] memory;

    public Calculator() {
        scan = new Scanner(System.in);
        memory = new Object[3];
    } // Calculator

    public double add(double term1, double term2) {
        return term1 + term2;
    } // add

    public double subtract(double minuend, double subtrahend) {
        return minuend - subtrahend;
    } // subtract

    public double multiply(double fakt1, double fakt2) {
        return fakt1 * fakt2;
    } // multiply

    public double divide(double dividend, double divisor) {

        if (divisor == 0.0) {
            System.out.println("Aja baja, nämnaren får ej vara noll");
            return 0;
        } // if fakt2...
        else
            return dividend / divisor;
    } // divide

    // Inte supersnyggt, men antingen detta eller extra kod i runCalculator för att ta hand om senaste operation från minnet
    public double dummy(double t1, double t2) {
        return 0;
    } // rerunLastOperation

    // Huvudloop som hanterar miniräknaren
    public void runCalculator() {
        boolean bStop = false;
        String sChoice;
        MenuChoice m;

        // Loopa tills användaren väljer att avsluta
        while (!bStop) {
            printMenu();
            m = getMenuChoice();
            if (m == null)
                System.out.println("Felaktigt val, försök igen!");
            else {
                System.out.printf("Du valde: %s%n", m.getTitle());
                bStop = m.operation == null;
                if (!bStop)
                    runOperation(m);
            } // else
        } // while

        System.out.print("\nTack för idag och välkommen åter!");
    } // runCalculator

    private void runOperation(MenuChoice m) {
        double[] tal = new double[2];
        BinaryOperator<Double> op;

        // Ska senaste operationen utföras från minnet?
        // I så fall, kopiera dessa från "minnet"
        if (m.getKey() == '5') {
            tal[0] = (double) memory[0];
            tal[1] = (double) memory[1];
            op = (BinaryOperator<Double>) memory[2];
        } else {
            // Annars, läs in operanderna från användaren och lägg dessa i minnet
            System.out.print("Ange tal 1: ");
            tal[0] = getOperand();
            System.out.print("Ange tal 2: ");
            tal[1] = getOperand();
            op = m.getOperation();

            memory[0] = tal[0];
            memory[1] = tal[1];
            memory[2] = m.getOperation();
        } // else

        // Anropa operationen med operandenra
        System.out.printf("Resultatet är: %.2f%n", op.apply(tal[0], tal[1]));
    } // runOperation

    // Hämta ett flyttal från användaren. Ser till så att man inte matar in fel, t.ex en sträng eller fel kommatecken
    private double getOperand() {
        boolean bBadInput = true;
        double tal = 0;

        do {
            try {
                tal = scan.nextDouble();
                if (scan.nextLine().equals(""))
                    bBadInput = false;
            } catch (InputMismatchException ex) {
                System.out.println("Hoppsan, det var visst inte ett flyttal. Försök igen!");
                scan.nextLine();
            } // catch
        } while (bBadInput);
        return tal;
    } // getOperand

    // Hämta användarens val
    private MenuChoice getMenuChoice() {
        String sChoice;

        // Se till så att det finns ett menyval och inte en tomrad (blir så efter nextDouble)
        do
            sChoice = scan.nextLine();
        while (sChoice.length() == 0);

        // Loopa igenom och returnera rätt menyval
        for (MenuChoice m : menu) {
            if (m.getKey() == sChoice.charAt(0))
                return m;
        } // for...

        // Fanns inget giltigt menyval, snopet - returnera null
        return null;
    } // getMenuChoice

    private void printMenu() {
        System.out.println("");
        for (MenuChoice m : menu) {
            System.out.printf("%s%n", m.getFullTitle());
        } // for m...
        System.out.print("Ange ditt val: ");
    } // printMenu

    private class MenuChoice {
        private char key;
        private String sTitle;
        private BinaryOperator<Double> operation;

        public MenuChoice(String sTitle, char key, BinaryOperator<Double> operation) {
            this.key = key;
            this.sTitle = sTitle;
            this.operation = operation;
        } // MenuChoice

        public BinaryOperator<Double> getOperation() {
            return operation;
        }

        public char getKey() {
            return key;
        }

        protected String getTitle() {
            return sTitle;
        } // getTitle

        protected String getFullTitle() {
            return getKey() + ". " + getTitle();
        } // getFullTitle
    } // inner class MenuChoice
} // Calculator