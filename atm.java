import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;



public class atm {
    String id;
    String nome;
    String cognome;
    String carta;
    float euro;

    public atm() {}
    public atm(String id, String nome, String cognome, String carta, float euro) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.carta = carta;
        this.euro = euro;
    }

    static int contatore = 0;
    static final String FILE_PATH = "C:\\Users\\aless\\Documents\\DOCUMENTI\\programmi\\python\\atm.txt";

    static void carica_dati(ArrayList<atm>database){
        try (Scanner fileScanner = new Scanner(new File(FILE_PATH))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                database.add(new atm(data[0], data[1], data[2], data[3], Float.parseFloat(data[4])));
            }
            contatore = database.size();
        } catch (FileNotFoundException e) {
            System.out.println("Nessun dato trovato.");
        }
    
    }   

    static void salva_dati(ArrayList<atm>database, Scanner scan, String file_path){
        try (PrintWriter writer = new PrintWriter(new FileWriter(file_path))) {
            for (atm oggetto : database) {
                writer.println(oggetto.id + "," + oggetto.nome + "," + oggetto.cognome + "," + oggetto.carta + "," + oggetto.euro);
            }
        } catch (IOException e) {
            System.out.println("Errore nel salvataggio dei dati.");
        }
    }


    


    @SuppressWarnings("UnnecessaryReturnStatement")
    static void registrazione(ArrayList<atm>database, Scanner scan){
        atm oggetto = new atm();

        System.out.println("crea id (4 cifre max): ");
        oggetto.id = scan.nextLine();
        if(oggetto.id.length() > 4){
            System.out.println("id non corretto!\n");
            return;
        }else{

            System.out.println("inserisci nome: ");
            oggetto.nome = scan.nextLine();

            System.out.println("inserisci cognome:");
            oggetto.cognome = scan.nextLine();

            System.out.println("inserisci identificativo carta (8 cifre max): ");
            oggetto.carta = scan.nextLine();
            if(oggetto.carta.length() > 8){
                System.out.println("identificativo non corretto!\n");
                return;
            }else{

                System.out.println("inserisci somma primo deposito: ");
                oggetto.euro = scan.nextFloat();
                scan.nextLine(); //buffer


                for(atm i : database){
                    if(i.id.equals(oggetto.id) || i.carta.equals(oggetto.carta)){
                        System.out.println("id and/or identificativo gia esistenti!, effettura login!\n");
                        return;
                    }
                }

                database.add(oggetto);
                contatore++;
                salva_dati(database, scan, FILE_PATH);
                System.out.println("account registrato nell'atm!\n\n");
            }
        }
    }



    @SuppressWarnings("UnnecessaryReturnStatement")
    static void login(ArrayList<atm>database, Scanner scan){
        if(contatore == 0){
            System.out.println("nessuna utenza presente nel sistema!\n");
            return;
        }

        String id_login;
        String carta_login;

        System.out.println("inserisci id (4 cifre): ");
        id_login = scan.nextLine();

        if(id_login.length() > 4){
            System.out.println("id errato!\n");
            return;
        }else{
            System.out.println("inserisci identificativo carta(8 cifre): ");
            carta_login = scan.nextLine();
            scan.nextLine();//buffer

            if(carta_login.length() > 8){
                System.out.println("identificativo errato!\n");
                return;
            }else{

                atm trovato = null;
                for(atm i : database){
                    if(i.id.equals(id_login) && i.carta.equals(carta_login)){
                        trovato = i;
                        break;
                    }
                }

                if(trovato == null){
                    System.out.println("credenaziali errate!\n");
                    return;
                }else{

                    int operazione;
                    do { 
                        
                        System.out.println("benvenuto " + trovato.nome + trovato.cognome + ", id: "+ trovato.id + ", iden: " + trovato.carta);
                        System.out.println("saldo disponibile: $"+ trovato.euro);
                        System.out.println("---OPERAZIONI---");
                        System.out.println("1 -> deposita");
                        System.out.println("2 -> preleva");
                        System.out.println("3 -> elimina questa utenza(attenzione!)");
                        System.out.println("4 -> logout");
                        operazione = scan.nextInt();
                        scan.nextLine();//buffer

                        switch(operazione){
                            case 1 ->{
                                float deposito;
                                System.out.println("inserisci somma da depositare: ");
                                deposito = scan.nextFloat();
                                trovato.euro += deposito;
                                salva_dati(database, scan, FILE_PATH);
                                System.out.println("somma depositata: "+ deposito +", saldo totale: "+ trovato.euro);
                            }

                            case 2 ->{
                                float prelevo;
                                System.out.println("inserisci somma da prelevare: ");
                                prelevo = scan.nextFloat();
                                if(prelevo > trovato.euro){
                                    System.out.println("fondi insufficienti!");
                                }else{
                                    trovato.euro -= prelevo;
                                    salva_dati(database, scan, FILE_PATH);
                                    System.out.println("somma prlevata: "+ prelevo + ", saldo totale: "+ trovato.euro);
                                }
                            }

                            case 3->{
                                int conferma;
                                System.out.println("scelta eliminazione utenza, (irreversibile!)");
                                System.out.println("sei sicuro di voler eliminare l'utenza? (1->si, 2->annulla)");
                                conferma = scan.nextInt();
                                scan.nextLine();//buffer

                                if(conferma == 1){
                                    String conferma_id;
                                    System.out.println("inserisci id carta (4 cifre), per confermare: ");
                                    conferma_id = scan.nextLine();
                                    if(conferma_id.equals(trovato.id)){
                                    database.remove(trovato);
                                    contatore--;
                                    salva_dati(database, scan, FILE_PATH);
                                    System.out.println("utenza eliminata...logout...\n\n");
                                    return;

                                    }else{
                                        System.out.println("id errato, operazione annullata!\n");
                                    }
                                }

                                if(conferma == 2){
                                    System.out.println("operazione annullata!...\n");
                                }

                                else{
                                    System.out.println("NULL\n");
                                }

                            }


                            case 4 ->{
                                System.out.println("logout...\n\n");
                                return;
                            }
                            

                            default -> System.out.println("NULL\n");

                        }
                    }while(operazione != 4);
                }

            }
        }
    }



    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        ArrayList<atm> database = new ArrayList<>();
        Scanner scan = new Scanner(System.in);
        carica_dati(database);



        int scelta;
        do {
            System.out.println("OPERAZIONI:");
            System.out.println("1 -> registrati nell atm");
            System.out.println("2 -> login atm");
            System.out.println("3 -> esci");
            scelta = scan.nextInt();
            scan.nextLine(); //buffer

            switch(scelta){

                case 1 -> registrazione(database, scan);
                case 2 -> login(database, scan);
                case 3 -> System.out.println("uscita!...\n\n");
                default -> System.out.println("NULL\n");
            }
            

        }while(scelta != 3);
        
        scan.close();
    }

}
