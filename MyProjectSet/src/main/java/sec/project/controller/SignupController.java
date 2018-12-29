package sec.project.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;


@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    
    private List<String> pcpartList;
    
    @Autowired
    private HttpSession session;

    public SignupController() throws SQLException {
        this.pcpartList = new ArrayList<>();
    }

    @RequestMapping("/")
    public String homeindex() {
        return "redirect:/index";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deletePcpart(@RequestParam String pcpartID) throws SQLException {
        int pcpartId = Integer.parseInt(pcpartID);

        Connection connection = dbConnect();

        String query = "DELETE FROM pcpart WHERE ID='" + pcpartId + "'";
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception exception) {
            System.out.println(exception);
        }

        return "redirect:/admin";
    }

    @RequestMapping("/admin")
    public String admin(Model model) throws SQLException {

        populateDatabase();
        model.addAttribute("list", this.pcpartList);

        return "admin";
    }

    @RequestMapping("/logon")
    public String logon() {

        return "logon";
    }

    @RequestMapping("/alterlogin")
    public String alterlogin(@RequestParam(required = false) String username, @RequestParam(required = false) String password) throws SQLException {

        if (username != null && password != null && !username.trim().isEmpty() && !password.trim().isEmpty()) {
            Connection connection = dbConnect();

            String query = "SELECT * FROM pcpart_users WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "'";
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            if (results.isBeforeFirst()) {
                session.setAttribute("Username", username);
                session.setAttribute("Login", "success");
                return "redirect:/alter";
            }
        }

        return "alterlogin";
    }

    @RequestMapping("/alter")
    public String alter(Model model) {

        if (session.getAttribute("Login") == null) {
            return "redirect:/alterlogin";
        }
        if (session.getAttribute("Login") == "success") {

            String user = this.session.getAttribute("Username").toString();
            model.addAttribute("user_name", user);
            return "alter";
        }

        return "redirect:/alterlogin";
    }

    @RequestMapping(value = "/passwordchange", method = RequestMethod.POST)
    public String passwordChange(@RequestParam String newPassword, @RequestParam String newPasswordConfirm, @RequestParam String username) throws SQLException {

        Connection connection = dbConnect();
        if (newPassword.equals(newPasswordConfirm)) {
            String query = "UPDATE pcpart_users SET password = '" + newPassword + "' WHERE username = '" + username + "'";
            Statement statement = connection.createStatement();

            statement.executeUpdate(query);
            return "redirect:/success";
        }
        return "redirect:/alter";
    }

    @RequestMapping("/success")
    public String success() {

        return "success";
    }

    @RequestMapping("/index")
    public String submitForm(Model model, @RequestParam(required = false) String name, @RequestParam(required = false) String company, @RequestParam(required = false) String topsecret) throws SQLException {

        Connection connection = dbConnect();

        if (name != null && company != null && !name.trim().isEmpty() && !company.trim().isEmpty()) {
            Signup content = signupRepository.save(new Signup(name, company, topsecret));

            
            String query = "INSERT INTO pcpart (PCPART_NAME, COMPANY, MASTER_CODE) VALUES (' " + name + "', '" + company + "', '" + topsecret + "')";
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            } catch (Exception exception) {
                System.out.println(exception);
            }

        }
        populateDatabase();
        model.addAttribute("list", this.pcpartList);
        return "index";
    }

    public void populateDatabase() throws SQLException {

        // Removing double entries
        this.pcpartList.clear();

        Connection connection = dbConnect();

        // Grab the list from database
        String query = "SELECT * FROM pcpart";

        // Select
        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);

            while (results.next()) {
                String pcpartID = results.getString("ID");
                String pcpartName = results.getString("PCPART_NAME");
                String company2 = results.getString("COMPANY");
                String masterCode = results.getString("MASTER_CODE");

                String result = pcpartID + ":" + pcpartName + ":" + company2 + ":" + masterCode;
                this.pcpartList.add(result);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public Connection dbConnect() throws SQLException {

        // Open connection to a database -- do not alter this code
        String databaseAddress = "";
        try {
            databaseAddress = "jdbc:h2:file:./database";
        } catch (Exception exception) {
            System.out.println(exception);
        }

        Connection connection = DriverManager.getConnection(databaseAddress, "sa", "");

        return connection;
    }

}