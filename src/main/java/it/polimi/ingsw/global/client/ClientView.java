package it.polimi.ingsw.global.client;
import java.util.Scanner;

public class ClientView {

    public void setup() {
        String nickname = null;
        boolean confirmation = false;
        Scanner scanner = new Scanner(System.in);
        while (!confirmation) {
            do {
                System.out.println(">Insert your nickname: ");
                System.out.print(">");
                nickname = scanner.nextLine();
            } while (nickname == null);
            System.out.println(">You chose: " + nickname);
            System.out.println(">Is it ok? [y/n] ");
            System.out.print(">");
            if (scanner.nextLine().equalsIgnoreCase("y")) {
                confirmation = true;
            } else {
                nickname = null;
            }
        }
    }
}
