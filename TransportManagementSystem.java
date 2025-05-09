import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class TransportManagementSystem extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel vehiclePanel;
    private JPanel driverPanel;
    private JPanel routePanel;
    private JPanel bookingPanel;
    private JPanel userPanel;

    // Vehicle components
    private JTextField plateNumberField;
    private JTextField modelField;
    private JComboBox<String> typeCombo;
    private JTextField capacityField;
    private JTextField purchaseDateField;
    private JTextField insuranceExpiryField;
    private JComboBox<String> statusCombo;
    private JTable vehicleTable;
    private DefaultTableModel vehicleTableModel;
    private VehicleDAO vehicleDAO;

    // Driver components
    private JTextField driverNameField;
    private JTextField licenseNumberField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField dobField;
    private JTextField userIdField;
    private JTable driverTable;
    private DefaultTableModel driverTableModel;
    private DriverDAO driverDAO;

    // Route components
    private JTextField originField;
    private JTextField destinationField;
    private JTextField distanceKmField;
    private JTextField estimatedTimeField;
    private JCheckBox tollRequiredCheckBox;
    private JComboBox<String> roadTypeCombo;
    private JTable routeTable;
    private DefaultTableModel routeTableModel;
    private RouteDAO routeDAO;

    // User components
    private JTextField usernameField;
    private JTextField passwordHashField;
    private JTextField emailField;
    private JComboBox<String> roleCombo;
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private UserDAO userDAO;

    // Booking components
    private JTextField bookingUserIdField;
    private JTextField bookingVehicleIdField;
    private JTextField bookingDateField;
    private JComboBox<String> bookingStatusCombo;
    private JTextField bookingRemarksField;
    private JTable bookingTable;
    private DefaultTableModel bookingTableModel;
    private BookingDAO bookingDAO;

    public TransportManagementSystem() {
        setTitle("Transport Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        vehicleDAO = new VehicleDAO();
        driverDAO = new DriverDAO();
        routeDAO = new RouteDAO();
        userDAO = new UserDAO();
        bookingDAO = new BookingDAO();

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Initialize panels
        vehiclePanel = createVehiclePanel();
        driverPanel = createDriverPanel();
        routePanel = createRoutePanel();
        bookingPanel = createBookingPanel();
        userPanel = createUserPanel();

        // Add panels to tabbed pane
        tabbedPane.addTab("Vehicles", vehiclePanel);
        tabbedPane.addTab("Drivers", driverPanel);
        tabbedPane.addTab("Routes", routePanel);
        tabbedPane.addTab("Bookings", bookingPanel);
        tabbedPane.addTab("Users", userPanel);

        add(tabbedPane);

        // Load initial data
        refreshVehicleTable();
        refreshDriverTable();
        refreshRouteTable();
        refreshUserTable();
        refreshBookingTable();
    }

    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Plate Number:"));
        plateNumberField = new JTextField();
        inputPanel.add(plateNumberField);

        inputPanel.add(new JLabel("Model:"));
        modelField = new JTextField();
        inputPanel.add(modelField);

        inputPanel.add(new JLabel("Type:"));
        typeCombo = new JComboBox<>(new String[] { "truck", "van", "bus", "car" });
        inputPanel.add(typeCombo);

        inputPanel.add(new JLabel("Capacity:"));
        capacityField = new JTextField();
        inputPanel.add(capacityField);

        inputPanel.add(new JLabel("Purchase Date (yyyy-mm-dd):"));
        purchaseDateField = new JTextField();
        inputPanel.add(purchaseDateField);

        inputPanel.add(new JLabel("Insurance Expiry (yyyy-mm-dd):"));
        insuranceExpiryField = new JTextField();
        inputPanel.add(insuranceExpiryField);

        inputPanel.add(new JLabel("Status:"));
        statusCombo = new JComboBox<>(new String[] { "available", "maintenance", "on_trip", "retired" });
        inputPanel.add(statusCombo);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Vehicle");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table Panel
        String[] columns = { "ID", "Plate Number", "Model", "Type", "Capacity", "Purchase Date", "Insurance Expiry",
                "Status" };
        vehicleTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        vehicleTable = new JTable(vehicleTableModel);
        JScrollPane tableScrollPane = new JScrollPane(vehicleTable);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addVehicle());
        updateButton.addActionListener(e -> updateVehicle());
        deleteButton.addActionListener(e -> deleteVehicle());
        clearButton.addActionListener(e -> clearVehicleFields());

        // Add table selection listener
        vehicleTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = vehicleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    plateNumberField.setText(vehicleTable.getValueAt(selectedRow, 1).toString());
                    modelField.setText(vehicleTable.getValueAt(selectedRow, 2).toString());
                    typeCombo.setSelectedItem(vehicleTable.getValueAt(selectedRow, 3).toString());
                    capacityField.setText(vehicleTable.getValueAt(selectedRow, 4).toString());
                    purchaseDateField.setText(vehicleTable.getValueAt(selectedRow, 5) == null ? ""
                            : vehicleTable.getValueAt(selectedRow, 5).toString());
                    insuranceExpiryField.setText(vehicleTable.getValueAt(selectedRow, 6) == null ? ""
                            : vehicleTable.getValueAt(selectedRow, 6).toString());
                    statusCombo.setSelectedItem(vehicleTable.getValueAt(selectedRow, 7).toString());
                }
            }
        });

        return panel;
    }

    private JPanel createDriverPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Name:"));
        driverNameField = new JTextField();
        inputPanel.add(driverNameField);

        inputPanel.add(new JLabel("License Number:"));
        licenseNumberField = new JTextField();
        inputPanel.add(licenseNumberField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        inputPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        inputPanel.add(addressField);

        inputPanel.add(new JLabel("Date of Birth (yyyy-mm-dd):"));
        dobField = new JTextField();
        inputPanel.add(dobField);

        inputPanel.add(new JLabel("User ID:"));
        userIdField = new JTextField();
        inputPanel.add(userIdField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Driver");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table Panel
        String[] columns = { "ID", "Name", "License Number", "Phone", "Address", "DOB", "User ID" };
        driverTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        driverTable = new JTable(driverTableModel);
        JScrollPane tableScrollPane = new JScrollPane(driverTable);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addDriver());
        updateButton.addActionListener(e -> updateDriver());
        deleteButton.addActionListener(e -> deleteDriver());
        clearButton.addActionListener(e -> clearDriverFields());

        // Add table selection listener
        driverTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = driverTable.getSelectedRow();
                if (selectedRow >= 0) {
                    driverNameField.setText(driverTable.getValueAt(selectedRow, 1).toString());
                    licenseNumberField.setText(driverTable.getValueAt(selectedRow, 2).toString());
                    phoneField.setText(driverTable.getValueAt(selectedRow, 3) == null ? ""
                            : driverTable.getValueAt(selectedRow, 3).toString());
                    addressField.setText(driverTable.getValueAt(selectedRow, 4) == null ? ""
                            : driverTable.getValueAt(selectedRow, 4).toString());
                    dobField.setText(driverTable.getValueAt(selectedRow, 5) == null ? ""
                            : driverTable.getValueAt(selectedRow, 5).toString());
                    userIdField.setText(driverTable.getValueAt(selectedRow, 6) == null ? ""
                            : driverTable.getValueAt(selectedRow, 6).toString());
                }
            }
        });

        return panel;
    }

    private JPanel createRoutePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Origin:"));
        originField = new JTextField();
        inputPanel.add(originField);

        inputPanel.add(new JLabel("Destination:"));
        destinationField = new JTextField();
        inputPanel.add(destinationField);

        inputPanel.add(new JLabel("Distance (km):"));
        distanceKmField = new JTextField();
        inputPanel.add(distanceKmField);

        inputPanel.add(new JLabel("Estimated Time:"));
        estimatedTimeField = new JTextField();
        inputPanel.add(estimatedTimeField);

        inputPanel.add(new JLabel("Toll Required:"));
        tollRequiredCheckBox = new JCheckBox();
        inputPanel.add(tollRequiredCheckBox);

        inputPanel.add(new JLabel("Road Type:"));
        roadTypeCombo = new JComboBox<>(new String[] { "highway", "city", "rural" });
        inputPanel.add(roadTypeCombo);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Route");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table Panel
        String[] columns = { "ID", "Origin", "Destination", "Distance (km)", "Estimated Time", "Toll Required",
                "Road Type" };
        routeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        routeTable = new JTable(routeTableModel);
        JScrollPane tableScrollPane = new JScrollPane(routeTable);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addRoute());
        updateButton.addActionListener(e -> updateRoute());
        deleteButton.addActionListener(e -> deleteRoute());
        clearButton.addActionListener(e -> clearRouteFields());

        // Add table selection listener
        routeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = routeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    originField.setText(routeTable.getValueAt(selectedRow, 1).toString());
                    destinationField.setText(routeTable.getValueAt(selectedRow, 2).toString());
                    distanceKmField.setText(routeTable.getValueAt(selectedRow, 3).toString());
                    estimatedTimeField.setText(routeTable.getValueAt(selectedRow, 4).toString());
                    tollRequiredCheckBox
                            .setSelected(Boolean.parseBoolean(routeTable.getValueAt(selectedRow, 5).toString()));
                    roadTypeCombo.setSelectedItem(routeTable.getValueAt(selectedRow, 6).toString());
                }
            }
        });

        refreshRouteTable();
        return panel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("User ID:"));
        bookingUserIdField = new JTextField();
        inputPanel.add(bookingUserIdField);

        inputPanel.add(new JLabel("Vehicle ID:"));
        bookingVehicleIdField = new JTextField();
        inputPanel.add(bookingVehicleIdField);

        inputPanel.add(new JLabel("Booking Date (yyyy-mm-dd):"));
        bookingDateField = new JTextField();
        inputPanel.add(bookingDateField);

        inputPanel.add(new JLabel("Status:"));
        bookingStatusCombo = new JComboBox<>(new String[] { "pending", "confirmed", "cancelled", "completed" });
        inputPanel.add(bookingStatusCombo);

        inputPanel.add(new JLabel("Remarks:"));
        bookingRemarksField = new JTextField();
        inputPanel.add(bookingRemarksField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Booking");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table Panel
        String[] columns = { "Booking ID", "User ID", "Vehicle ID", "Booking Date", "Status", "Remarks" };
        bookingTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(bookingTableModel);
        JScrollPane tableScrollPane = new JScrollPane(bookingTable);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // DAO
        bookingDAO = new BookingDAO();

        // Add action listeners
        addButton.addActionListener(e -> addBooking());
        updateButton.addActionListener(e -> updateBooking());
        deleteButton.addActionListener(e -> deleteBooking());
        clearButton.addActionListener(e -> clearBookingFields());

        // Add table selection listener
        bookingTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = bookingTable.getSelectedRow();
                if (selectedRow >= 0) {
                    bookingUserIdField.setText(bookingTable.getValueAt(selectedRow, 1).toString());
                    bookingVehicleIdField.setText(bookingTable.getValueAt(selectedRow, 2).toString());
                    bookingDateField.setText(bookingTable.getValueAt(selectedRow, 3) == null ? ""
                            : bookingTable.getValueAt(selectedRow, 3).toString());
                    bookingStatusCombo.setSelectedItem(bookingTable.getValueAt(selectedRow, 4).toString());
                    bookingRemarksField.setText(bookingTable.getValueAt(selectedRow, 5) == null ? ""
                            : bookingTable.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        refreshBookingTable();
        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Password Hash:"));
        passwordHashField = new JTextField();
        inputPanel.add(passwordHashField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Role:"));
        roleCombo = new JComboBox<>(new String[] { "admin", "manager", "driver" });
        inputPanel.add(roleCombo);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add User");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // Table Panel
        String[] columns = { "ID", "Username", "Password Hash", "Email", "Role", "Created At" };
        userTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        userTable = new JTable(userTableModel);
        JScrollPane tableScrollPane = new JScrollPane(userTable);

        // Add components to panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(tableScrollPane, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());
        clearButton.addActionListener(e -> clearUserFields());

        // Add table selection listener
        userTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow >= 0) {
                    usernameField.setText(userTable.getValueAt(selectedRow, 1).toString());
                    passwordHashField.setText(userTable.getValueAt(selectedRow, 2).toString());
                    emailField.setText(userTable.getValueAt(selectedRow, 3) == null ? ""
                            : userTable.getValueAt(selectedRow, 3).toString());
                    roleCombo.setSelectedItem(userTable.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        refreshUserTable();
        return panel;
    }

    private void addVehicle() {
        try {
            String plateNumber = plateNumberField.getText();
            String model = modelField.getText();
            String type = (String) typeCombo.getSelectedItem();
            int capacity = Integer.parseInt(capacityField.getText());
            java.sql.Date purchaseDate = purchaseDateField.getText().isEmpty() ? null
                    : java.sql.Date.valueOf(purchaseDateField.getText());
            java.sql.Date insuranceExpiry = insuranceExpiryField.getText().isEmpty() ? null
                    : java.sql.Date.valueOf(insuranceExpiryField.getText());
            String status = (String) statusCombo.getSelectedItem();

            vehicleDAO.addVehicle(plateNumber, model, type, capacity, purchaseDate, insuranceExpiry, status);
            refreshVehicleTable();
            clearVehicleFields();
            JOptionPane.showMessageDialog(this, "Vehicle added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding vehicle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid capacity number",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter dates in yyyy-mm-dd format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to update");
            return;
        }

        try {
            int vehicleId = (int) vehicleTable.getValueAt(selectedRow, 0);
            String plateNumber = plateNumberField.getText();
            String model = modelField.getText();
            String type = (String) typeCombo.getSelectedItem();
            int capacity = Integer.parseInt(capacityField.getText());
            java.sql.Date purchaseDate = purchaseDateField.getText().isEmpty() ? null
                    : java.sql.Date.valueOf(purchaseDateField.getText());
            java.sql.Date insuranceExpiry = insuranceExpiryField.getText().isEmpty() ? null
                    : java.sql.Date.valueOf(insuranceExpiryField.getText());
            String status = (String) statusCombo.getSelectedItem();

            vehicleDAO.updateVehicle(vehicleId, plateNumber, model, type, capacity, purchaseDate, insuranceExpiry,
                    status);
            refreshVehicleTable();
            clearVehicleFields();
            JOptionPane.showMessageDialog(this, "Vehicle updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating vehicle: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid capacity number",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter dates in yyyy-mm-dd format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this vehicle?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int vehicleId = (int) vehicleTable.getValueAt(selectedRow, 0);
                vehicleDAO.deleteVehicle(vehicleId);
                refreshVehicleTable();
                clearVehicleFields();
                JOptionPane.showMessageDialog(this, "Vehicle deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting vehicle: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearVehicleFields() {
        plateNumberField.setText("");
        modelField.setText("");
        typeCombo.setSelectedIndex(0);
        capacityField.setText("");
        purchaseDateField.setText("");
        insuranceExpiryField.setText("");
        statusCombo.setSelectedIndex(0);
        vehicleTable.clearSelection();
    }

    private void refreshVehicleTable() {
        vehicleTableModel.setRowCount(0);
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            for (Vehicle vehicle : vehicles) {
                vehicleTableModel.addRow(new Object[] {
                        vehicle.getVehicleId(),
                        vehicle.getPlateNumber(),
                        vehicle.getModel(),
                        vehicle.getType(),
                        vehicle.getCapacity(),
                        vehicle.getPurchaseDate(),
                        vehicle.getInsuranceExpiry(),
                        vehicle.getStatus()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading vehicles: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDriver() {
        try {
            String name = driverNameField.getText();
            String licenseNumber = licenseNumberField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            java.sql.Date dob = dobField.getText().isEmpty() ? null : java.sql.Date.valueOf(dobField.getText());
            int userId = Integer.parseInt(userIdField.getText());

            driverDAO.addDriver(name, licenseNumber, phone, address, dob, userId);
            refreshDriverTable();
            clearDriverFields();
            JOptionPane.showMessageDialog(this, "Driver added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding driver: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid User ID",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in yyyy-mm-dd format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDriver() {
        int selectedRow = driverTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a driver to update");
            return;
        }

        try {
            int driverId = (int) driverTable.getValueAt(selectedRow, 0);
            String name = driverNameField.getText();
            String licenseNumber = licenseNumberField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            java.sql.Date dob = dobField.getText().isEmpty() ? null : java.sql.Date.valueOf(dobField.getText());
            int userId = Integer.parseInt(userIdField.getText());

            driverDAO.updateDriver(driverId, name, licenseNumber, phone, address, dob, userId);
            refreshDriverTable();
            clearDriverFields();
            JOptionPane.showMessageDialog(this, "Driver updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating driver: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid User ID",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in yyyy-mm-dd format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDriver() {
        int selectedRow = driverTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a driver to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this driver?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int driverId = (int) driverTable.getValueAt(selectedRow, 0);
                driverDAO.deleteDriver(driverId);
                refreshDriverTable();
                clearDriverFields();
                JOptionPane.showMessageDialog(this, "Driver deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting driver: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearDriverFields() {
        driverNameField.setText("");
        licenseNumberField.setText("");
        phoneField.setText("");
        addressField.setText("");
        dobField.setText("");
        userIdField.setText("");
        driverTable.clearSelection();
    }

    private void refreshDriverTable() {
        driverTableModel.setRowCount(0);
        try {
            List<Driver> drivers = driverDAO.getAllDrivers();
            for (Driver driver : drivers) {
                driverTableModel.addRow(new Object[] {
                        driver.getDriverId(),
                        driver.getName(),
                        driver.getLicenseNumber(),
                        driver.getPhone(),
                        driver.getAddress(),
                        driver.getDob(),
                        driver.getUserId()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading drivers: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRoute() {
        try {
            String origin = originField.getText();
            String destination = destinationField.getText();
            float distanceKm = Float.parseFloat(distanceKmField.getText());
            String estimatedTime = estimatedTimeField.getText();
            boolean tollRequired = tollRequiredCheckBox.isSelected();
            String roadType = (String) roadTypeCombo.getSelectedItem();

            routeDAO.addRoute(origin, destination, distanceKm, estimatedTime, tollRequired, roadType);
            refreshRouteTable();
            clearRouteFields();
            JOptionPane.showMessageDialog(this, "Route added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding route: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid distance (number)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateRoute() {
        int selectedRow = routeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a route to update");
            return;
        }

        try {
            int routeId = (int) routeTable.getValueAt(selectedRow, 0);
            String origin = originField.getText();
            String destination = destinationField.getText();
            float distanceKm = Float.parseFloat(distanceKmField.getText());
            String estimatedTime = estimatedTimeField.getText();
            boolean tollRequired = tollRequiredCheckBox.isSelected();
            String roadType = (String) roadTypeCombo.getSelectedItem();

            routeDAO.updateRoute(routeId, origin, destination, distanceKm, estimatedTime, tollRequired, roadType);
            refreshRouteTable();
            clearRouteFields();
            JOptionPane.showMessageDialog(this, "Route updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating route: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid distance (number)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteRoute() {
        int selectedRow = routeTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a route to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this route?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int routeId = (int) routeTable.getValueAt(selectedRow, 0);
                routeDAO.deleteRoute(routeId);
                refreshRouteTable();
                clearRouteFields();
                JOptionPane.showMessageDialog(this, "Route deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting route: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearRouteFields() {
        originField.setText("");
        destinationField.setText("");
        distanceKmField.setText("");
        estimatedTimeField.setText("");
        tollRequiredCheckBox.setSelected(false);
        roadTypeCombo.setSelectedIndex(0);
        routeTable.clearSelection();
    }

    private void refreshRouteTable() {
        routeTableModel.setRowCount(0);
        try {
            List<Route> routes = routeDAO.getAllRoutes();
            for (Route route : routes) {
                routeTableModel.addRow(new Object[] {
                        route.getRouteId(),
                        route.getOrigin(),
                        route.getDestination(),
                        route.getDistanceKm(),
                        route.getEstimatedTime(),
                        route.isTollRequired(),
                        route.getRoadType()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading routes: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addUser() {
        try {
            String username = usernameField.getText();
            String passwordHash = passwordHashField.getText();
            String email = emailField.getText();
            String role = (String) roleCombo.getSelectedItem();

            userDAO.addUser(username, passwordHash, email, role);
            refreshUserTable();
            clearUserFields();
            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to update");
            return;
        }

        try {
            int userId = (int) userTable.getValueAt(selectedRow, 0);
            String username = usernameField.getText();
            String passwordHash = passwordHashField.getText();
            String email = emailField.getText();
            String role = (String) roleCombo.getSelectedItem();

            userDAO.updateUser(userId, username, passwordHash, email, role);
            refreshUserTable();
            clearUserFields();
            JOptionPane.showMessageDialog(this, "User updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating user: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this user?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int userId = (int) userTable.getValueAt(selectedRow, 0);
                userDAO.deleteUser(userId);
                refreshUserTable();
                clearUserFields();
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearUserFields() {
        usernameField.setText("");
        passwordHashField.setText("");
        emailField.setText("");
        roleCombo.setSelectedIndex(0);
        userTable.clearSelection();
    }

    private void refreshUserTable() {
        userTableModel.setRowCount(0);
        try {
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                userTableModel.addRow(new Object[] {
                        user.getUserId(),
                        user.getUsername(),
                        user.getPasswordHash(),
                        user.getEmail(),
                        user.getRole(),
                        user.getCreatedAt()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBooking() {
        try {
            int userId = Integer.parseInt(bookingUserIdField.getText());
            int vehicleId = Integer.parseInt(bookingVehicleIdField.getText());
            java.sql.Date bookingDate = bookingDateField.getText().isEmpty() ? null
                    : java.sql.Date.valueOf(bookingDateField.getText());
            String status = (String) bookingStatusCombo.getSelectedItem();
            String remarks = bookingRemarksField.getText();

            bookingDAO.addBooking(userId, vehicleId, bookingDate, status, remarks);
            refreshBookingTable();
            clearBookingFields();
            JOptionPane.showMessageDialog(this, "Booking added successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding booking: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid IDs (numbers)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in yyyy-mm-dd format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to update");
            return;
        }

        try {
            int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
            int userId = Integer.parseInt(bookingUserIdField.getText());
            int vehicleId = Integer.parseInt(bookingVehicleIdField.getText());
            java.sql.Date bookingDate = bookingDateField.getText().isEmpty() ? null
                    : java.sql.Date.valueOf(bookingDateField.getText());
            String status = (String) bookingStatusCombo.getSelectedItem();
            String remarks = bookingRemarksField.getText();

            bookingDAO.updateBooking(bookingId, userId, vehicleId, bookingDate, status, remarks);
            refreshBookingTable();
            clearBookingFields();
            JOptionPane.showMessageDialog(this, "Booking updated successfully!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating booking: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid IDs (numbers)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "Please enter date in yyyy-mm-dd format",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to delete");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this booking?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int bookingId = (int) bookingTable.getValueAt(selectedRow, 0);
                bookingDAO.deleteBooking(bookingId);
                refreshBookingTable();
                clearBookingFields();
                JOptionPane.showMessageDialog(this, "Booking deleted successfully!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting booking: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearBookingFields() {
        bookingUserIdField.setText("");
        bookingVehicleIdField.setText("");
        bookingDateField.setText("");
        bookingStatusCombo.setSelectedIndex(0);
        bookingRemarksField.setText("");
        bookingTable.clearSelection();
    }

    private void refreshBookingTable() {
        bookingTableModel.setRowCount(0);
        try {
            List<Booking> bookings = bookingDAO.getAllBookings();
            for (Booking booking : bookings) {
                bookingTableModel.addRow(new Object[] {
                        booking.getBookingId(),
                        booking.getUserId(),
                        booking.getVehicleId(),
                        booking.getBookingDate(),
                        booking.getStatus(),
                        booking.getRemarks()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TransportManagementSystem().setVisible(true);
        });
    }
}