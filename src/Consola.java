import java.util.Scanner;
import java.util.InputMismatchException;

public class Consola {
    private Scanner scanner;

    public Consola() {
        this.scanner = new Scanner(System.in);
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
        while (true) {
            int option = showUsersMenu();
            switch (option) {
                case 1:
                    // agregarUsuario();
                    break;
                case 2:
                    // buscarUsuario();
                    break;
                case 3:
                    // listarUsuarios();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private int showUsersMenu() {
        System.out.println("--- Gestión de Usuarios ---");
        System.out.println("1. Agregar nuevo usuario");
        System.out.println("2. Buscar usuario por ID");
        System.out.println("3. Listar todos los usuarios");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();
            return option;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada inválida. Intente de nuevo.");
            return -1;
        }
    }

    private void manageResources() {
        while (true) {
            int option = showResourcesMenu();
            switch (option) {
                case 1:
                    // agregarRecurso();
                    break;
                case 2:
                    // buscarRecurso();
                    break;
                case 3:
                    // listarRecursos();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private int showResourcesMenu() {
        System.out.println("--- Gestión de Recursos ---");
        System.out.println("1. Agregar nuevo recurso");
        System.out.println("2. Buscar recurso por ID");
        System.out.println("3. Listar todos los recursos");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();
            return option;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada inválida. Intente de nuevo.");
            return -1;
        }
    }
}