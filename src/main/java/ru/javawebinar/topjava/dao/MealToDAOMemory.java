package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealToDAOMemory {
    List<MealTo> list();
    void add(Meal meal);
    void edit(Meal meal);
    void delete(int id);
    Meal getMealById(int id);
    int getId();
}
