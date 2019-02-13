package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealToDAOMemory;
import ru.javawebinar.topjava.dao.MealToDAOMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealToDAOMemory mealToDAOMemory = new MealToDAOMemoryImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equalsIgnoreCase("delete")){
                log.debug("delete meal and redirect to meals");
                int id = Integer.parseInt(request.getParameter("id"));
                mealToDAOMemory.delete(id);
                response.sendRedirect("/topjava/meals");
            } else if (action.equalsIgnoreCase("edit")) {
                log.debug("redirect to edit page");
                int id = Integer.parseInt(request.getParameter("id"));
                Meal meal = mealToDAOMemory.getMealById(id);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("add-edit.jsp").forward(request, response);
            } else if (action.equalsIgnoreCase("add")) {
                log.debug("redirect to add page");
                Meal meal = new Meal(0, null, null, 0);
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("add-edit.jsp").forward(request, response);
            }
        } else {
            log.debug("redirect to meals");
            request.setAttribute("list", mealToDAOMemory.list());
            request.setAttribute("formatter", TimeUtil.getDateTimeFormatter());
            request.getRequestDispatcher("meals.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equalsIgnoreCase("edit")) {
                Meal meal = new Meal(Integer.parseInt(request.getParameter("id")),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));
                log.debug("edit meal and redirect to meals");
                mealToDAOMemory.edit(meal);
            } else if (action.equalsIgnoreCase("add")) {
                Meal meal = new Meal(mealToDAOMemory.getId(),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));
                log.debug("add meal and redirect to meals");
                mealToDAOMemory.add(meal);
            }
        }
        response.sendRedirect("/topjava/meals");
    }
}
