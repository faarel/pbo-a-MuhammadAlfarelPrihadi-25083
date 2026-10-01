public class Main {
    public static void main(String[] args) {

        // Upcasting: variabel bertipe induk, objek bertipe turunan.
        BangunDatar[] daftar = {
            new Lingkaran(7),
            new Persegi(5),
            new Segitiga(3, 4, 5),
            new Trapesium(10, 6, 5, 5, 4)
        };

        System.out.println("=== Bangun Datar ===");

        for (BangunDatar b : daftar) {
            System.out.println("  " + b);
        }

        double total = 0;

        for (BangunDatar b : daftar) {
            total += b.luas();
        }

        System.out.printf("%n  Total luas: %.2f%n", total);

        System.out.println();
        System.out.println("Periksa: Lingkaran(7) luas = 153,94 ; Persegi(5) luas = 25,00");
        System.out.println("         Segitiga(3,4,5) luas = 6,00");

        System.out.println();
        System.out.println("=== Downcasting hanya bila benar-benar perlu ===");

        for (BangunDatar b : daftar) {
            if (b instanceof Lingkaran l) {
                System.out.printf(
                    "  %s punya jari-jari %.1f%n",
                    l.getNama(),
                    l.getJariJari()
                );
            }
        }
    }
}
