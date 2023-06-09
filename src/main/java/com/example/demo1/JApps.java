package com.example.demo1;

import java.awt.*;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;

import static com.example.demo1.TransferDao.getTransferHistory;

public class JApps extends Application {
    private static final Logger logger = Logger.getLogger(JApps.class.getName());
    private UserDao userDao = new UserDao();
    static User user = new User();

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("JavaFX Welcome");
        loginPage(primaryStage);

    }

    public void loginPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene primaryStage  = new Scene(fxmlLoader.load(), 1280, 720);


        TextField usernameTextField = (TextField) primaryStage.lookup("#login");
        TextField passwordField = (TextField) primaryStage.lookup("#password");
        Button saveButton =  (Button) primaryStage.lookup("#loginBtn");
        saveButton.setOnAction(actionEvent -> {
            String username = usernameTextField.getText().trim();
            String password = passwordField.getText();
            if (!StringPool.BLANK.equals(username) && !StringPool.BLANK.equals(password)) {
                try {

                    user = this.loginUserObject(username, password);

                    boolean userId = userDao.userLogin(user.getUsername(),user.getPassword());
                    if (userId) {
                        //this.alert("Save", "Zalogowany!", AlertType.INFORMATION);
                        user = userDao.getInfoUser(user.getUsername());
                        if(user.getAdmin_permission() == false){
                            mainPage(stage);
                        } else if (user.getAdmin_permission() == true) {
                            adminPage(stage);
                        }


                    } else {
                        this.alert("Error", "Blad!", AlertType.ERROR);
                    }

                } catch (Exception exception) {
                    logger.log(Level.SEVERE, exception.getMessage());
                }
            } else {
                this.alert("Error", "Please complete fields!", AlertType.ERROR);
            }

        });

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();
    }

    public static void mainPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button creditapplication = (Button)  primaryStage.lookup("#creditapplication");
        creditapplication.setOnAction(
                e -> {
                    try {
                        applicastionsList(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button credit = (Button)  primaryStage.lookup("#credit");
        credit.setOnAction(
                e -> {
                    try {
                        getCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Label lblData = (Label) primaryStage.lookup("#personData");
        lblData.setText(user.getFirstName() +  " " + user.getLastName());
        List<BankAccount> bankAccounts = BankAccountDao.getAccountInfo(user.getId());
        Label lableAccout = (Label) primaryStage.lookup("#accout_holder_label");
        lableAccout.setText(" ");
        bankAccounts.forEach( account -> {
            lableAccout.setText(lableAccout.getText() + "\n" + account.getCurrencyLong() + " - " + account.getCurrentCash() + " \n");
        } );
        Button youraccout = (Button)  primaryStage.lookup("#youraccout");
        youraccout.setOnAction(
                e -> {
                    try {
                        mainPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button sendTransfer = (Button)  primaryStage.lookup("#sendTransfer");
        sendTransfer.setOnAction(
                e -> {
                    try {
                        sendTransderScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button historyPage = (Button)  primaryStage.lookup("#historyPage");
        historyPage.setOnAction(
                e -> {
                    try {
                        historyPageScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        ImageView card = (ImageView) primaryStage.lookup("#card_img_holder");
        String cardVisa = "src/main/resources/img/visa.png";
        String cardMasterCard = "src/main/resources/img/mastercard.png";
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
        //Image cardVisa = new Image("file:///Users/apple/Downloads/demo1/src/main/resources/img/visa.png");
        //Image cardMasterCard = new Image("file:///Users/apple/Downloads/demo1/src/main/resources/img/mastercard.png");
        List<Card> cards = CardDao.getCardInfo(user.getId());
        int counter = 0;
        for (Card BankCard : cards) {
            Scene  test= card.getParent().getScene();
            HBox box = (HBox) primaryStage.lookup("#holder_card");

            if (BankCard.getCardProducentId() == 1) {
                try {
                    //card.setImage(addTextOnImage(cardVisa, user, BankCard));
                    ImageView tempCard = new ImageView(addTextOnImage(cardVisa, user, BankCard));
                    box.getChildren().add(tempCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (BankCard.getCardProducentId() == 2) {
                try {
                    ImageView tempCard = new ImageView(addTextOnImage(cardMasterCard, user, BankCard));
                    box.getChildren().add(tempCard);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            counter ++;
        }


        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();
    }

    public void adminPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainAdmin.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button addClient = (Button)  primaryStage.lookup("#addClient");
        addClient.setOnAction(
                e -> {
                    try {
                        addClientPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button addAccountAndCard = (Button)  primaryStage.lookup("#addAccountAndCard");
        addAccountAndCard.setOnAction(
                e -> {
                    try {
                        addAccountAndCardPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button AthCredit = (Button)  primaryStage.lookup("#AthCredit");
        AthCredit.setOnAction(
                e -> {
                    try {
                        athCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();
    }

    public void addClientPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addClient.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button addClient = (Button)  primaryStage.lookup("#addClient");
        addClient.setOnAction(
                e -> {
                    try {
                        addClientPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button addAccountAndCard = (Button)  primaryStage.lookup("#addAccountAndCard");
        addAccountAndCard.setOnAction(
                e -> {
                    try {
                        addAccountAndCardPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button AthCredit = (Button)  primaryStage.lookup("#AthCredit");
        AthCredit.setOnAction(
                e -> {
                    try {
                        athCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        ChoiceBox userType =  (ChoiceBox)  primaryStage.lookup("#userType");
        userType.setItems(FXCollections.observableArrayList(
                "Klient","Admin")
        );

        Button addUserBtn =  (Button) primaryStage.lookup("#addUserBtn");
        addUserBtn.setOnAction(actionEvent -> {
            TextField username  = (TextField)  primaryStage.lookup("#username");
            TextField first_name  = (TextField)  primaryStage.lookup("#first_name");
            TextField last_name  = (TextField)  primaryStage.lookup("#last_name");
            TextField password  = (TextField)  primaryStage.lookup("#password");

            String usernameStr = username.getText();
            String first_nameStr = first_name.getText();
            String last_nameStr = last_name.getText();
            String passwordStr = password.getText();
            String typeStr = userType.getSelectionModel().getSelectedItem().toString();
            try {
                if( UserDao.userExists(usernameStr)){
                    this.alert("Bład","Zła nazwa użytkownia, taka już jest",AlertType.ERROR);
                } else {
                    UserDao.saveUser(usernameStr,last_nameStr,first_nameStr,passwordStr,typeStr);
                    this.alert("Info","Użytkownik poprawie zarejestrowany",AlertType.INFORMATION);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public void addAccountAndCardPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("addCard.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button addClient = (Button)  primaryStage.lookup("#addClient");
        addClient.setOnAction(
                e -> {
                    try {
                        addClientPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button addAccountAndCard = (Button)  primaryStage.lookup("#addAccountAndCard");
        addAccountAndCard.setOnAction(
                e -> {
                    try {
                        addAccountAndCardPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button AthCredit = (Button)  primaryStage.lookup("#AthCredit");
        AthCredit.setOnAction(
                e -> {
                    try {
                        athCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        List<User> usersList = new ArrayList<>(UserDao.getClientsList());
        ChoiceBox account_list =  (ChoiceBox)  primaryStage.lookup("#userId");
        usersList.forEach(user ->{
            account_list.getItems().add(user.getFirstName() + " " + user.getLastName());
        });

        List<Currency> currences = new ArrayList<>();
        currences = CurrencyDao.getCurrencyList();
        ChoiceBox currencyid =  (ChoiceBox)  primaryStage.lookup("#currencyid");
        currences.forEach(Currencae ->{
            currencyid.getItems().add(Currencae.getNameShort());
        });

        List<CardProducents> cardProducentslist = new ArrayList<>(CardProducentsDao.getCardProducentsList());
        ChoiceBox cardProducentField =  (ChoiceBox)  primaryStage.lookup("#cardProducent");
        cardProducentslist.forEach(cardProducent ->{
            cardProducentField.getItems().add(cardProducent.getName());
        });


        Button addCardBtn =  (Button) primaryStage.lookup("#addCardBtn");
        addCardBtn.setOnAction(actionEvent -> {

            TextField account_number  = (TextField)  primaryStage.lookup("#account_number");
            TextField starting_cash  = (TextField)  primaryStage.lookup("#starting_cash");
            TextField card_number  = (TextField)  primaryStage.lookup("#card_number");
            TextField year  = (TextField)  primaryStage.lookup("#year");
            TextField month  = (TextField)  primaryStage.lookup("#month");

            String account_numberStr = account_number.getText();
            Float starting_cashStr = Float.parseFloat(starting_cash.getText());
            String card_numberStr = card_number.getText();
            String yearStr = year.getText();
            String monthdStr = month.getText();
            String userName  = account_list.getSelectionModel().getSelectedItem().toString();
            int currencyId = currencyid.getSelectionModel().getSelectedIndex()+1;
            int cardProducents = cardProducentField.getSelectionModel().getSelectedIndex();
            AtomicInteger userId = new AtomicInteger();
            usersList.forEach(user -> {
                if((user.getFirstName() + " " + user.getLastName()).equals(userName)){
                    userId.set(user.getId());
                }
            });

            try {
                BankAccountDao.addBankAccount(userId.get(),currencyId,starting_cashStr,account_numberStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                CardDao.addBankAccount(cardProducents,userId.get(),card_numberStr,yearStr,monthdStr);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public static void getCreditPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("credit.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button credit = (Button)  primaryStage.lookup("#credit");
        credit.setOnAction(
                e -> {
                    try {
                        getCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button creditapplication = (Button)  primaryStage.lookup("#creditapplication");
        creditapplication.setOnAction(
                e -> {
                    try {
                        applicastionsList(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );


        ChoiceBox anotherCredits =  (ChoiceBox)  primaryStage.lookup("#anotherCredits");
        anotherCredits.setItems(FXCollections.observableArrayList(
                "Nie","Tak")
        );
        ChoiceBox listOfDebtor =  (ChoiceBox)  primaryStage.lookup("#listOfDebtor");
        listOfDebtor.setItems(FXCollections.observableArrayList(
                "Nie","Tak")
        );
        ChoiceBox workContract =  (ChoiceBox)  primaryStage.lookup("#workContract");
        workContract.setItems(FXCollections.observableArrayList(
                "Umowa zlecenie/ o dzieło","Umowa o pracę")
        );

        Button sendCreditBtn = (Button)  primaryStage.lookup("#sendCreditBtn");
        sendCreditBtn.setOnAction(
                e -> {
                    //age pesel cash month children workContractMonth AvgPayout
                    TextField ageField  =  (TextField) primaryStage.lookup("#age");
                    TextField peselField  =  (TextField) primaryStage.lookup("#pesel");
                    TextField cashField  = (TextField) primaryStage.lookup("#cash");
                    TextField monthField  =  (TextField)  primaryStage.lookup("#month");
                    TextField childrenField  =  (TextField)  primaryStage.lookup("#children");
                    TextField workContractMonthField  = (TextField)  primaryStage.lookup("#workContractMonth");
                    TextField AvgPayoutField  = (TextField) primaryStage.lookup("#AvgPayout");


                    Float age  = Float.parseFloat(ageField.getText());
                    String pesel  =   peselField.getText();
                    Float cash  =Float.parseFloat(cashField.getText());
                    int month  =  Integer.parseInt(monthField.getText());
                    int children  =   Integer.parseInt(childrenField.getText());
                    int workContractMonth  = Integer.parseInt( workContractMonthField.getText());
                    Float AvgPayout  = Float.parseFloat(AvgPayoutField.getText());
                    int anotherCreditsInt = anotherCredits.getSelectionModel().getSelectedIndex();
                    int listOfDebtorInt = listOfDebtor.getSelectionModel().getSelectedIndex();
                    int workContractInt = workContract.getSelectionModel().getSelectedIndex();
                    try {
                        creditDao.addCredit(user.getId(),age,pesel,cash,month,children,workContractMonth,AvgPayout,anotherCreditsInt,listOfDebtorInt,workContractInt);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button youraccout = (Button)  primaryStage.lookup("#youraccout");
        youraccout.setOnAction(
                e -> {
                    try {
                        mainPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button sendTransfer = (Button)  primaryStage.lookup("#sendTransfer");
        sendTransfer.setOnAction(
                e -> {
                    try {
                        sendTransderScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button historyPage = (Button)  primaryStage.lookup("#historyPage");
        historyPage.setOnAction(
                e -> {
                    try {
                        historyPageScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public static void applicastionsList(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("creditapplication.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button creditapplication = (Button)  primaryStage.lookup("#creditapplication");
        creditapplication.setOnAction(
                e -> {
                    try {
                        applicastionsList(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button credit = (Button)  primaryStage.lookup("#credit");
        credit.setOnAction(
                e -> {
                    try {
                        getCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button youraccout = (Button)  primaryStage.lookup("#youraccout");
        youraccout.setOnAction(
                e -> {
                    try {
                        mainPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button sendTransfer = (Button)  primaryStage.lookup("#sendTransfer");
        sendTransfer.setOnAction(
                e -> {
                    try {
                        sendTransderScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button historyPage = (Button)  primaryStage.lookup("#historyPage");
        historyPage.setOnAction(
                e -> {
                    try {
                        historyPageScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        List<Account> accounts = new ArrayList<>(accountDao.getAccountInfo(user.getId()));


        VBox historyBox = (VBox)  primaryStage.lookup("#applications_holder");
        ObservableList<Credit> transfers =  FXCollections.observableArrayList(creditDao.getApplication(user.getId()));
        historyBox.getChildren().clear();
        TableView table = new TableView();
        table.setEditable(false);
        TableColumn<Transfer, String> cash //
                = new TableColumn<Transfer, String>("Kwota");
        cash.setCellValueFactory(new PropertyValueFactory("cash"));
        TableColumn<Transfer, String> type //
                = new TableColumn<Transfer, String>("Status");
        type.setCellValueFactory(new PropertyValueFactory("accepted"));

        table.getColumns().addAll(cash,type);
        table.setItems(transfers);
        historyBox.getChildren().add(table);

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public void athCreditPage(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("athCredit.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button addClient = (Button)  primaryStage.lookup("#addClient");
        addClient.setOnAction(
                e -> {
                    try {
                        addClientPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button addAccountAndCard = (Button)  primaryStage.lookup("#addAccountAndCard");
        addAccountAndCard.setOnAction(
                e -> {
                    try {
                        addAccountAndCardPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button AthCredit = (Button)  primaryStage.lookup("#AthCredit");
        AthCredit.setOnAction(
                e -> {
                    try {
                        athCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        List<Account> accounts = new ArrayList<>(accountDao.getAccountInfo(user.getId()));


        VBox historyBox = (VBox)  primaryStage.lookup("#credit_holder");
        ObservableList<Credit> transfers =  FXCollections.observableArrayList(creditDao.getAllApplication());
        TableView table = new TableView();
        table.setEditable(false);
        TableColumn<Credit, String> id //
                = new TableColumn<Credit, String>("ID");
        id.setCellValueFactory(new PropertyValueFactory("id"));

        TableColumn<Credit, String> age //
                = new TableColumn<Credit, String>("Wiek");
        age.setCellValueFactory(new PropertyValueFactory("age"));


        TableColumn<Credit, String> pesel //
                = new TableColumn<Credit, String>("Pesel");
        pesel.setCellValueFactory(new PropertyValueFactory("pesel"));

        TableColumn<Credit, String> cash //
                = new TableColumn<Credit, String>("Kwota kredytu");
        cash.setCellValueFactory(new PropertyValueFactory("cash"));

        TableColumn<Credit, String> month //
                = new TableColumn<Credit, String>("Okres miesiecy");
        month.setCellValueFactory(new PropertyValueFactory("month"));

        TableColumn<Credit, String> children //
                = new TableColumn<Credit, String>("Ilość dzieci");
        children.setCellValueFactory(new PropertyValueFactory("children"));
        TableColumn<Credit, String> AvgPayout //
                = new TableColumn<Credit, String>("Srednie zarobki");
        AvgPayout.setCellValueFactory(new PropertyValueFactory("AvgPayout"));

        TableColumn<Credit, String> accept //
                = new TableColumn<Credit, String>("Akcja");

        Callback<TableColumn<Credit, String>, TableCell<Credit, String>> cellFactory
                = //
                new Callback<TableColumn<Credit, String>, TableCell<Credit, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Credit, String> param) {
                        final TableCell<Credit, String> cell = new TableCell<Credit, String>() {

                            final Button btn = new Button("Akceptuj");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Credit credit = getTableView().getItems().get(getIndex());
                                        try {
                                            creditDao.acceptCredit(credit.getId(),credit.getCash());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        accept.setCellFactory(cellFactory);


        TableColumn<Credit, String> denny //
                = new TableColumn<Credit, String>("Akcja");

        Callback<TableColumn<Credit, String>, TableCell<Credit, String>> cellFactory_
                = //
                new Callback<TableColumn<Credit, String>, TableCell<Credit, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Credit, String> param) {
                        final TableCell<Credit, String> cell = new TableCell<Credit, String>() {

                            final Button btn = new Button("Nie akcept.");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        Credit credit = getTableView().getItems().get(getIndex());
                                        try {
                                            creditDao.dennyCredit(credit.getId());
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        denny.setCellFactory(cellFactory_);





        table.getColumns().addAll(id,age,pesel,cash,month,children,AvgPayout,accept,denny);
        table.setItems(transfers);
        historyBox.getChildren().add(table);


        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public static void sendTransderScene(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sendTransfer.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button creditapplication = (Button)  primaryStage.lookup("#creditapplication");
        creditapplication.setOnAction(
                e -> {
                    try {
                        applicastionsList(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button credit = (Button)  primaryStage.lookup("#credit");
        credit.setOnAction(
                e -> {
                    try {
                        getCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button youraccout = (Button)  primaryStage.lookup("#youraccout");
        youraccout.setOnAction(
                e -> {
                    try {
                        mainPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button sendTransfer = (Button)  primaryStage.lookup("#sendTransfer");
        sendTransfer.setOnAction(
                e -> {
                    try {
                        sendTransderScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button historyPage = (Button)  primaryStage.lookup("#historyPage");
        historyPage.setOnAction(
                e -> {
                    try {
                        historyPageScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        List<Account> accounts = new ArrayList<>(accountDao.getAccountInfo(user.getId()));

        ChoiceBox account_from =  (ChoiceBox)  primaryStage.lookup("#account_from");
        accounts.forEach(account ->{
                    account_from.getItems().add(account.getAccout_number());
        });

        Button sendTransferBtn = (Button)  primaryStage.lookup("#sendTransferBtn");
        sendTransferBtn.setOnAction(
                e -> {
                    TextField cash_value  = (TextField)  primaryStage.lookup("#cash_value");
                    TextField currency_value  = (TextField)  primaryStage.lookup("#currency_value");
                    TextField account_number  = (TextField)  primaryStage.lookup("#account_number");
                    TextField title_value  = (TextField)  primaryStage.lookup("#title_value");

                    String accout_number_from = account_from.getSelectionModel().getSelectedItem().toString();
                    Float cash =  Float.parseFloat(cash_value.getText());
                    String account_to = account_number.getText();
                    String title = title_value.getText();

                    try {
                        TransferDao.sendTransfer(user,accout_number_from,account_to,cash,title,"Jan Kowalski");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public static void historyPageScene(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("history.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

        Button creditapplication = (Button)  primaryStage.lookup("#creditapplication");
        creditapplication.setOnAction(
                e -> {
                    try {
                        applicastionsList(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button credit = (Button)  primaryStage.lookup("#credit");
        credit.setOnAction(
                e -> {
                    try {
                        getCreditPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );

        Button youraccout = (Button)  primaryStage.lookup("#youraccout");
        youraccout.setOnAction(
                e -> {
                    try {
                        mainPage(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button sendTransfer = (Button)  primaryStage.lookup("#sendTransfer");
        sendTransfer.setOnAction(
                e -> {
                    try {
                        sendTransderScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        Button historyPage = (Button)  primaryStage.lookup("#historyPage");
        historyPage.setOnAction(
                e -> {
                    try {
                        historyPageScene(stage);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        List<Account> accounts = new ArrayList<>(accountDao.getAccountInfo(user.getId()));

        ChoiceBox account_select =  (ChoiceBox)  primaryStage.lookup("#account_select");
        accounts.forEach(account ->{
            account_select.getItems().add(account.getAccout_number());
        });
        Button show = (Button)  primaryStage.lookup("#show_history");
        show.setOnAction(
                e -> {
                    try {
                        VBox historyBox = (VBox)  primaryStage.lookup("#vbox_history");
                        ObservableList<Transfer> transfers =  FXCollections.observableArrayList(getTransferHistory(account_select.getSelectionModel().getSelectedItem().toString()));
                        historyBox.getChildren().clear();
                        TableView table = new TableView();
                        table.setEditable(false);
                        TableColumn<Transfer, String> dateColumn //
                                = new TableColumn<Transfer, String>("Data");
                        dateColumn.setCellValueFactory(new PropertyValueFactory("date"));
                        TableColumn<Transfer, String> cash //
                                = new TableColumn<Transfer, String>("Środki");
                        cash.setCellValueFactory(new PropertyValueFactory("cash"));
                        TableColumn<Transfer, String> type //
                                = new TableColumn<Transfer, String>("Typ przelewu");
                        type.setCellValueFactory(new PropertyValueFactory("type"));
                        TableColumn<Transfer, String> pdf //
                                = new TableColumn<Transfer, String>("Potwierdzenie");

                        Callback<TableColumn<Transfer, String>, TableCell<Transfer, String>> cellFactory
                                = //
                                new Callback<TableColumn<Transfer, String>, TableCell<Transfer, String>>() {
                                    @Override
                                    public TableCell call(final TableColumn<Transfer, String> param) {
                                        final TableCell<Transfer, String> cell = new TableCell<Transfer, String>() {

                                            final Button btn = new Button("PDF");

                                            @Override
                                            public void updateItem(String item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (empty) {
                                                    setGraphic(null);
                                                    setText(null);
                                                } else {
                                                    btn.setOnAction(event -> {
                                                        Transfer transfer = getTableView().getItems().get(getIndex());
                                                        System.out.println(transfer.getCash()
                                                                + "   " + transfer.getId_user_form());

                                                        Document document = new Document();
                                                        try
                                                        {
                                                            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("potwierdzenie_przelewu.pdf"));
                                                            document.open();
                                                            document.add(new Paragraph("Potwierdzenie przelwu"));
                                                            document.add(new Paragraph("Tutuł: " + transfer.getTitle()));
                                                            document.add(new Paragraph("Kwota: " + transfer.getCash() + " " + transfer.getCurrency_short()));
                                                            document.add(new Paragraph("Odbiorca: " + transfer.getRecpient()));


                                                            document.addTitle("Potwierdzenie przelewu");

                                                            document.close();
                                                            writer.close();
                                                        } catch (Exception e)
                                                        {
                                                            e.printStackTrace();
                                                        }
                                                    });
                                                    setGraphic(btn);
                                                    setText(null);
                                                }
                                            }
                                        };
                                        return cell;
                                    }
                                };
                        pdf.setCellFactory(cellFactory);


                        table.getColumns().addAll(dateColumn,cash,type,pdf);
                        table.setItems(transfers);
                        historyBox.getChildren().add(table);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );



        stage.setTitle("Hello!");
        stage.setScene(primaryStage);
        stage.show();

    }

    public void alert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public User createUserObject(String username, String lastName, String firstName, String password) {
        User user = new User();
        user.setUsername(username);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(password);

        return user;
    }

    public User loginUserObject(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    private static Image addTextOnImage(String url,User user, Card card) throws IOException {
        BufferedImage image = ImageIO.read(new File(url));
        //get the Graphics object
        Graphics g = image.getGraphics();
        //set font
        g.setFont(g.getFont().deriveFont(17f));
        g.setColor(java.awt.Color.BLACK);
        //display the text at the coordinates(x=50, y=150)
        g.drawString(card.getNumber(), 10, 120);
        g.drawString(user.getFirstName() +  " " + user.getLastName(), 10, 150);
        g.drawString(card.getValidMonth() + "/" + card.getValidYear() , 10, 170);
        g.dispose();
        return convertToFxImage(image);
    }

    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }

    private static String creditState(String state) {
        if(state == "0" ){
            return "W trakcjie rozpatrywania";
        } else if(state == "1" ){
            return "Kredyt udzielony";
        } else {
            return  "Kredyt nie udzielony";
        }
    }



    public static void main(String[] args) {
        launch(args);
    }

}

