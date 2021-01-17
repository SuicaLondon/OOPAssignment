

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;

public class MembershipForm extends JFrame implements ActionListener {
    private MembershipsListFrame membershipsListFrame;
    private MembershipManagement membershipManagement;
    private Membership membership;
    private Membership editedMember;
    private Membership newMember;
    private TextFieldWithPlaceHolder lastNameTextField;
    private TextFieldWithPlaceHolder firstNameTextField;
    private TextFieldWithPlaceHolder birthdayTextField;
    private TextFieldWithPlaceHolder addressTextField;
    private TextFieldWithPlaceHolder phoneNumberTextField;
    private TextFieldWithPlaceHolder healthConditionTextField;
    private TextFieldWithPlaceHolder allergyInformationTextField;
    private JButton saveButton;
    private JButton resetButton;
    private Dimension labelDimension = new Dimension(200, 20);
    private ButtonGroup genderButtonGroup;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    private ButtonGroup membershipTypeButtonGroup;
    private JRadioButton individualMembership;
    private JRadioButton familyMembership;
    private ButtonGroup payTypeButtonGroup;
    private JRadioButton monthly;
    private JRadioButton quarterly;
    private JRadioButton yearly;
    private JPanel formColumnPanel;
    private Container container;

    /**
     * Constructor of add membership
     * @param membershipsListFrame
     */
    MembershipForm(MembershipsListFrame membershipsListFrame) {
        this.membershipsListFrame = membershipsListFrame;
        this.membershipManagement = membershipsListFrame.getMembershipManagement();
        setTitle("Membership Form");
        setContentPane(getContentPane());
        setSize(StaticData.WINDOW_WIDTH, StaticData.WINDOW_HEIGHT);
        setLocation(new Point(40, 40));

        initItems();

        container = getContentPane();
        container.add(formColumnPanel);
    }

    /**
     * Constructor of edit membership
     * @param membershipsListFrame
     * @param memberShip
     */
    MembershipForm(MembershipsListFrame membershipsListFrame, Membership memberShip) {
        this.membershipsListFrame = membershipsListFrame;
        this.membershipManagement = membershipsListFrame.getMembershipManagement();
        this.membership = memberShip;

        setTitle("Membership Form");
        setContentPane(getContentPane());
        setSize(StaticData.WINDOW_WIDTH, StaticData.WINDOW_HEIGHT);
        setLocation(new Point(30, 30));

        initItems();
        initItemsWithMemberInformation();

        container = getContentPane();
        container.add(formColumnPanel);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if (source == saveButton) {
            saveMembership();
        }
    }

    /**
     * Save the change when added/edited membership
     */
    public void saveMembership() {
        if (checkHasEmpty()) {
            return;
        }
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String phone = phoneNumberTextField.getText();
        Gender gender = genderButtonGroup.getSelection().getActionCommand().equals(Gender.MALE.toString()) ? Gender.MALE : Gender.FEMALE;
        String memberShipType = membershipTypeButtonGroup.getSelection().getActionCommand();
        PayType payType = payTypeButtonGroup.getSelection().getActionCommand().equals("monthly") ? PayType.MONTHLY : (payTypeButtonGroup.getSelection().getActionCommand().equals("quarterly") ? PayType.QUARTERLY : PayType.YEARLY);
        Date birthday = DateConverter.stringToDate(birthdayTextField.getText());
        String healthCondition = healthConditionTextField.getRawText();
        String allergyDetail = allergyInformationTextField.getRawText();
        if (membership == null) { // add new member
            Membership newMembership;
            if (memberShipType.equals("Family")) {
                newMembership = membershipManagement.addFamilyMember(firstName, lastName, birthday, gender, address, phone, payType, healthCondition, allergyDetail);
                // add function will return when user is too young
            } else {
                newMembership = membershipManagement.addIndividualMember(firstName, lastName, birthday, gender, address, phone, payType, healthCondition, allergyDetail);
            }
            if (newMembership == null) {
                displayWarningDialog("Your are too young", "Something wrong");
                return;
            }
            membershipsListFrame.addRow(newMembership);
        } else { // edit member
            membership.setLastName(lastNameTextField.getText());
            membership.setFirstName(firstNameTextField.getText());
            membership.setBirthday(birthday);
            membership.setGender(gender);
            membership.setAddress(addressTextField.getText());
            membership.setTelephone(phoneNumberTextField.getText());
            membership.setPayType(payType);
            membership.setHealthCondition(healthCondition);
            membership.setAllergyInformation(allergyDetail);
            Boolean isSuccess = membershipManagement.editMember(membership.getMemberId(), membership);
            if (isSuccess) {
                membershipsListFrame.editRow(membership);
            } else {
                displayWarningDialog("Your are too young", "Something wrong");
            }
        }
        this.setVisible(false);
    }

    /**
     * Check if the essential input is empty
     * @return
     */
    public boolean checkHasEmpty() {
        if (lastNameTextField.getRawText().isEmpty()) {
            displayWarningDialog(lastNameTextField.getPlaceholder(), "Missing some information");
            return true;
        }
        if (firstNameTextField.getRawText().isEmpty()) {
            displayWarningDialog(firstNameTextField.getPlaceholder(), "Missing some information");
            return true;
        }
        if (birthdayTextField.getRawText().isEmpty()) {
            displayWarningDialog(birthdayTextField.getPlaceholder(), "Missing some information");
            return true;
        }
        if (addressTextField.getRawText().isEmpty()) {
            displayWarningDialog(addressTextField.getPlaceholder(), "Missing some information");
            return true;
        }
        if (phoneNumberTextField.getRawText().isEmpty()) {
            displayWarningDialog(phoneNumberTextField.getPlaceholder(), "Missing some information");
            return true;
        }
        return false;
    }

