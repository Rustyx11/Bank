package com.example.demo1;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
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
import org.json.JSONArray;
import org.json.JSONObject;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
                        mainPage(stage);

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


    public static void sendTransderScene(Stage stage) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("sendTransfer.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);

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
                                                            document.addSubject("An example to show how attributes can be added to pdf files.");

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






    public static void main(String[] args) {
        launch(args);
    }

}

