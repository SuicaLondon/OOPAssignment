import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserCheckIn extends JFrame implements ActionListener {
    private MembershipsListFrame membershipsListFrame;
    private MembershipManagement membershipManagement;
    private JButton individualMember;
    private JButton familyMemberButton;
    private JButton childrenMember;
    private JButton visitor;
    private Container container;
    private Panel cardPanel = new Panel();
    private Panel controlPanel = new Panel();
    private AccessRecord.VisitType selectedType = AccessRecord.VisitType.INDIVIDUAL;
    private ButtonGroup facilitiesGroup = new ButtonGroup();
    private JButton checkInButton;
    private TextFieldWithPlaceHolder individualTextField;
    private TextFieldWithPlaceHolder familyTextField;
    private TextFieldWithPlaceHolder childrenTextField;


    private CardLayout cardLayout = new CardLayout();

    public UserCheckIn(MembershipsListFrame membershipsListFrame) {
        this.membershipsListFrame = membershipsListFrame;
        this.membershipManagement = membershipsListFrame.getMembershipManagement();
        setTitle("Membership Management System");
        setSize(StaticData.WINDOW_WIDTH / 2, StaticData.WINDOW_HEIGHT / 2);
        setLocation(new Point(60, 60));

        checkInButton = addCheckInButton();
        initCard();

        container = getContentPane();
        container.add(controlPanel, BorderLayout.NORTH);
        container.add(cardPanel, BorderLayout.CENTER);
        container.add(checkInButton, BorderLayout.SOUTH);
    }

    public void initCard() {
        cardPanel.setLayout(cardLayout);

        individualTextField = new TextFieldWithPlaceHolder(40, "User's ID");
        individualTextField.addActionListener(this);
        familyTextField = new TextFieldWithPlaceHolder(40, "User's ID");
        familyTextField.addActionListener(this);
        childrenTextField = new TextFieldWithPlaceHolder(40, "User's ID");
        childrenTextField.addActionListener(this);
        cardPanel.add("individual", createMemberIDTextField(individualTextField));
        cardPanel.add("family", createMemberIDTextField(familyTextField));
        cardPanel.add("children", createMemberIDTextField(childrenTextField));
        cardPanel.add("visitor", createVisitorPanel());

        individualMember = new JButton("individualMember");
        individualMember.addActionListener(this);
        familyMemberButton = new JButton("Family");
        familyMemberButton.addActionListener(this);
        childrenMember = new JButton("Children");
        childrenMember.addActionListener(this);
        visitor = new JButton("Visitor");
        visitor.addActionListener(this);

        controlPanel.add(individualMember);
        controlPanel.add(familyMemberButton);
        controlPanel.add(childrenMember);
        controlPanel.add(visitor);
    }

    public JPanel createMemberIDTextField(TextFieldWithPlaceHolder textField) {
        JPanel vPanel = new JPanel();
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Input user's ID (simulate the membership card reader)");
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.SOUTH);
        vPanel.add(initFacilityTypeButton());
        vPanel.add(panel);
        return vPanel;
    }

    public JPanel createVisitorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel facilitiesPanel = initFacilityTypeButton();
        panel.add(facilitiesPanel, BorderLayout.NORTH);
        Object[] tableTitle = {"Swimming Pool", "Gym", "Yoga", "Aerobics"};
        Object[][] tableData = {{
                Facility.getFacilityFee(Facility.FacilityType.SWIMMING_POOL),
                Facility.getFacilityFee(Facility.FacilityType.GYM),
                Facility.getFacilityFee(Facility.FacilityType.YOGA),
                Facility.getFacilityFee(Facility.FacilityType.AEROBICS)
        }};
        DefaultTableModel model = new DefaultTableModel(tableData, tableTitle) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable jTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(jTable);
        scrollPane.setMaximumSize(new Dimension(StaticData.WINDOW_WIDTH, 300));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public JPanel initFacilityTypeButton() {
        facilitiesGroup = new ButtonGroup();
        JRadioButton swimming = new JRadioButton("Swimming Pool");
        swimming.setActionCommand("Swim");
        swimming.setSelected(true);
        JRadioButton gym = new JRadioButton("Gym");
        gym.setActionCommand("Gym");
        JRadioButton yoga = new JRadioButton("Yoga");
        yoga.setActionCommand("Yoga");
        JRadioButton aerobics = new JRadioButton("Aerobics");
        aerobics.setActionCommand("Aerobics");
        JPanel panel = new JPanel();
        panel.add(swimming);
        panel.add(gym);
        panel.add(yoga);
        panel.add(aerobics);
        facilitiesGroup.add(swimming);
        facilitiesGroup.add(gym);
        facilitiesGroup.add(yoga);
        facilitiesGroup.add(aerobics);
        return panel;
    }

    public JButton addCheckInButton() {
        JButton button = new JButton("Check in");
        button.addActionListener(this);
        return button;
    }

    public void checkIn() {
        switch (selectedType) {
            case INDIVIDUAL:
                if (checkTextFieldEmpty(individualTextField)) return;
                IndividualCheckInd();
                break;
            case FAMILY:
                if (checkTextFieldEmpty(familyTextField)) return;
                familyCheckInd();
                break;
            case CHILDREN:
                if (checkTextFieldEmpty(childrenTextField)) return;
                childrenCheckIn();
                break;
            case VISITOR:
                visitorCheckIn();
                break;
        }
    }
    public boolean checkTextFieldEmpty(TextFieldWithPlaceHolder textField) {
        if(textField.getRawText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please input user's ID", "Need ID", JOptionPane.WARNING_MESSAGE);
            return true;
        }
        return false;
    }

    public void IndividualCheckInd() {
        Boolean isSuccess = membershipManagement.individualCheckIn(Long.parseLong(individualTextField.getText()), Facility.getFacilityType(facilitiesGroup.getSelection().getActionCommand()));
        afterCheckIn(isSuccess);
    }

    public void familyCheckInd() {
        Boolean isSuccess = membershipManagement.familyCheckIn(Long.parseLong(familyTextField.getText()), Facility.getFacilityType(facilitiesGroup.getSelection().getActionCommand()));
        afterCheckIn(isSuccess);
    }

    public void childrenCheckIn() {
        Boolean isSuccess = membershipManagement.childrenCheckIn(Long.parseLong(childrenTextField.getText()), Facility.getFacilityType(facilitiesGroup.getSelection().getActionCommand()));
        afterCheckIn(isSuccess);
    }

    public void visitorCheckIn() {
        Boolean isSuccess = membershipManagement.visitorCheckIn(Facility.getFacilityType(facilitiesGroup.getSelection().getActionCommand()));
        afterCheckIn(isSuccess);
    }

    public void afterCheckIn(Boolean isSuccess) {
        if (isSuccess) {
            membershipsListFrame.updateSummary();
            this.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(null, "UserID wrong", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == individualMember) {
            cardLayout.show(cardPanel, "individual");
            selectedType = AccessRecord.VisitType.INDIVIDUAL;
        } else if (actionEvent.getSource() == familyMemberButton) {
            cardLayout.show(cardPanel, "family");
            selectedType = AccessRecord.VisitType.FAMILY;
        } else if (actionEvent.getSource() == childrenMember) {
            cardLayout.show(cardPanel, "children");
            selectedType = AccessRecord.VisitType.CHILDREN;
        } else if (actionEvent.getSource() == visitor) {
            cardLayout.show(cardPanel, "visitor");
            selectedType = AccessRecord.VisitType.VISITOR;
        } else if (actionEvent.getSource() == checkInButton) {
            checkIn();
        }
    }

}
