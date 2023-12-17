package model;

public class Category {
    static private int CATEGORY_ID = 0;
    private int categoryID;
    private String name;

    public Category(String name) {
        this.categoryID = CATEGORY_ID++;
        this.name = name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
