package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryList {
    private static CategoryList instance = null;
    private List<Category> categories;

    private CategoryList() {
        this.categories = new ArrayList<>();
    }

    public static CategoryList getInstance() {
        if (instance == null) {
            instance = new CategoryList();
            addCategory("Work");
            addCategory("Life");
            addCategory("Study");
        }
        return instance;
    }

    public static String addCategory(String name) {
        CategoryList cl = getInstance();
        if (cl.getCategoryByName(name).isPresent()) {
            return "Category already exists";
        }
        cl.getCategories().add(new Category(name));
        return "Category successfully added";
    }

    public Optional<Category> getCategoryByName(String name) {
        return categories.stream()
                .filter(category -> category.getName().equals(name))
                .findFirst();
    }

    public void removeCategory(String name) {
        categories.removeIf(category -> category.getName().equals(name));
    }

    public void updateCategory(String oldName, String newName) {
        getCategoryByName(oldName).ifPresent(category -> category.setName(newName));
    }

    public List<Category> getCategories() {
        return categories;
    }
}
