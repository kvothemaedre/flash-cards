package flashcards;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.io.PrintWriter;
import java.io.File;

public class Main {
    static Map<String, String> cards = new LinkedHashMap<>();
    static Map<String, String> reversedCards = new LinkedHashMap<>();
    static Map<String, Integer> error = new LinkedHashMap<>();
    static ArrayList<String> logSave = new ArrayList<>();
    static public void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The card:");
        logSave.add("The card:");
        String key = scanner.nextLine();
        logSave.add(key);
        if (cards.containsKey(key)) {
            System.out.println("The card \"" + key + "\" already exists. Try again:");
            logSave.add("The card \"" + key + "\" already exists. Try again:");
            return;
        }
        System.out.println("The definition of the card:");
        logSave.add("The definition of the card:");
        String value = scanner.nextLine();
        logSave.add(value);
        if (cards.containsValue(value)) {
            System.out.println("The definition \"" + value + "\" already exists. Try again:");
            logSave.add("The definition \"" + value + "\" already exists. Try again:");
            return;
        }
        cards.put(key, value);
        error.put(key, 0);
        reversedCards.put(value, key);
        System.out.println("The pair (\"" + key + ":\"" + value + "\") has been added.");
        logSave.add("The pair (\"" + key + ":\"" + value + "\") has been added.");
        return;
    }

    static public void remove() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("The card:");
        logSave.add("The card:");
        String key = scanner.nextLine();
        logSave.add(key);
        if (!cards.containsKey(key)) {
            System.out.println("Can't remove \"" + key + "\": there is no such card.");
            logSave.add("Can't remove \"" + key + "\": there is no such card.");
            scanner.close();
            return;
        }
        reversedCards.remove(cards.get(key));
        error.remove(key);
        cards.remove(key);

        System.out.println("The card has been removed.");
        logSave.add("The card has been removed.");
    }

    static public void ask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many times to ask?");
        logSave.add("How many times to ask?");
        int times = Integer.parseInt(scanner.nextLine());
        logSave.add(String.valueOf(times));
        Random random = new Random();
        for (int i = 0; i < times; i++) {
            int count = 0;
            int num = random.nextInt(cards.size());
            for (var card : cards.entrySet()) {
                if (count == num) {
                    System.out.println("Print the definition of "+"\""+card.getKey() +"\":");
                    logSave.add("Print the definition of "+"\""+card.getKey() +"\":");
                    String userInput = scanner.nextLine();
                    logSave.add(userInput);
                    if (userInput.equals(card.getValue())) {
                        System.out.println("Correct Answer.");
                        logSave.add("Correct Answer.");
                    } else if (cards.containsValue(userInput)) {
                        System.out.println("Wrong, The right answer is "+"\""+card.getValue()+"\"," + "but your definition is correct for " + "\"" + reversedCards.get(userInput) + "\"." );
                        error.replace(card.getKey(), error.get(card.getKey())+1);
                        logSave.add("Wrong, The right answer is "+"\""+card.getValue()+"\"," + "but your definition is correct for " + "\"" + reversedCards.get(userInput) + "\"." );
                    } else {
                        System.out.println("Wrong, The right answer is "+"\""+card.getValue()+"\".");
                        error.replace(card.getKey(), error.get(card.getKey())+1);
                        logSave.add("Wrong, The right answer is "+"\""+card.getValue()+"\".");
                    }
                }
                count++;
            }
        }
    }

    static public void hardestCard() {
        int max = 0;
        int countOfMax = 0;
        for (var err : error.entrySet()) {
            if (max < err.getValue()) {
                max = err.getValue();
            }
        }
        for (var err : error.entrySet()) {
            if (err.getValue() == max) {
                countOfMax++;
            }
        }
        if (max == 0) {
            System.out.println("There are no cards with errors.");
            logSave.add("There are no cards with errors.");
            return;
        }
        String[] array = new String[countOfMax];
        int count = 0;
        for (var err : error.entrySet()) {
            if  (err.getValue() == max) {
                array[count] = err.getKey();
                count++;
            }
        }
        if (countOfMax == 1) {
            System.out.println("The hardest card is \"" + array[0] + "\". You have "+max+" errors answering it.");
            logSave.add("The hardest card is \"" + array[0] + "\". You have "+max+" errors answering it.");
        } else {
            String hardCard = "\""+array[0]+"\"" ;
            for (int i = 1; i < countOfMax; i++) {
                 hardCard = hardCard + ", " + "\"" + array[i] + "\"";
            }
            System.out.println("The hardest cards are " + hardCard + ". You have " + max + " errors answering them.");
            logSave.add("The hardest cards are " + hardCard + ". You have " + max + " errors answering them.");
        }

    }

    static public void resetStats() {
        for (var err : error.entrySet()) {
            error.replace(err.getKey(),0);
        }
        System.out.println("Card statistics has been reset.");
        logSave.add("Card statistics has been reset.");
    }

    static public void log() {
        System.out.println("File name:");
        logSave.add("File name:");
        Scanner scanner = new Scanner(System.in);
        String fileName = scanner.nextLine();
        logSave.add(fileName);
        File file = new File(fileName);
        try(PrintWriter write = new PrintWriter(file)) {
            for(int i = 0; i < logSave.size(); i++){
                write.println(logSave.get(i));
            }
        } catch (IOException e) {
            System.out.println("error");
        }
        System.out.println("The log has been saved.");
        logSave.add("The log has been saved.");
    }

    static public void importCard() {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        System.out.println("File name:");
        logSave.add("File name:");
        String fileName = scanner.next();
        logSave.add(fileName);
        File file = new File(fileName);
        try(Scanner scan = new Scanner(file)) {
            while (scan.hasNext()){
                String key = scan.nextLine();
                String value = scan.nextLine();
                int errorCount = Integer.parseInt(scan.nextLine());

                count++;
                if(cards.containsKey(key)){
                    cards.replace(key, value);
                    error.replace(key, errorCount);
                    reversedCards.remove(value);
                    reversedCards.put(value, key);
                    continue;
                }

                cards.put(key, value);
                error.put(key, errorCount);
                reversedCards.put(value, key);
            }
        } catch (IOException e) {
            System.out.println("File not found.");
            logSave.add("File not found.");
            return;

        }
        System.out.println(count + " cards have been loaded.");
        logSave.add(count + " cards have been loaded.");
    }

    public static void exportCards() {
        Scanner scanner = new Scanner(System.in);
        int count = 0;
        System.out.println("File name:");
        logSave.add("File name:");
        String fileName = scanner.next();
        logSave.add(fileName);
        File file = new File(fileName);
        try(PrintWriter write = new PrintWriter((file))) {
            for (var s : cards.entrySet()){
                write.println(s.getKey());
                write.println(s.getValue());
                write.println(error.get(s.getKey()));
                count++;
            }
        } catch (IOException e) {
            System.out.println("error detected abort mission abort");
            logSave.add("error detected abort mission abort");
        }
        System.out.println(count + " cards have been saved.");
        logSave.add(count + " cards have been saved.");
    }

    public static void importStart(String fileName) {
        int count = 0;
        File file = new File(fileName);
        try(Scanner scan = new Scanner(file)) {
            while (scan.hasNext()){
                String key = scan.nextLine();
                String value = scan.nextLine();
                int errorCount = Integer.parseInt(scan.nextLine());

                count++;
                if(cards.containsKey(key)){
                    cards.replace(key, value);
                    error.replace(key, errorCount);
                    reversedCards.remove(value);
                    reversedCards.put(value, key);
                    continue;
                }

                cards.put(key, value);
                error.put(key, errorCount);
                reversedCards.put(value, key);
            }
        } catch (IOException e) {
            System.out.println("File not found.");
            logSave.add("File not found.");
            return;

        }
        System.out.println(count + " cards have been loaded.");
        logSave.add(count + " cards have been loaded.");
    }

    public static void exportExit(String fileName) {
        int count = 0;
        File file = new File(fileName);
        try(PrintWriter write = new PrintWriter((file))) {
            for (var s : cards.entrySet()){
                write.println(s.getKey());
                write.println(s.getValue());
                write.println(error.get(s.getKey()));
                count++;
            }
        } catch (IOException e) {
            System.out.println("error detected abort mission abort");
            logSave.add("error detected abort mission abort");
        }
        System.out.println(count + " cards have been saved.");
        logSave.add(count + " cards have been saved.");
    }
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-import")) {
                importStart(args[i+1]);
            }
        }
        Scanner scanner = new Scanner(System.in);
        boolean looper = true;
        while (looper) {
            System.out.println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            logSave.add("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String userInput = scanner.nextLine();
            logSave.add(userInput);
            switch(userInput) {
                case ("add"):
                    add();
                    break;
                case ("remove"):
                    remove();
                    break;
                case ("import"):
                    importCard();
                    break;
                case ("export"):
                    exportCards();
                    break;
                case ("ask"):
                    ask();
                    break;
                case("log"):
                    log();
                    break;
                case("hardest card"):
                    hardestCard();
                    break;
                case("reset stats"):
                    resetStats();
                    break;
                case ("exit"):
                default:
                    System.out.println("Bye bye!");
                    for (int i = 0; i < args.length; i++) {
                        if(args[i].equals("-export")){
                            exportExit(args[i+1]);
                        }
                    }
                    looper = false;
                    scanner.close();
                    break;
            }

        }
        scanner.close();
    }
}
