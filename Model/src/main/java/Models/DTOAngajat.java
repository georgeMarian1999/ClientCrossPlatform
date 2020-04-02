package Models;

import java.io.Serializable;

public class DTOAngajat implements Serializable,Comparable<DTOAngajat> {
    private String username;
    private String password;
    public DTOAngajat(String user,String pass){
        this.username=user;
        this.password=pass;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "DTOAngajat{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public int compareTo(DTOAngajat o) {
        return this.username.compareTo(o.username);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
