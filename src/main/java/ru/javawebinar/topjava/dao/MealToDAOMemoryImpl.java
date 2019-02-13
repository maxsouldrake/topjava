package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealToDAOMemoryImpl implements MealToDAOMemory {
    private static final AtomicInteger AUTO_ID = new AtomicInteger(0);

    private Map<Integer, Meal> map;

    {
        map = new ConcurrentHashMap<>();
        Meal meal1 = new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
        Meal meal2 = new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
        Meal meal3 = new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);
        Meal meal4 = new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
        Meal meal5 = new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
        Meal meal6 = new Meal(getId(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
        map.put(meal1.getId(), meal1);
        map.put(meal2.getId(), meal2);
        map.put(meal3.getId(), meal3);
        map.put(meal4.getId(), meal4);
        map.put(meal5.getId(), meal5);
        map.put(meal6.getId(), meal6);
    }

    public int getId() {
        return AUTO_ID.getAndIncrement();
    }

    @Override
    public List<MealTo> list() {
        return MealsUtil.getFilteredWithExcess(new ArrayList<>(map.values()), LocalTime.MIN, LocalTime.MAX,2000);
    }

    @Override
    public void add(Meal meal) {
        map.put(meal.getId(), meal);
    }

    @Override
    public void edit(Meal meal) {
        map.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        map.keySet().removeIf(i -> i.equals(id));
    }

    @Override
    public Meal getMealById(int id) {
        return map.get(id);
    }
}
