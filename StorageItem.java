public class StorageItem { // класс для роботи зі змінними таблиці

    // створення змінних для таблиці
    private int id;
    private String itemName;
    private int itemCount;

    public StorageItem(int id, String itemName, int itemCount) { //створення методу, що тримає змінні
        this.id = id;
        this.itemName = itemName;
        this.itemCount = itemCount;
    }

    public int getId() { // метод отримки айди
        return id;
    }

    public String getItemName() { // метод отримки назви запчастини
        return itemName;
    }

    public int getItemCount() { // метод отримки кількості запчастини
        return itemCount;
    }

    public void setItemCount(int itemCount) { // метод установки кількості запчастин
        this.itemCount = itemCount;
    }

    public void setItemName(String itemName) { // метод установки назви запчастин
        this.itemName = itemName;
    }



}
