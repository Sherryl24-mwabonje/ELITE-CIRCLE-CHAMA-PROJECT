package ui;

import dao.MemberDAO;
import model.Member;
import util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.List;

public class MemberPanel extends JPanel {
    private final MemberDAO memberDAO = new MemberDAO();

    private final JTextField txtName = new JTextField(18);
    private final JTextField txtNationalId = new JTextField(18);
    private final JTextField txtPhone = new JTextField(18);
    private final JTextField txtEmail = new JTextField(18);
    private final JTextField txtJoinDate = new JTextField(LocalDate.now().toString(), 18);
    private final JTextField txtPledge = new JTextField(18);
    private final JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});

    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"ID", "Full Name", "National ID", "Phone", "Email",
                     "Join Date", "Monthly Pledge (KES)", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    public MemberPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        refreshTable();
    }

    private JPanel buildFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Member Details"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        addRow(form, gbc, 0, 0, "Full Name *", txtName);
        addRow(form, gbc, 0, 2, "National ID *", txtNationalId);
        addRow(form, gbc, 1, 0, "Phone *", txtPhone);
        addRow(form, gbc, 1, 2, "Email (optional)", txtEmail);
        addRow(form, gbc, 2, 0, "Join Date * (yyyy-MM-dd)", txtJoinDate);
        addRow(form, gbc, 2, 2, "Monthly Pledge (KES) *", txtPledge);
        addRow(form, gbc, 3, 0, "Status", cmbStatus);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> addMember());
        btnUpdate.addActionListener(e -> updateMember());
        btnDelete.addActionListener(e -> deleteMember());
        btnClear.addActionListener(e -> clearForm());
        btnRefresh.addActionListener(e -> refreshTable());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttons.add(btnAdd);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnClear);
        buttons.add(btnRefresh);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        form.add(buttons, gbc);

        return form;
    }

    private void addRow(JPanel p, GridBagConstraints gbc, int row, int col, String label, JComponent field) {
        gbc.gridwidth = 1;
        gbc.gridy = row;
        gbc.gridx = col;
        p.add(new JLabel(label), gbc);
        gbc.gridx = col + 1;
        p.add(field, gbc);
    }

    private Member readForm() {
        String name = Validator.requireText(txtName.getText(), "Full name");
        String nationalId = Validator.requireNationalId(txtNationalId.getText());
        String phone = Validator.requirePhone(txtPhone.getText());
        String email = Validator.optionalEmail(txtEmail.getText());
        LocalDate joinDate = Validator.requireDate(txtJoinDate.getText(), "Join date");
        double pledge = Validator.requirePositiveAmount(txtPledge.getText(), "Monthly pledge");
        String status = (String) cmbStatus.getSelectedItem();
        return new Member(name, nationalId, phone, email, joinDate, pledge, status);
    }

    private void addMember() {
        try {
            Member m = readForm();
            memberDAO.addMember(m);
            JOptionPane.showMessageDialog(this, "Member added successfully.");
            clearForm();
            refreshTable();
        } catch (IllegalArgumentException ex) {
            showWarning(ex.getMessage());
        } catch (SQLIntegrityConstraintViolationException ex) {
            showWarning("A member with that National ID already exists.");
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void updateMember() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showWarning("Select a member in the table first.");
            return;
        }
        try {
            Member m = readForm();
            m.setMemberId((int) tableModel.getValueAt(row, 0));
            memberDAO.updateMember(m);
            JOptionPane.showMessageDialog(this, "Member updated successfully.");
            clearForm();
            refreshTable();
        } catch (IllegalArgumentException ex) {
            showWarning(ex.getMessage());
        } catch (SQLIntegrityConstraintViolationException ex) {
            showWarning("Another member already uses that National ID.");
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void deleteMember() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showWarning("Select a member in the table first.");
            return;
        }
        String name = (String) tableModel.getValueAt(row, 1);
        int choice = JOptionPane.showConfirmDialog(this,
            "Delete " + name + "?\nTheir contributions will also be deleted.",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        try {
            memberDAO.deleteMember((int) tableModel.getValueAt(row, 0));
            JOptionPane.showMessageDialog(this, "Member deleted.");
            clearForm();
            refreshTable();
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    public void refreshTable() {
        try {
            List<Member> members = memberDAO.getAllMembers();
            tableModel.setRowCount(0);
            for (Member m : members) {
                tableModel.addRow(new Object[]{
                    m.getMemberId(), m.getFullName(), m.getNationalId(), m.getPhone(),
                    m.getEmail() == null ? "" : m.getEmail(),
                    m.getJoinDate().toString(), m.getMonthlyPledge(), m.getStatus()});
            }
        } catch (SQLException ex) {
            showDbError(ex);
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtName.setText((String) tableModel.getValueAt(row, 1));
        txtNationalId.setText((String) tableModel.getValueAt(row, 2));
        txtPhone.setText((String) tableModel.getValueAt(row, 3));
        txtEmail.setText((String) tableModel.getValueAt(row, 4));
        txtJoinDate.setText((String) tableModel.getValueAt(row, 5));
        txtPledge.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        cmbStatus.setSelectedItem(tableModel.getValueAt(row, 7));
    }

    private void clearForm() {
        txtName.setText("");
        txtNationalId.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtJoinDate.setText(LocalDate.now().toString());
        txtPledge.setText("");
        cmbStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.WARNING_MESSAGE);
    }

    private void showDbError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(),
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}