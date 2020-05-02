package client.GUI;

import Models.*;
import Services.IObserver;
import Services.IServices;
import Services.ServerException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class MainController  implements Initializable,IObserver {

    ObservableList<DTOBJCursa> Curse= FXCollections.observableArrayList();
    ObservableList<DTOBJPartCapa> Participanti=FXCollections.observableArrayList();
    ObservableList<String> Teams=FXCollections.observableArrayList();

    private DTOAngajat crtAngajat;
    private IServices server;

    @FXML
    Button LogoutButton;
    @FXML
    TableView<DTOBJCursa> TabelCurse;
    @FXML
    TableColumn<DTOBJCursa,Integer> IdCol;
    @FXML
    TableColumn<DTOBJCursa,Integer> CapCol;
    @FXML
    TableColumn<DTOBJCursa,Integer> NrCol;
    @FXML
    Button clearsearch;
    @FXML
    TextField TFSearch;
    @FXML
    Button SearchByTeam;
    @FXML
    TableView<DTOBJPartCapa> TabelPart;
    @FXML
    TableColumn<DTOBJPartCapa,Integer> IDPart;
    @FXML
    TableColumn<DTOBJPartCapa,Integer> NumePart;
    @FXML
    TableColumn<DTOBJPartCapa,Integer> CapacitatePart;

    @FXML
    Button InscButton;
    @FXML
    ComboBox<Integer> CapBox;
    @FXML
    TextField TFNumePart;
    @FXML
            ComboBox<String> TeamBox;






    public MainController() {

    }

    public void setServer(IServices server1){
        this.server=server1;
    }

    public void setUser(DTOAngajat crtAngajat){
        this.crtAngajat=crtAngajat;
    }

    @FXML
    public void handleclear(){
        TFSearch.setText("");
        TFNumePart.setText("");
    }

    public void AngajatSubmitted(DTOBJCursa[] result) throws ServerException {
        Platform.runLater(()->{
            System.out.println("S-a apelat AngajatSubmitted din MainCtrl");
            AddNewDataCurse(result);

        });




    }
    public void AddNewDataCurse(DTOBJCursa [] source){
        this.Curse.clear();
        for(DTOBJCursa c:source){
            this.Curse.add(c);
        }
        this.TabelCurse.setItems(this.Curse);
    }
    @FXML
    public void handleLogout(ActionEvent actionEvent){
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();


    }
    @FXML
    public void handlesearch(){
        try{
            this.TabelPart.getItems().clear();
            String team=TFSearch.getText();
            DTOBJPartCapa[] result=this.server.searchByTeam(team);
            for(DTOBJPartCapa part : result){
                Participanti.add(part);
            }
            this.TabelPart.setItems(Participanti);
        }catch (ServerException e){
            System.out.println("Error when searching "+e);
        }
    }
    public void logout() {
        try {
            server.logout(crtAngajat, this);
        } catch (ServerException e) {
            System.out.println("Logout error " + e);
        }
    }

    public void Inscriere()throws ServerException{
        System.out.println("se apeleaza Inscriere");
        String numePart=TFNumePart.getText();
        String numeEchipa=TeamBox.getValue();
        int capacitate= CapBox.getValue();
        System.out.println(numePart+" "+numeEchipa+" "+capacitate);
        DTOInfoSubmit submit=new DTOInfoSubmit(capacitate,numePart,numeEchipa);
        this.server.submitInscriere(submit);
    }

    @FXML
    public void handlesubmit(){
        System.out.println("Se apeleaza Inscriere");
        try{
            Inscriere();
        }catch (ServerException e){
            System.out.println("Error when submitting from MainCtrl"+e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    public void initialiazeTabels(){
        IdCol.setCellValueFactory(new PropertyValueFactory<DTOBJCursa,Integer>("id"));
        CapCol.setCellValueFactory(new PropertyValueFactory<DTOBJCursa,Integer>("capacitate"));
        NrCol.setCellValueFactory(new PropertyValueFactory<DTOBJCursa,Integer>("Nrinscrisi"));
        IDPart.setCellValueFactory(new PropertyValueFactory<DTOBJPartCapa,Integer>("id"));
        NumePart.setCellValueFactory(new PropertyValueFactory<DTOBJPartCapa,Integer>("Nume"));
        CapacitatePart.setCellValueFactory(new PropertyValueFactory<DTOBJPartCapa,Integer>("capactiate"));
    }
    public void setCurseTabel() {
                try{
                    this.TabelCurse.getItems().clear();
                    DTOBJCursa[] curse=this.server.getCurseDisp();
                    for(DTOBJCursa c:curse){
                        Curse.add(c);
                    }
                    TabelCurse.setItems(Curse);
                }catch (ServerException e){
                    System.out.println("Error when setting CurseTabel"+e);
                }
            }
    public void setComboBox() {
        ObservableList<Integer> tipuriCurse=FXCollections.observableArrayList();
        for(DTOBJCursa c : this.Curse){
            tipuriCurse.add(c.getCapacitate());
        }
        CapBox.setItems(tipuriCurse);
    }
    public void setTeamBox(){
       try{
           String[] echipe=this.server.getTeams();
           for (String echipa:echipe
                ) {
               Teams.add(echipa);
           }
           TeamBox.setItems(Teams);
       }catch (ServerException e){
           System.out.println("Error getting all teams"+e);
       }
    }
}
