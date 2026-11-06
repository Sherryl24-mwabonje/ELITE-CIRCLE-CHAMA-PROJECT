package ui;

import dao.ContributionDAO;
import model.Contribution;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ContributionPanel extends JPanel {

    private ContributionDAO contributionDAO;

    private JTable contributionTable;
    private DefaultTableModel tableModel;

    private JTextField memberIdField;
    private JTextField amountField;
    private JTextField dateField;
    private JTextField paymentMethodField;
    private JTextField referenceCodeField;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    public ContributionPanel() {

        contributionDAO = new ContributionDAO();

        setLayout(new BorderLayout());

        // Create contribution form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        formPanel.add(new JLabel("Member ID:"));
        memberIdField = new JTextField();
        formPanel.add(memberIdField);

        formPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        formPanel.add(amountField);

        formPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Payment Method:"));
        paymentMethodField = new JTextField();
        formPanel.add(paymentMethodField);

        formPanel.add(new JLabel("Reference Code:"));
        referenceCodeField = new JTextField();
        formPanel.add(referenceCodeField);

        // Create buttons
        JPanel buttonPanel = new JPanel();

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Create contributions table
        tableModel = new DefaultTableModel(
            new String[]{"ID", "Member ID", "Amount", "Date",
                         "Payment Method", "Reference Code"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        contributionTable = new JTable(tableModel);

        // Arrange components
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(contributionTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        addButton.addActionListener(e -> addContribution());
        updateButton.addActionListener(e -> updateContribution());
        deleteButton.addActionListener(e -> deleteContribution());
        clearButton.addActionListener(e -> clearFields());

        // Select a table row to edit
        contributionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                displaySelectedContribution();
            }
        });

        loadContributions();
    }

    // Clear input fields
    private void clearFields() {
        memberIdField.setText("");
        amountField.setText("");
        dateField.setText("");
        paymentMethodField.setText("");
        referenceCodeField.setText("");

        contributionTable.clearSelection();
    }

    // Read and validate form information
    private Contribution getFormContribution() {

        int memberId = Integer.parseInt(memberIdField.getText().trim());
        double amount = Double.parseDouble(amountField.getText().trim());

        LocalDate date = LocalDate.parse(dateField.getText().trim());

        String paymentMethod = paymentMethodField.getText().trim();
        String referenceCode = referenceCodeField.getText().trim();

        if (memberId <= 0 || amount <= 0 ||
            paymentMethod.isEmpty() || referenceCode.isEmpty()) {
            throw new IllegalArgumentException("Please enter valid details.");
        }

        return new Contribution(
            memberId, amount, date, paymentMethod, referenceCode
        );
    }

    // Add contribution
    private void addContribution() {
        try {
            Contribution contribution = getFormContribution();

            contributionDAO.addContribution(contribution);

            JOptionPane.showMessageDialog(this,
                "Contribution added successfully!");

            clearFields();
            loadContributions();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Member ID and amount must be valid numbers.");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Date must be in YYYY-MM-DD format.");

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                "Could not add contribution: " + e.getMessage());
        }
    }

    // Display selected contribution in form
    private void displaySelectedContribution() {

        int selectedRow = contributionTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        memberIdField.setText(
            tableModel.getValueAt(selectedRow, 1).toString()
        );

        amountField.setText(
            tableModel.getValueAt(selectedRow, 2).toString()
        );

        dateField.setText(
            tableModel.getValueAt(selectedRow, 3).toString()
        );

        paymentMethodField.setText(
            tableModel.getValueAt(selectedRow, 4).toString()
        );

        referenceCodeField.setText(
            tableModel.getValueAt(selectedRow, 5).toString()
        );
    }

    // Update contribution
    private void updateContribution() {

        int selectedRow = contributionTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a contribution to update.");
            return;
        }

        try {
            int contributionId = Integer.parseInt(
                tableModel.getValueAt(selectedRow, 0).toString()
            );

            Contribution contribution = getFormContribution();
            contribution.setContributionId(contributionId);

            contributionDAO.updateContribution(contribution);

            JOptionPane.showMessageDialog(this,
                "Contribution updated successfully!");

            clearFields();
            loadContributions();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Member ID and amount must be valid numbers.");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                "Date must be in YYYY-MM-DD format.");

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                "Could not update contribution: " + e.getMessage());
        }
    }

    // Delete contribution
    private void deleteContribution() {

        int selectedRow = contributionTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Please select a contribution to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this contribution?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {

            try {
                int contributionId = Integer.parseInt(
                    tableModel.getValueAt(selectedRow, 0).toString()
                );

                contributionDAO.deleteContribution(contributionId);

                JOptionPane.showMessageDialog(this,
                    "Contribution deleted successfully!");

                clearFields();
                loadContributions();

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                    "Could not delete contribution: " + e.getMessage());
            }
        }
    }

    // Load contributions from database
    private void loadContributions() {

        tableModel.setRowCount(0);

        try {
            List<Contribution> contributions =
                contributionDAO.getAllContributions();

            for (Contribution c : contributions) {
                tableModel.addRow(new Object[]{
                    c.getContributionId(),
                    c.getMemberId(),
                    c.getAmount(),
                    c.getContributionDate(),
                    c.getPaymentMethod(),
                    c.getReferenceCode()
                });
            }

        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                "Could not load contributions: " + e.getMessage());
        }
    }
}
