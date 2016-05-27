package Database;

import java.security.PublicKey;
import java.sql.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Lukas on 02-Apr-16.
 */
public class SQLHandler {
    private static SQLHandler ourInstance = new SQLHandler();

    private Connection connection = null;
    private Statement statement = null;

    public static SQLHandler getInstance() {
        return ourInstance;
    }

    public ResultSet getPredpis(int id){
        String query = "SELECT datum, lekarnici.meno AS lekarnik, pacienti.meno AS pacient, ucinne_latky.nazov AS latka, lieky.nazov AS liek, lekarnici.id AS lekarnik_id, pacienti.id AS pacient_id, ucinne_latky.id AS latka_id, lieky.id AS liek_id FROM predpis " +
                "JOIN lekarnici ON predpis.lekarnik_id = lekarnici.id " +
                "JOIN pacienti ON predpis.pacient_id = pacienti.id " +
                "JOIN lieky ON predpis.liek_id = lieky.id " +
                "JOIN ucinne_latky ON predpis.ucinna_latka_id = ucinne_latky.id " +
                "WHERE predpis.id = " + id;
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getPacient(int id){
        String query = "SELECT meno, bydlisko, rodne_cislo, kod FROM pacienti " +
                "JOIN poistovne ON pacienti.poistovna_id = poistovne.\"id\" WHERE pacienti.id = " + id;
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getLekarnik(int id){
        String query = "SELECT lekarnici.meno, lekarnici.pracovisko, sum(cena) AS zarobok FROM lekarnici " +
                "JOIN predpis ON lekarnici.id = predpis.lekarnik_id " +
                "JOIN lieky ON predpis.liek_id = lieky.id " +
                "WHERE lekarnici.id = " +id+
                " GROUP BY lekarnici.meno, lekarnici.pracovisko";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getPoistovne(){
        String query = "SELECT * FROM poistovne";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getUcinneLatky(){
        String query = "SELECT * FROM ucinne_latky";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getVyrobcovia(){
        String query = "SELECT * FROM vyrobca";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getFormy(){
        String query = "SELECT * FROM forma";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getLiek(int id){
        String query = "SELECT ucinne_latky.nazov AS latka, lieky.nazov AS liek, forma.nazov AS forma, vyrobca.nazov AS vyrobca, " +
                "atc_level1.nazov AS atc1, atc_level2.nazov AS atc2, atc_level3.nazov AS atc3, atc_level4.nazov AS atc4, atc_level5.nazov AS atc5, " +
                "atc_level1.skratka AS atc1s, atc_level2.skratka AS atc2s, atc_level3.skratka AS atc3s, atc_level4.skratka AS atc4s, atc_level5.skratka AS atc5s FROM ucinne_latky\n" +
                "JOIN lieky ON lieky.ucinna_latka_id = ucinne_latky.id " +
                "JOIN forma ON lieky.forma_id = forma.id " +
                "JOIN vyrobca ON lieky.vyrobca_id = vyrobca.id " +
                "JOIN atc ON lieky.atc_id = atc.id " +
                "JOIN atc_level1 ON atc.l1 = atc_level1.id " +
                "JOIN atc_level2 ON atc.l2 = atc_level2.id " +
                "JOIN atc_level3 ON atc.l3 = atc_level3.id " +
                "JOIN atc_level4 ON atc.l4 = atc_level4.id " +
                "JOIN atc_level5 ON atc.l5 = atc_level5.id " +
                "WHERE lieky.id = "+id;
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getLieky(){
        String query = "SELECT * FROM lieky";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getLiekyByLatky(int id){
        String query = "SELECT * FROM lieky WHERE lieky.ucinna_latka_id = "+id;
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public boolean vytvorPredpis(String datum, int lekarnik_id, int pacient_id, int latka_id, int liek_id){
        String query = "BEGIN;INSERT INTO predpis (pacient_id,lekarnik_id,datum,ucinna_latka_id,liek_id) " +
                "VALUES ("+pacient_id+","+lekarnik_id+",'"+datum+"',"+latka_id+","+liek_id+");COMMIT;";
        try {
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
            return false;
        }
    }

    public boolean vytvorUcinnuLatku(String meno){
        String query = "BEGIN;INSERT INTO ucinne_latky (nazov) " +
                "VALUES ("+meno+");COMMIT;";
        try {
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
            return false;
        }
    }

    public ResultSet getLekarnici(){
        String query = "SELECT * FROM lekarnici";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getObmedzenia(){
        String query = "SELECT * FROM obmedzenie";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public boolean vytvorLiek(String nazov, int cena, int vyrobca, int forma, int latka, List<Integer> obmedzenia){
        String query;
        if(!obmedzenia.isEmpty()) {
            query = "BEGIN;WITH first_insert AS (INSERT INTO \"lieky\" (nazov,cena,atc_id,vyrobca_id,forma_id,ucinna_latka_id) VALUES ('"+nazov+"',"+cena+","+ ThreadLocalRandom.current().nextInt(1, 10)+","+vyrobca+","+forma+","+latka+") RETURNING id)";
            query += " INSERT INTO preskripcne_obmedzenie_lieku (liek_id,obmedzenie_id) VALUES ((SELECT first_insert.id FROM first_insert),";
            query += obmedzenia.get(0);

            for(int i =1;i<obmedzenia.size();i++){
                query+="), ((SELECT first_insert.id FROM first_insert),"+obmedzenia.get(i);
            }
            query+=");COMMIT;";
        }
        else{
            query = "BEGIN;INSERT INTO \"lieky\" (nazov,cena,atc_id,vyrobca_id,forma_id,ucinna_latka_id) VALUES ('"+nazov+"',"+cena+","+ ThreadLocalRandom.current().nextInt(1, 10)+","+vyrobca+","+forma+","+latka+");COMMIT;";
        }
        try {
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
            return false;
        }
    }

    public ResultSet getUcinneLatkyDetail(int id){
        String query = "SELECT ucinne_latky.nazov AS latka, lieky.nazov AS liek, forma.nazov AS forma, vyrobca.nazov AS vyrobca, " +
                "atc_level1.nazov AS atc1, atc_level2.nazov AS atc2, atc_level3.nazov AS atc3, atc_level4.nazov AS atc4, atc_level5.nazov AS atc5, " +
                "atc_level1.skratka AS atc1s, atc_level2.skratka AS atc2s, atc_level3.skratka AS atc3s, atc_level4.skratka AS atc4s, atc_level5.skratka AS atc5s FROM ucinne_latky\n" +
                "JOIN lieky ON lieky.ucinna_latka_id = ucinne_latky.id " +
                "JOIN forma ON lieky.forma_id = forma.id " +
                "JOIN vyrobca ON lieky.vyrobca_id = vyrobca.id " +
                "JOIN atc ON lieky.atc_id = atc.id " +
                "JOIN atc_level1 ON atc.l1 = atc_level1.id " +
                "JOIN atc_level2 ON atc.l2 = atc_level2.id " +
                "JOIN atc_level3 ON atc.l3 = atc_level3.id " +
                "JOIN atc_level4 ON atc.l4 = atc_level4.id " +
                "JOIN atc_level5 ON atc.l5 = atc_level5.id " +
                "WHERE ucinne_latky.id = "+id;
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getPacienti(){
        String query = "SELECT * FROM pacienti";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getPacientDetail(int id){
        String query = "SELECT lieky.nazov, pacienti.meno FROM pacienti " +
                "JOIN predpis ON pacienti.id = predpis.pacient_id " +
                "JOIN lieky ON lieky.id = predpis.liek_id " +
                "WHERE pacienti.id = "+id;
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public ResultSet getPredpisy(boolean datum, boolean lekarnik, boolean pacient, boolean latka){
        String query = "SELECT predpis.id ";
        if(datum) {
            query += ",datum AS Datum ";
        }
        if(lekarnik){
            query += ",lekarnici.meno AS Lekarnik ";
        }
        if(pacient){
            query += ",pacienti.meno AS Pacient ";
        }
        if(latka){
            query += ",ucinne_latky.nazov AS UcinnaLatka ";
        }
        query += "FROM predpis JOIN lekarnici ON predpis.lekarnik_id=lekarnici.id JOIN pacienti ON predpis.pacient_id = pacienti.id JOIN ucinne_latky ON predpis.ucinna_latka_id = ucinne_latky.id ORDER BY predpis.id";
        ResultSet result = null;
        try {
            result = statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query \n%s \n\n%s",query,e);
        }
        return result;
    }

    public void zmenMenoPacienta(int id, String meno){
        String query = "BEGIN;UPDATE pacienti SET meno = '" +meno+ "' WHERE id = "+id+";COMMIT;";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
        }
    }

    public void zmenRodneCisloPacienta(int id, String rc){
        String query = "BEGIN;UPDATE pacienti SET rodne_cislo = '" +rc+ "' WHERE id = "+id+";COMMIT;";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
        }
    }

    public void zmenAdresuPacienta(int id, String adresa){
        String query = "BEGIN;UPDATE pacienti SET bydlisko = '" +adresa+ "' WHERE id = "+id+";COMMIT;";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
        }
    }

    public void zmenPoistovnuPacienta(int id, int poistovna){
        String query = "BEGIN;UPDATE pacienti SET poistovna_id = (SELECT id FROM poistovne WHERE kod = " +poistovna+") WHERE id = "+id+";COMMIT";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
        }
    }

    public void vytvorPacienta(String meno, String bydlisko, String rodne_cislo, int poistovna){
        String query = "BEGIN;INSERT INTO pacienti (meno,bydlisko,rodne_cislo,poistovna_id) VALUES ('"+meno+"','"+bydlisko+"','"+rodne_cislo+"',(SELECT id FROM poistovne WHERE kod = " +poistovna+"));COMMIT;";
        try {
            statement.execute(query);
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
        }
    }

    public boolean vymazPacienta(int id){
        String query = "BEGIN;DELETE FROM pacienti WHERE id = "+id+";COMMIT;";
        try {
            statement.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.printf("Chyba v query: %s\n%s",query,e);
            return false;
        }
    }

    private SQLHandler() {

        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://127.0.0.1:5432/DBS_project", "postgres",
                    "1235789510");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
            return;
        }
    }
}
