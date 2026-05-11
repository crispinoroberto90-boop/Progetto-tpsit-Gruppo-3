package calcolatrice;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calcolatrice {

    private JFrame frmCalcolatriceBar;
    private JTextField display;

    private double num1 = 0;
    private double num2 = 0;
    private double result = 0;

    private String op = "";
    private boolean newInput = true;

    private double memory = 0;
    private double grandTotal = 0;
    private double taxRate = 0.22;

    public static void main(String[] args) {
        new Calcolatrice();
    }

    public Calcolatrice() {

        frmCalcolatriceBar = new JFrame("Olivetti Style Calculator");
        frmCalcolatriceBar.setTitle("CALCOLATRICE BAR");
        frmCalcolatriceBar.setSize(420, 600);
        frmCalcolatriceBar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmCalcolatriceBar.getContentPane().setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);

        frmCalcolatriceBar.getContentPane().add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 5, 5));

        ActionListener numberListener = e -> {
            JButton b = (JButton) e.getSource();

            if (newInput) {
                display.setText(b.getText());
                newInput = false;
            } else {
                display.setText(display.getText() + b.getText());
            }
        };

        ActionListener opListener = e -> {
            JButton b = (JButton) e.getSource();
            num1 = Double.parseDouble(display.getText());
            op = b.getText();
            newInput = true;
        };

        JButton[] nums = new JButton[10];
        for (int i = 0; i < 10; i++) {
            nums[i] = new JButton(String.valueOf(i));
            nums[i].addActionListener(numberListener);
        }

        JButton add = new JButton("+");
        JButton sub = new JButton("-");
        JButton mul = new JButton("*");
        JButton div = new JButton("/");
        JButton eq = new JButton("=");
        JButton c = new JButton("C");

        add.addActionListener(opListener);
        sub.addActionListener(opListener);
        mul.addActionListener(opListener);
        div.addActionListener(opListener);

        eq.addActionListener(e -> {
            num2 = Double.parseDouble(display.getText());

            switch (op) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/":
                    if (num2 == 0) {
                        display.setText("ERROR");
                        return;
                    }
                    result = num1 / num2;
                    break;
            }

            display.setText(String.valueOf(result));
            grandTotal += result;
            newInput = true;
        });

        c.addActionListener(e -> {
            display.setText("0");
            num1 = num2 = result = 0;
            op = "";
            newInput = true;
        });

        JButton mPlus = new JButton("M+");
        JButton mMinus = new JButton("M-");
        JButton mr = new JButton("MR");
        JButton mc = new JButton("MC");

        mPlus.addActionListener(e -> memory += Double.parseDouble(display.getText()));
        mMinus.addActionListener(e -> memory -= Double.parseDouble(display.getText()));
        mr.addActionListener(e -> display.setText(String.valueOf(memory)));
        mc.addActionListener(e -> memory = 0);

        JButton tax = new JButton("TAX+");
        JButton taxMinus = new JButton("TAX-");

        tax.addActionListener(e -> {
            double v = Double.parseDouble(display.getText());
            display.setText(String.valueOf(v + (v * taxRate)));
        });

        taxMinus.addActionListener(e -> {
            double v = Double.parseDouble(display.getText());
            display.setText(String.valueOf(v - (v * taxRate)));
        });

        JButton gt = new JButton("GT");
        gt.addActionListener(e -> display.setText(String.valueOf(grandTotal)));

        JButton pct = new JButton("%");
        pct.addActionListener(e -> {
            double v = Double.parseDouble(display.getText());
            display.setText(String.valueOf(v / 100));
        });

        panel.add(mc);
        panel.add(mr);
        panel.add(mMinus);
        panel.add(mPlus);

        panel.add(tax);
        panel.add(taxMinus);
        panel.add(gt);
        panel.add(pct);

        panel.add(nums[7]);
        panel.add(nums[8]);
        panel.add(nums[9]);
        panel.add(div);

        panel.add(nums[4]);
        panel.add(nums[5]);
        panel.add(nums[6]);
        panel.add(mul);

        panel.add(nums[1]);
        panel.add(nums[2]);
        panel.add(nums[3]);
        panel.add(sub);

        panel.add(c);
        panel.add(nums[0]);
        panel.add(eq);
        panel.add(add);

        frmCalcolatriceBar.getContentPane().add(panel, BorderLayout.CENTER);
        frmCalcolatriceBar.setVisible(true);
    }
}
