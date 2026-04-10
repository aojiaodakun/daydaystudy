package com.hzk.webserver.tongweb;

import java.util.List;

public class PersonWrapper {
    private String name;

    private List<Person> personList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    @Override
    public String toString() {
        return "PersonWrapper{" +
                "name='" + name + '\'' +
                ", personList=" + personList +
                '}';
    }
}
