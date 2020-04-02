package Models;

import java.io.Serializable;

public class Angajat implements Comparable<Angajat>,Serializable,Entity<Integer> {
    private Integer id;
    private String username;
    private String password;

    public Angajat(Integer id,String user,String pass){
        this.id=id;
        this.username=user;
        this.password=pass;
    }

    public Angajat(String username){
        this.username=username;
        this.id=0;
        this.password="";
    }
    @Override
    public String toString() {
        String string="Angajat "+username+" cu id-ul "+id;
        return string;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DTOAngajat convert(){
        DTOAngajat result=new DTOAngajat(this.username,this.password);
        return result;
    }

    @Override
    public int compareTo(Angajat o) {
        return this.username.compareTo(o.username);
    }
}