    public void displayWarningDialog(String text, String title) {
        showMessageDialog(getContentPane(), text, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * init all UI component
     */
    public void initItems() {
        formColumnPanel = new JPanel();
        formColumnPanel.setLayout(new BoxLayout(formColumnPanel, BoxLayout.Y_AXIS));
        formColumnPanel.setBounds(61, 11, 81, 140);
        lastNameTextField = addTextField("Last name:", formColumnPanel, "Please input your last name");
        firstNameTextField = addTextField("First name:", formColumnPanel, "Please input your first name");
        birthdayTextField = addTextField("Birthday:", formColumnPanel, "Please input your birthday, eg. 31/12/1998");

        addGenderRadioButton();

        addressTextField = addTextField("Address:", formColumnPanel, "Please input your address");
        phoneNumberTextField = addTextField("Phone:", formColumnPanel, "Please input your phone number");

        healthConditionTextField = addTextField("Health condition(Optional):", formColumnPanel, "Please input your health condition detail");
        allergyInformationTextField = addTextField("Allergy information(Optional):", formColumnPanel, "If you have any allergy history, pleas input here");

        addMembershipTypeRadioButton();
        addPayTypeRadioButton();
        initButton();
    }

    /**
     * init the data when it is editing mode
     */
    public void initItemsWithMemberInformation() {
        lastNameTextField.setRawText(membership.getLastName());
        firstNameTextField.setRawText(membership.getFirstName());
        if (membership.getBirthday() != null) {
            birthdayTextField.setRawText(DateConverter.dateToString(membership.getBirthday()));
        }
        if (membership.getGender() == Gender.MALE) {
            maleButton.setSelected(true);
        } else if (membership.getGender() == Gender.FEMALE) {
            femaleButton.setSelected(true);
        }
        addressTextField.setRawText(membership.getAddress());
        phoneNumberTextField.setRawText(membership.getTelephone());
        if (membership instanceof IndividualMembership) {
            individualMembership.setSelected(true);
        } else {
            familyMembership.setSelected(true);
        }
        if (membership.getPayType() == PayType.MONTHLY) {
            monthly.setSelected(true);
        } else if (membership.getPayType() == PayType.QUARTERLY) {
            quarterly.setSelected(true);
        } else {
            yearly.setSelected(true);
        }
        healthConditionTextField.setRawText(membership.getHealthCondition());
        allergyInformationTextField.setRawText(membership.getAllergyInformation());

    }

    public TextFieldWithPlaceHolder addTextField(String title, JPanel containerPanel, String placeholder) {
        JLabel label = new JLabel(title);
        label.setMinimumSize(labelDimension);
        label.setPreferredSize(labelDimension);
        label.setMaximumSize(labelDimension);
        TextFieldWithPlaceHolder textField = new TextFieldWithPlaceHolder(40, placeholder);
        textField.addActionListener(this);
        JPanel panel = new JPanel();
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        containerPanel.add(panel);
        return textField;
    }

    public void addGenderRadioButton() {
        genderButtonGroup = new ButtonGroup();
        maleButton = new JRadioButton(Gender.MALE.toString());
        maleButton.setActionCommand(Gender.MALE.toString());
        maleButton.setSelected(true);
        femaleButton = new JRadioButton(Gender.FEMALE.toString());
        femaleButton.setActionCommand(Gender.FEMALE.toString());
        genderButtonGroup.add(maleButton);
        genderButtonGroup.add(femaleButton);
        JPanel genderPanel = new JPanel();
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        formColumnPanel.add(genderPanel);
    }

    public void addMembershipTypeRadioButton() {
        membershipTypeButtonGroup = new ButtonGroup();
        individualMembership = new JRadioButton("Individual(£36/Month)");
        individualMembership.setActionCommand("Individual");
        individualMembership.setSelected(true);
        familyMembership = new JRadioButton("Family(£60/Month)");
        familyMembership.setActionCommand("Family");
        membershipTypeButtonGroup.add(individualMembership);
        membershipTypeButtonGroup.add(familyMembership);
        JPanel membershipTypePanel = new JPanel();
        membershipTypePanel.add(individualMembership);
        membershipTypePanel.add(familyMembership);
        formColumnPanel.add(membershipTypePanel);
    }

    public void addPayTypeRadioButton() {
        payTypeButtonGroup = new ButtonGroup();
        monthly = new JRadioButton("monthly");
        monthly.setActionCommand("monthly");
        monthly.setSelected(true);
        quarterly = new JRadioButton("quarterly");
        quarterly.setActionCommand("quarterly");
        yearly = new JRadioButton("yearly");
        yearly.setActionCommand("yearly");
        payTypeButtonGroup.add(monthly);
        payTypeButtonGroup.add(quarterly);
        payTypeButtonGroup.add(yearly);
        JPanel membershipTypePanel = new JPanel();
        membershipTypePanel.add(monthly);
        membershipTypePanel.add(quarterly);
        membershipTypePanel.add(yearly);
        formColumnPanel.add(membershipTypePanel);
    }

    public void initButton() {
        JPanel buttonPanel = new JPanel();
        saveButton = addButton(buttonPanel, "Save", this);
//        resetButton = addButton(buttonPanel, "Reset", this);
        formColumnPanel.add(buttonPanel);
    }

    public JButton addButton(JPanel panel, String name, ActionListener listener) {
        JButton button = new JButton(name);
        panel.add(button);
        button.addActionListener(listener);
        return button;
    }
}
