package com.kolesnikov.gamble.model;

public class UserDto {
    private  String name;
    private  int id;

    public UserDto() {
    }

    public UserDto(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
