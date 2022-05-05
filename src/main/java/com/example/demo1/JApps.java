package com.example.demo1;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.image.BufferedImage;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class JApps extends Application {
    private static final Logger logger = Logger.getLogger(JApps.class.getName());
    private UserDao userDao = new UserDao();
    User user = new User();

    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
        primaryStage.setTitle("JavaFX Welcome");
        loginPage(primaryStage);
    }

    public void loginPage(Stage primaryStage) throws IOException, SQLException {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));



        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 1);


        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button saveButton = new Button("Save");
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.getChildren().add(saveButton);
        grid.add(hBox, 1, 5);

        saveButton.setOnAction(actionEvent -> {
            String username = usernameTextField.getText().trim();
            String password = passwordField.getText();
            if (!StringPool.BLANK.equals(username) && !StringPool.BLANK.equals(password)) {
                try {

                    user = this.loginUserObject(username, password);

                    boolean userId = userDao.userLogin(user.getUsername(),user.getPassword());
                    if (userId) {
                        this.alert("Save", "Zalogowany!", AlertType.INFORMATION);
                        user = userDao.getInfoUser(user.getUsername());
                        mainPage(primaryStage);

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

        Scene scene = new Scene(grid, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void mainPage(Stage stage) throws IOException, SQLException {
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
                    //Scene  test= card.getParent().getScene();
                    //test
                    //card.setImage( addTextOnImage(cardMasterCard, user, BankCard));
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene primaryStage = new Scene(fxmlLoader.load(), 1280, 720);



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