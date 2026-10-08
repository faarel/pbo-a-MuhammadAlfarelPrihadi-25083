public class Sepeda extends Kendaraan implements Movable {

    public Sepeda(String merek, int tahun) {
        super(merek, tahun);
    }

    @Override
    public int jumlahRoda() {
        return 2;
    }

    @Override
    public void bergerak() {
        System.out.println("Sepeda sedang dikayuh.");
    }

    @Override
    public String ringkasanGerak() {
        return "Sepeda bergerak dengan cara dikayuh.";
    }

    @Override
    public double kecepatanMaksimum() {
        return 30;
    }
}