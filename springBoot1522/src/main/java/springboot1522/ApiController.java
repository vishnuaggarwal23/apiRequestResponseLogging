package springboot1522;

import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
public class ApiController {

    @PostMapping(value = "/save/{id}", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    User save(@RequestBody User user, @PathVariable(value = "id", required = true) int id, @RequestParam(value = "age", required = true) int age) {
        return new User(id, user.getName(), age);
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 3886790981336564500L;
    private int id;
    private String name;
    private int age;

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
