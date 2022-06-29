package edu.umcs.bookystore.dtos;

import java.util.HashSet;
import java.util.Set;

public class OrderDto {

    private long id;
    private UserInfoDto user;
    private boolean isCompleted;
    private boolean isPaid;
    private Set<String> bookNames = new HashSet<>();

    public OrderDto() {
    }

    public OrderDto(long id, UserInfoDto user, boolean isCompleted, boolean isPaid, Set<String> booksNames) {
        this.id = id;
        this.user = user;
        this.isPaid = isPaid;
        this.isCompleted = isCompleted;
        this.bookNames = booksNames;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserInfoDto getUser() {
        return user;
    }

    public void setUser(UserInfoDto user) {
        this.user = user;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Set<String> getBookNames() {
        return bookNames;
    }

    public void setBookNames(Set<String> booksNames) {
        this.bookNames = booksNames;
    }

    public void addBookName(String bookName) {
        this.bookNames.add(bookName);
    }

    public void removeBookName(String bookName) {
        this.bookNames.remove(bookName);
    }

}
