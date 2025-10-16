import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class BillingSystem extends JFrame implements ActionListener {
    JTextField itemField, qtyField, priceField;
    JLabel grandTotalLabel;
    JButton addBtn, removeBtn, clearBtn, printBtn, exitBtn;
    JTable table;
    DefaultTableModel model;
    double grandTotal = 0;

    public BillingSystem() {
        setTitle("Smart Billing System");
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JLabel header = new JLabel("Billing System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(33, 70, 139));
        add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));
        inputPanel.setBackground(Color.WHITE);

        itemField = new JTextField(10);
        qtyField = new JTextField(5);
        priceField = new JTextField(5);

        inputPanel.add(new JLabel("Item Name", SwingConstants.CENTER));
        inputPanel.add(new JLabel("Quantity", SwingConstants.CENTER));
        inputPanel.add(new JLabel("Price (₹)", SwingConstants.CENTER));
        inputPanel.add(new JLabel(""));

        inputPanel.add(itemField);
        inputPanel.add(qtyField);
        inputPanel.add(priceField);

        addBtn = new JButton("Add Item");
        addBtn.setBackground(new Color(67, 160, 71));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.addActionListener(this);
        inputPanel.add(addBtn);

        add(inputPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Item", "Qty", "Price", "Total"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        removeBtn = new JButton("Remove");
        clearBtn = new JButton("Clear All");
        printBtn = new JButton("Print Bill");
        exitBtn = new JButton("Exit");

        JButton[] buttons = {removeBtn, clearBtn, printBtn, exitBtn};
        Color[] colors = {new Color(229, 57, 53), new Color(30, 136, 229),
                          new Color(0, 150, 136), new Color(97, 97, 97)};
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(colors[i]);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 13));
            buttons[i].addActionListener(this);
            buttonPanel.add(buttons[i]);
        }

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel totalText = new JLabel("Grand Total: ");
        totalText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        grandTotalLabel = new JLabel("₹0.00");
        grandTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        grandTotalLabel.setForeground(new Color(46, 125, 50));
        totalPanel.add(totalText);
        totalPanel.add(grandTotalLabel);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        bottomPanel.add(totalPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addBtn) {
            String item = itemField.getText().trim();
            String qtyText = qtyField.getText().trim();
            String priceText = priceField.getText().trim();
            if (item.isEmpty() || qtyText.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }
            try {
                int qty = Integer.parseInt(qtyText);
                double price = Double.parseDouble(priceText);
                double total = qty * price;
                model.addRow(new Object[]{item, qty, price, total});
                grandTotal += total;
                grandTotalLabel.setText("₹" + String.format("%.2f", grandTotal));
                itemField.setText("");
                qtyField.setText("");
                priceField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers!");
            }
        } else if (e.getSource() == removeBtn) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                double total = (double) model.getValueAt(row, 3);
                grandTotal -= total;
                grandTotalLabel.setText("₹" + String.format("%.2f", grandTotal));
                model.removeRow(row);
            }
        } else if (e.getSource() == clearBtn) {
            model.setRowCount(0);
            grandTotal = 0;
            grandTotalLabel.setText("₹0.00");
        } else if (e.getSource() == printBtn) {
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items to print!");
                return;
            }
            StringBuilder bill = new StringBuilder("------- BILL RECEIPT -------\n\n");
            for (int i = 0; i < model.getRowCount(); i++) {
                bill.append(model.getValueAt(i, 0)).append("  x")
                    .append(model.getValueAt(i, 1)).append("  ₹")
                    .append(model.getValueAt(i, 3)).append("\n");
            }
            bill.append("\n----------------------------\n");
            bill.append("Total: ₹").append(String.format("%.2f", grandTotal));
            JOptionPane.showMessageDialog(this, bill.toString(), "Print Bill", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingSystem::new);
    }
}