import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class MembershipsListFrame extends JFrame implements ActionListener {
    private JButton importButton;
    private JButton importNewButton;
    private JButton exportNewButton;
    private JButton searchButton;
    private JButton checkInButton;
    private JButton addButton;
    private JButton checkInLogButton;
    private JButton visitorCheckInLogButton;
    private JLabel overallMembershipIncomeLabel;
    private JLabel overallTicketIncomeLabel;
    private JLabel overAllIncomeLabel;
    private JPanel buttonPanel;
    private JScrollPane tablePanel;
    private JPanel summaryPanel;
    private Container container;
    private MembershipManagement membershipManagement;
    private JTable jTable;

    private double overallMembershipFeeIncome = 0;
    private double overallTicketIncome = 0;

    public MembershipManagement getMembershipManagement() {
        return membershipManagement;
    }

    public MembershipsListFrame(MembershipManagement membershipManagement) {
        this.membershipManagement = membershipManagement;

        setTitle("Membership Management System");
        setSize(StaticData.WINDOW_WIDTH, StaticData.WINDOW_HEIGHT);
        setLocation(new Point(30, 30));

        this.initButton();

        this.initTable();
        this.initSummaryDetail();

        container = getContentPane();
        container.add(buttonPanel, "North");
        container.add(tablePanel, "Center");
        container.add(summaryPanel, "South");
    }

    public void initButton() {
        buttonPanel = new JPanel();
        importButton = addButton(buttonPanel, "Import Old Data", this);
        exportNewButton = addButton(buttonPanel, "Export New Data", this);
        importNewButton = addButton(buttonPanel, "Import New Data", this);
        searchButton = addButton(buttonPanel, "Search Member", this);
        checkInButton = addButton(buttonPanel, "Check in user", this);
        addButton = addButton(buttonPanel, "Add Member", this);
        checkInLogButton = addButton(buttonPanel, "Show check in log", this);
        visitorCheckInLogButton = addButton(buttonPanel, "Show visitor check in log", this);
    }

    public void initTable() {
        String[] columnNames = {"id", "First Name", "Last Name", "Birthday", "Gender", "Address", "Phone", "Health Condition", "Allergy Information", "Pay Type", "Start Time", "End Time", "Visitor Type", "Edit", "Delete"};
        Object[][] columnData = new Object[0][15];
        DefaultTableModel model = new DefaultTableModel(columnData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // only can change by edit button and delete button
                return column == 13 || column == 14;
            }
        };
        jTable = new JTable(model);
        JTableHeader header = jTable.getTableHeader();
        header.setBackground(Color.white);
        header.setForeground(Color.GRAY);
        jTable.getColumn("Edit").setCellRenderer(new ButtonRender());
        jTable.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox(), this));
        jTable.getColumn("Delete").setCellRenderer(new ButtonRender());
        jTable.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox(), this));
        jTable.setBounds(30, 40, 200, 300);
        tablePanel = new JScrollPane(jTable);
    }

    public void initSummaryDetail() {
        overallMembershipFeeIncome = membershipManagement.getOverallIncomeFromMembershipFee();
        overallMembershipIncomeLabel = new JLabel("Membership Income £" + overallMembershipFeeIncome);
        overallTicketIncomeLabel = new JLabel("Ticket Income £" + overallTicketIncome);
        overAllIncomeLabel = new JLabel("Overall Income £" + (overallMembershipFeeIncome + overallTicketIncome));
        summaryPanel = new JPanel(new FlowLayout());
        summaryPanel.add(overallTicketIncomeLabel);
        summaryPanel.add(overallMembershipIncomeLabel);
        summaryPanel.add(overAllIncomeLabel);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == importButton) {
            initMultipleRow();
        } else if (source == importNewButton) {
            membershipManagement.importCSV(StaticData.NEW_CSV_PATH);
            addMultipleRow();
        } else if (source == exportNewButton) {
            membershipManagement.exportCSV(StaticData.NEW_CSV_PATH);
        } else if (source == searchButton) {
            searchMember();
        } else if (source == addButton) {
            addMember();
        } else if (source == checkInButton) {
            checkInMember();
        } else if (source == checkInLogButton) {
            checkInLog();
        } else if (source == visitorCheckInLogButton) {
            visitorCheckInLog();
        } else if (actionEvent.getActionCommand().equals("Edit")) {
            int memberIndex = jTable.getSelectedRow();
            int memberId = Integer.parseInt(jTable.getModel().getValueAt(memberIndex, 0).toString());
            Membership member = membershipManagement.searchMember(memberId);
            if (member != null) {
                editMember(member);
            }
        } else if (actionEvent.getActionCommand().equals("Delete")) {
            int memberIndex = jTable.getSelectedRow();
            int memberId = Integer.parseInt(jTable.getModel().getValueAt(memberIndex, 0).toString());
            Membership member = membershipManagement.searchMember(memberId);
            if (member != null) {
                // proof the getSelectedRow return -1 after deleted
                if (jTable.isEditing()) jTable.getCellEditor().stopCellEditing();
                deleteMember(member, memberIndex);
            }
        }
    }

    public JButton addButton(JPanel panel, String name, ActionListener listener) {
        JButton button = new JButton(name);
        panel.add(button);
        button.addActionListener(listener);
        return button;
    }

    /**
     * update the overall data of home page (alway call by other page when checking or add/edit membership)
     */
    public void updateSummary() {
        overallMembershipFeeIncome = membershipManagement.getOverallIncomeFromMembershipFee();
        overallMembershipIncomeLabel.setText("Membership Income £" + overallMembershipFeeIncome);
        overallTicketIncome = membershipManagement.getOverallIncomeFromAccessFee();
        overallTicketIncomeLabel.setText("Ticket Income £" + overallTicketIncome);
        overAllIncomeLabel.setText("Overall Income £" + (overallMembershipFeeIncome + overallTicketIncome));
    }

    /**
     * Table add row (add membership)
     *
     * @param membership
     */
    public void addRow(Membership membership) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        String[] membershipStringArray = membershipManagement.getMembershipStringArray(membership);
        Object[] newMemberRow = {
                membershipStringArray[0],
                membershipStringArray[1],
                membershipStringArray[2],
                membershipStringArray[3],
                membershipStringArray[4],
                membershipStringArray[5],
                membershipStringArray[6],
                membershipStringArray[7],
                membershipStringArray[8],
                membershipStringArray[9],
                membershipStringArray[10],
                membershipStringArray[11],
                membershipStringArray[12],
                "Edit",
                "Delete"
        };
        model.addRow(newMemberRow);
        updateSummary();
    }

    /**
     * Table edit row (edit membership)
     *
     * @param membership
     */
    public void editRow(Membership membership) {
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        String[] membershipStringArray = membershipManagement.getMembershipStringArray(membership);
        for (int i = 0; i < model.getRowCount(); i++) {
            if (Long.parseLong(model.getValueAt(i, 0).toString()) == membership.getMemberId()) {
                for (int j = 0; j < 13; j++) {
                    model.setValueAt(membershipStringArray[j], i, j);
                }
                break;
            }
        }
        updateSummary();
    }

    /**
     * init the table
     */
    public void initMultipleRow() {
        membershipManagement.importCSV(StaticData.OLD_CSV_PATH);
        addMultipleRow();
    }

    /**
     * create table data (always from import)
     */
    public void addMultipleRow() {
        int length = membershipManagement.getMemberShipsByStringArray() != null ? membershipManagement.getMemberShipsByStringArray().length : 0;
        if (length > 0) {
            DefaultTableModel model = (DefaultTableModel) jTable.getModel();
            Object[][] membershipStringList = membershipManagement.getMemberShipsByStringArray();
            for (Object[] membership : membershipStringList) {
                Object[] newMemberRow = {
                        membership[0],
                        membership[1],
                        membership[2],
                        membership[3],
                        membership[4],
                        membership[5],
                        membership[6],
                        membership[7],
                        membership[8],
                        membership[9],
                        membership[10],
                        membership[11],
                        membership[12],
                        "Edit", "Delete"};
                model.addRow(newMemberRow);
            }
        } else {
            JOptionPane.showMessageDialog(null, "It seems not suitable file in the path", "No result", JOptionPane.PLAIN_MESSAGE);
        }
        updateSummary();
    }

    public void addMember() {
        new MembershipForm(this).setVisible(true);
    }

    public void editMember(Membership membership) {
        new MembershipForm(this, membership).setVisible(true);
    }

    public void checkInMember() {
        new UserCheckIn(this).setVisible(true);
    }

    public void checkInLog() {
        new CheckInLog(this, false).setVisible(true);
    }

    public void visitorCheckInLog() {
        new CheckInLog(this, true).setVisible(true);
    }

    public void deleteMember(Membership membership, int memberIndex) {
        membershipManagement.deleteMember(membership.getMemberId());
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        model.removeRow(memberIndex);
        updateSummary();
    }

    public void searchMember() {
        String inputValue = JOptionPane.showInputDialog("What do you want to search");
        DefaultTableModel model = (DefaultTableModel) jTable.getModel();
        Object[][] membershipStringList = membershipManagement.getMemberShipsByStringArray();
        for (int i = 0; i < membershipStringList.length; i++) {
            for (int j = 0; j < membershipStringList[i].length; j++) {
                if (membershipStringList[i][j].toString().equals(inputValue)) {
                    jTable.changeSelection(i, j, false, false);
                    return;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "It seems not result in this table, please check your input", "No result", JOptionPane.PLAIN_MESSAGE);
    }
}
