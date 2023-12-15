package model;

import java.util.*;

public class CategoryList {
    private static CategoryList instance = null;
    private List<Category> categories;

    private Map<String, Integer> map;

    private CategoryList() {
        this.categories = new ArrayList<>();
    }

    public static CategoryList getInstance() {
        if (instance == null) {
            instance = new CategoryList();
            createSet();
            createCategory("Work");
            createCategory("Life");
            createCategory("Study");
        }
        return instance;
    }
    public static void createSet(){
        CategoryList cl = getInstance();
        if(cl.map == null){
            cl.map = new HashMap<>();
        }
    }

    public void setSet(String category) {
        this.map.put(category, map.getOrDefault(category, 0) + 1);

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
        if(map.get(name) != null && map.get(name) > 0){
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
