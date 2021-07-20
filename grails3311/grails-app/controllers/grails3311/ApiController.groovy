package grails3311

import grails.converters.JSON
import grails.validation.Validateable

class ApiController {
    static responseFormats = ['json']
    static allowedMethods = [respondSave: 'POST', renderSave: 'POST']

    def respondSave(User user) {
        respond(new User(id: params.int('id'), name: user.name, age: params.int('age')))
    }

    def renderSave(User user) {
        render(new User(id: params.int('id'), name: user.name, age: params.int('age')) as JSON)
    }
}

class User implements Serializable, Validateable {
    private static final long serialVersionUID = 3886790981336564500L;
    private int id;
    private String name;
    private int age;

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    static constraints = {
        id nullable: true
        name nullable: true
        age nullable: true
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