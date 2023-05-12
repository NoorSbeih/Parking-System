package com.example.parking;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Objects;

public class Main extends Application {


    Connection con=DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/parking_lot","root","root");

    public Main() throws SQLException {
    }

    @Override
    public void start(Stage stage) {
        stage.setMaximized(true);
        stage.setTitle("Parking Lot Database Management System");
        stage.setScene(login(stage));
        stage.show();
    }



    public Scene login(Stage stage ){
        GridPane root = new GridPane();
        root.setId("pane");
        root.setAlignment(Pos.CENTER);
        TextField emailText = new TextField();
        Label emailLabel = new Label("email address:");
        Label passwordLabel = new Label("password:");
        PasswordField passwdTxt = new PasswordField();
        VBox texts = new VBox(emailText,passwdTxt);



        Button customerButt = new Button("Login as customer");
        Button empButt = new Button("Login as employee");
        HBox buttons = new HBox(customerButt,empButt);
        buttons.setSpacing(150);
        Hyperlink reg = new Hyperlink("Dont have an account? click here to register"    );
        reg.setId("link");
        reg.setOnAction(e-> stage.setScene(registerMenu(stage)));
        VBox labels = new VBox(emailLabel,passwordLabel,reg);
        labels.setSpacing(35);
        texts.setSpacing(20);
        HBox inputs = new HBox(labels,texts);
        inputs.setSpacing(20);

        VBox all = new VBox(inputs,buttons);
        all.setSpacing(30);

        customerButt.setOnAction(e-> {
            try{
                PreparedStatement pstmt = con.prepareStatement("SELECT Customerid from customer s where Email =  (?) and password like binary  (?)");
                pstmt.setString(1, emailText.getText());
                pstmt.setString(2, passwdTxt.getText());

                ResultSet rs = pstmt.executeQuery();
                StringBuilder result = new StringBuilder();
                if(rs.next())
                    result.append(rs.getString(1));




                if(result.toString().isEmpty()){
                    Label error = new Label("Invalid email or password");
                    root.getChildren().add(error);
                    error.setId("error");
                    error.setTranslateY(-150);
                }
                else {
                    stage.setScene(customerMenu(stage,result.toString()));
                }


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


        });

        empButt.setOnAction(e->{
            try{
                PreparedStatement pstmt = con.prepareStatement("SELECT Emp_ID from employee s where Emp_email =  (?) and password like binary  (?)");
                pstmt.setString(1, emailText.getText());
                pstmt.setString(2, passwdTxt.getText());

                ResultSet rs = pstmt.executeQuery();
               /* ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();*/
                StringBuilder result = new StringBuilder();
                if(rs.next())
                    result.append(rs.getString(1));



                System.out.println(result);
                if(result.toString().isEmpty()){
                    Label error = new Label("Invalid email or password");
                    root.getChildren().add(error);
                    error.setId("error");
                    error.setTranslateY(-150);
                }
                else {
                    stage.setScene(empMenu(stage,result.toString()));
                }


            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


        });

        root.add(all,0,0);

        Scene scene = new Scene(root,1536,864);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());







        return scene;
    }

    public Scene registerMenu(Stage stage){
        GridPane root = new GridPane() ;
        Scene scene = new Scene(root,1536,864);
        stage.setMaximized(true);
        root.setId("pane");
        root.setAlignment(Pos.CENTER);
        TextField emailText = new TextField();
        TextField fnameText =  new TextField();
        TextField lnameText = new TextField();
        TextField phoneText = new TextField();
        TextField idText = new TextField();
        Label emailLabel = new Label("email address:");
        Label passwordLabel = new Label("password:");
        Label fnameLabel = new Label("First Name:");
        Label lnameLabel = new Label("Last Name: ");
        Label phoneLabel = new Label("Phone Number:");
        Label idLabel = new Label("Your ID:");
        PasswordField passwdTxt = new PasswordField();
        VBox texts = new VBox(fnameText,lnameText,idText,emailText,passwdTxt,phoneText);


        VBox labels = new VBox(fnameLabel,lnameLabel,idLabel,emailLabel,passwordLabel,phoneLabel    );
        labels.setSpacing(30);
        texts.setSpacing(20);
        HBox inputs = new HBox(labels,texts);
        inputs.setSpacing(20);
        Button reg = new Button("Confirm"   );
        Button back =  new Button("Back");
        back.setOnAction(e->stage.setScene(login(stage)));
        HBox buttons = new HBox(reg,back);
        buttons.setSpacing(300);
        reg.setOnAction(e->{
                try{
                    PreparedStatement pstmt = con.prepareStatement("INSERT INTO customer values (?,?,?,?,?,?)");
                    pstmt.setString(1, idText.getText());
                    pstmt.setString(2, emailText.getText());
                    pstmt.setString(3,fnameText.getText());
                    pstmt.setString(4,lnameText.getText());
                    pstmt.setString(5,phoneText.getText());
                    pstmt.setString(6,passwdTxt.getText());

                    if (idText.getText().equals("")|| emailText.getText().equals("")||fnameText.getText().equals("")||lnameText.getText().equals("")
                            ||phoneText.getText().equals("")|| passwdTxt.getText().equals("")){
                        Label error = new Label("Please fill all of the required sections");
                        error.setTranslateY(160);
                        error.setId("error");
                        error.setTranslateX(120);
                        root.getChildren().add(error);
                    }
                    else {
                        pstmt.executeUpdate();
                        stage.setScene(customerMenu(stage,idText.getText()));
                    }




                } catch (SQLException ex) {
                    Label error = new Label("User already exists!");
                    error.setTranslateY(180);
                    error.setId("error");
                    error.setTranslateX(120);
                    root.getChildren().add(error);
                }

        });
        VBox all = new VBox(inputs,buttons);
        all.setSpacing(30);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
    root.getChildren().add(all);

        return scene;

    }
    public Scene customerMenu(Stage stage,String customerID) throws SQLException {






        GridPane root = new GridPane();
        root.setId("pane");
        root.setAlignment(Pos.TOP_LEFT);
        Hyperlink addVehicle = new Hyperlink("●Add Vehicle");
        Hyperlink myVehicles = new Hyperlink("●My Vehicles");
        Hyperlink reserveSlot = new Hyperlink("●Reserve Parking Slot");
        Hyperlink managePayments = new Hyperlink("●Payments History");
        Hyperlink back = new Hyperlink("●Logout");
        back.setOnAction(e->{
            stage.setScene(login(stage));
        });





        VBox links = new VBox(addVehicle,myVehicles,reserveSlot,managePayments,back);
        links.setTranslateX(50);
        links.setTranslateY(100);
        links.setSpacing(90);
        root.getChildren().add(links);


        //my vehicles
        TableView<vehicleTbl>  table = new TableView<>();
        table.setTranslateY(100);
        table.setPrefWidth(1000);
        TableColumn<vehicleTbl,String > vehicleID = new TableColumn<>("Vehicle ID");
        vehicleID.setPrefWidth(200);
        TableColumn<vehicleTbl,String> color = new TableColumn<>("Vehicle Color");
        color.setPrefWidth(200);
        TableColumn<vehicleTbl,String> licensePlate = new TableColumn<>("License Plate");
        licensePlate.setPrefWidth(200);
        TableColumn<vehicleTbl,String> type = new TableColumn<>("Vehicle Type");
        type.setPrefWidth(200);
        TableColumn<vehicleTbl,String > name = new TableColumn<>("Name");
        name.setPrefWidth(200);
        table.getColumns().addAll(name,vehicleID,color,licensePlate,type);
        table.setTranslateX(500);
        root.getChildren().add(table);
        table.setVisible(false);

        TableView<paymentsTbl> paymentstable = new TableView<>();
        paymentstable.setMaxWidth(700);
        paymentstable.setTranslateY(100);
        paymentstable.setTranslateX(500);
        TableColumn<paymentsTbl,String> paymentID = new TableColumn<>("Payment ID");
        paymentID.setPrefWidth(200);
        TableColumn<paymentsTbl,Double> paymentAmount = new TableColumn<>("Payment amount");
        paymentAmount.setPrefWidth(200);
        TableColumn<paymentsTbl,String> paymentDate = new TableColumn<>("Payment Date");
        paymentDate.setPrefWidth(300);
        paymentstable.getColumns().addAll(paymentID,paymentAmount,paymentDate);
        root.getChildren().add(paymentstable);
        paymentstable.setVisible(false);




        VBox reserves = reserveSlot(customerID);
        reserves.setVisible(false);
        root.getChildren().add(reserves);


        

        myVehicles.setOnAction(e->
        {
            paymentstable.setVisible(false);
            reserves.setVisible(false);
            try {
                table.getItems().clear();

                ResultSet rest;
                PreparedStatement stmt = con.prepareStatement("select * from vehicle where CustomerID = (?)");
                stmt.setString(1,customerID);
                rest = stmt.executeQuery();

                while (rest.next())
                {
                    vehicleTbl row = new vehicleTbl(rest.getString(1), rest.getString(2), rest.getString(3),
                            rest.getString(6), rest.getString(5));

                    vehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
                    name.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
                    color.setCellValueFactory(new PropertyValueFactory<>("vehicleColor"));
                    licensePlate.setCellValueFactory(new PropertyValueFactory<>("LicensePlate"));
                    type.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));



                    table.getItems().addAll(row);

                    table.setVisible(true);



                    }
                if(!table.isVisible())
                    System.out.println("You have no vehicles in the database");
            } catch (SQLException es)
            {
                throw new RuntimeException(es);
            }
        });


