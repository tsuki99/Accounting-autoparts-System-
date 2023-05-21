import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import javafx.collections.*;


public class DatabaseHandler extends Configs { // класс, що надає доступ до бази даних

    Connection dbConnection;
    private static final String TABLE_NAME = "storageitems";

    public Connection getDbConnection() // метод отримання доступу до бази даних
            throws ClassNotFoundException, SQLException {

        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,
                dbUser, dbPass);

        return dbConnection;

    }

    public void signItem(String item_name) { // метод додання назви запчастини у базу даних

        String insert = "INSERT INTO " + Const.STORAGEITEMS_TABLE + "(" +
                Const.ITEM_NAME + ")" +
                "VALUES(?)";


        try {
            PreparedStatement prST = getDbConnection().prepareStatement(insert);
            prST.setString(1, item_name);


            prST.executeUpdate();
        } catch ( SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    public List<StorageItem> getAllItems() { // метод отримання усіх даних із бази данних у таблицю tableview
        List<StorageItem> items = new ArrayList<>();

        try {
            Connection connection = getDbConnection();
            // String query = "SELECT " + Const.STORAGEITEMS_ID + ", " + Const.ITEM_NAME + ", " + Const.ITEM_COUNT + " FROM " + Const.STORAGEITEMS_TABLE;
            String query = "SELECT @rownum:=@rownum+1 'row_number', s.* FROM storageitems s, (SELECT @rownum:=0) r";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int row_number = resultSet.getInt("row_number");
                int id_item = resultSet.getInt(Const.STORAGEITEMS_ID);
                String name_item = resultSet.getString(Const.ITEM_NAME);
                int count_item = resultSet.getInt(Const.ITEM_COUNT);

                StorageItem item = new StorageItem(id_item, name_item, count_item);
                items.add(item);
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return items;
    }


    public void updateItemCount(int id, int count) { // метод оновлення кількость запчастин у таблиці
        String sql = "UPDATE storageitems SET item_count = ? WHERE idstorageItems = ?";
        String DB_URL = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        try (Connection connection = DriverManager.getConnection(DB_URL, dbUser, dbPass);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, count);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteItem(int id, String name) throws SQLException { // метод видалення запчастин ( 1. при виборі у tableview, 2. при введені у textfield )
        String DB_URL = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        try (Connection connection = DriverManager.getConnection(DB_URL, dbUser, dbPass)) {
            if (id > 0) {
                PreparedStatement pstmt = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE idstorageitems = ?");
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            } else {
                PreparedStatement pstmt = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE item_name = ?");
                pstmt.setString(1, name);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


}

