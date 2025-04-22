import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collection;

public class Consola {
    private Scanner scanner;
    private GestorUsuarios userManager;
    private GestorRecursos resourceManager;

    public Consola() {
        this.scanner = new Scanner(System.in);
        this.userManager = new GestorUsuarios();
        this.resourceManager = new GestorRecursos();
    }

    public void showMainMenu() {
        System.out.println("----- Menú Principal -----");
        System.out.println("1. Gestionar usuarios");
        System.out.println("2. Gestionar recursos");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");

    }

    public void init() {
        while (true) {
            showMainMenu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                if (option == 1) {
                    manageUsers();
                } else if (option == 2) {
                    manageResources();
                } else if (option == 0) {
                    System.out.println("Saliendo...");
                    break;
                } else {
                    System.out.println("Opción inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }
    }
    private void manageUsers() {
        System.out.println("Gestión de usuarios aún no implementada.");
    }

    private void manageResources() {
        System.out.println("Gestión de recursos aún no implementada.");
    }
}