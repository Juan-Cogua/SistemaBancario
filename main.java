import scr.controlador.BancoController;
import java.util.Scanner;

/**
 * Clase principal para simular el sistema bancario.
 * Actúa como un controlador simple que coordina las operaciones 
 * entre el Modelo (Cuentas) y la Persistencia (GestorArchivos).
 */
public class Main {

    public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    BancoController controller = new BancoController();



        boolean running = true;
        while (running) {
            System.out.println("\n--- MENÚ SISTEMA BANCARIO ---");
            System.out.println("1. Crear cuenta");
            System.out.println("2. Retirar");
            System.out.println("3. Depositar");
            System.out.println("4. Mostrar movimientos (retiros/depositos)");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");

            int opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1:
                    controller.crearCuentaInteractive(sc);
                    break;
                case 2:
                    controller.retirarInteractive(sc);
                    break;
                case 3:
                    controller.depositarInteractive(sc);
                    break;
                case 4:
                    controller.mostrarMovimientosInteractive(sc);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        sc.close();
    }
}