        managePayments.setOnAction(e->{
            reserves.setVisible(false);
            table.setVisible(false);
            try{
                paymentstable.getItems().clear();
                ResultSet rest;
                PreparedStatement stmt = con.prepareStatement("select * from payment where CustomerID = (?)");
                stmt.setString(1,customerID);
                rest = stmt.executeQuery();

                while (rest.next())
                {
                    paymentsTbl row = new paymentsTbl(rest.getString(1), rest.getDouble(3), rest.getString(4));


                    paymentID.setCellValueFactory(new PropertyValueFactory<>("paymentID"));
                    paymentAmount.setCellValueFactory(new PropertyValueFactory<>("paymentAmount"));
                    paymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));





                    paymentstable.getItems().addAll(row);

                    paymentstable.setVisible(true);



                }
                if(!paymentstable.isVisible())
                    System.out.println("You have no previous payments");
            } catch (SQLException es)
            {
                throw new RuntimeException(es);
            }
        });





        reserveSlot.setOnAction(e->{
            paymentstable.setVisible(false);
            table.setVisible(false);
            reserves.setVisible(true);


        });



        addVehicle.setOnAction(e->{
            addVehicle(customerID);


        });



        Scene scene = new Scene(root,1536,864);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        return scene;
    }

    public VBox reserveSlot(String customerID) throws SQLException {
        ObservableList<String> myvehicles = FXCollections.observableArrayList();
        PreparedStatement pstmt = con.prepareStatement("SELECT vehicle_ID from vehicle where customerID = (?)");
        pstmt.setString(1,customerID);
        ResultSet resultSet = pstmt.executeQuery();
        while(resultSet.next()) {
            myvehicles.add(resultSet.getString(1));
        }
        ObservableList<String> lotsList = FXCollections.observableArrayList();
        PreparedStatement pstmt2 = con.prepareStatement("select lot_id from parking_lot");
        ResultSet resultSet2 =pstmt2.executeQuery();
        while (resultSet2.next()){
            lotsList.add(resultSet2.getString(1));
        }
        ObservableList<String> slots = FXCollections.observableArrayList();
        PreparedStatement pstmt3 = con.prepareStatement("select space_id,space_type from parking_space where status='false'");
        ResultSet resultSet3 = pstmt3.executeQuery();
        while (resultSet3.next())
            slots.add(resultSet3.getString(1) + " (" + resultSet3.getString(2) + ")");
        ComboBox<String> vehiclesBox = new ComboBox<>(myvehicles);
        vehiclesBox.getSelectionModel().selectFirst();

        ComboBox<String>  availableSlots = new ComboBox<>(slots);

        availableSlots.getSelectionModel().selectFirst();


        Label selectVehicle = new Label("Select vehicle");
        Label selectSlot = new Label("Select slot");
        Label selectLot = new Label("Select Parking Lot");

        VBox labels = new VBox(selectVehicle,selectSlot);
        labels.setSpacing(110);

        VBox selections = new VBox(vehiclesBox,availableSlots);
        selections.setSpacing(100);

        HBox reserve = new HBox(labels,selections);

        Button confirmReserve = new Button("Reserve parking slot");
        Label res = new Label("");
        HBox butt = new HBox(confirmReserve,res);
        butt.setSpacing(100);

        VBox reserves = new VBox(reserve,butt);
        reserves.setSpacing(30);
        reserve.setSpacing(50);
        reserves.setTranslateX(500);
        reserves.setTranslateY(100);
        reserves.setVisible(false);
        confirmReserve.setOnAction(e->{


            try {
                PreparedStatement checkslot = con.prepareStatement("SELECT status,space_type from parking_space where space_id=(?)");
                checkslot.setInt(1, Integer.parseInt(availableSlots.getValue().split(" ")[0]));
                ResultSet check = checkslot.executeQuery();
                if(check.next()) {
                    String type = check.getString("space_type");
                    if(check.getString(1).equals("TRUE")){
                        res.setId("error");
                        res.setText("Space unavailable");

                    }
                    else {

                        PreparedStatement addReservation = con.prepareStatement("INSERT INTO reservations(space_id,vehicle_id) values (?,?)");
                        addReservation.setString(2, vehiclesBox.getValue());
                        addReservation.setInt(1, Integer.parseInt(availableSlots.getValue().split(" ")[0]));
                        addReservation.executeUpdate();

                        PreparedStatement makeReserved = con.prepareStatement("UPDATE parking_lot.parking_space t\n" +
                                "SET t.Status = 'TRUE'\n" +
                                "WHERE t.Space_ID = (?)");
                        makeReserved.setInt(1, Integer.parseInt(availableSlots.getValue().split(" ")[0]));
                        makeReserved.executeUpdate();

                        res.setId("result");
                        res.setText("Reservation complete!");
                        PreparedStatement getprice = con.prepareStatement("SELECT price from space_price where space_type = (?) ");
                        getprice.setString(1,type);
                        ResultSet price = getprice.executeQuery();
                        price.next();
                        double slotprice = price.getDouble(1);

                        PreparedStatement payment = con.prepareStatement("INSERT INTO payment (CustomerID,Payment_Amount) values (?,?)");
                        payment.setString(1,customerID);
                        payment.setDouble(2,slotprice);
                        payment.executeUpdate();


                    }

                }

            } catch (SQLException ex) {
                res.setText("Please select valid values");
                res.setId("error");
            }


        });
        return reserves;
    }
    public void addVehicle(String customerID){

        Stage stage = new Stage();


        GridPane root = new GridPane();
        Scene scene = new Scene(root,500,400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        root.setId("pane");
        root.setAlignment(Pos.CENTER);
        TextField plateText = new TextField();
        TextField nameText =  new TextField();
        TextField idText = new TextField();
        TextField colorText = new TextField();
        Label nameLabel = new Label("Vehicle Name:");
        Label colorLabel = new Label("Vehicle Color:");
        Label idLabel = new Label("Vehicle ID: ");
        Label plateLabel = new Label("License Plate: ");
        Label typeLabel = new Label("Vehicle Type:");
        ObservableList<String> options = FXCollections.observableArrayList(
                "Truck",
                "Sedan",
                "Coupe",
                "SUV"
        );

        ComboBox<String> typeText = new ComboBox<>(options);
        VBox texts = new VBox(nameText,idText,colorText,plateText,typeText);
        VBox labels = new VBox(nameLabel,idLabel,colorLabel,plateLabel,typeLabel);
        labels.setSpacing(30);
        texts.setSpacing(20);
        HBox inputs = new HBox(labels,texts);
        Button confirm = new Button("Add vehicle");
        Label resultLabel = new Label("");
        HBox bruh = new HBox(confirm,resultLabel);
        bruh.setSpacing(20);
        VBox all = new VBox(inputs,bruh);
        all.setSpacing(50);

        confirm.setOnAction(e->{
            try{

                if(idText.getText().equals("")|| plateText.getText().equals("")||nameText.getText().equals("")||typeText.getValue().equals("")){
                    resultLabel.setText("Please fill all of the required sections");
                    resultLabel.setId("error");
                }
                else {
                    PreparedStatement pstmt = con.prepareStatement("INSERT INTO vehicle values (?,?,?,?,?,?)    ");
                    pstmt.setString(1, idText.getText());
                    pstmt.setString(2, colorText.getText());
                    pstmt.setString(3, plateText.getText());
                    pstmt.setString(4, customerID);
                    pstmt.setString(5, typeText.getValue());
                    pstmt.setString(6, nameText.getText());

                    pstmt.executeUpdate();
                    resultLabel.setText("Vehicle added successfully!");
                    resultLabel.setId("result");
                }






            } catch (SQLException ex) {
                resultLabel.setText("Vehicle already exists in the database!");
                resultLabel.setId("error");

            }
        });
        root.getChildren().add(all);

        stage.setScene(scene);
        stage.show();


    }

    public void addSlot() throws SQLException {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root,500,400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        ObservableList<String> lotsList = FXCollections.observableArrayList();
        PreparedStatement pstmt2 = con.prepareStatement("select * from parking_lot");
        ResultSet resultSet2 =pstmt2.executeQuery();
        while (resultSet2.next()){
            lotsList.add(resultSet2.getString(1) + " (" + resultSet2.getString(2) + ")");
        }

        ObservableList<String> types = FXCollections.observableArrayList("Large", "Medium", "Small", "Handicap","Charging");

        Label lotIDlbl = new Label("Select parking lot");
        Label slotType = new Label("Select Slot Type");
        VBox labels = new VBox(lotIDlbl,slotType);
        labels.setSpacing(35);

        ComboBox<String> lotID = new ComboBox<>(lotsList);
        lotID.getSelectionModel().selectFirst();

        ComboBox<String> type = new ComboBox<>(types);
        type.getSelectionModel().selectFirst();
        VBox inputs = new VBox(lotID,type);
        inputs.setSpacing(30);
        HBox bruh = new HBox(labels,inputs);

        bruh.setSpacing(50);

        Button confirm = new Button("Add Parking Slot");
        Label result = new Label("");
        HBox results = new HBox(confirm,result);
        results.setSpacing(30);


        VBox all = new VBox(bruh,results);
        all.setSpacing(50);

        confirm.setOnAction(e->{
            try {
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO parking_space(Lot_id,space_type) values (?,?)");
                pstmt.setString(1,lotID.getValue().split(" ")[0]);
                pstmt.setString(2,type.getValue());
                pstmt.executeUpdate();
                result.setText("Success!");
                result.setId("result");
            } catch (SQLException ex) {
                result.setText("Failed to add");
                result.setId("error");
            }


        });

        root.getChildren().add(all);
        root.setId("pane");
        root.setAlignment(Pos.CENTER);
        stage.setScene(scene);
        stage.show();

    }
    public void clearSlot() throws SQLException {
        Stage stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root,500,400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        ObservableList<String> slotList = FXCollections.observableArrayList();
        PreparedStatement pstmt2 = con.prepareStatement("select space_id from parking_space where status = 'true'");
        ResultSet resultSet2 =pstmt2.executeQuery();
        while (resultSet2.next()){
            slotList.add(resultSet2.getString(1));
        }

        Label spaceID = new Label("Select parking space");

        ComboBox<String> spaceList = new ComboBox<>(slotList);
        spaceList.getSelectionModel().selectFirst();

        HBox bruh = new HBox(spaceID,spaceList  );
        bruh.setSpacing(50);

        Button confirm = new Button("Clear slot");
        Button clearAll = new Button("Clear all slots ");
        HBox buttons = new HBox(confirm,clearAll);
        buttons.setSpacing(30);
        VBox all = new VBox(bruh,buttons);
        all.setSpacing(50);

        confirm.setOnAction(e->{
            try {
                PreparedStatement pstmt = con.prepareStatement("update parking_space set status = 'FALSE' where space_ID = (?)");
                pstmt.setString(1,spaceList.getValue());
                pstmt.executeUpdate();

            } catch (SQLException ignored) {

            }


        });

        clearAll.setOnAction(e->{

            try {
                PreparedStatement   preparedStatement = con.prepareStatement("update parking_space space set status = 'FALSE'");
                preparedStatement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        });

        root.getChildren().add(all);
        root.setId("pane");
        root.setAlignment(Pos.CENTER);
        stage.setScene(scene);
        stage.show();

    }

    public Scene empMenu(Stage stage,String employeeID){
        GridPane root = new GridPane();
        root.setId("pane");
        root.setAlignment(Pos.TOP_LEFT);
        Hyperlink viewCustomers = new Hyperlink("●View all customers");
        Hyperlink viewVehicles = new Hyperlink("●View all Registered vehicles");
        Hyperlink addSlot = new Hyperlink("●Add parking Slot");
        Hyperlink viewEmployees = new Hyperlink("●View all employees");
        Hyperlink clearSlot = new Hyperlink("●Clear Parking slot");
        Hyperlink viewReservations = new Hyperlink("●View reservation history");
        Hyperlink back = new Hyperlink("●Logout");
        back.setOnAction(e->{
            stage.setScene(login(stage));
        });

        TableView<customerTbl>  customerTable = new TableView<>();
        customerTable.setTranslateY(100);
        customerTable.setPrefWidth(1000);
        TableColumn<customerTbl,String > customerID = new TableColumn<>("Customer ID");
        customerID.setPrefWidth(150);
        TableColumn<customerTbl,String> email = new TableColumn<>("E-Mail");
        email.setPrefWidth(350);
        TableColumn<customerTbl,String> fname = new TableColumn<>("First Name");
        fname.setPrefWidth(150);
        TableColumn<customerTbl,String> lname = new TableColumn<>("Last Name");
        lname.setPrefWidth(150);
        TableColumn<customerTbl,String > phoneNum = new TableColumn<>("Phone Number");
        phoneNum.setPrefWidth(200);
        customerTable.getColumns().addAll(customerID,fname,lname,email,phoneNum);
        customerTable.setTranslateX(500);
        root.getChildren().add(customerTable);
        customerTable.setVisible(false);


        TableView<vehicleTbl>  vehicleTable = new TableView<>();
        vehicleTable.setTranslateY(100);
        vehicleTable.setPrefWidth(1000);
        TableColumn<vehicleTbl,String > vehicleID = new TableColumn<>("Vehicle ID");
        vehicleID.setPrefWidth(200);
        TableColumn<vehicleTbl,String> color = new TableColumn<>("Vehicle Color");
        color.setPrefWidth(200);
        TableColumn<vehicleTbl,String> licensePlate = new TableColumn<>("License Plate");
        licensePlate.setPrefWidth(200);
        TableColumn<vehicleTbl,String> type = new TableColumn<>("Vehicle Type");
        type.setPrefWidth(200);
        TableColumn<vehicleTbl,String > name = new TableColumn<>("Name");
        name.setPrefWidth(200);
        vehicleTable.getColumns().addAll(name,vehicleID,color,licensePlate,type);
        vehicleTable.setTranslateX(500);
        root.getChildren().add(vehicleTable);
        vehicleTable.setVisible(false);

        TableView<empTbl>  empTable = new TableView<>();
        empTable.setTranslateY(100);
        empTable.setPrefWidth(1000);
        TableColumn<empTbl,String > empID = new TableColumn<>("Employee ID");
        empID.setPrefWidth(200);
        TableColumn<empTbl,String> empfname = new TableColumn<>("First Name");
        empfname.setPrefWidth(150);
        TableColumn<empTbl,String> emplname = new TableColumn<>("Last Name");
        emplname.setPrefWidth(150);
        TableColumn<empTbl,String> empEmail = new TableColumn<>("E-Mail");
        empEmail.setPrefWidth(350);
        TableColumn<empTbl,String > salary = new TableColumn<>("Salary");
        salary.setPrefWidth(150);
        empTable.getColumns().addAll(empID,empfname,emplname,empEmail,salary );
        empTable.setTranslateX(500);
        root.getChildren().add(empTable);
        empTable.setVisible(false);


        TableView<reservations> reservationsTable = new TableView<>();
        reservationsTable.setMaxWidth(700);
        reservationsTable.setTranslateY(100);
        reservationsTable.setTranslateX(500);
        TableColumn<reservations,String> spaceID = new TableColumn<>("Space ID");
        spaceID.setPrefWidth(200);
        TableColumn<reservations,Double> vehID = new TableColumn<>("Vehicle ID");
        vehID.setPrefWidth(200);
        TableColumn<reservations,String> resDate = new TableColumn<>("Reservation Date");
        resDate.setPrefWidth(300);
        reservationsTable.getColumns().addAll(spaceID,vehID,resDate);
        root.getChildren().add(reservationsTable);
        reservationsTable.setVisible(false);
        
        viewReservations.setOnAction(e->{
            customerTable.setVisible(false);
            empTable.setVisible(false);
            vehicleTable.setVisible(false);
            try{
                reservationsTable.getItems().clear();
                ResultSet rest;
                PreparedStatement stmt = con.prepareStatement("select * from reservations");
                rest = stmt.executeQuery();

                while (rest.next())
                {
                    reservations row = new reservations(rest.getInt(1), rest.getString(2), rest.getString(3));


                    spaceID.setCellValueFactory(new PropertyValueFactory<>("spaceID"));
                    vehID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
                    resDate.setCellValueFactory(new PropertyValueFactory<>("date"));





                    reservationsTable.getItems().addAll(row);

                    reservationsTable.setVisible(true);



                }
                if(!reservationsTable.isVisible())
                    System.out.println("You have no previous payments");
            } catch (SQLException es)
            {
                throw new RuntimeException(es);
            }
        });
        
        viewEmployees.setOnAction(e->
        {
            customerTable.setVisible(false);
            vehicleTable.setVisible(false);
            reservationsTable.setVisible(false);
            try {

                empTable.getItems().clear();

                ResultSet rest;
                PreparedStatement stmt = con.prepareStatement("select * from employee");
                rest = stmt.executeQuery();

                while (rest.next())
                {
                    empTbl row = new empTbl(rest.getString(1), rest.getString(2), rest.getString(3),
                            rest.getString(4), rest.getString(5));

                    empID.setCellValueFactory(new PropertyValueFactory<>("empID"));
                    empfname.setCellValueFactory(new PropertyValueFactory<>("fname"));
                    emplname.setCellValueFactory(new PropertyValueFactory<>("lname"));
                    empEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
                    salary.setCellValueFactory(new PropertyValueFactory<>("salary"));



                    empTable.getItems().addAll(row);

                    empTable.setVisible(true);



                }
                if(!empTable.isVisible())
                    System.out.println("You have no vehicles in the database");
            } catch (SQLException es)
            {
                throw new RuntimeException(es);
            }
        });

        viewVehicles.setOnAction(e->
        {
            empTable.setVisible(false);
            customerTable.setVisible(false);
            reservationsTable.setVisible(false);
            try {

                vehicleTable.getItems().clear();

                ResultSet rest;
                PreparedStatement stmt = con.prepareStatement("select * from vehicle");
                rest = stmt.executeQuery();

                while (rest.next())
                {
                    vehicleTbl row = new vehicleTbl(rest.getString(1), rest.getString(2), rest.getString(3),
                            rest.getString(6), rest.getString(5));

                    vehicleID.setCellValueFactory(new PropertyValueFactory<>("vehicleID"));
                    name.setCellValueFactory(new PropertyValueFactory<>("vehicleName"));
                    color.setCellValueFactory(new PropertyValueFactory<>("vehicleColor"));
                    licensePlate.setCellValueFactory(new PropertyValueFactory<>("LicensePlate"));
                    type.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));



                    vehicleTable.getItems().addAll(row);

                    vehicleTable.setVisible(true);



                }
                if(!vehicleTable.isVisible())
                    System.out.println("You have no vehicles in the database");
            } catch (SQLException es)
            {
                throw new RuntimeException(es);
            }
        });








        viewCustomers.setOnAction(e->
        {
            empTable.setVisible(false);
            vehicleTable.setVisible(false);
            reservationsTable.setVisible(false);
            try {

                customerTable.getItems().clear();

                ResultSet rest;
                PreparedStatement stmt = con.prepareStatement("select * from customer");
                rest = stmt.executeQuery();

                while (rest.next())
                {
                    customerTbl row = new customerTbl(rest.getString(1), rest.getString(2), rest.getString(3),
                            rest.getString(4), rest.getString(5));

                    customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
                    email.setCellValueFactory(new PropertyValueFactory<>("email"));
                    fname.setCellValueFactory(new PropertyValueFactory<>("fName"));
                    lname.setCellValueFactory(new PropertyValueFactory<>("lName"));
                    phoneNum.setCellValueFactory(new PropertyValueFactory<>("phone"));



                    customerTable.getItems().addAll(row);

                    customerTable.setVisible(true);



                }
                if(!customerTable.isVisible())
                    System.out.println("There are no customers in the database");
            } catch (SQLException es)
            {
                throw new RuntimeException(es);
            }
        });

        addSlot.setOnAction(e->{
            try {
                addSlot();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        clearSlot.setOnAction(e->{
            try {
                clearSlot();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });



        VBox links = new VBox(viewCustomers,viewVehicles,addSlot,clearSlot,viewEmployees,viewReservations,back);
        links.setTranslateX(50);
        links.setTranslateY(100);
        links.setSpacing(60);
        root.getChildren().add(links);
        Scene scene = new Scene(root,1536,864);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
    public static class customerTbl{
        private final SimpleStringProperty customerID ;
        private final SimpleStringProperty email;
        private final  SimpleStringProperty fName;
        private final  SimpleStringProperty lName;
        private final SimpleStringProperty phone;
        
        private customerTbl(String customerID, String email, String fname, String lname, String phone){
            this.customerID = new SimpleStringProperty(customerID);
            this.email = new SimpleStringProperty(email);
            this.fName= new SimpleStringProperty(fname);
            this.lName= new SimpleStringProperty(lname);
            this.phone = new SimpleStringProperty(phone);
        }

        public String getCustomerID() {
            return customerID.get();
        }

        public SimpleStringProperty customerIDProperty() {
            return customerID;
        }

        public String getEmail() {
            return email.get();
        }

        public SimpleStringProperty emailProperty() {
            return email;
        }

        public String getfName() {
            return fName.get();
        }

        public SimpleStringProperty fNameProperty() {
            return fName;
        }

        public String getlName() {
            return lName.get();
        }

        public SimpleStringProperty lNameProperty() {
            return lName;
        }

        public String getPhone() {
            return phone.get();
        }

        public SimpleStringProperty phoneProperty() {
            return phone;
        }
    }

    public static class empTbl{
        private final SimpleStringProperty empID;
        private final SimpleStringProperty fname;
        private final   SimpleStringProperty lname;
        private final SimpleStringProperty email;
        private final  SimpleStringProperty salary;
        
        private empTbl(String empID, String fname, String lname, String email, String salary){
            this.empID = new SimpleStringProperty(empID);
            this.fname = new SimpleStringProperty(fname);
            this.lname = new SimpleStringProperty(lname);
            this.email = new SimpleStringProperty(email);
            this.salary = new SimpleStringProperty(salary);
            
        }

        public String getEmpID() {
            return empID.get();
        }

        public SimpleStringProperty empIDProperty() {
            return empID;
        }

        public void setEmpID(String empID) {
            this.empID.set(empID);
        }

        public String getFname() {
            return fname.get();
        }

        public SimpleStringProperty fnameProperty() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname.set(fname);
        }

        public String getLname() {
            return lname.get();
        }

        public SimpleStringProperty lnameProperty() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname.set(lname);
        }

        public String getEmail() {
            return email.get();
        }

        public SimpleStringProperty emailProperty() {
            return email;
        }

        public void setEmail(String email) {
            this.email.set(email);
        }

        public String getSalary() {
            return salary.get();
        }

        public SimpleStringProperty salaryProperty() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary.set(salary);
        }
    }

    public static class reservations{
        private final SimpleIntegerProperty spaceID;
        private final SimpleStringProperty vehicleID;
        private final  SimpleStringProperty date;

        private reservations(int spaceID, String vehicleID, String date){
            this.spaceID= new SimpleIntegerProperty(spaceID);
            this.vehicleID = new SimpleStringProperty(vehicleID);
            this.date = new SimpleStringProperty(date);
        }

        public int getSpaceID() {
            return spaceID.get();
        }

        public SimpleIntegerProperty spaceIDProperty() {
            return spaceID;
        }

        public void setSpaceID(int spaceID) {
            this.spaceID.set(spaceID);
        }

        public String getVehicleID() {
            return vehicleID.get();
        }

        public SimpleStringProperty vehicleIDProperty() {
            return vehicleID;
        }

        public void setVehicleID(String vehicleID) {
            this.vehicleID.set(vehicleID);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }
    }
    public static class vehicleTbl {

        private final SimpleStringProperty vehicleID;
        private final SimpleStringProperty vehicleColor;
        private final SimpleStringProperty LicensePlate;
        private final SimpleStringProperty vehicleName;
        private final SimpleStringProperty vehicleType;


        private vehicleTbl(String vehicleID, String vehicleColor, String LicensePlate, String vehicleName, String vehicleType) {
            this.vehicleID = new SimpleStringProperty(vehicleID);
            this.vehicleColor = new SimpleStringProperty(vehicleColor);
            this.LicensePlate = new SimpleStringProperty(LicensePlate);
            this.vehicleName = new SimpleStringProperty(vehicleName);
            this.vehicleType = new SimpleStringProperty(vehicleType);

        }

        public String getVehicleID() {
            return vehicleID.get();
        }

        public SimpleStringProperty vehicleIDProperty() {
            return vehicleID;
        }

        public void setVehicleID(String vehicleID) {
            this.vehicleID.set(vehicleID);
        }

        public String getVehicleColor() {
            return vehicleColor.get();
        }

        public SimpleStringProperty vehicleColorProperty() {
            return vehicleColor;
        }

        public void setVehicleColor(String vehicleColor) {
            this.vehicleColor.set(vehicleColor);
        }

        public String getLicensePlate() {
            return LicensePlate.get();
        }

        public SimpleStringProperty licensePlateProperty() {
            return LicensePlate;
        }

        public void setLicensePlate(String licensePlate) {
            this.LicensePlate.set(licensePlate);
        }

        public String getVehicleName() {
            return vehicleName.get();
        }

        public SimpleStringProperty vehicleNameProperty() {
            return vehicleName;
        }

        public void setVehicleName(String vehicleName) {
            this.vehicleName.set(vehicleName);
        }

        public String getVehicleType() {
            return vehicleType.get();
        }

        public SimpleStringProperty vehicleTypeProperty() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType.set(vehicleType);
        }
    }
    public static class paymentsTbl{
        private final SimpleStringProperty paymentID;
        private final SimpleDoubleProperty paymentAmount;
        private final SimpleStringProperty paymentDate;

        private paymentsTbl(String  paymentID, double paymentAmount, String paymentDate) {
            this.paymentID = new SimpleStringProperty(paymentID);
            this.paymentAmount = new SimpleDoubleProperty(paymentAmount);
            this.paymentDate = new SimpleStringProperty(paymentDate);
        }

        public String getPaymentID() {
            return paymentID.get();
        }

        public SimpleStringProperty paymentIDProperty() {
            return paymentID;
        }

        public void setPaymentID(String paymentID) {
            this.paymentID.set(paymentID);
        }

        public double getPaymentAmount() {
            return paymentAmount.get();
        }

        public SimpleDoubleProperty paymentAmountProperty() {
            return paymentAmount;
        }

        public void setPaymentAmount(double paymentAmount) {
            this.paymentAmount.set(paymentAmount);
        }

        public String getPaymentDate() {
            return paymentDate.get();
        }

        public SimpleStringProperty paymentDateProperty() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate.set(paymentDate);
        }
    }
}

