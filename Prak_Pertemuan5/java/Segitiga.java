public class Segitiga extends BangunDatar {

    private double alas;
    private double tinggi;
    private double sisiMiring;

    public Segitiga(double alas, double tinggi, double sisiMiring) {
        super("Segitiga");

        if (alas <= 0 || tinggi <= 0 || sisiMiring <= 0) {
            throw new IllegalArgumentException(
                "Alas, tinggi, dan sisi miring harus lebih besar dari 0."
            );
        }

        this.alas = alas;
        this.tinggi = tinggi;
        this.sisiMiring = sisiMiring;
    }

    @Override
    public double luas() {
        return 0.5 * alas * tinggi;
    }

    @Override
    public double keliling() {
        return alas + tinggi + sisiMiring;
    }

    @Override
    public String toString() {
        return getNama() +
                "(alas=" + alas +
                ", tinggi=" + tinggi +
                ", sisi=" + sisiMiring +
                ") luas=" + luas() +
                ", keliling=" + keliling();
    }
}
