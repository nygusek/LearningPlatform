package controllers;

import dao.UserReportDao;
import model.UserReport;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/userReports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserReportController {
    @Inject
    UserReportDao userReport;

    @GET
    public List<UserReport> getAllReports() {
        return userReport.findAll();
    }
}
