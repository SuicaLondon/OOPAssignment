import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

public class CheckInLog extends JFrame {
    private MembershipsListFrame membershipsListFrame;
    private MembershipManagement membershipManagement;
    private String[][] accessRecordArrayList;
    private String[][] visitorAccessRecordArrayList;
    private JTable jTable = new JTable();
    private JScrollPane tablePanel;
    private Container container;
    private boolean isVisitorOnly;

    public CheckInLog(MembershipsListFrame membershipsListFrame, Boolean isVisitor) throws HeadlessException {
        this.membershipsListFrame = membershipsListFrame;
        this.membershipManagement = membershipsListFrame.getMembershipManagement();
        this.accessRecordArrayList = MembershipManagement.getAccessRecordsStringsList();
        this.visitorAccessRecordArrayList = MembershipManagement.getVisitorAccessRecordsStringsList();
        this.isVisitorOnly = isVisitor;
        setTitle("Membership Management System");
        setSize(StaticData.WINDOW_WIDTH / 2, StaticData.WINDOW_HEIGHT);
        setLocation(new Point(30, 30));

        this.initTable();

        container = getContentPane();
        container.add(tablePanel);
    }

    public void initTable() {
        String[] columnNames;
        if(isVisitorOnly) {
            // visitor dont have member id
            columnNames = new String[]{"Facility Type", "Visit Type", "Time"};
        }else  {
            columnNames = new String[]{"Member ID", "Facility Type", "Visit Type", "Time"};
        }
        Object[][] columnData = isVisitorOnly ? visitorAccessRecordArrayList :accessRecordArrayList;
        DefaultTableModel model = new DefaultTableModel(columnData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        jTable = new JTable(model);
        JTableHeader header = jTable.getTableHeader();
        header.setBackground(Color.white);
        header.setForeground(Color.GRAY);
        jTable.setBounds(30, 40, 200, 300);
        tablePanel = new JScrollPane(jTable);
    }
}
