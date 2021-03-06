package dao;


import model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.Query;
import java.util.List;

@ApplicationScoped
public class UserDao extends DaoAbstract<User, Integer> {

    @Inject
    CourseDao course;

    @Inject
    UserReportDao userReport;

    public List<User> findAll() {
        connectionDB.openCurrentSession();
        List<User> users = connectionDB.getCurrentSession().createQuery("from User").list();
        connectionDB.closeCurrentSession();
        return users;
    }

    public List<User> findUserByRole(Integer idRoli) {
        connectionDB.openCurrentSession();
        Role role = new Role();
        role.setId(idRoli);
        Query query = connectionDB.getCurrentSession().createQuery("FROM User where role =: role");
        List<User> users =  query
                .setParameter("role", role)
                .getResultList();
        connectionDB.closeCurrentSession();
        return users;
    }



    public User findUserByUsernameAndPassword(String username, String password) {
        connectionDB.openCurrentSession();
        Query query = connectionDB.getCurrentSession().createQuery("FROM User WHERE username = :username AND password = :password ");
        query.setParameter("username", username);
        query.setParameter("password", password);
        return getUserFromQuery(query);
    }

    private User getUserFromQuery(Query query) {
        User user = null;
        try {
            user = (User) query.getSingleResult();
            connectionDB.closeCurrentSession();
            return user;
        } catch (Exception e) {
            connectionDB.closeCurrentSession();
            return user;
        }
    }

    public User findByUsername(String username) {
        connectionDB.openCurrentSession();
        Query query = connectionDB.getCurrentSession().createQuery("FROM User WHERE username = :username ");
        query.setParameter("username", username);
        return getUserFromQuery(query);
    }


    public boolean checkUniquenessUsername(User user) {
        connectionDB.openCurrentSession();
        List<User> users = findAll();
        for (User it : users) {
            if (it.getUsername().equalsIgnoreCase(user.getUsername())) {
                connectionDB.closeCurrentSession();
                return false;
            }
        }
        connectionDB.closeCurrentSession();
        return true;
    }

    public void updateUserCourses(String username, Course newCourse) {
        User loadedUser = findByUsername(username);
        loadedUser.getCourses().add(newCourse);
        update(loadedUser);
    }

    public void updateStudentCourses(String username, Course forwardedCourse) {
        User loadedUser = findByUsername(username);
        Course loadedCourse = course.findById(forwardedCourse.getId());
        loadedUser.getStudentCourses().add(loadedCourse);
        update(loadedUser);
    }

    public void updateUserReports(String username, UserReport newReport) {
        User loadedUser = findByUsername(username);
        loadedUser.getUserReports().add(newReport);
        update(loadedUser);
    }

    public void updateUserOpinions(String username, UserOpinion newOpinion) {
        User loadedUser = findByUsername(username);
        loadedUser.getOpinions().add(newOpinion);
        update(loadedUser);
    }

    public List<UserReport> returnUserReports(String username) {
        User loadedUser = findByUsername(username);
        return loadedUser.getUserReports();
    }

    public List<UserOpinion> getOpinions(String username) {
        User loadedUser = findByUsername(username);
        return loadedUser.getOpinions();
    }

    public double getAvarangeOpinions(String username) {
        List<UserOpinion> opinions = getOpinions(username);
        return opinions
                .stream()
                .mapToDouble(UserOpinion::getValue)
                .average()
                .orElse(Double.NaN);
    }

    public List<User> getUserByEnteredString(String enteredString) {
        connectionDB.openCurrentSession();
        Query query = connectionDB.getCurrentSession().createQuery("FROM User where username like :username OR firstName like :firstname OR lastName like :lastname");
        List<User> users =  query
                .setParameter("username", "%" + enteredString + "%")
                .setParameter("firstname", "%" + enteredString + "%")
                .setParameter("lastname", "%" + enteredString + "%")
                .getResultList();
        connectionDB.closeCurrentSession();
        return users;
    }
}


