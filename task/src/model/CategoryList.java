package model;

import java.util.*;

public class CategoryList {
    private static CategoryList instance = null;
    private List<Category> categories;


    private CategoryList() {
        this.categories = new ArrayList<>();
    }

    public static CategoryList getInstance() {
        if (instance == null) {
            instance = new CategoryList();
            createCategory("Work");
            createCategory("Life");
            createCategory("Study");
        }
        return instance;
    }



    public static boolean createCategory(String name) {
        CategoryList cl = getInstance();
        if (cl.getCategoryByName(name).isPresent()) {
            return false;
        }
        return cl.getCategories().add(new Category(name));
    }

    public Optional<Category> getCategoryByName(String name) {
        return categories.stream()
                .filter(category -> category.getName().equals(name))
                .findFirst();
    }

    public boolean deleteCategory(String name) {
        if (categories.size() == 1) {
            return false;
        }
        return categories.removeIf(category -> category.getName().equals(name));
    }

//    public void updateCategory(String oldName, String newName) {
//        getCategoryByName(oldName).ifPresent(category -> category.setName(newName));
//    }

    public List<Category> getCategories() {
        return categories;
    }
}
