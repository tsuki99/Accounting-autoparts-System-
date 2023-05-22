import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.*;
import javafx.util.Duration;
import java.util.List;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller { // метод для керування усіма функціями інтерфейсу

    @FXML
    private Text text_title;

    @FXML
    private Button button_add;

    @FXML
    private Button button_delete;

    @FXML
    private Button button_search;

    @FXML
    private TableView<StorageItem> table_view_dataBase;

    private final ObservableList<StorageItem> data = FXCollections.observableArrayList();

    @FXML
    private TextField text_field_enter;

    @FXML
    private AnchorPane anchorpane_central;

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @FXML
    void initialize() {

        System.out.println("Початок роботи програми!");

        DatabaseHandler dbHandler = new DatabaseHandler();

        button_add.setOnAction((event) -> { // Використання кнопки додавання запчастин
            dbHandler.signItem(text_field_enter.getText());
            text_field_enter.clear(); // Очищення поля вводу після натискання кнопки додавання запчастини
            table_view_dataBase.setItems(FXCollections.observableArrayList(dbHandler.getAllItems())); // Автооновлення таблиці після натискання кнопки додавання

        });


        button_delete.setOnAction((event) -> { // Використання кнопки видалення запчастин
            StorageItem selected_item = table_view_dataBase.getSelectionModel().getSelectedItem();
            if (selected_item != null) {
                try {
                    dbHandler.deleteItem(selected_item.getId(), null);
                    table_view_dataBase.setItems(FXCollections.observableArrayList(dbHandler.getAllItems()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                String name = text_field_enter.getText();
                if (!name.isEmpty()) {
                    try {
                        dbHandler.deleteItem(0, name);
                        table_view_dataBase.setItems(FXCollections.observableArrayList(dbHandler.getAllItems()));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            text_field_enter.clear(); // Очищення поля вводу після натискання кнопки видалення запчастини
        });

        button_search.setOnAction((event) -> { // Викорання кнопки пошуку ( виділення потрібного рядка з написаною запчастиною у textfield
            String searchQuery = text_field_enter.getText();
            ObservableList<StorageItem> allItems = table_view_dataBase.getItems();

            for (StorageItem item : allItems) {
                if (item.getItemName().equals(searchQuery)) {
                    table_view_dataBase.getSelectionModel().select(item);
                    break;
                }
            }
        });


        TableColumn<StorageItem, Integer> itemIdColumn = new TableColumn<>("Item ID");
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));

        TableColumn<StorageItem, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<StorageItem, Integer> itemCountColumn = new TableColumn<>("Item Count");
        itemCountColumn.setCellValueFactory(new PropertyValueFactory<>("itemCount"));


        itemCountColumn.setEditable(true);

        table_view_dataBase.getColumns().addAll(itemIdColumn,itemNameColumn, itemCountColumn);

        table_view_dataBase.setItems(data);

        List<StorageItem> items = dbHandler.getAllItems();
        data.addAll(items);

        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(60), e -> { // Автооновлення таблиці кожну хвилину
            table_view_dataBase.getItems().setAll(dbHandler.getAllItems());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


        table_view_dataBase.requestFocus();

        itemCountColumn.setCellFactory(col -> new CountCell()); // Звертання колонки Item Count до методу CountCell, що створює редагування полей

        table_view_dataBase.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                table_view_dataBase.requestFocus(); // установити фокус на таблицю
            }
        });


        itemCountColumn.setOnEditCommit(event -> { // редагування полей кількості у колонці item count
            int count = event.getNewValue();
            StorageItem selectedItem = table_view_dataBase.getSelectionModel().getSelectedItem();
            selectedItem.setItemCount(count);
            dbHandler.updateItemCount(selectedItem.getId(), count);
            // оновлення значень у таблиці
            table_view_dataBase.getItems().set(event.getTablePosition().getRow(), selectedItem);
            table_view_dataBase.getSelectionModel().select(event.getTablePosition().getRow());
            table_view_dataBase.requestFocus();
            event.consume();

        });


    }


}
