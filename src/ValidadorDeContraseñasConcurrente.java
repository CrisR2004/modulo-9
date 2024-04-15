import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class ValidadorDeContraseñasConcurrente {
    private static final int LONGITUD_MINIMA_CONTRASEÑA = 8;
    private static final int MIN_LETRAS_MAYUSCULAS = 2;
    private static final int MIN_LETRAS_MINUSCULAS = 3;
    private static final int MIN_NUMEROS = 1;
    private static final Pattern PATRON_CONTRASEÑA = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]{3,})(?=.*[A-Z]{2,})(?=.*[~!@#$%^&*()_+=\\-{}|;':\"\\?><./,\\[\\]]).{" + LONGITUD_MINIMA_CONTRASEÑA + ",}$");

    public static void main(String[] args) {
        ExecutorService ejecutorDeTareas = Executors.newCachedThreadPool();
        Scanner lectorEntrada = new Scanner(System.in);

        try {
            System.out.println("Requisitos de contraseña:");
            System.out.println("- Longitud mínima: " + LONGITUD_MINIMA_CONTRASEÑA + " caracteres.");
            System.out.println("- Debe contener " + "un número, dos letras mayúsculas, tres letras minúsculas y un caracter especial.\n");

            System.out.println("Ingrese la cantidad de contraseñas a validar:");
            int numeroDeContraseñas = lectorEntrada.nextInt();

            for (int i = 0; i < numeroDeContraseñas; i++) {
                System.out.println("Ingrese la contraseña " + (i + 1) + ":");
                String contraseña = lectorEntrada.next();

                Runnable tareaValidacionContraseña = () -> {
                    boolean esValida = validarContraseña(contraseña);
                    System.out.println("La contraseña '" + contraseña + "' es " + (esValida ? "válida" : "inválida"));
                };

                ejecutorDeTareas.submit(tareaValidacionContraseña);
            }
        } catch (Exception e) {
            System.err.println("Error al leer la entrada: " + e.getMessage());
        } finally {
            ejecutorDeTareas.shutdown();
            lectorEntrada.close();
        }
    }

    private static boolean validarContraseña(String contraseña) {
        boolean esValida = true;

        if (contraseña.length() < LONGITUD_MINIMA_CONTRASEÑA) {
            System.out.println("- La longitud mínima requerida es de " + LONGITUD_MINIMA_CONTRASEÑA + " caracteres.");
            esValida = false;
        }
        if (!PATRON_CONTRASEÑA.matcher(contraseña).matches()) {
            System.out.println("- La contraseña no cumple con los requisitos necesarios.");
            esValida = false;
        }

        return esValida;
    }
}