import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;


public class CountCell extends TableCell<StorageItem, Integer> { // класс для роботи з полями колонки item count таблиці

    private TextField textField; // створення текстового поля для додавання у колонку item count таблиці

    private boolean tryParseInt(String value) { // Перехват помилки у випадку, якщо в поле колонки Item Count вводиться не число
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public CountCell() { // Метод створення полей для колонки Item Count, перевірки введених даних на числа, та вивід вікна помилки у випадку, якщо введено не число
        textField = new TextField();
        textField.setOnAction(event -> {
            if(tryParseInt(textField.getText())){
                commitEdit(Integer.parseInt(textField.getText()));
            } else {
                cancelEdit();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Помилка!");
                alert.setHeaderText("Нечислове значення");
                alert.setContentText("Будь ласка, введіть число");
                alert.showAndWait();
            }
        });

    }



    /*
    public CountCell() { // Старий метод створення полів для колонки Item Count ( про всяк випадок )
        textField = new TextField();
        textField.setOnAction(event -> commitEdit(Integer.parseInt(textField.getText())));
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                commitEdit(Integer.parseInt(textField.getText()));
            }
        });

    }

     */

    @Override
    protected void updateItem(Integer item, boolean empty) { // метод оновлення даних
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (isEditing()) {
            setText(null);
            setGraphic(textField);
            textField.setText(item.toString());
        } else {
            setText(item.toString());
            setGraphic(null);
        }
    }

    @Override
    public void startEdit() { // метод початку редагування
        super.startEdit();
        textField.setText(getItem().toString());
        setText(null);
        setGraphic(textField);
        textField.requestFocus();
        textField.selectAll();
    }

    @Override
    public void cancelEdit() { // метод закриття процесу редагування
        super.cancelEdit();
        setText(getItem().toString());
        setGraphic(null);
    }

    @Override
    public void commitEdit(Integer newValue) { // метод створення процесу редагування
        super.commitEdit(newValue);
        if (getTableRow() != null) {
            ((StorageItem) getTableRow().getItem()).setItemCount(newValue);
        }
    }




}
