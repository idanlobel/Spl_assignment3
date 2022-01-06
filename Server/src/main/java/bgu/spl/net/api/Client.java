package bgu.spl.net.api;

import bgu.spl.net.message.Message;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class Client {
    private String userName;
    private String password;
    private String birthday;//may change to dateTime
    private boolean isLoggedIn;
    private ConcurrentSkipListSet<String> followers; //usernames who follow me
    private ConcurrentSkipListSet<String> following; //usernames I follow
    private short numPosts; //public posts
    private ConcurrentLinkedQueue<Message> unreadMessages; //Messages from other users that were sent while we were logged out
    private int connId;
    private LinkedList<String> usersIBlocked;
    private ConcurrentLinkedQueue<String> userBlockedMe; //maybe readers writers lock - for the case our user going over this list, and another user i blocking us and adding himself

    public Client(String username, String password, String birthday, int connId) {
        this.userName = username;
        this.password = password;
        this.birthday = birthday;
        this.connId = connId;
        followers = new ConcurrentSkipListSet<>();
        following = new ConcurrentSkipListSet<>();
        unreadMessages = new ConcurrentLinkedQueue<>();
        usersIBlocked = new LinkedList<>();
        userBlockedMe = new ConcurrentLinkedQueue<>();
    }
    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public short getAge() {
        return calcAge(birthday);
    }

    public void setBirthday(String birthday) {
        this.birthday=birthday;
    }

    public ConcurrentSkipListSet<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ConcurrentSkipListSet<String> followers) {
        this.followers = followers;
    }

    public ConcurrentSkipListSet<String> getFollowing() {
        return following;
    }

    public void setFollowing(ConcurrentSkipListSet<String> following) {
        this.following = following;
    }

    public short getNumPosts() {
        return numPosts;
    }

    public void setNumPosts(short numPosts) {
        this.numPosts = numPosts;
    }

    public ConcurrentLinkedQueue<Message> getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(ConcurrentLinkedQueue<Message> unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public int getConnId() {
        return connId;
    }

    public void setConnId(int connId) {
        this.connId = connId;
    }

    public LinkedList<String> getUsersIBlocked() {
        return usersIBlocked;
    }

    public void setUsersIBlocked(LinkedList<String> usersIBlocked) {
        this.usersIBlocked = usersIBlocked;
    }

    public ConcurrentLinkedQueue<String> getUserBlockedMe() {
        return userBlockedMe;
    }

    public void setUserBlockedMe(ConcurrentLinkedQueue<String> userBlockedMe) {
        this.userBlockedMe = userBlockedMe;
    }

    public void addUserToFollowing(String username) {
        following.add(username);
    }

    public void addUserToFollowers(String username) {
        followers.add(username);
    }

    public void removeUserFromFollowing(String username) {
        following.remove(username);
    }

    public void removeUserFromFollowers(String username) {
        followers.remove(username);
    }

    public void increaseNumPosts() {
        this.numPosts++;
    }

    public void addUnreadMessageToQueue(Message message) {
        unreadMessages.add(message);
    }

    public void addToUserIBLocked(String username) {
        usersIBlocked.add(username);
    }

    public void addToUserBLockedME(String username) {
        userBlockedMe.add(username);
    }
    private short calcAge(String birthday) {//calculate the age with difference betweenn DateTime.now and birthday
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthday1 = LocalDate.parse(birthday, formatter);
        Period period = Period.between(birthday1, LocalDate.now());
        return (short)period.getYears();
    }
}
